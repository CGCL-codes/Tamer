package org.opennms.jicmp.jna;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

class Server implements Runnable {

    private AtomicReference<Throwable> m_exception = new AtomicReference<Throwable>();

    private AtomicBoolean m_stopped = new AtomicBoolean(false);

    private CountDownLatch m_latch = new CountDownLatch(1);

    private Thread m_thread;

    private int m_port;

    Server(int port) {
        m_port = port;
    }

    public void start() {
        m_thread = new Thread(this);
        m_thread.start();
    }

    public void waitForStart() throws InterruptedException {
        m_latch.await();
    }

    public boolean isStopped() {
        return m_stopped.get();
    }

    public void stop() throws InterruptedException {
        m_stopped.set(true);
        m_thread.join();
    }

    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(m_port);
            socket.setSoTimeout(500);
            m_latch.countDown();
            while (!m_stopped.get()) {
                DatagramPacket p = new DatagramPacket(new byte[128], 128);
                socket.receive(p);
                String cmd = new String(p.getData(), p.getOffset(), p.getLength(), "UTF-8");
                System.err.print(String.format("SERVER: %s\n", cmd));
                socket.send(p);
                if (cmd.startsWith("quit")) {
                    m_stopped.set(true);
                }
            }
        } catch (Exception e) {
            m_exception.set(e);
        } finally {
            if (socket != null) socket.close();
        }
    }
}
