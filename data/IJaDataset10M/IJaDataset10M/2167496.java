package net.bnubot.logging;

import java.io.PrintStream;
import net.bnubot.util.TimeFormatter;

/**
 * An {@link OutputLogger} that wraps a {@link PrintStream}
 * @author scotta
 */
public class PrintStreamOutputLogger implements OutputLogger {

    private static final String ERROR = "ERROR";

    private static final String INFO = "INFO";

    private static final String DEBUG = "DEBUG";

    private PrintStream out;

    public PrintStreamOutputLogger(PrintStream s) {
        this.out = s;
    }

    public void exception(Throwable e) {
    }

    public void error(Class<?> source, String text) {
        print(source, ERROR, text);
    }

    public void info(Class<?> source, String text) {
        print(source, INFO, text);
    }

    public void debug(Class<?> source, String text) {
        print(source, DEBUG, text);
    }

    private void print(Class<?> source, String type, String text) {
        out.println("[" + TimeFormatter.getTimestamp() + "] (" + source.getSimpleName() + ") " + type + " " + text);
    }
}
