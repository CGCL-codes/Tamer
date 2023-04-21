package com.yeep.app.demo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Algorithms {

    /**
	* 打印九九乘法口诀表
	*/
    public static void nineNineMulitTable() {
        for (int i = 1, j = 1; j <= 9; i++) {
            System.out.print(i + "*" + j + "=" + i * j + " ");
            if (i == j) {
                i = 0;
                j++;
                System.out.println();
            }
        }
    }

    public static int removeNM(int n, int m) {
        LinkedList ll = new LinkedList();
        for (int i = 0; i < n; i++) ll.add(new Integer(i + 1));
        int removed = -1;
        while (ll.size() > 1) {
            removed = (removed + m) % ll.size();
            ll.remove(removed--);
        }
        return ((Integer) ll.get(0)).intValue();
    }

    public static void main(String[] args) {
        test();
    }

    private static int[] array = new int[] { 1, 2, 2, 3, 4, 5 };

    public static void test() {
        for (int i = 122345; i < 543221; i++) {
            String string = String.valueOf(i);
            int idx4 = string.indexOf(4);
            int idx3 = string.indexOf(3);
            int idx5 = string.indexOf(5);
            if (idx4 == 3 && (idx3 == idx5 - 1 || idx3 == idx5 + 1)) {
            }
        }
    }
}
