package au.jSummit;

import au.jSummit.Core.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.imageio.*;

public class BroadcastFrame extends JWindow implements ImageObserver, ComponentListener {

    private SplashPanel image;

    private static String S_SPLASH;

    private Dimension dSize;

    private File imgFile;

    private ClientSide csClient;

    private Vector vSummits;

    public BroadcastFrame() {
        dSize = new Dimension(500, 320);
        setSize(dSize);
        vSummits = new Vector();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width / 2) - 250, (d.height / 2) - 160);
        S_SPLASH = new String("au/jSummit/images/splash.jpg");
        image = new SplashPanel(S_SPLASH);
        getContentPane().add(image);
        image.setStatus(Globals.S_VERSION);
    }

    public void loadjSummit() {
        try {
            csClient = new ClientSide();
            csClient.start();
            ClientSide.marcoSend();
            image.setStatus(new String("Listening for summits (3)"));
            Thread.sleep(1000);
            image.setStatus(new String("Listening for summits (2)"));
            Thread.sleep(1000);
            image.setStatus(new String("Listening for summits (1)"));
            Thread.sleep(1000);
            csClient.interrupt();
        } catch (Exception e) {
            System.err.println("loadjsummit: " + e);
        }
    }

    /**
     * Shutdown procedure when run as an application.
     */
    protected void windowClosed() {
        System.exit(0);
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        if (getHeight() != 320 || getWidth() != 500) {
            setSize(dSize);
        }
    }

    public Vector getVSummits() {
        return new Vector(csClient.getVSummits());
    }
}

class SplashPanel extends JPanel {

    private Image image;

    private String status;

    public SplashPanel(String ImageLocation) {
        status = new String();
        try {
            image = ImageIO.read(new File(ImageLocation));
        } catch (IOException e) {
            System.err.println("Error: no image for splash screen");
            image = new BufferedImage(500, 320, BufferedImage.TYPE_INT_BGR);
            Graphics g = image.getGraphics();
            g.setColor(Color.gray);
            g.fillRect(0, 0, 500, 320);
        }
    }

    public void setStatus(String s) {
        status = s;
        repaint();
    }

    public String getStatus() {
        return status;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, 500, 320, this);
        g.drawString(status, 300, 285);
    }
}

class ClientSide extends Thread {

    private static final int POLOPORT = 58832;

    private static final int MARCOPORT = 58833;

    private String S_MARCO;

    private String S_POLO;

    private Vector vSummits;

    private DatagramSocket polo;

    private DatagramPacket polopacket;

    public ClientSide() {
        try {
            S_MARCO = Globals.S_MARCO;
            S_POLO = Globals.S_POLO;
            vSummits = new Vector();
            polo = new DatagramSocket(POLOPORT);
            polo.setReuseAddress(true);
            polo.setSoTimeout(1000);
        } catch (Exception e) {
            System.err.println("clientside(): " + e);
        }
    }

    public Vector getVSummits() {
        return vSummits;
    }

    public static void marcoSend() {
        InetAddress CAST_ADDRESS;
        DatagramSocket marco;
        try {
            CAST_ADDRESS = InetAddress.getByName("255.255.255.255");
            marco = new DatagramSocket();
        } catch (Exception e) {
            System.out.println("MarcoSend:" + e);
            return;
        }
        DatagramPacket marcopacket;
        try {
            marcopacket = new DatagramPacket(Globals.S_MARCO.getBytes(), Globals.S_MARCO.length(), CAST_ADDRESS, MARCOPORT);
            marco.send(marcopacket);
            marco.close();
        } catch (Exception e) {
            System.err.println("client:" + e.toString());
        }
    }

    public void run() {
        byte[] b = new byte[512];
        DatagramPacket polopacket = new DatagramPacket(b, b.length);
        while (true) {
            if (interrupted()) break;
            try {
                polo.receive(polopacket);
            } catch (SocketTimeoutException e) {
                continue;
            } catch (Exception e) {
                break;
            }
            b = polopacket.getData();
            String s1 = new String(b);
            if (s1.startsWith(S_POLO)) {
                SummitInfo siNewSumm = new SummitInfo();
                siNewSumm.setSummitAddress(polopacket.getAddress());
                siNewSumm.setToAdd(vSummits);
                siNewSumm.start();
            }
        }
        polo.close();
    }
}
