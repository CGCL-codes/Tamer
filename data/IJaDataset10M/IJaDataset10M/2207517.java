package net.sf.jeda.gedasymbols.api.parts;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import net.sf.jeda.gedasymbols.api.parts.enums.ColorCategory;

/**
 *
 * @author eduardo-costa
 */
public class Pin extends Part {

    private int x1;

    private int y1;

    private int x2;

    private int y2;

    private ColorCategory color;

    private boolean busPin;

    private int whichEnd;

    public Pin(String[] line) {
        setX1(Integer.parseInt(line[1]));
        setY1(Integer.parseInt(line[2]));
        setX2(Integer.parseInt(line[3]));
        setY2(Integer.parseInt(line[4]));
        setColor(ColorCategory.values()[Integer.parseInt(line[6])]);
        setBusPin("1".equals(line[6]));
        setWhichEnd(Integer.parseInt(line[7]));
    }

    public boolean isBusPin() {
        return busPin;
    }

    public void setBusPin(boolean busPin) {
        this.busPin = busPin;
    }

    public ColorCategory getColor() {
        return color;
    }

    public void setColor(ColorCategory color) {
        this.color = color;
    }

    public int getWhichEnd() {
        return whichEnd;
    }

    public void setWhichEnd(int whichEnd) {
        this.whichEnd = whichEnd;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    @Override
    protected Rectangle calculatePartArea(Graphics2D g) {
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int w = Math.abs(x1 - x2) + 1;
        int h = Math.abs(y1 - y2) + 1;
        return new Rectangle(x, y, w, h);
    }

    @Override
    protected void paintPart(Graphics2D g) {
        g.setColor(getColor().getColor());
        g.drawLine(x1, y1, x2, y2);
    }
}
