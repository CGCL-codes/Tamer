package jtcpfwd.listener;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import jtcpfwd.NoMoreSocketsException;
import jtcpfwd.forwarder.Forwarder;
import jtcpfwd.forwarder.ScreenForwarder;
import jtcpfwd.util.PipedSocketImpl;
import jtcpfwd.util.WrappedPipedOutputStream;

public class ScreenListener extends Listener {

    public static final String SYNTAX = "Screen@";

    public static final Class[] getRequiredClasses() {
        return new Class[] { Forwarder.class, ScreenForwarder.class, PipedSocketImpl.class, PipedSocketImpl.PipedSocket.class, WrappedPipedOutputStream.class, ListenerCanvas.class };
    }

    private static boolean listening = false;

    private final OutputStream out;

    private final InputStream in;

    private Socket socket;

    private ListenerCanvas canvas;

    public ScreenListener(String rule) throws Exception {
        if (listening) throw new IllegalStateException("Only one ScreenListener supported");
        listening = true;
        PipedSocketImpl sock = new PipedSocketImpl();
        in = sock.getLocalInputStream();
        out = sock.getLocalOutputStream();
        socket = sock.createSocket();
    }

    protected Socket tryAccept() throws IOException, NoMoreSocketsException {
        if (socket != null) {
            Socket s = socket;
            socket = null;
            return s;
        }
        ListenerCanvas lc = new ListenerCanvas(out);
        lc.showWindow();
        canvas = lc;
        while (true) {
            int b = in.read();
            if (b == -1) throw new NoMoreSocketsException();
            lc.acceptByte(b);
        }
    }

    public void tryDispose() throws IOException {
        if (canvas != null) ((Frame) canvas.getParent()).dispose();
    }

    private static class ListenerCanvas extends Canvas implements KeyListener {

        private final BufferedImage buffer;

        private int state = STATE_INACTIVE, drawX = 0, drawY = 0, ackX = 0, ackY = 0, bits = 0, bitCount = 0, ackBits = 0, recvUnacked = 0;

        private static final int STATE_INACTIVE = 0, STATE_WAITRECV = 1, STATE_ACTIVE = 2, STATE_ACK = 3;

        private boolean shutdownActive = false;

        private final OutputStream out;

        public ListenerCanvas(OutputStream out) {
            this.out = new BufferedOutputStream(out);
            buffer = new BufferedImage(604, 404, BufferedImage.TYPE_INT_RGB);
            drawInactive();
            setFocusable(true);
            addKeyListener(this);
        }

