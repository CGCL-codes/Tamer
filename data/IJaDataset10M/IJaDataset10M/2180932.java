package co.edu.unal.geditor.view.controlpoints;

import java.awt.Graphics;
import java.awt.Point;
import co.edu.unal.geditor.model.BoundBox;

/**
 * @author root
 * 
 */
public abstract class ControlPoint {

    public ControlPoint() {
        this(new Point(10, 10));
    }

    public ControlPoint(Point point, int ControlPointDimension) {
        controlPointDimension = ControlPointDimension;
        m_ptLeftUp = new Point((point.x - (controlPointDimension / 2)), (point.y - (controlPointDimension / 2)));
        m_ptRightDown = new Point((point.x + (controlPointDimension / 2)), (point.y + (controlPointDimension / 2)));
    }

    public ControlPoint(Point point) {
        this(point, 10);
    }

    public void changePoint(Point newPoint) {
        m_ptLeftUp = new Point((newPoint.x - (controlPointDimension / 2)), (newPoint.y - (controlPointDimension / 2)));
        m_ptRightDown = new Point((newPoint.x + (controlPointDimension / 2)), (newPoint.y + (controlPointDimension / 2)));
    }

    public void paint(Graphics g) {
        g.fillRect(m_ptLeftUp.x, m_ptLeftUp.y, controlPointDimension, controlPointDimension);
    }

    public boolean contain(Point ptContained) {
        boolean contained = false;
        if (m_ptLeftUp.x < ptContained.x && m_ptLeftUp.y < ptContained.y && m_ptRightDown.x > ptContained.x && m_ptRightDown.y > ptContained.y) {
            contained = true;
        }
        return contained;
    }

    public abstract void resizing(Point cursorPosition, BoundBox modifiedBoundBox);

    public abstract void changeCursor();

    private Point m_ptRightDown;

    protected Point m_ptLeftUp;

    private int controlPointDimension;
}
