package util.parser;

import java.util.LinkedList;

public class GenericEntryParser {

    /**
	 * @param args
	 */
    public static String[] parseEntry(String line, String SEP, String DEL) {
        LinkedList<String> list = new LinkedList<String>();
        int index = 0;
        index = line.indexOf(DEL);
        String t[] = line.split(DEL + SEP + DEL);
        t[0] = t[0].replaceFirst(DEL, "");
        if (DEL.length() > 0) {
            if (t[t.length - 1].length() - 1 > 1) {
                t[t.length - 1] = t[t.length - 1].substring(0, t[t.length - 1].length() - 1);
            } else {
                return null;
            }
        }
        return t;
    }

    public static void main(String[] args) {
        String a = "\"13100013\"	\"Pepin_d'Herstal\"	\"a\"	\"\"	\"Pepin_of_Herstal\"	\"\"";
        parseEntry(a, "\t", "\"");
    }
}
