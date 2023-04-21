package uebung12.as.aufgabe03;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class BoyerMoore {

    private static int totCount = 0;

    private static int task = 0;

    public static int bmMatch(String t, int startIndex, int[] last, String p) {
        return -1;
    }

    static int[] buildLastFunction(String p) {
        return null;
    }

    static void printLastFunction(String t, int[] last) {
        char[] charArr = t.toCharArray();
        Arrays.sort(charArr);
        HashSet<Character> set = new LinkedHashSet<Character>();
        for (int i = 0; i < charArr.length; i++) {
            set.add(charArr[i]);
        }
        System.out.print("last: ");
        for (char c : set) {
            System.out.print(c + "  ");
        }
        System.out.print("\n    ");
        for (char c : set) {
            System.out.format("%3d", last[c]);
        }
        System.out.println('\n');
    }

    public static void main(String[] args) {
        String text;
        String pattern;
        if (args.length == 0 || args[0].equals("1")) {
            text = "Anna Kurnikowa ist eine Tennisspielerin. Sie spielt wieder ein wenig nachdem ihre Beinverletzung fast wieder geheilt ist.";
            pattern = "ein";
            task = 1;
        } else {
            text = "adbaacaabedacedbccede";
            pattern = "daeda";
            task = 2;
        }
        int res = 0;
        int pos = 0;
        int[] last = buildLastFunction(pattern);
        printLastFunction(text, last);
        while (res >= 0) {
            res = bmMatch(text, pos, last, pattern);
            if (res >= 0) {
                pos = res + 1;
                System.out.println("Position: " + res);
            }
        }
        System.out.println();
        System.out.println("Total of comparison: " + totCount);
    }
}
