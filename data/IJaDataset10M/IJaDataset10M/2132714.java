package net.sf.jtreemap.swing;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Color Provider by default.<BR>
 * All values are associated to a unique color.
 * 
 * @author Laurent DUTHEIL
 * 
 */
public class UniqueColorProvider extends ColorProvider {

    /**
     * 
     */
    private static final long serialVersionUID = -7571926934516139432L;

    private static final Color DEFAULT_COLOR = new Color(153, 153, 51);

    private Color color;

    private JPanel legend;

    /**
     * Constructor.
     */
    public UniqueColorProvider() {
        this.color = DEFAULT_COLOR;
    }

    /**
     * Constructor.
     * 
     * @param color
     *            unique color
     */
    public UniqueColorProvider(final Color color) {
        this.color = color;
    }

    @Override
    public Color getColor(final Value value) {
        return this.color;
    }

    @Override
    public JPanel getLegendPanel() {
        if (this.legend == null) {
            this.legend = new Legend();
        }
        return this.legend;
    }

    /**
     * Panel with the legend.
     * 
     * @author Laurent Dutheil
     */
    private static class Legend extends JPanel {

        private static final int LEGEND_Y_POS = 20;

        private static final int LEGEND_X_POS = 20;

        private static final int LEGEND_HEIGHT = 40;

        private static final int LEGEND_WIDTH = 100;

        private static final long serialVersionUID = -8046211081305644785L;

        private static final String TEXT = "Unique Color Provider";

        /**
         * Constructor.
         */
        public Legend() {
            this.setPreferredSize(new java.awt.Dimension(LEGEND_WIDTH, LEGEND_HEIGHT));
        }

        @Override
        public void paint(final Graphics g) {
            g.setColor(Color.black);
            g.drawString(Legend.TEXT, LEGEND_X_POS, LEGEND_Y_POS);
        }
    }
}
