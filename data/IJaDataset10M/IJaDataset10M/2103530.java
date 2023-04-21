package testcode;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.media.*;
import javax.media.control.*;
import javax.media.format.*;
import javax.media.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.lang.*;

public class FramGrab1 {

    public static void main(String[] args) throws Exception {
        MediaFrame M = new MediaFrame();
        M.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        M.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        M.show();
    }
}

class MediaFrame extends JFrame {

    public MediaFrame() {
        this.setSize(400, 300);
        Container c = this.getContentPane();
        MediaPanel p = new MediaPanel();
        c.add(p, BorderLayout.CENTER);
        JPanel buttonpanel = new JPanel();
        JButton okbutton = new JButton("启动设备");
        JButton imagebutton = new JButton("拍照");
        JButton closebutton = new JButton("停止设备");
        okbutton.addActionListener(new okaction());
        imagebutton.addActionListener(new imageaction());
        closebutton.addActionListener(new closeaction());
        buttonpanel.add(okbutton);
        buttonpanel.add(imagebutton);
        buttonpanel.add(closebutton);
        c.add(buttonpanel, BorderLayout.SOUTH);
    }

    class MediaPanel extends JPanel {

        public MediaPanel() {
            deviceInfo = CaptureDeviceManager.getDevice("vfw:Microsoft   WDM   Image   Capture   (Win32):0");
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            if (img != null) drawimage(g2);
        }
    }

    public void windowClosing(WindowEvent event) {
        this.setVisible(false);
    }

    public void drawimage(Graphics2D g2) {
        g2.drawImage(img, null, null);
    }

    public class okaction implements ActionListener {

        public void actionPerformed(ActionEvent envent) {
            try {
                player = Manager.createRealizedPlayer(deviceInfo.getLocator());
                player.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class imageaction implements ActionListener {

        public void actionPerformed(ActionEvent envent) {
        }
    }

    public class closeaction implements ActionListener {

        public void actionPerformed(ActionEvent envent) {
            try {
                player.close();
                player.deallocate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private CaptureDeviceInfo deviceInfo = null;

    private Player player = null;

    private Image img = null;

    private Buffer buf = null;

    private BufferToImage btoi = null;
}
