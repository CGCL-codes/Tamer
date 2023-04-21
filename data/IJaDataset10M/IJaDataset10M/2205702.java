package org.netbeans.modules.visual.action;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;
import java.awt.event.MouseEvent;
import java.util.List;
import org.netbeans.api.visual.action.WidgetAction.State;

/**
 * @author Alex
 */
public class AddRemoveControlPointAction extends WidgetAction.Adapter {

    private double createSensitivity = 1.00, deleteSensitivity = 5.00;

    private ConnectionWidget cWidget;

    public AddRemoveControlPointAction() {
    }

    public AddRemoveControlPointAction(double createSensitivity, double deleteSensitivity) {
        this.createSensitivity = createSensitivity;
        this.deleteSensitivity = deleteSensitivity;
    }

    public State mouseClicked(Widget widget, WidgetMouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() == 2 && widget instanceof ConnectionWidget) {
            cWidget = (ConnectionWidget) widget;
            Point point = event.getPoint();
            addRemoveControlPoint(point);
        }
        return State.REJECTED;
    }

    /**
     * Adds or removes a control point on a specified location
     * @param localLocation the local location
     */
    private void addRemoveControlPoint(Point localLocation) {
        ArrayList<Point> list = new ArrayList<Point>(cWidget.getControlPoints());
        if (!removeControlPoint(localLocation, list, deleteSensitivity)) {
            Point exPoint = null;
            int index = 0;
            for (Point elem : list) {
                if (exPoint != null) {
                    Line2D l2d = new Line2D.Double(exPoint, elem);
                    if (l2d.ptLineDist(localLocation) < createSensitivity) {
                        list.add(index, localLocation);
                        break;
                    }
                }
                exPoint = elem;
                index++;
            }
        }
        cWidget.setControlPoints(list, false);
    }

    private boolean removeControlPoint(Point point, ArrayList<Point> list, double deleteSensitivity) {
        for (Point elem : list) {
            if (elem.distance(point) < deleteSensitivity) {
                list.remove(elem);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a control point at a specific index.
     * @param index the index in the list of control points
     * @return the control point at specified index; null, if the connection widget does not have control points
     * @throws ArrayIndexOutOfBoundsException when index is out of bounds
     */
    private Point getControlPoint(int index) {
        List<Point> controlPoints = cWidget.getControlPoints();
        if (controlPoints.size() <= 0) return null;
        return new Point(controlPoints.get(index));
    }

    /**
     * Sets a sensitivity.
     * @param createSensitivity the sensitivity for adding a control point
     * @param deleteSensitivity the sensitivity for removing a control point
     */
    public void setSensitivity(double createSensitivity, double deleteSensitivity) {
        this.createSensitivity = createSensitivity;
        this.deleteSensitivity = deleteSensitivity;
    }
}
