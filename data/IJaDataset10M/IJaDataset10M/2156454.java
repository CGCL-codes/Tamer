package net.rmanager.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageFactory {

    public void buildImage(Graphics2D g, BufferedImage bg, ArrayList<BufferedImage> images, ArrayList<Dimension> coords) {
        Graphics2D graphics = bg.createGraphics();
        for (int i = 0; i < images.size(); i++) {
            Dimension dim = coords.get(i);
            graphics.drawImage(images.get(i), null, dim.width, dim.height);
        }
        g.drawImage(bg, null, 0, 0);
    }

    public void writeText(Graphics2D g, ArrayList<String> textList, ArrayList<Dimension> coords, ArrayList<Color> colors) {
        for (int i = 0; i < textList.size(); i++) {
            Dimension dim = coords.get(i);
            g.setColor(colors.get(i));
            g.drawString(textList.get(i), dim.width, dim.height);
        }
        g.setColor(Color.BLACK);
    }

    public void writeText(Graphics2D g, String text, int x, int y, Color color) {
        g.setColor(color);
        g.drawString(text, x, y);
        g.setColor(Color.BLACK);
    }
}
