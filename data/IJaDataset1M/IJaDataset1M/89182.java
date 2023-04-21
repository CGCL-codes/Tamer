package fr.ign.cogit.geoxygene.appli.event;

import java.awt.Color;
import java.awt.Graphics;
import fr.ign.cogit.geoxygene.appli.LayerViewPanel;

public class ScalePaintListener implements PaintListener {

    @Override
    public void paint(final LayerViewPanel layerViewPanel, Graphics graphics) {
        int shift = 10;
        int barHeight = 5;
        double dist = (layerViewPanel.getWidth() / 3) / layerViewPanel.getViewport().getScale();
        int log = (int) Math.log10(dist);
        dist = Math.pow(10, log);
        int barLength = (int) (dist * layerViewPanel.getViewport().getScale());
        String text = Double.toString(dist) + " m";
        int textWidth = graphics.getFontMetrics().stringWidth(text);
        int textHeight = graphics.getFontMetrics().getHeight();
        int scaleBackgroundWidth = Math.max(textWidth, barLength) + 2 * shift;
        int scaleBackgroundHeight = textHeight + barHeight + 1 * shift;
        graphics.setColor(Color.WHITE);
        graphics.fillRect(shift, layerViewPanel.getHeight() - 4 * shift - barHeight, scaleBackgroundWidth, scaleBackgroundHeight);
        graphics.setColor(Color.BLACK);
        graphics.drawRect(shift, layerViewPanel.getHeight() - 4 * shift - barHeight, scaleBackgroundWidth, scaleBackgroundHeight - 1);
        graphics.drawString(text, 2 * shift + 1, layerViewPanel.getHeight() - 2 * shift - barHeight - 1);
        graphics.fillRect(2 * shift, layerViewPanel.getHeight() - 2 * shift - barHeight, barLength, barHeight);
    }
}
