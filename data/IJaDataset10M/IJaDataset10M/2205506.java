package com.yerihyo.yeritools.debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.yerihyo.yeritools.io.CompoundOutputStream;
import com.yerihyo.yeritools.text.StringToolkit;

/***************************************************************************************
* YeriDebug - debug class
* Debugging class for java like DEBUG algorithm in C++
*
* @author	elf.yeri^^
* @version	2003/08/02 JMS
****************************************************************************************/
public class YeriDebug {

    /** Trace independent to enabled flags if this flag is given as param. */
    public static final char dbgAll = '+';

    /** Default error tracing output file */
    public static final String DEFAULT_TRACE_FILE_NAME = "trace.txt";

    private File traceFile = new File(DEFAULT_TRACE_FILE_NAME);

    public boolean[] mask = new boolean['z' - 'a' + 1];

    private boolean fileOutputEnabled = false;

    private static boolean debugMode = true;

    public static boolean isDebugMode() {
        return debugMode;
    }

    protected static YeriDebug debug = null;

    public YeriDebug() {
        initialize();
    }

    public YeriDebug(String flagString) {
        initialize();
        this.setFlagEnabled(flagString, true);
    }

    public boolean isFlagEnabled(char[] flagArray) {
        for (char c : flagArray) {
            if (this.isFlagEnabled(c)) {
                return true;
            }
        }
        return false;
    }

    /** check whether given flag is enabled */
    public boolean isFlagEnabled(char flag) {
        if (flag == '+') {
            return true;
        }
        int index = getIndex(flag);
        return mask[index];
    }

    private static int getIndex(char flag) {
        if (!StringToolkit.isRomanCharacter(flag)) {
            return -1;
        }
        char lowC = Character.toLowerCase(flag);
        int index = StringToolkit.getLowerCharIndex(lowC);
        return index;
    }

    public void setFlagEnabled(char flag, boolean b) {
        int index = getIndex(flag);
        if (index < 0) {
            return;
        }
        mask[index] = b;
    }

    public void setFlagEnabled(char[] flagArray, boolean b) {
        for (char c : flagArray) {
            this.setFlagEnabled(c, b);
        }
    }

    public void setFlagEnabled(String flagString, boolean b) {
        setFlagEnabled(flagString.toCharArray(), b);
    }

    public static YeriDebug getYeriDebug() {
        if (debug == null) {
            debug = new YeriDebug();
        }
        return debug;
    }

    public static YeriDebug getYeriDebug(String flags) {
        if (debug == null) {
            debug = new YeriDebug(flags);
        }
        return debug;
    }

    /**
	* Initialize flag
	* @param in_enabledFlags char set of flags enabled
	*/
    private void initialize() {
        traceFile.delete();
        outStreamList.addOutputStream(System.out);
    }

    private CompoundOutputStream outStreamList = new CompoundOutputStream();

    private PrintStream out = new PrintStream(outStreamList);

    private PrintStream err = System.err;

    public void addOutputLog() throws FileNotFoundException {
        DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileTimestamp = format.format(new Date());
        File ofile = new File("log/", "dump_" + fileTimestamp + "_out.log");
        try {
            PrintStream outPrintStream = new PrintStream(ofile);
            outStreamList.addOutputStream(outPrintStream);
        } catch (IOException e) {
            System.err.println("Dumping failed!");
        }
    }

    /** trace without conditional value*/
    public void trace(Object o) {
        trace(o.toString());
    }

    public void trace(CharSequence expr) {
        trace(true, expr);
    }

    public void trace(boolean condition, CharSequence expr) {
        trace(dbgAll, condition, expr);
    }

    public void trace(char flag, CharSequence expr) {
        trace(flag, true, expr);
    }

    public void trace(char flag, boolean condition, CharSequence expr) {
        trace(new char[] { flag }, condition, expr);
    }

    public void trace(String flagString, CharSequence expr) {
        trace(flagString.toCharArray(), true, expr);
    }

    public void trace(String flagString, boolean condition, CharSequence expr) {
        trace(flagString.toCharArray(), condition, expr);
    }

