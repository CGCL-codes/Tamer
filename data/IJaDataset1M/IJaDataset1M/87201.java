package org.nakedobjects.metamodel.commons.logging;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.TriggeringEventEvaluator;

public class SmtpSnapshotAppender extends SnapshotAppender {

    private static final Logger LOG = Logger.getLogger(SmtpSnapshotAppender.class);

    private String server;

    private String recipient;

    private int port = 25;

    private String senderDomain = "domain";

    public SmtpSnapshotAppender(final TriggeringEventEvaluator evaluator) {
        super(evaluator);
    }

    public SmtpSnapshotAppender() {
        super();
    }

    public void setServer(final String mailServer) {
        if (mailServer == null) {
            throw new NullPointerException("mail server cannot be null");
        }
        this.server = mailServer;
    }

    public void setRecipient(final String recipient) {
        if (recipient == null) {
            throw new NullPointerException("recipient cannot be null");
        }
        this.recipient = recipient;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public void setSenderDomain(final String senderDomain) {
        if (senderDomain == null) {
            throw new NullPointerException("sender domain cannot be null");
        }
        this.senderDomain = senderDomain;
    }

    @Override
    protected void writeSnapshot(final String message, final String details) {
        try {
            if (server == null) {
                throw new NullPointerException("mail server must be specified");
            }
            if (recipient == null) {
                throw new NullPointerException("recipient must be specified");
            }
            final Socket s = new Socket(server, port);
            final BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), "8859_1"));
            final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), "8859_1"));
            send(in, out, "HELO " + senderDomain);
            send(in, out, "MAIL FROM: <no-reply@nakedobjects.org>");
            send(in, out, "RCPT TO: " + recipient);
            send(in, out, "DATA");
            send(out, "Subject: " + message);
            send(out, "From: Autosend");
            send(out, "Content-Type: " + layout.getContentType());
            send(out, "\r\n");
            send(out, details);
            send(out, "\r\n.\r\n");
            send(in, out, "QUIT");
            s.close();
        } catch (final Exception e) {
            LOG.info("failed to send email with log", e);
        }
    }

    private void send(final BufferedReader in, final BufferedWriter out, final String s) throws IOException {
        out.write(s + "\r\n");
        out.flush();
        in.readLine();
    }

    private void send(final BufferedWriter out, final String s) throws IOException {
        out.write(s + "\r\n");
        out.flush();
    }
}
