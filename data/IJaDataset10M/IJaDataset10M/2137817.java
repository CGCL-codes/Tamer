package net.sf.jnclib.tp.ssh2;

/**
 * Simple {@link LogSink} implementation which throws away all of jaramiko's
 * log messages.
 */
public class NullLog implements LogSink {

    public void error(String text) {
    }

    public void warning(String text) {
    }

    public void notice(String text) {
    }

    public void debug(String text) {
    }

    public void dump(String text, byte[] data, int offset, int length) {
    }
}
