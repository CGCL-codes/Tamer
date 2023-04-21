package org.iqual.chaplin.example.pump;

/**
 * @author Zbynek Slajchrt
 * @since Jun 28, 2009 9:26:17 PM
 */
public interface Writer {

    void write(byte[] buffer, int off, int len) throws Exception;

    void close() throws Exception;
}
