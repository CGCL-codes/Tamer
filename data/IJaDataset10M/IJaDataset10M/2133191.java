package com.ajdigital.chat.server.remote;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.lyrisoft.chat.IConnectionListener;
import com.lyrisoft.chat.server.remote.ChatServer;
import com.lyrisoft.chat.server.remote.ConnectionHandler;
import com.lyrisoft.chat.server.remote.ReaderThread;

/**
 * Adaptation of a ReaderThread to accept messages coming from a
 * Flash client that uses a null byte as it's message delimiter and
 * wraps it's messages in simple XML.
 * 
 * @author <A HREF="mailto:alon@ajdigital.com">Alon Salant</A>
 */
public class FlashReaderThread extends ReaderThread {

    /**
     * Input stream that we watch for new messages
     */
    protected InputStreamReader _instream;

    /**
     * String that starts XML wrapper
     */
    public static String XML_START = "<message value=\"";

    /**
     * String that ends XML wrapper
     */
    public static String XML_END = "\" />";

    /**
     * Use an InputStreamReader rather than BufferedReader from original ReaderThread
     * 
     * @param handler
     * @param listener
     * @param in
     */
    FlashReaderThread(ConnectionHandler handler, IConnectionListener listener, InputStream in) {
        super(handler, listener, in);
        _instream = new InputStreamReader(in);
    }

    public void normalRun() {
        try {
            StringBuffer newMsg = new StringBuffer();
            int i = 0;
            while (_keepGoing && (i = _instream.read()) != -1) {
                if (i == 0 || i == 10) {
                    ChatServer.debug("> " + newMsg.toString());
                    String msg = stripXML(newMsg.toString());
                    ChatServer.debug("! Using: " + msg);
                    _connectionListener.incomingMessage(msg);
                    newMsg = new StringBuffer();
                } else {
                    newMsg.append((char) i);
                }
            }
        } catch (IOException e) {
            ChatServer.log(e);
        }
    }

    /**
     * Look for null byte as message delimiter
     * We'll also accept \n so we can test w/ telnet
     */
    public void bsdHackRun() {
        try {
            StringBuffer newMsg = new StringBuffer();
            int i = 0;
            while (_keepGoing) {
                try {
                    while (_inputStream.available() < 1) {
                        Thread.sleep(25);
                    }
                } catch (InterruptedException e) {
                }
                i = _instream.read();
                if (i == 0 || i == 10) {
                    ChatServer.debug("> " + newMsg.toString());
                    String msg = stripXML(newMsg.toString());
                    ChatServer.debug("! Using: \"" + msg + "\"");
                    _connectionListener.incomingMessage(msg);
                    newMsg = new StringBuffer();
                } else {
                    newMsg.append((char) i);
                }
            }
        } catch (IOException e) {
            ChatServer.log(e);
        }
    }

    /**
     * Discard XML wrapper
     * 
     * @param msg    The full message
     * @return The message without XML in the format the rest of NFC likes to see
     */
    public static String stripXML(String msg) {
        if (msg.startsWith(XML_START)) {
            msg = msg.substring(XML_START.length(), msg.length() - XML_END.length());
        }
        return msg;
    }
}
