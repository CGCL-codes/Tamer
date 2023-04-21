package uk.ac.lkl.migen.mockup.shapebuilder.ui.tool.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.lkl.common.ui.NotifyingLine;
import uk.ac.lkl.common.util.value.DoubleValue;
import uk.ac.lkl.migen.mockup.shapebuilder.model.shape.*;
import uk.ac.lkl.migen.mockup.shapebuilder.ui.*;

public class NewTieShapeTool extends ShapeTool {

    private ArrayList<Anchor> anchors;

    private ArrayList<Point2D.Double> intersectionPoints;

    static Logger dplogger = Logger.getLogger("Darren");

    public NewTieShapeTool(ShapePlotter shapePlotter) {
        super(shapePlotter, "New Tie");
    }

    protected void initialiseTool() {
        anchors = new ArrayList<Anchor>();
        intersectionPoints = new ArrayList<Point2D.Double>();
        List<NotifyingLine<DoubleValue>> lines = shapePlotter.getLines();
        for (int i = 0; i < lines.size(); i++) {
            for (int j = i + 1; j < lines.size(); j++) {
                NotifyingLine<DoubleValue> firstLine = lines.get(i);
                NotifyingLine<DoubleValue> secondLine = lines.get(j);
                if (shapePlotter.getShape(firstLine) == shapePlotter.getShape(secondLine)) continue;
                Line2D.Double line1 = firstLine.getLine();
                Line2D.Double line2 = secondLine.getLine();
                if (line1.intersectsLine(line2)) {
                    dplogger.debug("Intersection");
                    double xDiff1 = line1.getX2() - line1.getX1();
                    double yDiff1 = line1.getY2() - line1.getY1();
                    double gradient1 = yDiff1 / xDiff1;
                    double c1 = line1.getY1() - gradient1 * line1.getX1();
                    double xDiff2 = line2.getX2() - line2.getX1();
                    double yDiff2 = line2.getY2() - line2.getY1();
                    double gradient2 = yDiff2 / xDiff2;
                    double c2 = line2.getY1() - gradient2 * line2.getX1();
                    double x = (c1 - c2) / (gradient2 - gradient1);
                    double y = gradient1 * x + c1;
                    intersectionPoints.add(new Point2D.Double(x, y));
                }
            }
        }
    }

    protected void processMousePressed(MouseEvent e) {
    }

    protected void processMouseReleased(MouseEvent e) {
    }

    protected void processMouseClicked(MouseEvent e) {
    }

    @SuppressWarnings("unused")
    private Anchor getClosestAnchor(MouseEvent e, double threshold) {
        Double minDistance = null;
        Anchor closestAnchor = null;
        Point2D.Double mousePoint = new Point2D.Double(e.getX() / shapePlotter.getGridSize(), e.getY() / shapePlotter.getGridSize());
        for (Anchor anchor : anchors) {
            double distance = mousePoint.distance(anchor.getX(), anchor.getY());
            if (minDistance == null || distance < minDistance) {
                minDistance = distance;
                closestAnchor = anchor;
            }
        }
        if (minDistance < 1) return closestAnchor; else return null;
    }

    protected void processMouseMoved(MouseEvent e) {
    }

    protected void processMouseDragged(MouseEvent e) {
    }

    protected void paintToolMarks(Graphics2D g2) {
        for (Point2D point : intersectionPoints) {
            int x = (int) Math.round(point.getX()) * shapePlotter.getGridSize();
            int y = (int) Math.round(point.getY()) * shapePlotter.getGridSize();
            g2.setColor(Color.WHITE);
            g2.fillOval(x - 5, y - 5, 10, 10);
            g2.setColor(Color.BLACK);
            g2.drawOval(x - 5, y - 5, 10, 10);
        }
    }
}
