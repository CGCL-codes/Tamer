package mipt.gui.graph.primitives.dots;

import java.awt.*;

public class OvalDot extends DotPrototype {

    /**
 * 
 */
    public OvalDot() {
        super();
    }

    /**
 * OvalDot constructor comment.
 * @param s Dimension
 * @param f boolean
 */
    public OvalDot(Dimension s, boolean f, Color c) {
        super(s, f, c);
    }

    /**
 * 
 * @return mipt.gui.graph.primitives.dots.DotPrototype
 */
    public DotPrototype create(DotPrototype dot) {
        return new OvalDot(dot.size, dot.fill, dot.color);
    }

    /**
 * paint method comment.
 */
    public void paint(Graphics g, int x, int y) {
        x -= size.width / 2;
        y -= size.width / 2;
        g.setColor(color);
        if (fill) g.fillOval(x, y, size.width - 1, size.height - 1); else g.drawOval(x, y, size.width - 1, size.height - 1);
    }
}
