package net.sf.mailsomething.mail.parsers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

/**
 * 
 * A protocol adapter. I couldnt find a better name. Has methods which all
 * session classes use.
 * 
 * 
 * 
 * @author Stig Tanggaard
 *
 * 
 */
public class ProtocolAdapter {

    protected InputStream in;

    protected BufferedWriter writer;

    protected BufferedReader reader;

    protected BufferedOutputStream bufferedOut;

    private Vector conversationListeners;

    public ProtocolAdapter() {
        conversationListeners = new Vector();
    }

    /**
	 * For setting the inputstream to be used by this session. This
	 * method is introduced to make it possible to have different
	 * kinda streams and/or sockets. And making this transparent to
	 * the session class.
	 * 
	 * 
	 */
    public void setInputStream(InputStream stream) {
        this.in = new BufferedInputStream(stream);
        reader = new BufferedReader(new InputStreamReader(in));
    }

    public void setOutputStream(OutputStream stream) {
        try {
            bufferedOut = new BufferedOutputStream(stream);
            writer = new BufferedWriter(new OutputStreamWriter(stream, "ascii"));
        } catch (IOException f) {
            f.printStackTrace();
        }
    }

    protected synchronized void writeLine(String command) {
        try {
            writer.write(command, 0, command.length());
            writer.write(13);
            writer.write(10);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized String readLine() throws IOException {
        String buff = null;
        buff = reader.readLine();
        return (buff);
    }

    public synchronized String readCRLFTerminatedLine() {
        byte[] buffer = new byte[4096];
        byte CR = (byte) 13;
        byte LF = (byte) 10;
        int i = 0;
        try {
            int b;
            while ((b = in.read()) != -1) {
                buffer[i++] = (byte) b;
                if (b == LF) {
                    if (i > 1 && buffer[i - 2] == CR) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
        }
        byte[] byteline;
        if (i > 1) byteline = new byte[i - 2]; else byteline = new byte[0];
        if (i > 1) System.arraycopy(buffer, 0, byteline, 0, i - 2);
        String line = null;
        try {
            line = new String(byteline, "ASCII");
        } catch (UnsupportedEncodingException e) {
        }
        return line;
    }

    public synchronized String write(String command) throws IOException {
        notifyTo(command);
        writeLine(command);
        String reply = reader.readLine();
        notifyFrom(reply);
        return reply;
    }

    /**
	 * Method to add a conversationlistener which recieves events when
	 * commands are send to or recieved from server.
	 * Note, this can NOT be used for validating replies, since u cant
	 * rely on the notification to happen. It is done primarely to allow
	 * an easy way to monitor the conversation, for example, where a 
	 * conversation with the server consists of multiple to/from events.
	 * ie. 
	 * Client: dothis
	 * Server: Okay, dothat
	 * Client: imdoingthat
	 * Server: Okay, accepted.
	 * 
	 * 
	 */
    public void addConversationListener(ConversationListener l) {
        if (conversationListeners.size() > 0) return;
        conversationListeners.add(l);
    }

    /**
	 * Should be called to notify listeners of conversation
	 * This scheme is done to simplify the observation of conversation, ie
	 * to separate this from the actual parsing of replies. 
	 * 
	 */
    protected void notifyTo(String string) {
        if (conversationListeners == null) return;
        for (int i = 0; i < conversationListeners.size(); i++) {
            ((ConversationListener) conversationListeners.elementAt(i)).to(string);
        }
    }

    protected void notifyFrom(String string) {
        if (conversationListeners == null) return;
        for (int i = 0; i < conversationListeners.size(); i++) {
            ((ConversationListener) conversationListeners.elementAt(i)).from(string);
        }
    }
}
