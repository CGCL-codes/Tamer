package net.sf.xml2cb.cobol.compiler;

import java.io.BufferedReader;
import java.io.IOException;
import net.sf.xml2cb.cobol.CobolCopyParserException;

class ParserUtils {

    public static String nextFullLine(BufferedReader in) throws IOException {
        String line = nextLine(in);
        if (line == null) return null;
        StringBuffer sb = new StringBuffer(200);
        sb.append(line);
        while (sb.charAt(sb.length() - 1) != '.') {
            String appendLine = nextLine(in);
            if (appendLine == null) throw new CobolCopyParserException("Missing end of line caracter '.'");
            sb.append(' ').append(appendLine);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String nextLine(BufferedReader in) throws IOException {
        String line = in.readLine();
        while (line != null) {
            if (line.length() > 6 && line.charAt(6) != '*') {
                if (line.length() > 72) {
                    line = line.substring(7, 72).trim();
                } else {
                    line = line.substring(7).trim();
                }
                if (line.length() > 0) break;
            }
            line = in.readLine();
        }
        return line;
    }
}
