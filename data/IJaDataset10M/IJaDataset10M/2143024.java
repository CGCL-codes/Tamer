package net.quies.math.plot;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.math.BigDecimal;
import java.text.Format;
import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 @author Pascal S. de Kloe
 */
public class ZoomSelection extends JComponent {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected ZoomSelection(InteractiveGraph graph, Point start) {
        this.graph = graph;
        origin = start;
        current = start;
        graphHeight = graph.getHeight();
        format = graph.getXAxis().getFormat();
        setSize(graph.getSize());
        graph.setCursor(DRAG_EAST_CURSOR);
    }

    public void setMousePosition(Point position) {
        current = position;
    }

    protected InteractiveGraph getGraph() {
        return graph;
    }

    /**
 * Gets the mathematical notation.
 */
    public String getIntervalDescription() {
        GraphInstance render = graph.getRender();
        if (render == null) return "";
        BigDecimal a = render.getXValue(origin.x);
        BigDecimal b = render.getXValue(current.x);
        StringBuffer buffer = new StringBuffer(40);
        buffer.append('[');
        if (a.compareTo(b) < 0) {
            graph.setCursor(DRAG_EAST_CURSOR);
            buffer.append(format.format(a));
            buffer.append(", ");
            buffer.append(format.format(b));
        } else {
            graph.setCursor(DRAG_WEST_CURSOR);
            buffer.append(format.format(b));
            buffer.append(", ");
            buffer.append(format.format(a));
        }
        buffer.append(')');
        return buffer.toString();
    }

    public boolean apply() {
        GraphInstance render = graph.getRender();
        if (render == null) return false;
        final BigDecimal A = render.getXValue(origin.x);
        final BigDecimal B = render.getXValue(current.x);
        final int compare = A.compareTo(B);
        if (compare == 0) return false;
        graph.setEnabled(false);
        graph.setCursor(InteractiveGraph.BUSSY_CURSOR);
        new Thread() {

            public void run() {
                try {
                    if (compare < 0) graph.setDomain(new GraphDomain(A, B)); else graph.setDomain(new GraphDomain(B, A));
                    graph.render();
                    graph.repaint(30L);
                } finally {
                    graph.setEnabled(true);
                    graph.setCursor(InteractiveGraph.DEFAULT_CURSOR);
                }
            }
        }.start();
        return true;
    }

    @Override
    public void paintComponent(Graphics g) {
        int x = Math.min(origin.x, current.x);
        int y = 0;
        int width = Math.max(origin.x, current.x) - x;
        int height = graphHeight - 1;
        g.setColor(SELECTION_COLOR);
        g.drawRect(x, y, width, height);
        ++x;
        ++y;
        --width;
        --height;
        g.setColor(SELECTION_TRANSPARENT);
        g.fillRect(x, y, width, height);
    }

    public Point getCurrent() {
        return current;
    }

    public void setCurrent(Point current) {
        this.current = current;
    }

    public Point getOrigin() {
        return origin;
    }

    public BigDecimal getStartValue() {
        GraphInstance render = graph.getRender();
        if (render == null) return null;
        if (origin == null || current == null) {
            return null;
        }
        return render.getXValue(Math.min(origin.x, current.x));
    }

    public BigDecimal getEndValue() {
        GraphInstance render = graph.getRender();
        if (render == null) return null;
        if (origin == null || current == null) {
            return null;
        }
        return render.getXValue(Math.max(origin.x, current.x));
    }

    public void setOrigin(Point origin) {
        this.origin.x = origin.x;
        this.origin.y = origin.y;
    }

    public static final Cursor DRAG_EAST_CURSOR = new Cursor(Cursor.E_RESIZE_CURSOR);

    public static final Cursor DRAG_WEST_CURSOR = new Cursor(Cursor.W_RESIZE_CURSOR);

    public final Color SELECTION_COLOR = UIManager.getColor("textHighlight");

    public final Color SELECTION_TRANSPARENT = new Color(SELECTION_COLOR.getRGB() & 0x00FFFFFF | 0x77000000, true);

    private final InteractiveGraph graph;

    private final int graphHeight;

    private final Format format;

    protected final Point origin;

    protected Point current;
}
