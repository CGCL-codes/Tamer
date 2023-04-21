package tm.displayEngine;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;
import tm.utilities.Assert;

public class Link extends Object {

    private Attachment start;

    private Attachment end;

    private Vector iPoints = null;

    private boolean isNull = false;

    private class InteriorPoint {

        public int x;

        public int y;

        public double xScale;

        public double yScale;

        public InteriorPoint(int x, double xScale, int y, double yScale) {
            this.x = x;
            this.y = y;
            this.xScale = xScale;
            this.yScale = yScale;
        }
    }

    public Link(Attachment s, Attachment e) {
        Assert.check(s != null);
        start = s;
        end = e;
        start.makeConnect(this, true);
        if (end != null) {
            end.makeConnect(this, false);
        }
    }

    public void update(LinkedDatumDisplay endLDD) {
        if (end != null) end.breakConnect();
        if (endLDD == null) end = null; else {
            if (endLDD.getExpander() != null && endLDD.getExpander().getExpanded()) end = endLDD.getAttachment(endLDD.getIndirection() > start.getOwner().getIndirection() ? LinkedDatumDisplay.LT : LinkedDatumDisplay.RT); else end = endLDD.getAttachment(endLDD.getIndirection() > start.getOwner().getIndirection() ? LinkedDatumDisplay.LU : LinkedDatumDisplay.RU);
            end.makeConnect(this, false);
            if (endLDD.getIndirection() == start.getOwner().getIndirection()) {
                if (iPoints == null) iPoints = new Vector<InteriorPoint>(2);
                iPoints.setSize(2);
                iPoints.setElementAt(new InteriorPoint(14, 0., 0, 0.), 0);
                iPoints.setElementAt(new InteriorPoint(7, 0., 0, 1.), 1);
            } else if (iPoints != null) iPoints.removeAllElements();
        }
    }

    public void setNull(boolean isN) {
        Assert.check(end == null);
        isNull = isN;
    }

    public Attachment getStart() {
        return start;
    }

    public Attachment getEnd() {
        return end;
    }

    public void draw(Graphics screen) {
        Point from = start.getPoint();
        Point to = start.getStub();
        screen.drawLine(from.x, from.y, to.x, to.y);
        if (end == null) if (isNull) drawGround(screen, to.x, to.y); else drawInvalid(screen, to.x, to.y); else {
            from = to;
            to = end.getStub();
            if (iPoints != null) {
                int insideFromX = from.x;
                int insideFromY = from.y;
                for (int i = 0; i < iPoints.size(); i++) {
                    InteriorPoint ip = (InteriorPoint) iPoints.elementAt(i);
                    int insideToX = from.x + ip.x + (int) (ip.xScale * (to.x - from.x) + 0.5);
                    int insideToY = from.y + ip.y + (int) (ip.yScale * (to.y - from.y) + 0.5);
                    screen.drawLine(insideFromX, insideFromY, insideToX, insideToY);
                    insideFromX = insideToX;
                    insideFromY = insideToY;
                }
                from.x = insideFromX;
                from.y = insideFromY;
            }
            screen.drawLine(from.x, from.y, to.x, to.y);
            from = to;
            to = end.getPoint();
            screen.drawLine(from.x, from.y, to.x, to.y);
            if (from.x > to.x) drawLeftArrowhead(screen, to.x, to.y); else drawRightArrowhead(screen, to.x, to.y);
        }
    }

    private void drawGround(Graphics screen, int x, int y) {
        screen.drawLine(x, y, x, y + 4);
        screen.drawLine(x - 3, y + 4, x + 3, y + 4);
        screen.drawLine(x - 2, y + 6, x + 2, y + 6);
        screen.drawLine(x - 1, y + 8, x + 1, y + 8);
    }

    private void drawInvalid(Graphics screen, int x, int y) {
        screen.drawLine(x - 3, y - 3, x + 3, y + 3);
        screen.drawLine(x - 3, y + 3, x + 3, y - 3);
    }

    private void drawRightArrowhead(Graphics screen, int x, int y) {
        screen.drawLine(x, y, x - 4, y - 2);
        screen.drawLine(x, y, x - 4, y + 2);
    }

    private void drawLeftArrowhead(Graphics screen, int x, int y) {
        screen.drawLine(x, y, x + 4, y - 2);
        screen.drawLine(x, y, x + 4, y + 2);
    }

    public String toString() {
        return "link from " + start.toString() + ((end == null) ? "" : " to " + end.toString());
    }
}
