package com.gregor.mailping;

import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

/**
 * Implements the server side of an SMTP protocol and passes any received
 * message to an SMTPSink for processing.
 */
public class SMTPServerSocket implements Runnable {

    Socket socket;

    SMTPSink sink;

    static final int STATE_HELO = 1;

    static final int STATE_MAIL = 2;

    static final int STATE_RCPT = 3;

    static final int STATE_DATA = 4;

    static Logger logger = Logger.getLogger(SMTPServerSocket.class);

    /**
	 * Create a new server socket with specified socket and sink.
	 */
    public SMTPServerSocket(Socket socket, SMTPSink sink) {
        this.socket = socket;
        this.sink = sink;
    }

    /**
	 * Run the SMTP server loop.
	 *
	 * This method does not return until an IOException occurs or the
	 * remote host sends the "QUIT" commad.  It should be run in its
	 * own thread.
	 */
    public void run() {
        NDC.push(socket.getInetAddress().getHostAddress());
        try {
            SMTPServerLoop();
        } catch (IOException e) {
            logger.warn("Unexpected IOException", e);
        }
        NDC.pop();
    }

    void SMTPServerLoop() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        SMTPReceivedMessage m = new SMTPReceivedMessage();
        String t;
        String T = null;
        ArrayList message = null;
        int state = 1;
        m.setLocal(socket.getLocalAddress());
        m.setRemote(socket.getInetAddress());
        out.print("220 " + socket.getLocalAddress().getHostName() + " SMTPServerSocket\r\n");
        out.flush();
        while (true) {
            t = in.readLine();
            if (t == null) {
                break;
            }
            if (state != STATE_DATA) {
                T = parseCommand(t);
                if (T.equals("HELO")) {
                    out.print("250 Hello " + socket.getInetAddress().getHostName() + "\r\n");
                    out.flush();
                    continue;
                } else if (T.equals("RSET")) {
                    out.print("250 Ok\r\n");
                    out.flush();
                    state = STATE_HELO;
                    continue;
                } else if (T.equals("QUIT")) {
                    out.print("221 Bye\r\n");
                    out.flush();
                    socket.close();
                    return;
                }
            }
            switch(state) {
                case STATE_HELO:
                    {
                        if (T.equals("MAIL")) {
                            m.setSender(getAddress(t));
                            m.setRecipients(new String[0]);
                            message = new ArrayList();
                            out.print("250 Ok\r\n");
                            out.flush();
                            state = STATE_MAIL;
                        } else if (T.equals("RCPT")) {
                            out.print("503 need MAIL command\r\n");
                            out.flush();
                        } else if (T.equals("DATA")) {
                            out.print("503 no valid recipients specified\r\n");
                            out.flush();
                        } else {
                            out.print("501 Invalid command\r\n");
                            out.flush();
                        }
                        break;
                    }
                case STATE_MAIL:
                    {
                        if (T.equals("MAIL")) {
                            out.print("503 nested mail commands\r\n");
                            out.flush();
                        } else if (T.equals("RCPT")) {
                            m.addRecipient(getAddress(t));
                            out.print("250 Ok\r\n");
                            out.flush();
                            state = STATE_RCPT;
                        } else if (T.equals("DATA")) {
                            out.print("503 no valid recipients specified\r\n");
                            out.flush();
                        } else {
                            out.print("501 Invalid command\r\n");
                            out.flush();
                        }
                        break;
                    }
                case STATE_RCPT:
                    {
                        if (T.equals("MAIL")) {
                            out.print("503 nested mail commands\r\n");
                            out.flush();
                        } else if (T.equals("RCPT")) {
                            m.addRecipient(getAddress(t));
                            out.print("250 Ok\r\n");
                            out.flush();
                        } else if (T.equals("DATA")) {
                            out.print("354 End data with <CR><LF>.<CR><LF>\r\n");
                            out.flush();
                            state = STATE_DATA;
                        } else {
                            out.print("501 Invalid command\r\n");
                            out.flush();
                        }
                        break;
                    }
                case STATE_DATA:
                    {
                        if (t.equals(".")) {
                            m.setMessage((String[]) message.toArray(new String[0]));
                            m.setReceived(System.currentTimeMillis());
                            sink.sink(m);
                            out.print("250 Ok\r\n");
                            out.flush();
                            state = STATE_HELO;
                        } else {
                            message.add(t);
                        }
                        break;
                    }
            }
        }
    }

    String parseCommand(String line) {
        String command;
        int i;
        if ((i = line.indexOf(" ")) == -1) {
            command = line;
        } else {
            command = line.substring(0, i);
        }
        return command.toUpperCase();
    }

    String getAddress(String line) {
        String address;
        int i;
        if ((i = line.indexOf(":")) == -1) {
            return null;
        }
        return line.substring(i + 1).trim();
    }
}
