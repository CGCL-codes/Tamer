package patho.encryptdbstring;

import java.io.*;
import java.util.regex.*;

/**
 *
 * @author Ruppi
 */
public class Systeminfo {

    private static InputStream IS_ = null;

    /** Creates a new instance of Systeminfo */
    public Systeminfo() {
        try {
            Process proc = Runtime.getRuntime().exec("cmd /c systeminfo");
            IS_ = proc.getInputStream();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static String getSysteminfo() throws IOException {
        String ipconfig = parseISToString(IS_).replace("\r", "").replace("\n", "");
        String r = null;
        try {
            Pattern Regex = Pattern.compile("(Produktkennung|Product ID):\\s*(.*?-.*?-.*?-.*?)(U|O)", Pattern.CANON_EQ);
            Matcher RegexMatcher = Regex.matcher(ipconfig);
            while (RegexMatcher.find()) {
                r = RegexMatcher.group(2);
                if (r != null) break;
            }
        } catch (PatternSyntaxException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        return r;
    }

    private static String parseISToString(java.io.InputStream is) {
        java.io.DataInputStream din = new java.io.DataInputStream(is);
        StringBuffer sb = new StringBuffer();
        try {
            String line = null;
            while ((line = din.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (Exception ex) {
            ex.getMessage();
        } finally {
            try {
                is.close();
            } catch (Exception ex) {
            }
        }
        return sb.toString();
    }
}
