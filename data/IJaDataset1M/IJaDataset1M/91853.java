package sheep.view.Component;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import javax.swing.JPanel;

/**
 *
 * @author geek
 */
public class RoundedBox extends JPanel {

    private int width;

    private int height;

    private Color color;

    private GradientPaint gradient;

    private Graphics2D g2d;

    private Color brightColor;

    public RoundedBox(int width, int height, Color color) {
        this.width = width;
        this.height = height;
        this.color = color;
        brightColor = new Color(this.color.getRed(), this.color.getGreen() - 100, this.color.getBlue() - 100);
        this.setPreferredSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));
        this.setMaximumSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        g2d = (Graphics2D) g.create();
        gradient = new GradientPaint(0, 0, color, width, 0, Color.WHITE);
        Shape clip1 = g2d.getClip();
        g2d.setPaint(gradient);
        g2d.setClip(clip1);
        g2d.fillRoundRect(3, 3, width - 6, height - 6, 5, 5);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
