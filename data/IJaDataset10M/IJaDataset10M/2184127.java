package game.graph;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * <p>Title: GraphLayerText</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * @author Jan Saidl
 * @version 1.0
 */
public class GraphLayerText extends GraphLayerLine {

    String text;

    public static final int SET_TEXT = GraphLayerLine.GET_MAX + 1;

    public GraphLayerText(int dx, int dy, Color c, String text) {
        super(dx, dy, 1, 1, c);
        Graphics2D g = buffer.createGraphics();
        this.text = text;
        width = g.getFontMetrics().stringWidth(text);
        height = g.getFontMetrics().getHeight();
        change(true);
    }

    protected void change(boolean dispatchEvent) {
        if (buffer != null && text != null) {
            Graphics2D g = buffer.createGraphics();
            width = g.getFontMetrics().stringWidth(text);
            height = g.getFontMetrics().getHeight();
        }
        super.change(true);
    }

    public String toString() {
        return "LayerText: [dx,dy] = [" + dx + "," + dy + "], [width,height] = [" + width + "," + height + "]";
    }

    public void set(int co, String value) {
        switch(co) {
            case SET_TEXT:
                text = value;
                change(true);
                break;
            default:
                super.set(co, value.length());
        }
    }

    protected void paintBuffer() {
        clearBuffer(true);
        Graphics2D g = buffer.createGraphics();
        g.setColor(paint_color);
        g.drawString(text, 0, height - 2);
        change = false;
    }
}
