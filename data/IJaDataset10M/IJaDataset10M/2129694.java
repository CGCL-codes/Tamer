package com.teletalk.jserver.tcp.messaging;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Message writer implementation for sending a byte array message body.
 * 
 * @author Tobias L�fstrand
 * 
 * @since 1.2
 */
public final class ByteArrayMessageWriter implements MessageWriter {

    private final byte[] byteArrayMessageBody;

    /**
	 * Creates a new ByteArrayMessageWriter for writing the specified byte array message body.
	 * 
	 * @param byteArrayMessageBody the message body to dispatch.
	 */
    public ByteArrayMessageWriter(final byte[] byteArrayMessageBody) {
        this.byteArrayMessageBody = byteArrayMessageBody;
    }

    /**
	 * Called to write a message (header and body) to an endpoint.
	 * 
	 * @param header the header of the message that is to be dispatched.
    * @param endPoint the endpoint on which the message is to be dispatched on.
    * @param endPointOutputStream the output stream of the endpoint on which the message is to be written to.
	 */
    public void writeMessage(MessageHeader header, MessagingEndPoint endPoint, OutputStream endPointOutputStream) throws IOException {
        int bodyLength = (byteArrayMessageBody != null) ? byteArrayMessageBody.length : 0;
        header.setBodyLength(bodyLength);
        if (endPoint.getDestination().getProtocolVersion() >= 4) {
            endPoint.dispatchHeader(header);
            if (endPoint.isDebugMode()) endPoint.logDebug("Sending byte array message with header " + header + ".");
        } else {
            endPoint.dispatchHeader(header);
            if (endPoint.isDebugMode()) endPoint.logDebug("Sending byte array message with header " + header + ".");
            endPoint.resetObjectSerializer(true, false);
        }
        if (bodyLength > 0) {
            endPointOutputStream.write(byteArrayMessageBody);
        }
        if (endPoint.isDebugMode()) endPoint.logDebug("Done sending byte array message with header " + header + ".");
    }

    /**
	 * Gets a description of the message body (for debug).
	 */
    public String getDescription() {
        return null;
    }
}
