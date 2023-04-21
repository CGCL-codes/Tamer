package simulation_1_11;

public class Problem_2_insertSort {

    public static void insertSort(int[] array) {
        if (array.length > 0) {
            for (int index = 1; index < array.length; index++) {
                for (int i = 0; i < index; i++) {
                    if (array[index] <= array[0]) {
                        int temp = array[index];
                        for (int j = index - 1; j >= 0; j--) {
                            array[j + 1] = array[j];
                        }
                        array[0] = temp;
                    }
                    if (array[index] >= array[index - 1]) {
                    }
                    if (array[index] >= array[i] && array[index] <= array[i + 1]) {
                        int temp = array[index];
                        for (int j = index - 1; j >= i + 1; j--) {
                            array[j + 1] = array[j];
                        }
                        array[i + 1] = temp;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] myArray = new GetArray(10).getArray();
        for (int i = 0; i < myArray.length; i++) {
            System.out.print(myArray[i] + " ");
        }
        Problem_2_insertSort app = new Problem_2_insertSort();
        System.out.println();
        app.insertSort(myArray);
        for (int i = 0; i < myArray.length; i++) {
            System.out.print(myArray[i] + " ");
        }
    }
}
