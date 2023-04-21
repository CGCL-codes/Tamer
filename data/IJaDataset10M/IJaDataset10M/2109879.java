package net.sf.xsdutils;

/**
 * a default ProgressLogger implementation, does nothing
 */
public class NullLogger implements ProgressLogger {

    public void info(String msg) {
    }

    public void warn(String msg) {
    }
}
