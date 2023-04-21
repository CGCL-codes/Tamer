package org.columba.ristretto.imap;

import org.columba.ristretto.concurrency.Mutex;
import org.columba.ristretto.imap.parser.IMAPResponseParser;
import org.columba.ristretto.imap.parser.MessageAttributeParser;
import org.columba.ristretto.io.AsyncInputStream;
import org.columba.ristretto.io.ConnectionDroppedException;
import org.columba.ristretto.io.TempSourceFactory;
import org.columba.ristretto.message.Attributes;
import org.columba.ristretto.parser.ParserException;
import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * InputStream to read IMAPReponses from an IMAP server. It handles all IMAP
 * specific features like literals in an transparent way.
 * 
 * @author tstich <tstich@users.sourceforge.net>
 */
public class IMAPInputStream extends FilterInputStream {

    private static final Pattern literalPattern = Pattern.compile("\\{(\\d+)\\}$");

    private IMAPProtocol protocol;

    private Matcher literalMatcher;

    private Mutex mutex;

    private StringBuffer lineBuffer;

    /**
	 * Constructs the IMAPInputStream.
	 * 
	 * @param in
	 *            the socket InputStream.
	 * @param protocol
	 *            the associated protocol class.
	 */
    public IMAPInputStream(InputStream in, IMAPProtocol protocol) {
        super(in);
        literalMatcher = literalPattern.matcher("");
        lineBuffer = new StringBuffer();
        final java.util.LinkedList observerList = new java.util.LinkedList();
        mutex = new Mutex();
        this.protocol = protocol;
    }

    /**
	 * Reads a body part from the asynchronously from the InputStream. This is
	 * done using the IMAPDownloadThread.
	 * 
	 * @see IMAPDownloadThread
	 * 
	 * @return the InputStream from the downloaded part
	 * @throws IOException
	 * @throws IMAPException
	 */
    public InputStream readBodyNonBlocking() throws IOException, IMAPException {
        mutex.lock();
        boolean dontrelease = false;
        try {
            IMAPResponse response;
            try {
                readLineInBuffer();
                response = IMAPResponseParser.parse(lineBuffer);
            } catch (ParserException e) {
                throw new IMAPException(e);
            }
            if (response.isTagged() && (response.isBAD() || response.isNO())) {
                mutex.release();
                throw new IMAPException(response);
            }
            while (!response.isTagged() && !response.getResponseSubType().equals("FETCH") && response.getResponseMessage().indexOf("BODY") == -1) {
                protocol.handleResponse(response);
                try {
                    readLineInBuffer();
                    response = IMAPResponseParser.parse(lineBuffer);
                } catch (ParserException e) {
                    mutex.release();
                    throw new IMAPException(e);
                }
            }
            if (response.isTagged() && (response.isBAD() || response.isNO())) {
                mutex.release();
                throw new IMAPException(response);
            }
            literalMatcher.reset(response.getResponseMessage());
            if (literalMatcher.find()) {
                int size = Integer.parseInt(literalMatcher.group(1));
                AsyncInputStream result = IMAPDownloadThread.asyncDownload(this, size, mutex);
                dontrelease = true;
                return result;
            } else {
                Attributes attributes = MessageAttributeParser.parse(response.getResponseMessage());
                String body = (String) attributes.get("BODY");
                if (body == null) {
                    mutex.release();
                    return new ByteArrayInputStream(new byte[0]);
                }
                if (body.length() > 0 && body.charAt(0) == '\"') {
                    body = body.substring(1, body.length() - 1);
                }
                try {
                    readLineInBuffer();
                } catch (IOException e1) {
                    mutex.release();
                    throw e1;
                }
                return new ByteArrayInputStream(body != null ? body.getBytes("US-ASCII") : new byte[0]);
            }
        } finally {
            if (dontrelease == false) {
                mutex.release();
            }
        }
    }

    /**
	 * Reads a response from the InputStream.
	 * 
	 * @return the read reponse
	 * @throws IOException
	 * @throws IMAPException
	 */
    public IMAPResponse readResponse() throws IOException, IMAPException {
        IMAPResponse response;
        mutex.lock();
        try {
            try {
                readLineInBuffer();
                response = IMAPResponseParser.parse(lineBuffer);
            } catch (ParserException e) {
                throw new IMAPException(e);
            }
            literalMatcher.reset(response.getResponseMessage());
            int literalIndex = 0;
            while (literalMatcher.find()) {
                int literalSize = Integer.parseInt(literalMatcher.group(1));
                response.setResponseMessage(response.getResponseMessage().substring(0, literalMatcher.start()) + '{' + (literalIndex++) + '}');
                response.addLiteral(TempSourceFactory.createTempSource(this, literalSize));
                readLineInBuffer();
                String restresponse = lineBuffer.toString();
                restresponse = restresponse.substring(0, restresponse.length() - 2);
                response.appendResponseText(restresponse);
                if (restresponse.length() > 3) {
                    literalMatcher.reset(response.getResponseMessage());
                }
            }
        } finally {
            mutex.release();
        }
        return response;
    }

    private void readLineInBuffer() throws IOException {
        lineBuffer.delete(0, lineBuffer.length());
        int read = in.read();
        while (read != '\r' && read != -1) {
            lineBuffer.append((char) read);
            read = in.read();
        }
        lineBuffer.append((char) read);
        read = in.read();
        if (read != '\n') throw new ConnectionDroppedException();
        lineBuffer.append((char) read);
    }

    /**
	 * Checks if the is a unread response waiting to be read. This can e.g.
	 * occur if the IMAP server sends a BYE response before closing the socket
	 * because of a idle timeout.
	 * 
	 * @return <code>true</code> there is response waiting to be read.
	 * @throws IOException
	 */
    public boolean hasUnsolicitedReponse() throws IOException {
        boolean result;
        mutex.lock();
        try {
            result = in.available() > 0;
        } finally {
            mutex.release();
        }
        return result;
    }
}
