package org.kubiki.ide;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import org.kubiki.util.*;
import com.keypoint.PngEncoder;

public class IconEditorApplet extends Applet implements MouseListener {

    int iconWidth = 24;

    int iconHeight = 24;

    byte[] bytes;

    Vector colors;

    byte actualValue;

    String value;

    Button uploadButton;

    FileUpload fu;

    Image icon = null;

    PngEncoder png;

    public IconEditorApplet() {
        setLayout(null);
        setBackground(Color.gray);
        bytes = new byte[iconWidth * iconHeight];
        colors = new Vector();
        colors.add(Color.white);
        colors.add(Color.black);
        colors.add(new Color(20, 20, 20));
        colors.add(new Color(40, 40, 40));
        colors.add(new Color(60, 60, 60));
        colors.add(new Color(80, 80, 80));
        colors.add(new Color(180, 180, 180));
        colors.add(Color.red);
        colors.add(Color.blue);
        colors.add(Color.green);
        addMouseListener(this);
        uploadButton = new Button("Upload");
        uploadButton.setBounds(0, 330, 350, 20);
        add(uploadButton);
        uploadButton.addMouseListener(this);
        value = new String(bytes);
        fu = new FileUpload();
    }

    public void init() {
        loadIcon();
        repaint();
    }

    public void loadIcon() {
        System.out.println(getCodeBase());
        value = fu.downloadFile(getCodeBase() + "getIconString", "", "");
        if (value.length() == (iconWidth * iconHeight)) {
            bytes = value.getBytes();
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.black);
        for (int i = 0; i < (iconWidth + 1); i++) {
            g.drawLine(i * 10, 0, i * 10, (iconWidth * 10));
        }
        for (int i = 0; i < (iconHeight + 1); i++) {
            g.drawLine(0, i * 10, (iconHeight * 10), i * 10);
        }
        for (int i = 0; i < iconHeight; i++) {
            for (int j = 0; j < iconWidth; j++) {
                byte b = bytes[(i * iconHeight) + j];
                if (b > -1 & b < 10) {
                    g.setColor((Color) colors.elementAt((int) b));
                    g.fillRect(j * 10 + 1, i * 10 + 1, 8, 8);
                    g.setColor(Color.black);
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                g.drawRect(j * 32 + (iconWidth * 10 + 10), i * 32, 32, 32);
                g.setColor(Color.black);
                if (colors.size() > (i * 2) + j) {
                    Color c = (Color) colors.elementAt((i * 2) + j);
                    g.setColor(c);
                    g.fillRect(j * 32 + (iconWidth * 10 + 11), i * 32 + 1, 31, 31);
                }
                g.setColor(Color.black);
            }
        }
        if (icon != null) {
            g.drawImage(icon, 0, 300, this);
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.getSource() != uploadButton) {
            if (e.getX() < (iconWidth * 10)) {
                int i = e.getY() / 10;
                int j = e.getX() / 10;
                System.out.println(i + ":" + j);
                bytes[(i * iconWidth) + j] = actualValue;
                repaint();
                value = new String(bytes);
            }
            if (e.getX() > (iconWidth * 10 + 10) && e.getX() < 320) {
                int i = e.getY() / 32;
                int j = (e.getX() - (iconHeight * 10 + 10)) / 32;
                actualValue = (byte) ((i * 2) + j);
                System.out.println(actualValue);
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(uploadButton)) {
            System.out.println(getCodeBase());
            fu.uploadFile("", bytes, getCodeBase() + "setIconString", "admin", "IlmE,bwss.");
            createImage();
            uploadImage();
        }
    }

    public void uploadImage() {
        BufferedImage buf;
        buf = new BufferedImage(iconWidth, iconHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buf.createGraphics();
        RenderingHints qualityHints;
        qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHints(qualityHints);
        g.drawImage(icon, 0, 0, this);
        Image im = createImage(buf.getSource());
        boolean encodeAlpha = false;
        int filter = 0;
        int compressionLevel = 0;
        byte[] pngbytes;
        png = new PngEncoder(im, (encodeAlpha) ? PngEncoder.ENCODE_ALPHA : PngEncoder.NO_ALPHA, filter, compressionLevel);
        try {
            pngbytes = png.pngEncode();
            if (pngbytes == null) {
                System.out.println("Null image");
            } else {
                fu.uploadFile("", pngbytes, getCodeBase() + "setIcon", "admin", "IlmE,bwss.");
            }
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    public Image createImage() {
        BufferedImage buf;
        buf = new BufferedImage(iconWidth, iconHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buf.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, iconWidth, iconHeight);
        g.setColor(Color.black);
        for (int k = 0; k < iconHeight; k++) {
            for (int j = 0; j < iconWidth; j++) {
                byte b = bytes[(k * iconHeight) + j];
                if (b > -1 & b < 10) {
                    g.setColor((Color) colors.elementAt((int) b));
                    g.fillRect(j, k, 1, 1);
                    g.setColor(Color.black);
                }
            }
        }
        icon = createImage(buf.getSource());
        return icon;
    }
}