    public void trace(char[] flagArray, CharSequence expr) {
        trace(flagArray, true, expr);
    }

    public void trace(char[] flagArray, boolean condition, CharSequence expr) {
        if (!condition) {
            return;
        }
        if (!isFlagEnabled(flagArray)) {
            return;
        }
        out.print(expr);
        if (this.isFileOutputEnabled()) {
            try {
                PrintWriter ofile = new PrintWriter(new FileOutputStream(traceFile, true));
                ofile.print(expr);
                ofile.close();
            } catch (FileNotFoundException e) {
                YeriDebug.die("FILE IO ERROR!");
            }
        }
    }

    public void traceError(Object o) {
        traceError(o.toString());
    }

    public void traceError(CharSequence expr) {
        traceError(true, expr);
    }

    public void traceError(boolean condition, CharSequence expr) {
        traceError(dbgAll, condition, expr);
    }

    public void traceError(char flag, CharSequence expr) {
        traceError(flag, true, expr);
    }

    public void traceError(char flag, boolean condition, CharSequence expr) {
        traceError(new char[] { flag }, condition, expr);
    }

    public void traceError(String flagString, CharSequence expr) {
        traceError(flagString.toCharArray(), true, expr);
    }

    public void traceError(String flagString, boolean condition, CharSequence expr) {
        traceError(flagString.toCharArray(), condition, expr);
    }

    public void traceError(char[] flagArray, CharSequence expr) {
        traceError(flagArray, true, expr);
    }

    public void traceError(char[] flagArray, boolean condition, CharSequence expr) {
        if (!condition) {
            return;
        }
        if (!isFlagEnabled(flagArray)) {
            return;
        }
        err.print(expr);
        if (this.isFileOutputEnabled()) {
            try {
                PrintWriter ofile = new PrintWriter(new FileOutputStream(traceFile, true));
                ofile.print(expr);
                ofile.close();
            } catch (FileNotFoundException e) {
                YeriDebug.die("FILE IO ERROR!");
            }
        }
    }

    public void traceln(CharSequence expr) {
        traceln(true, expr);
    }

    public void traceln(boolean condition, CharSequence expr) {
        traceln(dbgAll, condition, expr);
    }

    public void traceln(char flag, CharSequence expr) {
        traceln(flag, true, expr);
    }

    public void traceln(char flag, boolean condition, CharSequence expr) {
        traceln(new char[] { flag }, condition, expr);
    }

    public void traceln(String flagString, CharSequence expr) {
        traceln(flagString.toCharArray(), true, expr);
    }

    public void traceln(String flagString, boolean condition, CharSequence expr) {
        traceln(flagString.toCharArray(), condition, expr);
    }

    public void traceln(char[] flagArray, CharSequence expr) {
        traceln(flagArray, true, expr);
    }

    public void traceln(char[] flagArray, boolean condition, CharSequence expr) {
        trace(flagArray, condition, expr + StringToolkit.newLine());
    }

    public void traceErrorln(CharSequence expr) {
        traceErrorln(true, expr);
    }

    public void traceErrorln(boolean condition, CharSequence expr) {
        traceErrorln(dbgAll, condition, expr);
    }

    public void traceErrorln(char flag, CharSequence expr) {
        traceErrorln(flag, true, expr);
    }

    public void traceErrorln(char flag, boolean condition, CharSequence expr) {
        traceErrorln(new char[] { flag }, condition, expr);
    }

    public void traceErrorln(String flagString, CharSequence expr) {
        traceErrorln(flagString.toCharArray(), true, expr);
    }

    public void traceErrorln(String flagString, boolean condition, CharSequence expr) {
        traceErrorln(flagString.toCharArray(), condition, expr);
    }

    public void traceErrorln(char[] flagArray, CharSequence expr) {
        traceErrorln(flagArray, true, expr);
    }

    public void traceErrorln(char[] flagArray, boolean condition, CharSequence expr) {
        traceError(flagArray, condition, expr + StringToolkit.newLine());
    }

    public static void ASSERT() {
        ASSERT(false);
    }

