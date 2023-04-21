package basics;

import java.util.ArrayList;

public class ArrayListSum2 {

    public static void main(String[] args) {
        ArrayList<Double> arr = new ArrayList<Double>();
        arr.add(-54.3);
        arr.add(6.9);
        arr.add(2.12);
        arr.add(0.0);
        arr.add(41.0);
        double sum = 0;
        for (Double x : arr) {
            sum += x;
        }
        System.out.printf("arr = %s\nsum = %.2f", arr.toString(), sum);
    }
}
