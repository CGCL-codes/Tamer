package net.sourceforge.helpgui.util;

import java.lang.StringBuffer;
import net.sourceforge.helpgui.HelpGui;

/**
  * Class aible to write a simple message on the console
	* @author Alexandre THOMAS
	*/
public class Out {

    public static int OK = 0;

    public static int FAILED = 1;

    private static String[] _state = { "[  OK  ]", "[FAILED]" };

    public static int length = 80;

    /** Add '.' at the end of the message.  */
    private static String addPoints(String msg) {
        StringBuffer result = new StringBuffer(msg);
        for (int i = msg.length(); i < length; ++i) result.append(".");
        return result.toString();
    }

    /** Write a message on the standar output and finish it by [ OK ]. */
    public static void msg(String msg) {
        if (HelpGui.debug) System.out.println(addPoints(msg) + _state[0]);
    }

    /** Write a message on the standar output and finish it by the specify state (OK, or FAILED). */
    public static void msg(String msg, int state) {
        if (HelpGui.debug) System.out.println(addPoints(msg) + _state[state]);
    }
}
