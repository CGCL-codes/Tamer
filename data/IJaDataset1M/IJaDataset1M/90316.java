package net.sourceforge.akrogen.core.component.listener;

import java.util.Map;

/**
 * 
 * Replace Update.
 * eg : <replace source="$pageConfigName" token="." value="/" />
 * 
 * @version 1.0.0 
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 *
 */
public class AkrogenReplace extends AbstractAkrogenStringUpdate {

    private String token;

    private String value;

    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public String updateSource(String source, Map akrogenContextMap) {
        return replace(source, token, value);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns a string representation of the object.
     */
    public String toString() {
        StringBuffer results = new StringBuffer();
        results.append("\t\tAkrogenReplace : ");
        results.append("\n");
        results.append("\t\t\tsource = " + getSource());
        results.append("\n");
        results.append("\t\t\ttarget = " + getTarget());
        results.append("\n");
        results.append("\t\t\ttoken = " + token);
        results.append("\n");
        results.append("\t\t\tvalue = " + value);
        return results.toString();
    }

    public String replace(String source) {
        if (token != null && value != null) {
            return replace(source, token, value);
        }
        return null;
    }

    /**
	 * Test Akrogen  Replace for get directory of package
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            AkrogenReplace akrogenReplace = new AkrogenReplace();
            akrogenReplace.setToken(".");
            akrogenReplace.setValue("/");
            String result = akrogenReplace.replace("net.sourceforge.akrogen.core.component.listener");
            System.out.println("Replace result : " + result);
        } catch (Exception e) {
            System.out.println("ERROR : " + e.getMessage());
        }
    }

    private String replace(String text, String repl, String with) {
        return replace(text, repl, with, -1);
    }

    private String replace(String text, String repl, String with, int max) {
        if (text == null || isEmpty(repl) || with == null || max == 0) return text;
        StringBuffer buf = new StringBuffer(text.length());
        int start = 0;
        for (int end = 0; (end = text.indexOf(repl, start)) != -1; ) {
            buf.append(text.substring(start, end)).append(with);
            start = end + repl.length();
            if (--max == 0) break;
        }
        buf.append(text.substring(start));
        return buf.toString();
    }
}
