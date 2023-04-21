package bunny.x.nio.http;

import bunny.x.nio.ServerProcessor;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * A buffer that processes data as it is received.
 * It creates events for the custom HTTP processor to use.
 *
 * @author Christopher Ottley.
 */
public class HTTPServerProcessorBuffer implements ServerProcessor {

    /** The HTTP haders received. */
    private StringBuffer headers;

    /** The regular expression to extract the pattern. */
    private static Pattern eol = Pattern.compile("(.*)(\r?\n)", Pattern.CASE_INSENSITIVE);

    /** The processor event class. */
    private HTTPServerProcessorEvent pe;

    /** Set if a request line was received. */
    private boolean gotRequestLine = false;

    /** Set if all HTTP headers were received. */
    private boolean gotAllHeaders = false;

    /** The size of the HTTP body. */
    private int bodyDataSize = 0;

    /** The length of the entire content. */
    private int contentLength = 0;

    /** The current buffer that has been read from the network. */
    private ByteBuffer currentReadBuffer;

    /**
    * By default create a class of the processorClassName and cast it.
    * @param processorClassName The name of the HTTPServerProcessorEvent class.
    * @throws Exception If the processor event class cannot be created.
    *                   Or if the class is not a HTTPServerProcessorEvent.
    */
    public HTTPServerProcessorBuffer(final String processorClassName) throws Exception {
        Class customHTTPProcessor = Class.forName(processorClassName);
        headers = new StringBuffer("");
        pe = (HTTPServerProcessorEvent) customHTTPProcessor.newInstance();
        pe.setBaseProcessor(this);
    }

    /**
    * Event that is called when a client is accepted.
    * @param incomingChannel The client's channel.
    */
    public final void onServerAccept(final SocketChannel incomingChannel) {
        pe.onReceiveAccept(incomingChannel);
    }

    /**
    * Event that is called when the client sends some data that is read.
    * @param readBuffer The buffer that is read.
    */
    public final void onServerRead(final ByteBuffer readBuffer) {
        currentReadBuffer = readBuffer.duplicate();
        if (gotAllHeaders) {
            bodyDataSize += currentReadBuffer.limit();
            pe.onReceiveBodyData(currentReadBuffer);
            if (bodyDataSize >= contentLength) {
                pe.onReceiveAllBodyData();
            }
        }
    }

    /**
    * Event that is called when the client sends some data.
    * Data is converted to a string using the encoding of the superclass.
    * @param readString The sent data as a string.
    */
    public final void onServerRead(final String readString) {
        if (!gotAllHeaders) {
            headers.append(readString);
            processBuffer();
        }
    }

    /**
    * Event that is called when the client closes the connection.
    */
    public void onServerClose() {
    }

    /**
    * Seperates the Headers from the body that the client has sent.
    * Calls events as it processes the buffered data.
    */
    private void processBuffer() {
        Matcher hbMatcher = eol.matcher(headers.toString());
        while (hbMatcher.find()) {
            String foundMatch = hbMatcher.group(1);
            if (!foundMatch.equals("")) {
                if (gotRequestLine) {
                    HTTPHeader h = new HTTPHeader(foundMatch);
                    if (h.header().equalsIgnoreCase("content-length")) {
                        contentLength = h.intValue();
                    }
                    pe.onReceiveHeader(h);
                } else {
                    pe.onReceiveRequestLine(new HTTPRequestLine(foundMatch));
                    gotRequestLine = true;
                }
            } else {
                gotAllHeaders = true;
                pe.onReceiveAllHeaders();
                if (bodyDataSize >= contentLength) {
                    pe.onReceiveAllBodyData();
                } else {
                    if (headers.length() > 0) {
                        try {
                            headers.delete(0, hbMatcher.group(2).length());
                            ByteBuffer body = ByteBuffer.wrap(headers.toString().getBytes("US-ASCII"));
                            currentReadBuffer = null;
                            onServerRead(body);
                        } catch (UnsupportedEncodingException ignored) {
                            System.err.println("Encoding exception in the HTTP server processor buffer...");
                        }
                        headers.delete(0, headers.length());
                    }
                }
            }
            headers.delete(0, hbMatcher.end());
        }
    }
}