    /**
	* ASSERTION CHECK! print given error message
	* @param s assertion message
	*/
    public static void ASSERT(String s) throws RuntimeException {
        ASSERT(false, s);
    }

    /**
	* ASSERTION CHECK! print "ASSERTION FAILED"
	* @param con condition for ASSERTION
	*/
    public static void ASSERT(boolean con) throws RuntimeException {
        ASSERT(con, "ASSERTION FAILED");
    }

    public static void ASSERT_compareInteger(int size1, int size2) {
        if (size1 == size2) {
            return;
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("Number '").append(size1).append('\'');
        buffer.append("is different from '").append(size2).append('\'');
        throw new IllegalArgumentException(buffer.toString());
    }

    /**
	* ASSERTION CHECK! print expression if condition is true
	* @param con condition for ASSERTION
	* @param expression assertion message
	*/
    public static void ASSERT(boolean con, String expression) throws RuntimeException {
        if (con) return;
        throw new RuntimeException("ASSERTION FAILED : " + expression);
    }

    public static void ASSERT(Exception e) throws RuntimeException {
        ASSERT(e, "");
    }

    public static void ASSERT(Exception e, String expression) throws RuntimeException {
        System.err.print("ASSERTION FAILED");
        if (expression.length() > 0) System.out.print(" : " + expression);
        System.err.println("");
        e.toString();
        e.printStackTrace();
    }

    public static void die(String s) {
        die(false, s);
    }

    /**
	* ASSERTION CHECK! print "ASSERTION FAILED"
	* @param con condition for ASSERTION
	*/
    public static void die(boolean con) {
        die(con, "ASSERTION FAILED");
    }

    /**
	* ASSERTION CHECK! print expression if condition is true
	* @param con condition for ASSERTION
	* @param expression assertion message
	*/
    public static void die(boolean con, String expression) {
        try {
            ASSERT(con, expression);
        } catch (Exception e) {
            die(e);
        }
    }

    public static void die(Throwable t) {
        die(t, null);
    }

    public static void die(Throwable t, String expression) {
        if (expression != null) {
            System.err.println(expression);
        }
        System.err.println(t.toString());
        System.err.println(stackTraceToString(t.getStackTrace()));
        System.exit(-1);
    }

    public static void die(Exception e) throws RuntimeException {
        die(e, "");
    }

    /**
	 * @deprecated die(Exception e, String expression)
	 */
    public static void die(Exception e, String expression) throws RuntimeException {
        System.err.print("ASSERTION FAILED");
        if (expression.length() > 0) System.out.print(" : " + expression);
        System.err.println("");
        e.toString();
        e.printStackTrace();
        System.exit(-1);
    }

    public static String stackTraceToString(StackTraceElement[] elements) {
        StringBuffer buffer = new StringBuffer();
        for (StackTraceElement element : elements) {
            buffer.append(element.toString());
            buffer.append('\n');
        }
        return buffer.toString();
    }

    public static StackTraceElement[] getCurrentStackTrace() {
        return (new Exception()).getStackTrace();
    }

    public static void pinpoint(PrintStream out, String s) {
        out.println(s);
        out.println(stackTraceToString(getCurrentStackTrace()));
        out.flush();
    }

    public static void pinpoint(PrintStream out, Throwable t) {
        pinpoint(out, "", t);
    }

    public static void pinpoint(PrintStream out, String s, Throwable t) {
        out.println(s);
        out.println(t.toString());
        out.println(stackTraceToString(t.getStackTrace()));
        out.flush();
    }

    public boolean isFileOutputEnabled() {
        return fileOutputEnabled;
    }

    public void setFileOutputEnabled(boolean fileOutputEnabled) {
        this.fileOutputEnabled = fileOutputEnabled;
    }

    public static void isSame(int s1, int s2) {
        YeriDebug.ASSERT(s1 == s2, "Number different! : " + s1 + " vs " + s2);
    }

    public static boolean updateValidity(boolean original, boolean newValue, List<CharSequence> commentList, String comment) {
        if (newValue) {
            return original;
        }
        commentList.add(comment);
        return false;
    }
}
