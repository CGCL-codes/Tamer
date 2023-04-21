package org.gamegineer.client.ui.console;

import java.io.Flushable;
import java.io.PrintWriter;
import java.io.Reader;

/**
 * Represents a character-based console display device.
 * 
 * <p>
 * This interface is not intended to be implemented or extended by clients.
 * </p>
 */
public interface IDisplay extends Flushable {

    /**
     * Flushes the display and forces any buffered output to be written
     * immediately.
     */
    public void flush();

    public IDisplay format(String format, Object... args);

    public Reader getReader();

    public PrintWriter getWriter();

    public String readLine();

    public String readLine(String format, Object... args);

    public char[] readSecureLine();

    public char[] readSecureLine(String format, Object... args);
}
