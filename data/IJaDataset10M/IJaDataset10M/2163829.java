package cz.jAlgorithm.search.string;

import java.util.ArrayList;
import java.util.List;

/**
 * Levenshtein distance
 * @author Pavel Micka
 */
public class LevenshteinDistance {

    /**
     * Returns all occurences of the given pattern in the text
     * (last index in the text) in Levenshtain distance maxDistance
     * @param text text
     * @param pattern pattern
     * @param maxDistance maximal distance (inclusive)
     * @return list of all occurences
     */
    public static List<Integer> levenshteinDistance(String text, String pattern, int maxDistance) {
        List<Integer> l = new ArrayList<Integer>();
        int[] first = new int[pattern.length()];
        int[] second = new int[pattern.length()];
        for (int i = 0; i < first.length; i++) {
            first[i] = i + 1;
        }
        for (int i = 0; i < text.length(); i++) {
            for (int j = 0; j < second.length; j++) {
                if (pattern.charAt(j) == text.charAt(i)) {
                    if (j != 0) {
                        second[j] = first[j - 1];
                    } else {
                        second[j] = 0;
                    }
                } else {
                    int prevVert = 0;
                    if (j != 0) prevVert = second[j - 1];
                    int prevHor = j;
                    if (i != 0) prevHor = first[j];
                    int prevDiag = 0;
                    if (i != 0 || j != 0) {
                        if (i != 0 && j != 0) {
                            prevDiag = first[j - 1];
                        } else if (i != 0) {
                            prevDiag = 0;
                        } else if (j != 0) {
                            prevDiag = first[j - 1];
                        } else assert true;
                    } else {
                        prevDiag = 0;
                    }
                    second[j] = 1 + Math.min(prevVert, Math.min(prevHor, prevDiag));
                }
            }
            if (second[pattern.length() - 1] <= maxDistance) l.add(i);
            int[] tmp = first;
            first = second;
            second = tmp;
        }
        return l;
    }

    public static void main(String[] args) {
        List<Integer> l = levenshteinDistance("bbabababbbb", "bbb", 1);
        for (Integer i : l) {
            System.out.println(i);
        }
    }
}
