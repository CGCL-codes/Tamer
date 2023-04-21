package org.jpxx.mail.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 * The conversation of Server and client
 * 
 * @author Jun Li
 * @version $Revision: 0.0.1 $, $Date: 2008/04/30 08:57:00 $
 */
public abstract class AbstractSession implements Session {

    /**
     * Creates an instance of Logger and initializes it. It is to write log for
     * <code>AbstractSession</code>.
     */
    private Logger log = Logger.getLogger(AbstractSession.class);

    private BufferedReader br = null;

    private PrintWriter pw = null;

    private Socket socket = null;

    private HashMap<String, Object> map = null;

    protected String line = null;

    private int lastAction = 0;

    public AbstractSession() {
        map = new HashMap<String, Object>();
    }

    /**
     * @see org.jpxx.mail.Service.Session#getClinetSocket()
     * @return client socket
     */
    public Socket getClinetSocket() {
        return this.socket;
    }

    /**
     * @see org.jpxx.mail.Service.Session#setClientSocket(Socket socket)
     */
    public void setClientSocket(Socket socket) {
        this.socket = socket;
        String remoteIP = socket.getInetAddress().getHostAddress();
        String remoteHost = socket.getInetAddress().getHostName();
        try {
            this.pw = new PrintWriter(socket.getOutputStream(), true);
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            StringBuffer buffer = new StringBuffer(256);
            buffer.append("Cannot open connection from ");
            buffer.append(remoteHost);
            buffer.append("(");
            buffer.append(remoteIP);
            buffer.append("):");
            buffer.append(e.getMessage());
            log.error(buffer.toString());
        }
    }

    /**
     * 
     * @see org.jpxx.mail.Service.Session#readCommandLine()
     * @return a line of command string
     * @throws java.io.IOException
     * @since JMS 0.0.1
     */
    public final String readCommandLine() throws IOException {
        line = br.readLine();
        if (line != null) {
            line = line.trim();
        }
        return line;
    }

    /**
     * @see org.jpxx.mail.Service.Session#readLine()
     * @return a origin line of string
     * @throws java.io.IOException
     * @since JMS 0.0.3
     */
    public final String readLine() throws IOException {
        line = br.readLine();
        return line;
    }

    /**
     * @see org.jpxx.mail.Service.Session#writeResponse(String response)
     *      Response to client
     * 
     * @param response
     *            the string
     */
    public void writeResponse(String response) {
        pw.println(response);
        pw.flush();
    }

    /**
     * @see org.jpxx.mail.Service.Session#getOperation()
     * 
     * @return operation hasp map
     */
    public HashMap<String, Object> getOperation() {
        return this.map;
    }

    /**
     * @see org.jpxx.mail.Service.Session#addOperation(String name, Object
     *      value)
     * @param name
     *            Operation name
     * @param value
     *            Operation value.
     */
    public void addOperation(String name, Object value) {
        this.map.put(name, value);
    }

    /**
     * @see org.jpxx.mail.Service.Session#clear() Clear Operation which recoed
     *      in hasp map
     */
    public void clear() {
        this.map.clear();
    }

    /**
     * @see org.jpxx.mail.Service.Session#getLastAction() Get Last command
     *      value(Integer)
     * @return Get last command of SMTP session
     */
    public int getLastAction() {
        return this.lastAction;
    }

    /**
     * @see org.jpxx.mail.Service.Session#setLastAction(int lastAction) Set Last
     *      command value(Integer)
     * @param lastAction
     *            the last command of SMTP session
     */
    public void setLastAction(int lastAction) {
        this.lastAction = lastAction;
    }

    /**
     * An SMTP connection is terminated when the client sends a QUIT command.
     * The server responds with a positive reply code, after which it closes the
     * connection.
     * 
     * @see org.jpxx.mail.Service.Session#close() Close current session.
     */
    public void close() {
        try {
            if (pw != null) {
                pw.close();
            }
            if (br != null) {
                br.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * @see org.jpxx.mail.Service.Session#getCommandLine()
     * @return An object of CommandLine
     */
    public CommandLine getCommandLine() {
        return getCommandLine(line);
    }
}
