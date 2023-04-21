package net.sourceforge.herald;

import java.io.*;
import java.net.*;
import java.util.*;
import net.sourceforge.util.*;

/**
 * This class represents Message Packets as specified by the 
 * MSN Messenger V1.0 protocol.
 *
 * @author  Chad Gibbons
 */
public class MessagePacket extends MessengerPacket {

    protected String userHandle;

    protected String friendlyName;

    protected int length;

    protected String rawData;

    protected Hashtable headers;

    protected byte[] dataBuffer;

    private MessageLogSingleton log = MessageLogSingleton.instance();

    public MessagePacket(String rawPacket) throws ProtocolException {
        init(rawPacket);
    }

    protected void init(String rawPacket) throws ProtocolException {
        this.rawPacket = rawPacket;
        try {
            StringTokenizer t = new StringTokenizer(rawPacket);
            command = t.nextToken();
            userHandle = t.nextToken();
            friendlyName = t.nextToken();
            try {
                length = Integer.parseInt(t.nextToken());
            } catch (NumberFormatException ex) {
                throw new ProtocolException("invalid message length");
            }
        } catch (NoSuchElementException ex) {
            throw new ProtocolException("malformed packet");
        }
    }

    public String getUserHandle() {
        return userHandle;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public int getLength() {
        return length;
    }

    public void setRawData(char[] rawData) {
        this.rawData = new String(rawData);
        parse(this.rawData);
    }

    protected void parse(String buf) {
        int n = buf.indexOf(CRLF2);
        parseHeaders(buf.substring(0, n));
        parseData(buf.substring(n + CRLF2.length()));
    }

    protected void parseHeaders(String headers) {
        try {
            StringTokenizer st = new StringTokenizer(headers, "\r\n");
            String key = "";
            String value = "";
            while (st.hasMoreTokens()) {
                String line = st.nextToken();
                StringTokenizer subTokens = new StringTokenizer(line, ":");
                key = subTokens.nextToken();
                value = subTokens.nextToken().trim();
                headerFields.put(key, value);
            }
        } catch (Exception ex) {
            log.append("Unable to process message headers: " + ex.getMessage());
        }
    }

    protected void parseData(String data) {
        try {
            dataBuffer = data.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            log.append("Fatal Exception: " + ex.getMessage());
            System.exit(1);
        }
    }

    public byte[] getMessageData() {
        return dataBuffer;
    }

    protected static final String CRLF2 = "\r\n\r\n";

    protected static final String LF2 = "\n\n";

    protected Hashtable headerFields = new Hashtable();

    protected static final String CONTENT_LENGTH = "Content-Length";

    public int getContentLength() {
        if (headerFields.containsKey(CONTENT_LENGTH)) {
            return Integer.parseInt((String) headerFields.get(CONTENT_LENGTH));
        } else {
            return -1;
        }
    }

    public String getHeaderData(String headerKey) {
        return (String) headerFields.get(headerKey);
    }
}
