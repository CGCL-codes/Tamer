package scratch;

import java.util.ArrayList;
import java.util.Collections;

public class ArrayListTest {

    public static void main(String[] args) {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        for (int i = 1; i <= 49; ++i) {
            arr.add(i);
        }
        System.out.println(arr.toString());
        int first = arr.get(0);
        int last = arr.get(arr.size() - 1);
        System.out.printf("first = %s\nlast = %s\n", first, last);
        arr.set(1, 5);
        System.out.println(arr.toString());
        Collections.shuffle(arr);
        System.out.println(arr.toString());
    }
}
