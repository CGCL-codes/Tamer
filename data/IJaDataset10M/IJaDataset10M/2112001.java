package problem7.doublebuffer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JPanel;

public class Rectangle extends Shape {

    public int x, y;

    public int width, height;

    public Rectangle() {
        x = y = -1;
        width = height = -1;
    }

    @Override
    public void draw() {
        System.out.println("Not yet implement!");
    }

    @Override
    public void draw(JPanel drawee) {
        System.out.println("Not yet implement!");
    }

    @Override
    public void draw(Image image) {
        if (image == null || x < 0 || y < 0 || width < 0 || height < 0) {
            return;
        }
        this.drawHelp((Graphics2D) image.getGraphics());
    }

    private void drawHelp(Graphics2D g) {
        if (g == null) {
            return;
        }
        Color oldc = g.getColor();
        g.setColor(color);
        if (fill) {
            g.fillRect(x, y, width, height);
        } else {
            g.drawRect(x, y, width, height);
        }
        g.setColor(oldc);
    }
}
