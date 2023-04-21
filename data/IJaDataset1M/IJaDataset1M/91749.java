package logicswarm.net.test;

import logicswarm.util.Arrays;

public class arraytest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String[][][] test = { { { "000", "001", "002" } }, { { "100", "101", "102" }, { "110", "111", "112" } }, { { "200", "201", "202" }, { "210", "211", "212", "213" }, { "220", "221", "222" } } };
        int[] size = Arrays.maxArrayIndexSize(test);
        for (int i = 0; i < size.length; i++) System.out.print("[" + size[i] + "]");
        System.out.println();
        String[][][] tmp = { { { "300", "301", "303", "304", "305" } } };
        test = Arrays.appendArray(test, tmp);
        size = Arrays.maxArrayIndexSize(test);
        for (int i = 0; i < size.length; i++) System.out.print("[" + size[i] + "]");
        System.out.println();
        Arrays.printStringMatrix(test);
    }
}
