package messy.base;

import java.util.Vector;

/**
* Parses Gcp lines.
* 
 * @author Cohan Sujay C.
 * @version X
 * @see java.lang.Object
**/
public class GcpParser {

    public static Vector getTokens(String line) {
        if (line == null) return null;
        Vector tempVec = new Vector();
        StringBuffer temp = new StringBuffer("");
        for (int i = 0; i < line.length(); ++i) {
            char ch = line.charAt(i);
            if (ch == ' ' || ch == '\t' || ch == '\n') {
                if (temp.length() > 0) {
                    tempVec.addElement(new String(temp));
                    temp = new StringBuffer("");
                }
            } else {
                temp.append(ch);
            }
        }
        if (temp.length() > 0) {
            tempVec.addElement(new String(temp));
        }
        return tempVec;
    }

    public static String getToken(String line, int number) {
        if (line == null) return null;
        synchronized (syncObject_) {
            if (line.equals(strLine_)) {
            } else {
                strLine_ = line;
                vecLine_ = getTokens(line);
            }
            if (number < 0 || number >= vecLine_.size()) return null;
            return (String) vecLine_.elementAt(number);
        }
    }

    public static int getTokenCount(String line) {
        if (line == null) return 0;
        synchronized (syncObject_) {
            if (line.equals(strLine_)) {
            } else {
                strLine_ = line;
                vecLine_ = getTokens(line);
            }
            return vecLine_.size();
        }
    }

    public static String escapeString(String str) {
        StringBuffer strBuff = new StringBuffer("");
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\t') strBuff.append("\\t"); else if (str.charAt(i) == ' ') strBuff.append("\\s"); else if (str.charAt(i) == '\n') strBuff.append("\\n"); else if (str.charAt(i) == '\\') strBuff.append("\\"); else strBuff.append(str.charAt(i));
        }
        return strBuff.toString();
    }

    public static String unEscapeString(String str) {
        StringBuffer strBuff = new StringBuffer("");
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\\' && str.length() > i + 1) {
                ++i;
                char c = str.charAt(i);
                if (c == '\\') strBuff.append('\\');
                if (c == 'n') strBuff.append('\n');
                if (c == 't') strBuff.append('\t');
                if (c == 's') strBuff.append(' ');
            } else strBuff.append(str.charAt(i));
        }
        return strBuff.toString();
    }

    /**
* A printable string representation of the state of the object.
* @return The string representation of the state of the object.
* @see java.lang.Object
**/
    public String toString() {
        return this.getClass().getName();
    }

    private static String strLine_ = null;

    private static Vector vecLine_ = null;

    private static Object syncObject_ = new Object();

    /**
	* The main method runs a simple test of functionality.
	* @return void
	* @param argv The array of command line parameters.
	**/
    public static void main(String argv[]) {
        String str = "";
        for (int i = 0; i < argv.length; ++i) {
            str += argv[i];
        }
        System.out.println(str);
        Vector v = GcpParser.getTokens(str);
        for (int i = 0; i < v.size(); ++i) {
            System.out.println(GcpParser.getToken(str, i));
        }
        str = "Hi Boys!";
        for (int i = 0; i < 2; ++i) {
            System.out.println(GcpParser.getToken(str, i));
        }
    }
}
