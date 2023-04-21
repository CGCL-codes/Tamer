package com.nibble.managers;

import com.nibble.tools.Tk;

/**
 * This manager class provides easy printing functionality like printing pre-configured banners and lines to the
 * console. Also converts all line feeds '\n' to system independent values.<br />
 * <br />
 * More importantly, this manager prints using modes, which is very handy when working with alot of code that need
 * different kind of levels of output. Output is printed to the console if its level is the same as or lower than the
 * current printing mode.
 * 
 * @author B. Bottema
 */
public class LogConsoleManager implements Logger {

    /**
	 * A mode indicator that can be used by other classes When writing to the console, this mode is checked with the
	 * mode passed to the writing shortcut function
	 */
    private Mode mode = Mode.SILENT;

    /**
	 * A simple line used several times (ie, headers).
	 */
    protected static final String LINE = "----------------------------------------------------------------";

    protected static final String NONE = "|-1";

    /**
	 * Constructor: intializes the mode for this logger.
	 * 
	 * @param m A Mode to configure this logger with.
	 */
    public LogConsoleManager(final Mode m) {
        setMode(m);
    }

    public void setMode(final Mode m) {
        mode = m;
    }

    public Mode getMode() {
        return mode;
    }

    /**
	 * LogConsoleManager logging implementation doesn't use buffering.
	 */
    public void startLog() {
    }

    public String write(final String s, final Mode m) {
        if (m.ordinal() <= mode.ordinal()) {
            return Tk.w(s);
        }
        return NONE;
    }

    public final void writebanner(final String banner, final Mode m) {
        write("\n" + LINE, m);
        write(" " + banner, m);
        write(LINE, m);
    }

    public final void writeSeperatorLine(final Mode m) {
        write(LINE + "\n", m);
    }

    public final void writeError(final Exception e, final boolean printStacktrace) {
        writebanner("Error log:", Mode.SILENT);
        write(Tk.indentMessage(e) + "\n", Mode.SILENT);
        if (printStacktrace) {
            final StackTraceElement[] StackTrace = e.getStackTrace();
            for (final StackTraceElement aStackTrace : StackTrace) {
                write("	" + aStackTrace.toString(), Mode.SILENT);
            }
        }
        writeSeperatorLine(Mode.SILENT);
    }

    /**
	 * LogConsoleManager logging implementation doesn't use buffering.
	 */
    public void flushLog() {
    }
}
