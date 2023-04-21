package com.anaxima.eslink.server;

/**
 * Interface to listen for log line output.
 * <p/>
 * @author Thomas Vater
 */
public interface ILogListener {

    /**
	 * Recieve a new piece of log text. This may be an arbitrary
	 * text fragment. Take <b>special note</b> that there is no
	 * guarenty that the given text will end at a line terminater.
	 * <p/> 
	 * @param argText Log text to recieve.
	 */
    void recieveLog(String argText);

    /**
	 * Close and terminate this log listener. After this method
	 * gets called there are no more calls to {@link #recieveLog(String)}.
	 */
    void closeLog();
}
