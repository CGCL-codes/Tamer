package zipwire.text;

import java.util.*;

public class CamelCase {

    private String s;

    public CamelCase(String s) {
        this.s = s;
    }

    public String separateWith(String delimiter) {
        return join(split(), delimiter);
    }

    private List<String> split() {
        return cutAlong(cutLines());
    }

    private List<Integer> cutLines() {
        ArrayList<Integer> cutLines = new ArrayList<Integer>();
        char[] text = s.toCharArray();
        for (int i = 0; i < text.length; i++) if (Character.isUpperCase(text[i])) cutLines.add(i);
        return cutLines;
    }

    private List<String> cutAlong(List<Integer> cutLines) {
        ArrayList<String> cuts = new ArrayList<String>();
        int last = 0;
        for (int cutLine : cutLines) if (cutLine != 0) {
            cuts.add(s.substring(last, cutLine));
            last = cutLine;
        }
        cuts.add(s.substring(last));
        return cuts;
    }

    private String join(List<String> cuts, String delimiter) {
        StringBuffer text = new StringBuffer();
        for (String cut : cuts) text.append(delimiter).append(cut);
        return text.toString().replaceFirst(delimiter, "");
    }
}
