package com.cosylab.vdct.visual.primitive;

import com.cosylab.vdct.model.primitive.LinePrimitive;
import com.cosylab.vdct.visual.scene.ModelScene;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;

/**
 * Renders line.
 *
 * @author jgolob
 */
public class LineWidget extends AbstractPrimitiveWidget {

    private LinePrimitive primitive;

    private ModelScene scene;

    public LineWidget(ModelScene scene, LinePrimitive primitive) {
        super(scene, primitive);
        this.primitive = primitive;
        this.scene = scene;
    }

    protected void paintWidget() {
        Graphics2D g = getGraphics();
        if (getState().isSelected()) {
            g.setColor(Color.BLUE);
        } else {
            g.setColor(primitive.getForegroundColor());
        }
        Stroke oldStroke = null;
        if (primitive.isDashed()) {
            oldStroke = g.getStroke();
            g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[] { 10, 10 }, 0.0f));
        }
        ArrayList<Point> controlPoints = primitive.getControlPoints();
        Point temp = controlPoints.get(0);
        temp = convertSceneToLocal(temp);
        for (int i = 1; i < controlPoints.size(); i++) {
            Point p = controlPoints.get(i);
            p = convertSceneToLocal(p);
            g.drawLine(temp.x, temp.y, p.x, p.y);
            temp = p;
        }
        if (primitive.isSourceAnchorDisplayed()) {
            Point p = convertSceneToLocal(controlPoints.get(0));
            Point p2 = convertSceneToLocal(controlPoints.get(1));
            int posY2 = p2.y;
            int posY = p.y;
            int posX2 = p2.x;
            int posX = p.x;
            int dirX = (posX < posX2) ? (1) : (-1);
            int dirY = (posY < posY2) ? (1) : (-1);
            double angle = Math.atan(((double) (Math.abs(posY2 - posY))) / Math.abs(posX2 - posX));
            double arrowSize = LinePrimitive.ARROW_SIZE;
            double lineLength = Math.sqrt((posX2 - posX) * (posX2 - posX) + (posY2 - posY) * (posY2 - posY));
            if (arrowSize > lineLength / 2) {
                arrowSize = lineLength / 2;
            }
            int[] vertexX = new int[3];
            int[] vertexY = new int[3];
            vertexX[0] = posX;
            vertexY[0] = posY;
            vertexX[1] = (int) (posX + dirX * Math.cos(angle + LinePrimitive.ARROW_SHARPNESS) * arrowSize);
            vertexY[1] = (int) (posY + dirY * Math.sin(angle + LinePrimitive.ARROW_SHARPNESS) * arrowSize);
            vertexX[2] = (int) (posX + dirX * Math.cos(angle - LinePrimitive.ARROW_SHARPNESS) * arrowSize);
            vertexY[2] = (int) (posY + dirY * Math.sin(angle - LinePrimitive.ARROW_SHARPNESS) * arrowSize);
            g.fillPolygon(vertexX, vertexY, 3);
        }
        if (primitive.isTargetAnchorDisplayed()) {
            Point p = convertSceneToLocal(controlPoints.get(controlPoints.size() - 2));
            Point p2 = convertSceneToLocal(controlPoints.get(controlPoints.size() - 1));
            int posY2 = p2.y;
            int posY = p.y;
            int posX2 = p2.x;
            int posX = p.x;
            int dirX = (posX < posX2) ? (1) : (-1);
            int dirY = (posY < posY2) ? (1) : (-1);
            double angle = Math.atan(((double) (Math.abs(posY2 - posY))) / Math.abs(posX2 - posX));
            double arrowSize = LinePrimitive.ARROW_SIZE;
            double lineLength = Math.sqrt((posX2 - posX) * (posX2 - posX) + (posY2 - posY) * (posY2 - posY));
            if (arrowSize > lineLength / 2) {
                arrowSize = lineLength / 2;
            }
            int[] vertexX = new int[3];
            int[] vertexY = new int[3];
            vertexX[0] = posX2;
            vertexY[0] = posY2;
            vertexX[1] = (int) (posX2 - dirX * Math.cos(angle + LinePrimitive.ARROW_SHARPNESS) * arrowSize);
            vertexY[1] = (int) (posY2 - dirY * Math.sin(angle + LinePrimitive.ARROW_SHARPNESS) * arrowSize);
            vertexX[2] = (int) (posX2 - dirX * Math.cos(angle - LinePrimitive.ARROW_SHARPNESS) * arrowSize);
            vertexY[2] = (int) (posY2 - dirY * Math.sin(angle - LinePrimitive.ARROW_SHARPNESS) * arrowSize);
            g.fillPolygon(vertexX, vertexY, 3);
        }
        if (oldStroke != null) {
            g.setStroke(oldStroke);
        }
        super.drawControlPoints(g, controlPoints);
    }
}
