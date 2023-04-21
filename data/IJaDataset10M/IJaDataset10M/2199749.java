package org.unicolet.axl.utils;

import java.io.*;

/**
 * Created:
 * User: unicoletti
 * Date: 11:35:24 AM Oct 25, 2005
 */
public class MapServerFormatter {

    private static String[] tokens = { "CLASS", "FEATURE", "GRID", "JOIN", "LABEL", "LAYER", "LEGEND", "MAP", "OUTPUTFORMAT", "PROJECTION", "QUERYMAP", "REFERENCE", "SCALEBAR", "STYLE", "WEB", "SYMBOL" };

    private static final String INDENTCHAR = "\t";

    private static final String NEWLINE = "\n";

    public static String format(String text) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(text));
        StringWriter writer = new StringWriter();
        doFormat(reader, writer, "");
        return writer.getBuffer().toString();
    }

    private static void doFormat(BufferedReader reader, Writer writer, String indent) throws IOException {
        String line = reader.readLine();
        while (line != null) {
            line = line.trim();
            if (containsStartToken(line)) {
                writer.write(indent + line + NEWLINE);
                doFormat(reader, writer, indent + INDENTCHAR);
            } else if (containsEndToken(line)) {
                indent = indent.substring(0, (indent.length() - 1));
                writer.write(indent + line + NEWLINE);
                return;
            } else {
                writer.write(indent + line + NEWLINE);
            }
            line = reader.readLine();
        }
    }

    private static boolean containsEndToken(String l) {
        return l.equalsIgnoreCase("END");
    }

    private static boolean containsStartToken(String l) {
        boolean result = false;
        for (int i = 0; i < tokens.length; i++) {
            if (l.equalsIgnoreCase(tokens[i])) {
                result = true;
                break;
            }
        }
        return result;
    }
}
