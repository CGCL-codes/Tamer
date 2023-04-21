package net.jadoth.util;

import java.util.Arrays;
import net.jadoth.collections.JaArrays;

public class MainTestArrayCombine {

    public static void main(final String[] args) {
        final String[][] stringsstrings = { { "A", "B", "C" }, { "D", "E" }, { "F", "G", "H", "I" } };
        System.out.println(Arrays.toString(JaArrays.combine(stringsstrings)));
    }
}
