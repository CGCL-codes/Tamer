package fancyClient.graphics.gameEffects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import fancyClient.Camera;
import fancyClient.graphics.GraphicalEffect;

public class ConversionShieldPop extends GraphicalEffect {

    private int radius = 14;

    private int maxRadius = 2 * radius;

    private float lifeTime = .5f;

    private float timeAlive = 0;

    private BasicStroke outline = new BasicStroke(2);

    private Color fill = new Color(0, 255, 0, 128);

    private Color outlineColor = new Color(128, 255, 128);

    @Override
    public boolean draw(Graphics2D g, float elapsedSeconds, Camera cam) {
        timeAlive += elapsedSeconds;
        Stroke pStroke = g.getStroke();
        float percent = Math.min(timeAlive / (lifeTime), 1f);
        Color fillAlpha = new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), (int) (fill.getAlpha() * (1 - percent)));
        Color outlineAlpha = new Color(outlineColor.getRed(), outlineColor.getGreen(), outlineColor.getBlue(), (int) (255 * (1 - percent)));
        int nr = (int) Math.rint((maxRadius - radius) * percent) + radius;
        g.setStroke(outline);
        g.setColor(fillAlpha);
        g.fillOval(-nr, -nr, 2 * nr, 2 * nr);
        g.setColor(outlineAlpha);
        g.drawOval(-nr, -nr, 2 * nr, 2 * nr);
        g.setStroke(pStroke);
        return timeAlive < lifeTime;
    }
}
