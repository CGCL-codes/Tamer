package org.goniolab.unitcircle.visualizer.trigline;

import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import org.goniolab.unitcircle.Options;
import org.goniolab.unitcircle.visualizer.CanvasPanel;

/**
 *
 * @author Patrik Karlsson <patrik@trixon.se>
 */
public class TrigonometricLineCanvas extends CanvasPanel {

    public TrigonometricLineCanvas() {
        init();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackgroundColor());
        g2.fillRect(0, 0, this.getWidth(), this.getSize().height);
    }

    @Override
    protected void mouseDragged(MouseEvent evt) {
    }

    @Override
    protected void mousePressed(MouseEvent evt) {
    }

    @Override
    protected void preferenceChangeColors() {
        setBackgroundColor(Options.getInstance().getColor(Options.ColorPref.BG_TRIG_LINE));
        setForegroundColor(Options.getInstance().getColor(Options.ColorPref.FG_TRIG_LINE));
    }

    @Override
    protected void zoomCheckBoxStateChanged(boolean aState) {
    }

    @Override
    protected void zoomSliderStateChanged(int aValue) {
    }

    private void init() {
        preferenceChangeColors();
    }

    @Override
    protected void componentResized(ComponentEvent evt) {
    }
}
