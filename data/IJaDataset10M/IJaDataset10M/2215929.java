package jemu.core.device.tape;

import java.awt.*;
import jemu.system.cpc.CPC;
import java.awt.image.BufferedImage;

public class GraphicDisplay extends javax.swing.JPanel {

    Graphics page;

    BufferedImage panel;

    private final int CANVAS_WIDTH = 246;

    private final int CANVAS_HEIGHT = 20;

    public String message = "";

    private int oldlevel = 0;

    private int oldi = 0;

    Color green, red;

    public GraphicDisplay() {
        panel = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_RGB);
        page = panel.getGraphics();
        setBackground(Color.BLACK);
        setForeground(Color.GRAY);
        setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        page.setFont(new Font("Arial", 1, 9));
        page.setColor(Color.WHITE);
        green = new Color(0x00, 0x80, 0x00);
        red = new Color(0x80, 0x00, 0x00);
    }

    public void paintWAV() {
        page.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        int divider = 10;
        oldi = 0;
        for (int i = 0; i < 2500; i = i + divider) {
            int level = 0;
            if (CPC.tapesample != null && CPC.number + (i) < CPC.tapesample.length) {
                level = (CPC.tapesample[CPC.number + (i)]) & 0xff;
            }
            level = level / 12;
            if (level < 255 / 24) {
                page.setColor(red);
            } else {
                page.setColor(green);
            }
            page.drawLine(oldi, oldlevel, i / divider, (255 / 12) - level);
            oldlevel = (255 / 12) - level;
            oldi = i / divider;
        }
        showText();
    }

    public void showText(String text) {
        page.setColor(Color.WHITE);
        page.drawString(text, 1, 20);
        page.drawString(text, 2, 20);
        try {
            getGraphics().drawImage(panel, 0, 0, this);
        } catch (Exception e) {
        }
    }

    public void showText() {
        page.setColor(Color.WHITE);
        page.drawString(message, 1, 20);
        page.drawString(message, 2, 20);
        try {
            getGraphics().drawImage(panel, 0, 0, this);
        } catch (Exception e) {
        }
    }
}
