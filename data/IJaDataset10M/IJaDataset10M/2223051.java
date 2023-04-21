package com.onehao.middle.l4;

public class BubbleSortTest {

    public static void bubbleSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
            System.out.println("��" + (i + 1) + "������");
            for (int k = 0; k < array.length; k++) {
                System.out.print(array[k] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[] array = { 4, 7, 8, 9, 3, 2 };
        bubbleSort(array);
    }
}
