package com.dokumentarchiv.pop;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.mail.MessagingException;
import javax.mail.Session;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.dokumentarchiv.EEMProxy;
import com.dokumentarchiv.ProxyWorker;
import com.dokumentarchiv.filter.FilterGroup;
import de.inovox.AdvancedMimeMessage;

/**
 * Worker implementation that handles POP3 communication
 * 
 * @author Carsten Burghardt
 * @version $Id: POP3ProxyWorker.java 613 2008-03-12 21:40:35Z carsten $
 */
public class POP3ProxyWorker extends ProxyWorker {

    private static Log log = LogFactory.getLog(POP3ProxyWorker.class);

    /**
     * @param sock
     * @param server
     * @param port
     * @param filter
     * @param encoding
     * @param config
     */
    public POP3ProxyWorker(Socket sock, InetAddress server, int port, FilterGroup filter, String encoding, Configuration config) {
        super(sock, server, port, filter, encoding, config);
    }

    /**
     * @param importDir
     * @param filter
     * @param config
     */
    public POP3ProxyWorker(File importDir, FilterGroup filter, Configuration config) {
        super(importDir, filter, config);
    }

    public void run() {
        if (client != null) {
            processSocket();
        } else {
            log.error("No valid params given");
        }
    }

    /**
     * Processes data of the client socket
     */
    protected void processSocket() {
        BufferedReader cis = null;
        DataOutputStream cos = null;
        BufferedReader sis = null;
        DataOutputStream sos = null;
        try {
            cis = new BufferedReader(new InputStreamReader(client.getInputStream(), encoding));
            cos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream(), BUFFER_MAX));
        } catch (IOException io) {
            log.error("Failed to create client streams", io);
            close();
            return;
        }
        Socket out = null;
        try {
            out = new Socket(targetServer, targetPort);
            sis = new BufferedReader(new InputStreamReader(out.getInputStream(), encoding));
            sos = new DataOutputStream(new BufferedOutputStream(out.getOutputStream(), BUFFER_MAX));
            readReply(sis, cos);
        } catch (IOException io) {
            log.error("Failed to create server streams", io);
            try {
                cos.writeBytes("421 " + EEMProxy.getHelo() + ". POP3 Proxy temporary unavailable.\r\n");
                cos.close();
            } catch (IOException ignore) {
            } finally {
                close();
            }
            return;
        }
        String req;
        while (true) {
            try {
                req = cis.readLine();
                if (req == null) break;
                StringTokenizer st = new StringTokenizer(req);
                if (!st.hasMoreTokens()) {
                    send("500 Syntax error - No command entered.", cos);
                    continue;
                }
                String com = st.nextToken().toUpperCase();
                if (com.equals("QUIT")) {
                    send("QUIT", sos);
                    sos.close();
                    send("221 " + EEMProxy.getHelo() + ". POP3 Proxy connection closed.", cos);
                    close();
                    return;
                }
                if (com.equals("RETR")) {
                    send(req, sos);
                    String tmp = sis.readLine();
                    if (tmp == null) throw new EOFException();
                    cos.writeBytes(tmp);
                    cos.writeBytes("\r\n");
                    cos.flush();
                    if (tmp.startsWith("-ERR")) {
                        continue;
                    }
                    StringBuffer buffer = new StringBuffer();
                    String line;
                    while ((line = sis.readLine()) != null) {
                        cos.writeBytes(line);
                        cos.writeBytes("\r\n");
                        if (line.equals(".")) {
                            break;
                        }
                        buffer.append(line + "\r\n");
                    }
                    cos.flush();
                    byte[] bytes = buffer.toString().getBytes();
                    ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
                    Session session = Session.getDefaultInstance(new Properties());
                    AdvancedMimeMessage message = null;
                    try {
                        message = new AdvancedMimeMessage(session, stream);
                    } catch (MessagingException e) {
                        log.error("Failed to create Message", e);
                        continue;
                    }
                    processMessage(message);
                    continue;
                }
                if (com.equals("STARTTLS")) {
                    send("500 STARTTLS is not implemented.", cos);
                    continue;
                }
                send(req, sos);
                if (isMultiline(com)) {
                    readMultilineReply(sis, cos);
                } else {
                    readReply(sis, cos);
                }
            } catch (IOException io) {
                log.error("IO Error", io);
                try {
                    out.close();
                } catch (IOException ignore) {
                }
                close();
                return;
            }
        }
    }

    /**
     * Returns true if the command has a multi-line answer
     * @param com
     * @return
     */
    private boolean isMultiline(String com) {
        if (com.equals("LIST") || com.equals("UIDL") || com.equals("TOP")) {
            return true;
        }
        return false;
    }

    /**
     * Read the answer from the target server
     * @param di
     * @param d
     * @throws IOException
     */
    protected void readReply(BufferedReader di, DataOutputStream d) throws IOException {
        String line = di.readLine();
        if (line == null) throw new EOFException();
        d.writeBytes(line);
        d.writeBytes("\r\n");
        d.flush();
    }

    /**
     * Read the answer from the target server
     * @param di
     * @param d
     * @throws IOException
     */
    protected void readMultilineReply(BufferedReader di, DataOutputStream d) throws IOException {
        String line;
        while (true) {
            line = di.readLine();
            if (line == null) throw new EOFException();
            d.writeBytes(line);
            d.writeBytes("\r\n");
            if (line.equals(".") || line.startsWith("-ERR")) {
                break;
            }
        }
        d.flush();
    }
}
