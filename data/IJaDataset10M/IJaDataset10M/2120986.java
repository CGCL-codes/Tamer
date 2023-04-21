package calclipse.caldron.gui.theme.util;

import java.awt.Adjustable;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicScrollBarUI;
import calclipse.caldron.gui.skinning.BackgroundPanel;
import calclipse.core.gui.skin.style.Painter;

/**
 * This UI uses painters to render the track and thumb.
 * @author T. Sommerland
 */
public abstract class PainterScrollBarUI extends BasicScrollBarUI {

    private final BackgroundPanel hTrack = new BackgroundPanel();

    private final BackgroundPanel vTrack = new BackgroundPanel();

    private final BackgroundPanel hThumb = new BackgroundPanel();

    private final BackgroundPanel vThumb = new BackgroundPanel();

    private boolean initialized;

    public PainterScrollBarUI() {
        hThumb.setOpaque(false);
        vThumb.setOpaque(false);
    }

    protected abstract Painter createHorizontalTrackPainter();

    protected abstract Painter createVerticalTrackPainter();

    protected abstract Painter createHorizontalThumbPainter();

    protected abstract Painter createVerticalThumbPainter();

    private void initPainters() {
        if (!initialized) {
            hTrack.setPainter(createHorizontalTrackPainter());
            vTrack.setPainter(createVerticalTrackPainter());
            hThumb.setPainter(createHorizontalThumbPainter());
            vThumb.setPainter(createVerticalThumbPainter());
            initialized = true;
        }
    }

    @Override
    protected void paintTrack(final Graphics g, final JComponent c, final Rectangle trackBounds) {
        initPainters();
        if (((Adjustable) c).getOrientation() == HORIZONTAL) {
            SwingUtilities.paintComponent(g, hTrack, c, trackBounds);
        } else {
            SwingUtilities.paintComponent(g, vTrack, c, trackBounds);
        }
    }

    @Override
    protected void paintThumb(final Graphics g, final JComponent c, final Rectangle thumbBounds) {
        initPainters();
        if (((Adjustable) c).getOrientation() == HORIZONTAL) {
            SwingUtilities.paintComponent(g, hThumb, c, thumbBounds);
        } else {
            SwingUtilities.paintComponent(g, vThumb, c, thumbBounds);
        }
    }
}
