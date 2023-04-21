package problem7;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.event.MouseEvent;

public class CreatePolygonLintener extends MyMouseAdapter {

    public CreatePolygonLintener(DrawPanel l) {
        this.listenee = l;
        rightClicked = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!rightClicked && tmp != null) {
            Polygon p = (Polygon) tmp.getShape();
            if (p.npoints == 2) {
                if (fill) {
                    tmp.setFill(false);
                }
            } else {
                tmp.setFill(fill);
            }
            if (p.npoints != 1) {
                --p.npoints;
            }
            p.addPoint(e.getX(), e.getY());
        }
        listenee.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (tmp == null) {
            tmp = new MyShape(ShapeType.POLYGON);
            tmp.setColor(c);
            tmp.setFill(fill);
            listenee.addShape(tmp);
            rightClicked = false;
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightClicked = true;
            tmp = null;
        } else {
            Polygon p = (Polygon) tmp.getShape();
            p.addPoint(e.getX(), e.getY());
            System.out.println("Add Point: (" + e.getX() + "," + e.getY() + ")");
            listenee.repaint();
        }
    }

    public void setColor(Color c) {
        this.c = c;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    private DrawPanel listenee;

    private MyShape tmp = null;

    ;

    private boolean rightClicked;

    private boolean fill, oldfill;

    private Color c;
}
