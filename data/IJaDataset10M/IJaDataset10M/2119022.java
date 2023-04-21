package net.sf.filosof.example.wordssearch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.filosof.framework.problem.Solution;

public class GridScorer {

    public static int wordsScore(Grid grid) {
        if (1 == 1) return 0;
        Map<String, Integer> words = new HashMap<String, Integer>();
        int n = grid.n;
        for (int x = 0; x < n; ++x) {
            for (int y = 0; y < n; ++y) {
                collectWords(x, y, 0, 1, grid, words);
                collectWords(x, y, 1, 0, grid, words);
                collectWords(x, y, 0, -1, grid, words);
                collectWords(x, y, -1, 0, grid, words);
                collectWords(x, y, 1, 1, grid, words);
                collectWords(x, y, -1, 1, grid, words);
                collectWords(x, y, 1, -1, grid, words);
                collectWords(x, y, -1, -1, grid, words);
            }
        }
        int score = 0;
        for (String s : words.keySet()) {
            score += s.length();
        }
        if (!inErrMode) {
            for (int i = 0; i < grid.problem.allWords.length; ++i) {
                int w = 0;
                if (!isPali(grid.problem.allWords[i])) {
                    if (words.containsKey(grid.problem.allWords[i])) {
                        w = words.get(grid.problem.allWords[i]);
                    }
                    if (grid.wordCount[i] != w) {
                        inErrMode = true;
                        System.out.println(grid);
                        throw new RuntimeException(grid.problem.allWords[i] + " " + grid.wordCount[i] + " " + w);
                    }
                }
            }
        }
        return score;
    }

    static boolean inErrMode = false;

    private static boolean isPali(String s) {
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) != s.charAt(s.length() - 1 - i)) {
                return false;
            }
        }
        return true;
    }

    private static void collectWords(int x, int y, int dx, int dy, Grid grid, Map<String, Integer> words) {
        int n = grid.n;
        StringBuffer sb = new StringBuffer();
        sb.append(grid.field[x][y]);
        if (grid.field[x][y] == '.') {
            return;
        }
        for (; ; ) {
            if (grid.problem.wordToAllWordsIndex.containsKey(sb.toString())) {
                words.put(sb.toString(), words.get(sb.toString()) == null ? 1 : (1 + words.get(sb.toString())));
            }
            x += dx;
            y += dy;
            if (x < 0 || x >= n || y < 0 || y >= n) {
                break;
            }
            char c = grid.field[x][y];
            if (c == '.') {
                break;
            }
            sb.append(c);
        }
    }

    public static Map<String, List<String>> getInfixes(List<String> words) {
        File file = new File("infix");
        if (file.exists()) {
            try {
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
                Map<String, List<String>> solution = (Map<String, List<String>>) is.readObject();
                is.close();
                return solution;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Map<String, List<String>> numOfInfixes = new HashMap<String, List<String>>();
        Set<String> setWords = new HashSet<String>(words);
        int i = 0;
        for (String word : words) {
            List<String> infList = new ArrayList<String>();
            int l = word.length();
            for (int a = 0; a < l; ++a) {
                for (int b = a + 1; b <= l; ++b) {
                    String infix = word.substring(a, b);
                    if (setWords.contains(infix)) {
                        infList.add(infix);
                    } else {
                        StringBuffer rev = new StringBuffer();
                        for (int k = infix.length() - 1; k >= 0; --k) {
                            rev.append(infix.charAt(k));
                        }
                        String s = rev.toString();
                        if (setWords.contains(s)) {
                            infList.add(s);
                        }
                    }
                }
            }
            numOfInfixes.put(word, infList);
            System.out.println(i++ + ": " + word + infList);
        }
        try {
            System.out.println("saving " + file.getName());
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
            os.writeObject(numOfInfixes);
            os.close();
            System.out.println("saved " + file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numOfInfixes;
    }
}
