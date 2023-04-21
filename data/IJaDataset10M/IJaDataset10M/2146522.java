package net.sourceforge.kas.cViewer.java;

public class JMathComponentHelper {

    public static String cleanString(String s) {
        String toRemove1 = " mathcolor=\"#FF0000\"";
        String toRemove2 = " mathcolor=\"#007777\"";
        String toRemove3 = " mathbackground=\"#B0C4DE\"";
        String toReplace = "";
        String t = s.replaceAll(toRemove1, toReplace);
        t = t.replaceAll(toRemove2, toReplace);
        return t.replaceAll(toRemove3, toReplace);
    }
}
