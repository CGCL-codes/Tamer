package fr.uha.ensisa.ir.walther.milcityblue.gui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class ImageDrawer {

    private Image image;

    public static final String alpha = " !\"#%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ\\[]^_abcdefghijklmnopqrstuvwxyz{|}~$�������";

    public ImageDrawer() {
        this.image = null;
    }

    public boolean init() {
        try {
            this.image = Image.createImage("/images.png");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR : ImageDrawer : Image not fully loaded !");
        }
        return (this.image != null);
    }

    private int charToInt(char c) {
        int i = alpha.indexOf(c);
        if (i < 0) i = 0;
        return i;
    }

    public void stringToImage(Graphics g, String s, int x, int y, int anchor) {
        if (image != null) {
            int l = s.length();
            int clipX = g.getClipX();
            int clipY = g.getClipY();
            int clipH = g.getClipHeight();
            int clipW = g.getClipWidth();
            int x_offset = 0;
            int y_offset = 0;
            int charpos = 0;
            for (int i = 0; i < l; ++i) {
                charpos = charToInt(s.charAt(i));
                y_offset = charpos / 16;
                x_offset = charpos % 16;
                g.setClip(x + (i * 6), y, 6, 8);
                g.drawImage(image, x + (i * 6) - (x_offset * 7), y - (y_offset * 9), anchor);
            }
            g.setClip(clipX, clipY, clipW, clipH);
        } else {
            g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL));
            g.setColor(0x000000);
            g.drawString(s, x, y, anchor);
        }
    }

    public void cellToImage(Graphics g, int type, int x, int y, int size) {
        if (image != null) {
            g.setClip(x * size, y * size, size, size);
            g.drawImage(image, (x * size) - ((type % 12) * 9), (y * size) - (65 + ((type / 12) * 9)), Graphics.TOP | Graphics.LEFT);
        }
    }
}
