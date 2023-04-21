package net.sf.l2j.gameserver.script;

/**
 * @author Luis Arias
 */
public class IntList {

    public static int[] parse(String range) {
        if (range.contains("-")) return getIntegerList(range.split("-")); else if (range.contains(",")) return getIntegerList(range.split(","));
        int[] list = { getInt(range) };
        return list;
    }

    private static int getInt(String number) {
        return Integer.parseInt(number);
    }

    private static int[] getIntegerList(String[] numbers) {
        int[] list = new int[numbers.length];
        for (int i = 0; i < list.length; i++) list[i] = getInt(numbers[i]);
        return list;
    }
}
