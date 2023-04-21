package org.granite.logging;

/**
 * @author Franck WOLFF
 */
public interface LoggingFormatter {

    public String format(String message, Object... args);
}
