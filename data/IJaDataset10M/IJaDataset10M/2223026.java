package jtcpfwd.listener.knockrule;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import jtcpfwd.util.BinaryExpression;
import jtcpfwd.util.MaskedBinaryExpression;
import jtcpfwd.util.NumberExpression;

public class TCPKnockRule extends PatternBasedKnockRule implements Runnable {

    public static final String SYNTAX = "TCP#<port>[/<timeout>][#<bytes>[#<response>]]";

    public static final Class[] getRequiredClasses() {
        return new Class[] { PatternBasedKnockRule.class, BinaryExpression.class, MaskedBinaryExpression.class, NumberExpression.class, NumberExpression.NumberRange.class, SocketThread.class };
    }

    private final int port, timeout;

    private ServerSocket ss;

    public TCPKnockRule(String[] args) throws Exception {
        super(args, 1);
        String arg = args[0];
        int pos = arg.indexOf('/');
        if (pos != -1) {
            timeout = Integer.parseInt(arg.substring(pos + 1));
            arg = arg.substring(0, pos);
        } else {
            timeout = 0;
        }
        port = Integer.parseInt(arg);
    }

    public void listen() throws IOException {
        ss = new ServerSocket(port);
        new Thread(this).start();
    }

    public void tryDispose() throws IOException {
        if (ss != null) ss.close();
    }

    public void run() {
        try {
            while (true) {
                Socket s = ss.accept();
                if (timeout != 0) {
                    s.setSoTimeout(timeout);
                }
                new SocketThread(s, this);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void trigger(InetAddress peer) throws IOException {
        Socket s = new Socket(peer, port);
        s.getOutputStream().write(matchExpression.getSampleValue());
        if (response != null) {
            byte[] bytes = new byte[response.length];
            new DataInputStream(s.getInputStream()).readFully(bytes);
            if (!Arrays.equals(response, bytes)) throw new IOException("Trigger failed");
        }
        s.close();
    }

    boolean check(Socket s, byte[] bytes) throws IOException {
        if (matchExpression.matches(bytes)) {
            addAddress(s.getInetAddress());
            if (response != null) {
                s.getOutputStream().write(response);
                s.close();
                return true;
            }
        }
        return false;
    }

    public static class SocketThread extends Thread {

        private Socket s;

        private final TCPKnockRule rule;

        SocketThread(Socket s, TCPKnockRule rule) {
            this.s = s;
            this.rule = rule;
            start();
        }

        public void run() {
            try {
                try {
                    InputStream in = s.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    if (in.available() == 0 && rule.check(s, baos.toByteArray())) return;
                    byte[] buf = new byte[32];
                    int len;
                    while ((len = in.read(buf)) != -1) {
                        baos.write(buf, 0, len);
                        if (in.available() == 0 && rule.check(s, baos.toByteArray())) return;
                    }
                } catch (SocketTimeoutException ex) {
                    s.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
