package n2hell.xmlrpc.protocol.scgi;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.StringTokenizer;

public class Utils {

    public static String readLine(InputStream pIn, byte[] pBuffer) throws IOException {
        int next;
        int count = 0;
        while (true) {
            next = pIn.read();
            if (next < 0 || next == '\n') {
                break;
            }
            if (next != '\r') {
                pBuffer[count++] = (byte) next;
            }
            if (count >= pBuffer.length) {
                throw new IOException("HTTP Header too long");
            }
        }
        return new String(pBuffer, 0, count);
    }

    public static Properties parseQuery(String query) throws UnsupportedEncodingException {
        StringTokenizer st = new StringTokenizer(query, "&");
        Properties params = new Properties();
        while (st.hasMoreTokens()) {
            String current = st.nextToken();
            String[] kv = current.split("=");
            String key = URLDecoder.decode(kv[0], "UTF-8").trim();
            String value = null;
            if (kv.length == 2) value = URLDecoder.decode(kv[1], "UTF-8");
            if (value != null) params.setProperty(key, value);
        }
        return params;
    }
}
