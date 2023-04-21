package toolkit.curveEditor.drawables;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;

public class Grid implements Paintable {

    private Color color;

    private double stepX;

    private double stepY;

    public Grid(double intervalX, double intervalY, Color lineColor) {
        color = lineColor;
        stepX = intervalX;
        stepY = intervalY;
    }

    @Override
    public void paint(Graphics2D g) {
        Rectangle clip = g.getClipBounds();
        g.setStroke(new BasicStroke(0));
        g.setColor(color);
        if (null != clip) {
            GeneralPath path = new GeneralPath();
            double x = clip.x + (stepX - (clip.x % stepX));
            for (; x < clip.x + clip.width; x += stepX) {
                path.moveTo(x, clip.y);
                path.lineTo(x, clip.y + clip.height);
            }
            double y = clip.y + (stepY - (clip.y % stepY));
            for (; y < clip.y + clip.height; y += stepY) {
                path.moveTo(clip.x, y);
                path.lineTo(clip.x + clip.width, y);
            }
            g.draw(path);
        }
    }
}
