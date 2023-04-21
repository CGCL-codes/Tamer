package org.equanda.auth;

import javolution.lang.TextBuilder;
import org.equanda.util.StringSplitter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ...
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class ParsedAuthAndConfigProvider extends MapAuthAndConfigProvider {

    public ParsedAuthAndConfigProvider(String data) {
        if (data != null) {
            StringSplitter sp = new StringSplitter(data, '\n');
            for (String line : sp) {
                if (line.length() == 0 || line.charAt(0) == ';' || line.charAt(0) == '#') continue;
                String key = getAuthKey(line);
                String value = getAuthValue(line);
                int aacValue = 0;
                if (value.indexOf('R') >= 0) aacValue |= AuthAndConfig.AUTH_READABLE;
                if (value.indexOf('W') >= 0) aacValue |= AuthAndConfig.AUTH_WRITABLE + AuthAndConfig.AUTH_READABLE;
                if (value.indexOf('D') >= 0) {
                    aacValue |= AuthAndConfig.AUTH_DELETE + AuthAndConfig.AUTH_WRITABLE + AuthAndConfig.AUTH_READABLE;
                }
                if (value.indexOf('S') >= 0) {
                    aacValue |= AuthAndConfig.DISPLAY_SUMMARY + AuthAndConfig.AUTH_READABLE;
                }
                if (value.indexOf('L') >= 0) aacValue |= AuthAndConfig.DISPLAY_LIST + AuthAndConfig.AUTH_READABLE;
                if (value.indexOf('P') >= 0) aacValue |= AuthAndConfig.DISPLAY_PRINT + AuthAndConfig.AUTH_READABLE;
                if (value.indexOf('H') >= 0) {
                    aacValue &= ~(AuthAndConfig.AUTH_READABLE + AuthAndConfig.AUTH_WRITABLE);
                }
                AuthAndConfig aac = new AuthAndConfig(aacValue);
                put(key, aac);
            }
        }
    }

    /**
     * Given a full rights string (e.g "Table.table=RW") return the Table.table part
     *
     * @param data The auth string
     * @return The key part
     */
    public String getAuthKey(String data) {
        String key = data;
        int pos = data.indexOf("=");
        if (pos > 0) {
            key = data.substring(0, pos);
        }
        return key;
    }

    /**
     * Given a full rights string (e.g "Table.table=RW") return the "RW" part
     *
     * @param data The auth string
     * @return The value part
     */
    public String getAuthValue(String data) {
        String value = "";
        int pos = data.indexOf("=");
        if (pos > 0) {
            value = data.substring(pos + 1);
        }
        return value;
    }

    /**
     * This method will add a new right, or update an existing
     *
     * @param key The key to be used, e.g table.Table
     * @param aacValue An integer representing the rights.
     */
    public void addOrUpdate(String key, int aacValue) {
        if (key == null || key.length() == 0) {
            return;
        }
        AuthAndConfig value = new AuthAndConfig(aacValue);
        put(key, value);
    }

    /**
     * This method returns the current full list of rights. Each right is separated by a newline.
     * The keys are sorted to assure consistent output and make it easier to search in long lists.
     *
     * @return A string containing the list of rights.
     */
    public String getAuthAndConfigString() {
        TextBuilder tb = new TextBuilder();
        List<String> sortedKeys = new ArrayList<String>();
        sortedKeys.addAll(getKeys());
        Collections.sort(sortedKeys);
        for (String key : sortedKeys) {
            tb.append(key);
            tb.append("=");
            tb.append(getFor(key).toString());
            tb.append("\n");
        }
        return tb.toString();
    }
}
