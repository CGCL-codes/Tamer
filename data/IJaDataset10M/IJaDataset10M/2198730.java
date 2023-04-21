package net.sf.atmodem4j.core;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author aploese
 */
public interface Connection {

    public Modem getModem();

    InputStream getInputStream();

    OutputStream getOutputStream();

    void disconnect();

    void exitDataMode();

    void reenterDataMode();

    boolean isConnected();

    boolean isOnlineDataMode();

    boolean isOnlineCommandMode();
}
