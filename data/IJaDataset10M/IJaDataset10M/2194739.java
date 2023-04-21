package ru.vsu.triang.visual.op;

import java.awt.event.MouseEvent;
import ru.vsu.triang.model.Point;
import ru.vsu.triang.model.interfaces.IPoint;
import ru.vsu.triang.visual.EditorArea;
import ru.vsu.triang.visual.ToolOperation;

public class DragCanvasOperation implements ToolOperation {

    private IPoint startPoint;

    public void start(EditorArea ea, MouseEvent me) {
        startPoint = ea.toModelCoordinate(me.getPoint());
    }

    public void middle(EditorArea ea, MouseEvent me) {
        double vectorX = startPoint.getX() - ea.toModelCoordinateX(me.getPoint().getX());
        double vectorY = startPoint.getY() - ea.toModelCoordinateY(me.getPoint().getY());
        IPoint center = ea.getCenterView();
        ea.setCenterView(new Point(center.getX() + vectorX, center.getY() + vectorY));
    }

    public void end(EditorArea ea, MouseEvent me) {
    }
}
