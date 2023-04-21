package ie.ucd.clops.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * 
 * @author Fintan
 *
 */
public class FileUtil {

    public static LineNumberReader getResourceReader(String filePath) {
        final InputStream istream = new FileUtil().getClass().getClassLoader().getResourceAsStream(filePath);
        if (istream != null) {
            final LineNumberReader br = new LineNumberReader(new InputStreamReader(istream));
            return br;
        } else {
            return null;
        }
    }

    public static String readToString(String filePath) throws IOException {
        final LineNumberReader lnr = getResourceReader(filePath);
        final StringBuilder blder = new StringBuilder();
        blder.append(lnr.readLine());
        blder.append(" (");
        blder.append(lnr.readLine());
        blder.append(")");
        return blder.toString();
    }
}
