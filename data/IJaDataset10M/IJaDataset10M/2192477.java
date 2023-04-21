package de.jaret.util.ui.timebars.swing.renderer;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import de.jaret.util.swing.GraphicsHelper;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;

/**
 * A default title renderer rendering the name of the viewer as large as possible.
 * 
 * @author kliem
 * @version $Id: DefaultTitleRenderer.java 802 2008-12-28 12:30:41Z kliem $
 */
public class DefaultTitleRenderer implements ITitleRenderer {

    /** the cached component used for rendering. */
    protected TitleRendererComponent _titleRendererComponent;

    /**
     * Default constructor.
     */
    public DefaultTitleRenderer() {
        _titleRendererComponent = new TitleRendererComponent();
    }

    /**
     * {@inheritDoc}
     */
    public JComponent getTitleRendererComponent(TimeBarViewer tbv) {
        _titleRendererComponent.setDisplayName(tbv.getName());
        return _titleRendererComponent;
    }

    /**
     * Component used for rendering. This always tries to print out the name set at a maximum size.
     * 
     * @author kliem
     * @version $Id: DefaultTitleRenderer.java 802 2008-12-28 12:30:41Z kliem $
     */
    public static class TitleRendererComponent extends JComponent {

        /** the text to display. */
        private String _name;

        /** the last width of painting. Used to determine whether the font size has to be determined. */
        private int _lastWidth = -1;

        /** margin in pixel to use. */
        private static final int MARGIN = 3;

        /** remebered size. */
        private float _size = 8;

        /**
         * Default constructor.
         */
        public TitleRendererComponent() {
            setLayout(null);
            setOpaque(false);
        }

        /**
         * Set the name to be displayed.
         * 
         * @param name the name
         */
        public void setDisplayName(String name) {
            _name = name;
        }

        /**
         * {@inheritDoc}
         */
        public String getToolTipText() {
            return "<html><b>" + _name + "</b></html>";
        }

        /**
         * {@inheritDoc}
         */
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            int height = getHeight();
            int width = getWidth();
            Graphics2D g2 = (Graphics2D) graphics.create();
            GradientPaint gradientPaint = new GradientPaint(0, 0, Color.BLUE, width / 2, height / 2, Color.WHITE, false);
            g2.setPaint(gradientPaint);
            g2.fillRect(0, 0, width, height);
            if (_lastWidth != width) {
                _lastWidth = width;
                for (float size = 6; size < 37; size += 1) {
                    graphics.setFont(graphics.getFont().deriveFont(size));
                    Rectangle2D rect = graphics.getFontMetrics().getStringBounds(_name, graphics);
                    if (rect.getWidth() > width - 2 * MARGIN) {
                        graphics.setFont(graphics.getFont().deriveFont(size - 1));
                        _size = size - 1;
                        break;
                    }
                    _size = size;
                }
            } else {
                graphics.setFont(graphics.getFont().deriveFont(_size));
            }
            graphics.setColor(Color.BLACK);
            GraphicsHelper.drawStringCenteredVCenter(graphics, _name, 0, width, height / 2);
        }
    }
}
