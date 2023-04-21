package kello.teacher.rfb.viewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import javax.swing.JPanel;
import kello.teacher.protocol.RfbServerDescription;
import kello.teacher.rfb.MulticastRfbOptions;
import kello.teacher.rfb.RfbOptions;
import kello.teacher.rfb.rfbProto;

public class JRfbMulticastViewer extends JPanel implements java.lang.Runnable {

    /** should be updates directly be painted on the panel (incrementally)*/
    private boolean paint;

    private ColorModel cm;

    private Color[] colors;

    private Graphics sg;

    private Image paintImage;

    private Graphics pig;

    private boolean needToResetClip;

    private Thread rfbThread;

    private RfbOptions options;

    private boolean scaled;

    private RfbServerDescription desc;

    private MulticastSocket socket;

    /**
	 * Creates a viewer
	 * 
	 * @param scaled true if the viewer will be scaled, false otherwise
	 */
    public JRfbMulticastViewer(boolean scaled, RfbServerDescription desc, MulticastRfbOptions mo) throws IOException, UnknownHostException {
        options = new RfbOptions();
        this.scaled = scaled;
        this.desc = desc;
        socket = new MulticastSocket(mo.getMulticastPort());
        socket.joinGroup(InetAddress.getByName(mo.getMulticastGroup()));
        socket.setReceiveBufferSize(65000);
    }

    /**
	 * Connect to the remote host and starts showing the remote framebuffer
	 *
	 */
    public synchronized void start() {
        if (rfbThread == null) {
            rfbThread = new Thread(this);
            rfbThread.start();
        }
    }

    /**
	 * Closes the connection to the remote framebuffer
	 *
	 */
    public synchronized void stop() {
        if (rfbThread != null) {
            socket.close();
            rfbThread = null;
        }
    }

    /**
	 * Does the intialisation of the structures needed to display the remote framebuffer
	 * 
	 * @throws IOException in case it is impossible to send the pixel format to the server
	 */
    private void setupPanel() throws IOException {
        framebufferWidth = desc.getFramebufferWidth();
        framebufferHeight = desc.getFramebufferHeight();
        if (scaled) paint = false; else paint = true;
        cm = new DirectColorModel(8, 7, (7 << 3), (3 << 6));
        colors = new Color[256];
        for (int i = 0; i < 256; i++) {
            colors[i] = new Color(cm.getRGB(i));
        }
        paintImage = createImage(framebufferWidth, framebufferHeight);
        pig = paintImage.getGraphics();
        this.setPreferredSize(new Dimension(framebufferWidth, framebufferHeight));
        if (!scaled) {
            this.setMinimumSize(new Dimension(framebufferWidth, framebufferHeight));
        }
        revalidate();
        repaint();
    }

