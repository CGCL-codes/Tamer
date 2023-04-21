package PINFinal;

import java.util.ArrayList;

public abstract class Analyser {

    protected ArrayList<String> lines;

    public Analyser(ArrayList<String> lines) {
        this.lines = lines;
    }

    public abstract Result analyse();

    protected int getOccurenceCountOfSequence(String source, String pattern) {
        return source.split(pattern).length - 1;
    }

    protected String removeStrings(String line) {
        if (line == null) return null;
        line = line.replace("\\\"", "");
        char[] chars = line.toCharArray();
        ArrayList<Character> filtered = new ArrayList<Character>();
        boolean stringDetected = false;
        for (char c : chars) {
            if (!stringDetected) filtered.add(c);
            if (c == '"') stringDetected = !stringDetected;
        }
        String result = "";
        for (char c : filtered) {
            result += c;
        }
        return result;
    }

    public boolean detectComment(String line) {
        boolean commentDetected = false;
        if (line == null) return false;
        if ((line.startsWith("/") || (line.startsWith("/*")) || (line.startsWith("*")) || (line.endsWith("*/"))) || (!line.endsWith(";"))) {
            commentDetected = true;
        }
        return commentDetected;
    }

    protected ArrayList<String> removeComments(ArrayList<String> lines) {
        if (lines == null || lines.size() == 0) return null;
        ArrayList<String> results = new ArrayList<String>();
        boolean commentDetected = false;
        for (String line : lines) {
            if (!line.startsWith("//")) {
                char[] chars = line.toCharArray();
                ArrayList<Character> filtered = new ArrayList<Character>();
                for (int i = 0; i < chars.length; i++) {
                    if (chars[i] == '/' && (i + 1) != chars.length && chars[i + 1] == '*') {
                        commentDetected = true;
                    }
                    if (!commentDetected && i < chars.length) filtered.add(chars[i]);
                    if (chars[i] == '*' && (i + 1) != chars.length && chars[i + 1] == '/') {
                        commentDetected = false;
                        i++;
                    }
                }
                String resultLine = "";
                for (char c : filtered) {
                    resultLine += c;
                }
                resultLine.trim();
                if (!resultLine.equals("")) results.add((resultLine));
            }
        }
        return results;
    }
}
