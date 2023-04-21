package iwork.patchpanel.manager.script;

public class EscapeUtil {

    public static String escape(String iStr) {
        String oStr = iStr;
        oStr = oStr.replaceAll("\\\\\\\\", "<backslash>");
        oStr = oStr.replaceAll("\\\\f", "<feedfoward>");
        oStr = oStr.replaceAll("\\\\n", "<linefeed>");
        oStr = oStr.replaceAll("\\\\t", "<tab>");
        oStr = oStr.replaceAll("\\\\r", "<return>");
        oStr = oStr.replaceAll("\\\\b", "<backspace>");
        oStr = oStr.replaceAll("\\\\\\\'", "<singlequote>");
        oStr = oStr.replaceAll("\\\\\\\"", "<doublequote>");
        oStr = oStr.replaceAll("\\\"", "\\\\\"");
        return oStr;
    }

    public static String unescape(String iStr) {
        String oStr = iStr.replaceAll("\\\\\\\"", "\\\"");
        return oStr;
    }

    public static String interpolates(String iStr) {
        String oStr = iStr;
        oStr = oStr.replaceAll("<linefeed>", "\n");
        oStr = oStr.replaceAll("<tab>", "\t");
        oStr = oStr.replaceAll("<return>", "\r");
        oStr = oStr.replaceAll("<backspace>", "\b");
        oStr = oStr.replaceAll("<singlequote>", "'");
        oStr = oStr.replaceAll("<doublequote>", "\\\"");
        oStr = oStr.replaceAll("<backslash>", "\\\\");
        return oStr;
    }

    public static String stripOffQuotes(String iStr) {
        String oStr = iStr.trim();
        if (oStr.startsWith("\"") && oStr.endsWith("\"")) oStr = oStr.substring(1, oStr.length() - 1);
        return oStr;
    }

    public static void appendLine(String line, String fileStr) {
        try {
            java.io.File file = new java.io.File(fileStr);
            java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(file, true));
            writer.newLine();
            writer.write(line);
            writer.close();
        } catch (Exception exp) {
            System.out.println(exp);
        }
    }
}
