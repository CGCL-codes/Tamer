package inline.sys;

import java.util.*;

public class Log {

    private static Vector history;

    private static final int HISTORYMAX = 10;

    static {
        history = new Vector();
    }

    public static void fire(String s) {
        System.err.println(s);
        history.insertElementAt(s, 0);
        history.setSize(HISTORYMAX);
    }

    public static Vector getHistory() {
        return history;
    }

    public static String getString() {
        String out = "";
        for (int i = 0; i < history.size(); i++) {
            String el = (String) history.elementAt(i);
            if (el == null) break;
            out = out + el + "\n";
        }
        return out;
    }
}