        public synchronized void acceptByte(int b) {
            try {
                while (shutdownActive || state == STATE_INACTIVE || state == STATE_WAITRECV) wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            drawByte(b);
        }

        public void keyPressed(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
            try {
                handleKey(e.getKeyCode());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        protected synchronized void handleKey(int code) throws IOException {
            if (code == KeyEvent.VK_ESCAPE) {
                if (state == STATE_INACTIVE) {
                    state = STATE_WAITRECV;
                    drawX = drawY = ackX = ackY = bits = bitCount = 0;
                    drawActive();
                } else if (!shutdownActive) {
                    if (state != STATE_WAITRECV) drawByte(-1);
                    shutdownActive = true;
                }
            } else if (code == KeyEvent.VK_W) {
                if (state == STATE_ACTIVE) {
                    state = STATE_ACK;
                    ackBits = 0;
                }
            } else if (code == KeyEvent.VK_X) {
                if (state == STATE_ACTIVE) {
                    if (bits != 0) throw new IllegalStateException("" + bits);
                    bitCount = 0;
                    out.flush();
                } else if (state == STATE_WAITRECV) {
                    state = STATE_ACTIVE;
                    drawActive();
                    if (shutdownActive) {
                        shutdownActive = false;
                        drawByte(-1);
                        shutdownActive = true;
                    } else {
                        notifyAll();
                    }
                } else if (state == STATE_ACK) {
                    if (shutdownActive) {
                        out.flush();
                        state = STATE_INACTIVE;
                        shutdownActive = false;
                        drawInactive();
                    } else {
                        throw new IllegalStateException("ACK+SYNC received, but not waiting for shutdown");
                    }
                }
            } else if (code >= KeyEvent.VK_0 && code <= KeyEvent.VK_9) {
                handleBits(code - KeyEvent.VK_0);
            } else if (code >= KeyEvent.VK_A && code <= KeyEvent.VK_V) {
                handleBits(code - KeyEvent.VK_A + 10);
            }
        }

        private void handleBits(int newBits) throws IOException {
            if (state == STATE_ACTIVE) {
                bits = (bits << 5) + newBits;
                bitCount += 5;
                if (bitCount >= 8) {
                    int data = bits >> (bitCount - 8);
                    bits ^= data << (bitCount - 8);
                    bitCount -= 8;
                    out.write(data);
                    recvUnacked++;
                    if (recvUnacked >= ScreenForwarder.HALF_KEYBOARD_BUFFER && !shutdownActive && (drawX != ackX || (drawY + 1) % 400 != ackY)) {
                        recvUnacked -= ScreenForwarder.HALF_KEYBOARD_BUFFER;
                        drawByte(-2);
                    }
                }
            } else if (state == STATE_ACK) {
                if (newBits < 16) {
                    ackBits = (ackBits << 4) + newBits;
                    drawAcks(ackBits);
                    state = STATE_ACTIVE;
                } else {
                    ackBits = (ackBits << 4) + (newBits & 0x0F);
                }
            }
        }

        private void drawAcks(int count) {
            int color = Color.BLACK.getRGB();
            for (int i = 0; i < count; i++) {
                if (ackX == drawX && ackY == drawY) throw new IllegalStateException();
                buffer.setRGB(ackX + 2, ackY + 2, color);
                buffer.setRGB(ackX + 3, ackY + 2, color);
                buffer.setRGB(ackX + 4, ackY + 2, color);
                buffer.setRGB(ackX + 5, ackY + 2, color);
                ackX += 4;
                if (ackX == 600) {
                    ackY = (ackY + 1) % 400;
                    ackX = 0;
                }
            }
            notifyAll();
            repaint();
        }

        private static final Color[] BIT_COLORS = new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.WHITE };

        private void drawByte(int b) {
            if (shutdownActive || state == STATE_INACTIVE || state == STATE_WAITRECV) throw new IllegalStateException();
            Color[] cols = new Color[4];
            if (b == -1) Arrays.fill(cols, Color.YELLOW); else if (b == -2) {
                cols[0] = cols[3] = Color.YELLOW;
                cols[1] = cols[2] = Color.RED;
            } else {
                for (int i = 0; i < cols.length; i++) {
                    cols[i] = BIT_COLORS[b & 0x03];
                    b >>>= 2;
                }
            }
            try {
                while (b != -1 && drawX == ackX && (drawY + 1) % 400 == ackY) wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            for (int i = 0; i < cols.length; i++) {
                buffer.setRGB(drawX + 2, drawY + 2, cols[i].getRGB());
                drawX++;
                if (drawX == 600) {
                    drawY = (drawY + 1) % 400;
                    drawX = 0;
                }
            }
            repaint();
        }

        private void drawActive() {
            Graphics2D g = buffer.createGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 604, 404);
            g.setColor(state == STATE_WAITRECV ? Color.YELLOW : Color.GREEN);
            g.drawRect(0, 0, 603, 403);
            g.dispose();
            buffer.setRGB(1, 1, Color.RED.getRGB());
            buffer.setRGB(602, 1, Color.GREEN.getRGB());
            buffer.setRGB(1, 402, Color.BLUE.getRGB());
            buffer.setRGB(602, 402, Color.WHITE.getRGB());
            repaint();
        }

        private void drawInactive() {
            Graphics2D g = buffer.createGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 604, 404);
            g.setColor(Color.RED);
            g.drawRect(0, 0, 603, 403);
            g.setColor(Color.GREEN);
            g.drawString("Press the [ESC] key to start/stop transfer.", 150, 185);
            g.drawString("Make sure to leave the focus on this window and", 150, 200);
            g.drawString("do not type anything else while transfer is active.", 150, 215);
            g.dispose();
            repaint();
        }

        public void showWindow() {
            Frame f = new Frame("jTCPFwd Screen@ Listener Window");
            f.setBackground(Color.GRAY);
            f.setLayout(new GridBagLayout());
            f.add(this, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        }

        public Dimension getPreferredSize() {
            return new Dimension(604, 404);
        }

        public void update(Graphics g) {
            paint(g);
        }

        public void paint(Graphics g) {
            g.drawImage(buffer, 0, 0, this);
        }
    }
}
