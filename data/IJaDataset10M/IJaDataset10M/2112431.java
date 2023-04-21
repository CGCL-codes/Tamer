package com.sistemask.sc.yapk.gui.frames;

import com.sistemask.sc.yapk.gui.main.Okno;
import com.sistemask.common.util.ImageUtil;
import java.awt.*;
import java.util.Enumeration;
import javax.swing.*;

public class Panel extends JPanel {

    private Okno o;

    /**
     * Constructor que recive el padre y lo alamcena
     * @param o
     */
    public Panel(Okno o) {
        new JPanel();
        this.o = o;
    }

    /**
     * Pintado del Graphic del elemento
     * @param gdc
     */
    public void paint(Graphics gdc) {
        Enumeration<Point> poczatek = o.start.elements();
        Enumeration<Point> koniec = o.finish.elements();
        Enumeration<String> tool = o.currentTool.elements();
        Enumeration<String> texts = o.texts.elements();
        Enumeration<Color> kolory = o.figureColor.elements();
        Enumeration<Image> obrazyT = o.images.elements();
        Enumeration<Integer> grubosc = o.fat.elements();
        String textS;
        Image img;
        int grub = 0;
        while (tool.hasMoreElements() && poczatek.hasMoreElements()) {
            String s = new String(tool.nextElement());
            if (s.equals("line")) {
                ((Graphics2D) gdc).setStroke(new BasicStroke(grubosc.nextElement(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                gdc.setColor(kolory.nextElement());
                Point p = poczatek.nextElement();
                Point p2 = koniec.nextElement();
                gdc.drawLine(p.x, p.y, p2.x, p2.y);
            } else if (s.equals("ellipse")) {
                gdc.setColor(kolory.nextElement());
                Point p = poczatek.nextElement();
                Point p2 = koniec.nextElement();
                ((Graphics2D) gdc).setStroke(new BasicStroke(grubosc.nextElement(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                if (p2.x > p.x && p2.y > p.y) gdc.drawOval(p.x, p.y, p2.x - p.x, p2.y - p.y); else if (p2.x > p.x && p2.y < p.y) gdc.drawOval(p.x, p2.y, p2.x - p.x, p.y - p2.y); else if (p2.x < p.x && p2.y > p.y) gdc.drawOval(p2.x, p.y, p.x - p2.x, p2.y - p.y); else if (p2.x < p.x && p2.y < p.y) gdc.drawOval(p2.x, p2.y, p.x - p2.x, p.y - p2.y);
            } else if (s.equals("ellipse_fill")) {
                gdc.setColor(kolory.nextElement());
                Point p = poczatek.nextElement();
                Point p2 = koniec.nextElement();
                if (p2.x > p.x && p2.y > p.y) gdc.fillOval(p.x, p.y, p2.x - p.x, p2.y - p.y); else if (p2.x > p.x && p2.y < p.y) gdc.fillOval(p.x, p2.y, p2.x - p.x, p.y - p2.y); else if (p2.x < p.x && p2.y > p.y) gdc.fillOval(p2.x, p.y, p.x - p2.x, p2.y - p.y); else if (p2.x < p.x && p2.y < p.y) gdc.fillOval(p2.x, p2.y, p.x - p2.x, p.y - p2.y);
            } else if (s.equals("airbrush")) {
                gdc.setColor(kolory.nextElement());
                grub = grubosc.nextElement();
                for (int i = 0; i <= grub * 5; i++) {
                    Point p = poczatek.nextElement();
                    gdc.fillRect(p.x, p.y, 1, 1);
                }
            } else if (s.equals("rectangle")) {
                gdc.setColor(kolory.nextElement());
                Point p = poczatek.nextElement();
                Point p2 = koniec.nextElement();
                ((Graphics2D) gdc).setStroke(new BasicStroke(grubosc.nextElement(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                if (p2.x > p.x && p2.y > p.y) gdc.drawRect(p.x, p.y, p2.x - p.x, p2.y - p.y); else if (p2.x > p.x && p2.y < p.y) gdc.drawRect(p.x, p2.y, p2.x - p.x, p.y - p2.y); else if (p2.x < p.x && p2.y > p.y) gdc.drawRect(p2.x, p.y, p.x - p2.x, p2.y - p.y); else if (p2.x < p.x && p2.y < p.y) gdc.drawRect(p2.x, p2.y, p.x - p2.x, p.y - p2.y);
            } else if (s.equals("rectangle_fill")) {
                gdc.setColor(kolory.nextElement());
                Point p = poczatek.nextElement();
                Point p2 = koniec.nextElement();
                if (p2.x > p.x && p2.y > p.y) gdc.fillRect(p.x, p.y, p2.x - p.x, p2.y - p.y); else if (p2.x > p.x && p2.y < p.y) gdc.fillRect(p.x, p2.y, p2.x - p.x, p.y - p2.y); else if (p2.x < p.x && p2.y > p.y) gdc.fillRect(p2.x, p.y, p.x - p2.x, p2.y - p.y); else if (p2.x < p.x && p2.y < p.y) gdc.fillRect(p2.x, p2.y, p.x - p2.x, p.y - p2.y);
            } else if (s.equals("roundedrect")) {
                gdc.setColor(kolory.nextElement());
                Point p = poczatek.nextElement();
                Point p2 = koniec.nextElement();
                if (p2.x > p.x && p2.y > p.y) gdc.drawRoundRect(p.x, p.y, p2.x - p.x, p2.y - p.y, (p2.x - p.x) / 5, (p2.y - p.y) / 5); else if (p2.x > p.x && p2.y < p.y) gdc.drawRoundRect(p.x, p.y, p2.x - p.x, p.y - p2.y, (p2.x - p.x) / 5, (p.y - p2.y) / 5); else if (p2.x < p.x && p2.y > p.y) gdc.drawRoundRect(p2.x, p.y, p.x - p2.x, p2.y - p.y, (p.x - p2.x) / 5, (p2.y - p.y) / 5); else if (p2.x < p.x && p2.y < p.y) gdc.drawRoundRect(p2.x, p2.y, p.x - p2.x, p.y - p2.y, (p.x - p2.x) / 5, (p.y - p2.y) / 5);
            } else if (s.equals("text")) {
                gdc.setColor(kolory.nextElement());
                Point p = poczatek.nextElement();
                textS = texts.nextElement();
                gdc.setFont(new Font("font", Font.PLAIN, grubosc.nextElement() * 2));
                gdc.drawString(textS, p.x, p.y);
            } else if (s.equals("imageB")) {
                Point p = poczatek.nextElement();
                img = obrazyT.nextElement();
                gdc.drawImage(img, p.x, p.y, ImageUtil.getImageWidth(img), ImageUtil.getImageHeight(img), null);
                System.out.println("Se carga Imagen en B sobre Panel");
            } else if (s.equals("imageT")) {
                Point p = poczatek.nextElement();
                img = obrazyT.nextElement();
                gdc.drawImage(img, p.x, p.y, ImageUtil.getImageWidth(img), ImageUtil.getImageHeight(img), null);
                System.out.println("Se carga Imagen en T sobre Panel");
            }
        }
    }
}