    /**
	 *  The main method responsible to update the local copy of the remote framebuffer.
	 *  It first connect and authenticate to the remote then it process incoming rects.
	 */
    public void run() {
        try {
            setupPanel();
            processNormalProtocol();
        } catch (SocketException e) {
            System.out.println("Disconnected");
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    void getDatagram() throws IOException {
        int size = socket.getReceiveBufferSize();
        msg = new byte[size];
        cp = 0;
        DatagramPacket packet = new DatagramPacket(msg, msg.length);
        socket.receive(packet);
    }

    public boolean isStopped() {
        return rfbThread == null;
    }

    public void paintComponent(Graphics g) {
        if (paintImage != null) {
            lastRepaint = new Date().getTime();
            if (scaled) g.drawImage(paintImage, 0, 0, getWidth(), getHeight(), 0, 0, framebufferWidth, framebufferHeight, this); else g.drawImage(paintImage, 0, 0, this);
        } else {
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
    }

    private int framebufferWidth;

    private int framebufferHeight;

    private byte msg[];

    int cp;

    /**
	 * Executed by the rfbThread to deal with the RFB socket.
	 */
    public void processNormalProtocol() throws IOException {
        needToResetClip = false;
        while (true) {
            getDatagram();
            if (paint) sg = getGraphics(); else sg = null;
            int msgType = (0xFF & msg[cp++]);
            cp++;
            switch(msgType) {
                case rfbProto.FramebufferUpdate:
                    int updateNRects = ((0xFF & msg[cp++]) << 8) + (0xFF & msg[cp++]);
                    for (int i = 0; i < updateNRects; i++) {
                        int updateRectX = ((0xFF & msg[cp++]) << 8) + (0xFF & msg[cp++]);
                        int updateRectY = ((0xFF & msg[cp++]) << 8) + (0xFF & msg[cp++]);
                        int updateRectW = ((0xFF & msg[cp++]) << 8) + (0xFF & msg[cp++]);
                        int updateRectH = ((0xFF & msg[cp++]) << 8) + (0xFF & msg[cp++]);
                        int updateRectEncoding = ((0xFF & msg[cp++]) << 24) + ((0xFF & msg[cp++]) << 16) + ((0xFF & msg[cp++]) << 8) + (0xFF & msg[cp++]);
                        if (needToResetClip && (updateRectEncoding != rfbProto.EncodingRaw)) {
                            try {
                                if (paint) sg.setClip(0, 0, framebufferWidth, framebufferHeight);
                                pig.setClip(0, 0, framebufferWidth, framebufferHeight);
                            } catch (NoSuchMethodError e) {
                            }
                            needToResetClip = false;
                        }
                        switch(updateRectEncoding) {
                            case rfbProto.EncodingRaw:
                                drawRawRect(updateRectX, updateRectY, updateRectW, updateRectH);
                                break;
                            case rfbProto.EncodingCopyRect:
                                int copyRectSrcX = ((0xFF & msg[cp++]) << 8) + (0xFF & msg[cp++]);
                                int copyRectSrcY = ((0xFF & msg[cp++]) << 8) + (0xFF & msg[cp++]);
                                pig.copyArea(copyRectSrcX, copyRectSrcY, updateRectW, updateRectH, updateRectX - copyRectSrcX, updateRectY - copyRectSrcY);
                                if (options.isCopyRectFast()) {
                                    if (paint) sg.copyArea(copyRectSrcX, copyRectSrcY, updateRectW, updateRectH, updateRectX - copyRectSrcX, updateRectY - copyRectSrcY);
                                } else {
                                    if (paint) sg.drawImage(paintImage, framebufferWidth, framebufferHeight, 0, 0, this);
                                }
                                break;
                            case rfbProto.EncodingRRE:
                                {
                                    int nSubrects = ((0xFF & msg[cp++]) << 24) + ((0xFF & msg[cp++]) << 16) + ((0xFF & msg[cp++]) << 8) + (0xFF & msg[cp++]);
                                    int bg = (0xFF & msg[cp++]);
                                    int pixel, x, y, w, h;
                                    if (paint) sg.translate(updateRectX, updateRectY);
                                    if (paint) sg.setColor(colors[bg]);
                                    if (paint) sg.fillRect(0, 0, updateRectW, updateRectH);
                                    pig.translate(updateRectX, updateRectY);
                                    pig.setColor(colors[bg]);
                                    pig.fillRect(0, 0, updateRectW, updateRectH);
                                    for (int j = 0; j < nSubrects; j++) {
                                        pixel = (0xFF & msg[cp++]);
                                        x = ((0xFF & msg[cp++]) << 8) + (0xFF & msg[cp++]);
                                        y = ((0xFF & msg[cp++]) << 8) + (0xFF & msg[cp++]);
                                        w = ((0xFF & msg[cp++]) << 8) + (0xFF & msg[cp++]);
                                        h = ((0xFF & msg[cp++]) << 8) + (0xFF & msg[cp++]);
                                        if (paint) sg.setColor(colors[pixel]);
                                        if (paint) sg.fillRect(x, y, w, h);
                                        pig.setColor(colors[pixel]);
                                        pig.fillRect(x, y, w, h);
                                    }
                                    if (paint) sg.translate(-updateRectX, -updateRectY);
                                    pig.translate(-updateRectX, -updateRectY);
                                    break;
                                }
                            case rfbProto.EncodingCoRRE:
                                {
                                    int nSubrects = ((0xFF & msg[cp++]) << 24) + ((0xFF & msg[cp++]) << 16) + ((0xFF & msg[cp++]) << 8) + (0xFF & msg[cp++]);
                                    int bg = (0xFF & msg[cp++]);
                                    int pixel, x, y, w, h;
                                    if (paint) sg.translate(updateRectX, updateRectY);
                                    if (paint) sg.setColor(colors[bg]);
                                    if (paint) sg.fillRect(0, 0, updateRectW, updateRectH);
                                    pig.translate(updateRectX, updateRectY);
                                    pig.setColor(colors[bg]);
                                    pig.fillRect(0, 0, updateRectW, updateRectH);
                                    for (int j = 0; j < nSubrects; j++) {
                                        pixel = (0xFF & msg[cp++]);
                                        x = (0xFF & msg[cp++]);
                                        y = (0xFF & msg[cp++]);
                                        w = (0xFF & msg[cp++]);
                                        h = (0xFF & msg[cp++]);
                                        if (paint) sg.setColor(colors[pixel]);
                                        if (paint) sg.fillRect(x, y, w, h);
                                        pig.setColor(colors[pixel]);
                                        pig.fillRect(x, y, w, h);
                                    }
                                    if (paint) sg.translate(-updateRectX, -updateRectY);
                                    pig.translate(-updateRectX, -updateRectY);
                                    break;
                                }
                            case rfbProto.EncodingHextile:
                                {
                                    int bg = 0, fg = 0, sx, sy, sw, sh;
                                    for (int ty = updateRectY; ty < updateRectY + updateRectH; ty += 16) {
                                        for (int tx = updateRectX; tx < updateRectX + updateRectW; tx += 16) {
                                            int tw = 16, th = 16;
                                            if (updateRectX + updateRectW - tx < 16) tw = updateRectX + updateRectW - tx;
                                            if (updateRectY + updateRectH - ty < 16) th = updateRectY + updateRectH - ty;
                                            int subencoding = (0xFF & msg[cp++]);
                                            if ((subencoding & rfbProto.HextileRaw) != 0) {
                                                drawRawRect(tx, ty, tw, th);
                                                continue;
                                            }
                                            if (needToResetClip) {
                                                try {
                                                    if (paint) sg.setClip(0, 0, framebufferWidth, framebufferHeight);
                                                    pig.setClip(0, 0, framebufferWidth, framebufferHeight);
                                                } catch (NoSuchMethodError e) {
                                                }
                                                needToResetClip = false;
                                            }
                                            if ((subencoding & rfbProto.HextileBackgroundSpecified) != 0) bg = (0xFF & msg[cp++]);
                                            if (paint) sg.setColor(colors[bg]);
                                            if (paint) sg.fillRect(tx, ty, tw, th);
                                            pig.setColor(colors[bg]);
                                            pig.fillRect(tx, ty, tw, th);
                                            if ((subencoding & rfbProto.HextileForegroundSpecified) != 0) fg = (0xFF & msg[cp++]);
                                            if ((subencoding & rfbProto.HextileAnySubrects) == 0) continue;
                                            int nSubrects = (0xFF & msg[cp++]);
                                            if (paint) sg.translate(tx, ty);
                                            pig.translate(tx, ty);
                                            if ((subencoding & rfbProto.HextileSubrectsColoured) != 0) {
                                                for (int j = 0; j < nSubrects; j++) {
                                                    fg = (0xFF & msg[cp++]);
                                                    int b1 = (0xFF & msg[cp++]);
                                                    int b2 = (0xFF & msg[cp++]);
                                                    sx = b1 >> 4;
                                                    sy = b1 & 0xf;
                                                    sw = (b2 >> 4) + 1;
                                                    sh = (b2 & 0xf) + 1;
                                                    if (paint) sg.setColor(colors[fg]);
                                                    if (paint) sg.fillRect(sx, sy, sw, sh);
                                                    pig.setColor(colors[fg]);
                                                    pig.fillRect(sx, sy, sw, sh);
                                                }
                                            } else {
                                                if (paint) sg.setColor(colors[fg]);
                                                pig.setColor(colors[fg]);
                                                for (int j = 0; j < nSubrects; j++) {
                                                    int b1 = (0xFF & msg[cp++]);
                                                    int b2 = (0xFF & msg[cp++]);
                                                    sx = b1 >> 4;
                                                    sy = b1 & 0xf;
                                                    sw = (b2 >> 4) + 1;
                                                    sh = (b2 & 0xf) + 1;
                                                    if (paint) sg.fillRect(sx, sy, sw, sh);
                                                    pig.fillRect(sx, sy, sw, sh);
                                                }
                                            }
                                            if (paint) sg.translate(-tx, -ty);
                                            pig.translate(-tx, -ty);
                                        }
                                    }
                                    break;
                                }
                            default:
                                throw new IOException("Unknown RFB rectangle encoding " + updateRectEncoding);
                        }
                    }
                    if (scaled) repaintWidget();
                    break;
                case rfbProto.SetColourMapEntries:
                    throw new IOException("Can't handle SetColourMapEntries message");
                case rfbProto.Bell:
                    System.out.print((char) 7);
                    break;
                case rfbProto.ServerCutText:
                    break;
                default:
                    throw new IOException("Unknown RFB message type " + msgType);
            }
        }
    }

    /**
	 * Draw a raw rectangle.
	 */
    void drawRawRect(int x, int y, int w, int h) throws IOException {
        if (options.isDrawEachPixelForRawRects()) {
            for (int j = y; j < (y + h); j++) {
                for (int k = x; k < (x + w); k++) {
                    int pixel = (0xFF & msg[cp++]);
                    if (paint) sg.setColor(colors[pixel]);
                    if (paint) sg.fillRect(k, j, 1, 1);
                    pig.setColor(colors[pixel]);
                    pig.fillRect(k, j, 1, 1);
                }
            }
            return;
        }
    }

    long lastRepaint = 0;

    private void repaintWidget() {
        long now = new Date().getTime();
        if (now - lastRepaint < 100) return;
        lastRepaint = now;
        repaint();
    }
}
