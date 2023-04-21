package org.game.thyvin.graphics;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import javax.media.opengl.GL;
import org.ai.influencemap.InfluenceMap;
import org.game.thyvin.logic.room.ThyvinSampleNode;
import org.graphics.BaseDElement;
import org.graphics.DElement;
import org.graphics.GLDrawing;
import org.utils.Triple;

public class InfluenceMapDElement extends BaseDElement implements DElement {

    private final Collection<Triple<Point, Polygon, Double>> nodeValues = new ArrayList<Triple<Point, Polygon, Double>>();

    private double lowest, highest;

    public static final Color INFLUENCE_COLOR_ALLY = Color.yellow, INFLUENCE_COLOR_ENEMY = Color.red;

    private static final int depth = -150;

    public void setInfluenceMap(InfluenceMap<ThyvinSampleNode> influenceMap) {
        nodeValues.clear();
        if (influenceMap != null) {
            lowest = influenceMap.getLowest();
            highest = influenceMap.getHighest();
            for (Entry<ThyvinSampleNode, Double> nodeValue : influenceMap.getInfluenceMap().entrySet()) {
                final ThyvinSampleNode node = nodeValue.getKey();
                if (node != null) {
                    nodeValues.add(new Triple<Point, Polygon, Double>(new Point(node.getX(), node.getY()), node.getOutline().getPolygon(), nodeValue.getValue()));
                }
            }
        }
    }

    public boolean draw(GL gl, int time) {
        gl.glPushAttrib(GL.GL_CURRENT_BIT);
        for (Triple<Point, Polygon, Double> nodeValue : nodeValues) {
            final double value = nodeValue.getThird();
            final Point point = nodeValue.getFirst();
            if (value < 0.0) {
                GLDrawing.setColorSlow(gl, INFLUENCE_COLOR_ENEMY, value / lowest * 0.4);
            } else {
                GLDrawing.setColorSlow(gl, INFLUENCE_COLOR_ALLY, value / highest * 0.4);
            }
            gl.glPushMatrix();
            gl.glTranslated(point.getX(), point.getY(), 0);
            GLDrawing.fillPoly(gl, nodeValue.getSecond());
            gl.glPopMatrix();
        }
        gl.glPopAttrib();
        return true;
    }

    public int getDepth() {
        return depth;
    }
}
