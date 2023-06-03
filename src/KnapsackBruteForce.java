import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KnapsackBruteForce {

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public record Item(int weight, int value) { }
    public static int capacity;
    public static int numberOfItems;
    public static ArrayList<Item> items;
    public static int maxVal = 0;
    private static ArrayList<Item> bestCombination;
    public static int iterations = 0;
    public static int lastIterations = 0;

    public static void main(String[] args){
        initialize();
        bruteForce(new ArrayList<>(), 0, 0, 0);
        System.out.println(ANSI_GREEN + "Best permutation so far: " + ANSI_RESET + bitRepresentation(bestCombination) + ANSI_GREEN + " max value: " + (maxVal < 100 ? " " : "") + ANSI_RESET + maxVal + ANSI_GREEN + " bruteForce method iterations: " + ANSI_RESET + lastIterations);
        System.out.println("Total iterations: " + iterations);
    }

    public static void initialize(){
        System.out.println("Path to file: ");
        String path = new Scanner(System.in).nextLine();
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            var capNum = reader.readLine().split(" ");
            capacity = Integer.parseInt(capNum[0]);
            numberOfItems = Integer.parseInt(capNum[1]);
            var weights = reader.readLine().split(",");
            var values = reader.readLine().split(",");
            items = new ArrayList<>();
            for (int i=0; i<numberOfItems; i++)
                items.add(new Item(Integer.parseInt(weights[i]), Integer.parseInt(values[i])));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void bruteForce(ArrayList<Item> includedItems, int currentIndex, int currentWeight, int currentValue) {
        iterations++;

        if (currentIndex == items.size() || currentWeight == capacity) {
            if (currentValue > maxVal) {
                if(bestCombination != null)
                    System.out.println(ANSI_RED + "Best permutation so far: " + ANSI_RESET + bitRepresentation(bestCombination) + ANSI_RED + " max value: " + (maxVal < 100 ? " " : "") + ANSI_RESET + maxVal + ANSI_RED + " bruteForce method iterations: " + ANSI_RESET + lastIterations);
                lastIterations = iterations;
                maxVal = currentValue;
                bestCombination = new ArrayList<>(includedItems);
            }
            return;
        }

        if (currentWeight + items.get(currentIndex).weight() > capacity) {
            bruteForce(includedItems, currentIndex + 1, currentWeight, currentValue);
        } else {
            includedItems.add(items.get(currentIndex));
            bruteForce(includedItems, currentIndex + 1, currentWeight + items.get(currentIndex).weight(), currentValue + items.get(currentIndex).value());
            includedItems.remove(items.get(currentIndex));
            bruteForce(includedItems, currentIndex + 1, currentWeight, currentValue);
        }
    }

    public static List<Integer> bitRepresentation(ArrayList<Item> includedItems) {
        List<Integer> bits = new ArrayList<>();
        for (int i = 0; i < numberOfItems; i++) {
            if (includedItems.contains(items.get(i))) {
                bits.add(1);
            } else {
                bits.add(0);
            }
        }
        return bits;
    }

}
