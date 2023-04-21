package com.hs.mail.smtp.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.StringTokenizer;
import com.hs.mail.container.config.Config;
import com.hs.mail.container.server.socket.TcpTransport;
import com.hs.mail.io.CharTerminatedInputStream;
import com.hs.mail.io.MessageSizeException;
import com.hs.mail.io.SizeLimitedInputStream;
import com.hs.mail.smtp.SmtpException;
import com.hs.mail.smtp.SmtpSession;
import com.hs.mail.smtp.message.SmtpMessage;

/**
 * Handler for DATA command. Reads in message data, creates header, and store to
 * message spool for delivery.
 * 
 * @author Won Chul Doh
 * @since May 29, 2010
 * 
 */
public class DataProcessor extends AbstractSmtpProcessor {

    /**
	 * The character array that indicates termination of an SMTP connection
	 */
    private static final char[] DATA_TERMINATOR = { '\r', '\n', '.', '\r', '\n' };

    @Override
    protected void doProcess(SmtpSession session, TcpTransport trans, StringTokenizer st) throws SmtpException {
        SmtpMessage message = session.getMessage();
        if (message == null || message.getFrom() == null) {
            throw new SmtpException(SmtpException.COMMAND_OUT_OF_SEQUENCE);
        }
        if (message.getRecipientsSize() == 0) {
            throw new SmtpException(SmtpException.NO_VALID_RECIPIENTS);
        }
        if (st.hasMoreTokens()) {
            throw new SmtpException(SmtpException.INVALID_COMMAND_PARAM);
        }
        session.writeResponse("354 Start mail input; end with <CRLF>.<CRLF>");
        long maxMessageSize = Config.getMaxMessageSize();
        InputStream msgIn = null;
        try {
            String received = new StringBuilder().append("Received: from ").append(session.getClientDomain()).append(" (").append(session.getRemoteHost()).append(" [").append(session.getRemoteIP()).append("])\r\n").append("\tby ").append(Config.getHelloName()).append(" with ").append(session.getProtocol()).append(" id ").append(session.getSessionID()).append(";\r\n").append("\t").append(message.getDate()).append("\r\n").toString();
            msgIn = new PushbackInputStream(trans.getInputStream(), received.length());
            ((PushbackInputStream) msgIn).unread(received.getBytes("ASCII"));
            if (maxMessageSize > 0) {
                msgIn = new SizeLimitedInputStream(msgIn, maxMessageSize);
            }
            message.setContent(new CharTerminatedInputStream(msgIn, DATA_TERMINATOR));
            message.store();
            message.createTrigger();
            session.setMessage(null);
            session.writeResponse("250 2.6.0 OK");
        } catch (IOException e) {
            message.dispose();
            if (e instanceof MessageSizeException) {
                StringBuilder errorBuffer = new StringBuilder(256).append("Rejected message from ").append(session.getClientDomain()).append(" from host ").append(session.getRemoteHost()).append(" (").append(session.getRemoteIP()).append(") exceeding system maximum message size of ").append(maxMessageSize);
                logger.error(errorBuffer.toString());
                receiveJunkData((SizeLimitedInputStream) msgIn);
                throw new SmtpException("552 5.3.4 Error processing message: " + e.getMessage());
            } else {
                StringBuilder errorBuffer = new StringBuilder(256).append("Unknown error occurred while processing DATA.");
                logger.error(errorBuffer.toString(), e);
                throw new SmtpException("451 4.0.0 Error processing message: " + e.getMessage());
            }
        }
    }

    private void receiveJunkData(SizeLimitedInputStream msgIn) {
        try {
            InputStream in = msgIn.getInputStream();
            while (in.read() != -1) {
            }
        } catch (IOException e) {
        }
    }
}
