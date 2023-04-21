package edu.udo.scaffoldhunter.plugins.datacalculation;

import java.util.BitSet;

/**
 * @author Philipp Lewe
 * 
 */
public class CalcHelpers {

    /**
     * Converts the given {@link BitSet} to a {0,1}-String of the given length.
     * 
     * @param bitset
     *            the {@link BitSet} to convert
     * @param length
     *            the length of the resulting String
     * @return {0,1}-String representation of bitset
     * @throws IllegalArgumentException
     *             if {@link BitSet#size()} smaller than the given length
     */
    public static String Bitset2BitString(BitSet bitset, int length) {
        if (bitset.size() < length) {
            throw new IllegalArgumentException("length if longer than the bitset lenght");
        }
        StringBuilder builder = new StringBuilder(length);
        for (int i = length - 1; i >= 0; i--) {
            if (bitset.get(i)) {
                builder.append("1");
            } else {
                builder.append("0");
            }
        }
        return builder.toString();
    }
}
