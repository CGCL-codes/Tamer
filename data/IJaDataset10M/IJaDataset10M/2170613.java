package net.sourceforge.numbertrans.tools;

import java.util.HashSet;
import java.util.List;
import net.sourceforge.numbertrans.framework.base.WholeNumber;
import net.sourceforge.numbertrans.framework.base.AbstractNumber.Context;
import net.sourceforge.numbertrans.languages.english.EnglishCardinalScribe;

public class LexiconGenerator {

    static EnglishCardinalScribe sourceScribe = new EnglishCardinalScribe();

    static EnglishCardinalScribe targetScribe = new EnglishCardinalScribe();

    static HashSet<String> prev = new HashSet<String>();

    public static void main(String[] args) throws Exception {
        for (long magnitude = 1; magnitude <= 1000000000000l; magnitude *= 1000) {
            for (int hundreds = 0; hundreds < 1000; hundreds++) {
                print(hundreds * magnitude);
            }
        }
    }

    private static void print(long i) {
        WholeNumber number = new WholeNumber(i, 0, Context.CARDINAL);
        List<String> sourceStrings = sourceScribe.getAllNumberStrings(number);
        List<String> targetStrings = targetScribe.getAllNumberStrings(number);
        for (final String source : sourceStrings) {
            for (final String target : targetStrings) {
                String result = source + " <> " + target;
                if (!prev.contains(result)) {
                    prev.add(result);
                    System.out.println(result);
                }
            }
        }
    }
}
