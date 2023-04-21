package algorithms.interview;

/**
 * Created on  May 21, 2011
 * @author leeing
 */
public class LeftRotate {

    public static void rotate(int array[], int pos) {
        for (int p = 0; p < pos; p++) {
            int temp = array[p];
            int q;
            for (q = p + pos; q < array.length; q += pos) {
                array[q - pos] = array[q];
            }
            array[q - p] = temp;
        }
    }

    public static void main(String[] args) {
        int array[] = { 1, 2, 3, 4, 5, 6, 7, 8 };
        rotate(array, 3);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }
}
