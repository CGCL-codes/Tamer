package jtcpfwd.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

public class Watchdog {

    private static SecureRandom rnd = new SecureRandom();

    final int timeout, initialtimeout;

    private long nextTimeout;

    private boolean disposed;

    public Watchdog(String rule) {
        int pos = rule.indexOf(',');
        if (rule.length() == 0) {
            timeout = initialtimeout = 0;
        } else if (pos == -1) {
            timeout = Integer.parseInt(rule);
            initialtimeout = 0;
        } else {
            timeout = Integer.parseInt(rule.substring(0, pos));
            initialtimeout = Integer.parseInt(rule.substring(pos + 1));
        }
        nextTimeout = initialtimeout == 0 ? Long.MAX_VALUE : System.currentTimeMillis() + initialtimeout;
        new Thread(new TimeoutHandler(this)).start();
    }

    private synchronized void setNextTimeout(long value) {
        nextTimeout = disposed ? Long.MAX_VALUE : value;
        notifyAll();
    }

    public Socket createSocket() {
        try {
            final PipedSocketImpl socket = new PipedSocketImpl();
            new Thread(new SocketHandler(this, socket)).start();
            return socket.createSocket();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public synchronized void dispose() {
        disposed = true;
        nextTimeout = Long.MAX_VALUE;
        notifyAll();
    }

    public static final class SocketHandler implements Runnable {

        private final Watchdog dog;

        private final PipedSocketImpl socket;

        public SocketHandler(Watchdog dog, PipedSocketImpl socket) {
            this.dog = dog;
            this.socket = socket;
        }

        public void run() {
            try {
                MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
                DataInputStream in = new DataInputStream(socket.getLocalInputStream());
                OutputStream out = new DataOutputStream(socket.getLocalOutputStream());
                byte[] randomOut = new byte[128];
                rnd.nextBytes(randomOut);
                out.write(randomOut);
                out.flush();
                byte[] randomIn = new byte[128];
                in.readFully(randomIn);
                sha1.reset();
                byte[] digestOut = sha1.digest(randomOut);
                sha1.reset();
                byte[] digestIn = sha1.digest(randomIn);
                out.write(digestOut);
                out.flush();
                byte[] digestInReal = new byte[digestIn.length];
                in.readFully(digestInReal);
                if (!Arrays.equals(digestIn, digestInReal)) {
                    System.out.println("Watchdog digest does not match!");
                    if (dog.timeout != 0) dog.setNextTimeout(0);
                } else if (dog.timeout != 0) {
                    dog.setNextTimeout(System.currentTimeMillis() + dog.timeout);
                }
                out.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                if (dog.timeout != 0) dog.setNextTimeout(0);
            }
        }
    }

    public static final class TimeoutHandler implements Runnable {

        private final Watchdog dog;

        TimeoutHandler(Watchdog dog) {
            this.dog = dog;
        }

        public void run() {
            synchronized (dog) {
                while (!dog.disposed) {
                    long now = System.currentTimeMillis();
                    if (dog.nextTimeout < now) {
                        System.exit(42);
                    }
                    long remaining = dog.nextTimeout - now - 1000;
                    if (remaining < 100) remaining = 100;
                    try {
                        dog.wait(remaining);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
