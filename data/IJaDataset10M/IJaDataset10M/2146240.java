package hoverball.debug;

import hoverball.math.*;
import hoverball.layout.Layout;
import java.awt.*;

/**
 * Plots a text.
 */
public class Text extends Debug {

    private final Vector p;

    private final String text;

    private final double line;

    private final int align;

    /**
    * Creates a Text debug.
    *
    * @param p position
    * @param text text
    */
    public Text(Vector p, String text) {
        this(p, text, 0, 0);
    }

    /**
    * Creates a Text debug with line number.
    * <p>
    * The line number shifts the text upward and downward
    * independently of the perspective. It is thus possible
    * to output a multiple line comment without any distorting by
    * perspective.
    *
    * @param p position
    * @param text text
    * @param line line
    */
    public Text(Vector p, String text, double line) {
        this(p, text, line, 0);
    }

    /**
    * Creates a Text debug with line number and horizontal alignment.
    * <p>
    * The line number shifts the text upward and downward
    * independently of the perspective. It is thus possible
    * to output a multiple line comment without any distorting by
    * perspective.
    * <p>
    * With alignment values <i>x</i> &lt; 0 the text is right justified,
    * <i>x</i> = 0 centers, and <i>x</i> &gt; 0 outputs left justified.
    *
    * @param p position
    * @param text text
    * @param line line
    * @param align alignment
    */
    public Text(Vector p, String text, double line, int align) {
        this.p = vector(p);
        this.text = (text == null) ? "" : "" + text;
        this.line = line;
        this.align = align;
    }

    /**
    * [Implementation]
    */
    public void paint(Graphics g, Color color, Color globe, double scale, Sphere sphere, Matrix base, boolean front) {
        Vector v = Vector.mul(p, base);
        if (front ^ v.z > 0) return;
        Point X = scale(scale, v);
        int height = g.getFontMetrics().getAscent();
        int spacing = g.getFontMetrics().getHeight();
        g.setColor(color(color, globe, v.z));
        String[] lines = Layout.lines(text);
        for (int i = 0; i < lines.length; ++i) {
            int width = (align > 0) ? 0 : (align < 0) ? g.getFontMetrics().stringWidth(lines[i]) : g.getFontMetrics().stringWidth(lines[i]) / 2;
            g.drawString(lines[i], X.x - width, X.y + height / 2 + (int) ((line + i) * spacing) - 1);
        }
    }
}
