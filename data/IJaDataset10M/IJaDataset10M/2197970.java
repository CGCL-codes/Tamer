package genj.timeline;

import java.awt.*;
import java.awt.event.*;
import genj.gedcom.*;

/**
 * The scala at the lower portion of the window showing the years
 */
class TimelineScala extends Component {

    /** members */
    private TimelineModel model;

    private TimelineView timeline;

    /**
   * Returns the logical size of the renderer's view
   */
    public Dimension getPreferredSize() {
        return new Dimension(16, getGraphics().getFontMetrics().getHeight());
    }

    /**
   * Paints this content's data
   */
    public void paint(Graphics g) {
        Dimension size = getSize();
        g.setColor(Color.white);
        g.fillRect(0, 0, size.width, size.height);
        FontMetrics fm = g.getFontMetrics();
        int w = size.width, h = size.height, fh = fm.getHeight(), minYear = model.getMinYear(), maxYear = model.getMaxYear();
        int modulo = 1;
        if (timeline.getPixelsPerYear() <= 2 * fm.stringWidth("9999")) {
            modulo = 1 + (int) Math.ceil(fm.stringWidth("9999") * 2 / timeline.getPixelsPerYear());
        }
        int x = 0;
        for (int y = minYear; (x < w) && (y <= maxYear); y++) {
            x = (y - minYear) * timeline.getPixelsPerYear();
            g.setColor(Color.lightGray);
            if ((y - minYear) % modulo == 0) {
                g.setColor(Color.black);
                String s = "" + y;
                g.drawString(s, x - (y == minYear ? 0 : fm.stringWidth(s) / (y == maxYear ? 1 : 2)), h - fm.getMaxDescent());
            }
            g.drawLine(x, 0, x, 4);
        }
    }

    /**
   * Constructor
   */
    TimelineScala(TimelineModel model, TimelineView timeline) {
        this.model = model;
        this.timeline = timeline;
    }
}
