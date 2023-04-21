package com.cosylab.vdct.visual.scene;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;
import com.cosylab.vdct.model.Edge;
import com.cosylab.vdct.model.Model;
import com.cosylab.vdct.model.Node;
import com.cosylab.vdct.model.edit.MoveModelSceneObjectsEdit;
import com.cosylab.vdct.model.edit.UndoRedoSupport;
import com.cosylab.vdct.model.primitive.Primitive;

/**
 * This class is intended to be used only whit <code> ModelScene</scene> scene
 * implementation. It takes care of moving object on the scene. 
 *
 * @author jgolob
 */
public class ModelSceneMoveProvider implements MoveProvider {

    private HashMap<Object, Object> originals = new HashMap<Object, Object>();

    private ModelScene scene;

    private Point original;

    private boolean mouseDraged = false;

    private boolean enabled = true;

    private Model model;

    public ModelSceneMoveProvider(ModelScene scene, Model model) {
        this.scene = scene;
        this.model = model;
    }

    public void movementStarted(Widget widget) {
        mouseDraged = false;
        Object object = scene.findObject(widget);
        if (object != null && widget.getState().isSelected() == true) {
            for (Object o : scene.getSelectedObjects()) {
                if (scene.isNode(o)) {
                    Widget w = scene.findWidget(o);
                    if (w != null) {
                        originals.put(o, w.getPreferredLocation());
                    }
                } else if (scene.isEdge(o)) {
                    ConnectionWidget w = (ConnectionWidget) scene.findWidget(o);
                    if (w != null) {
                        originals.put(o, w.getControlPoints());
                    }
                } else if (scene.isPrimitive(o)) {
                    Widget w = scene.findWidget(o);
                    if (w != null) {
                        originals.put(o, w.getPreferredLocation());
                    }
                }
            }
        }
    }

    public void movementFinished(Widget widget) {
        Object o = scene.findObject(widget);
        if (mouseDraged == true) {
            UndoRedoSupport.instance.getUndoableEditSupport(model).postEdit(new MoveModelSceneObjectsEdit(scene, new HashMap<Object, Object>(originals)));
            mouseDraged = false;
        }
        originals.clear();
        original = null;
        enabled = true;
    }

    public Point getOriginalLocation(Widget widget) {
        original = widget.getPreferredLocation();
        return original;
    }

    public void setNewLocation(Widget widget, Point location) {
        int dx = location.x - original.x;
        int dy = location.y - original.y;
        if (dx == 0 && dy == 0 || enabled == false) {
            return;
        } else {
            mouseDraged = true;
        }
        for (Map.Entry<Object, Object> entry : originals.entrySet()) {
            if (entry.getKey() instanceof Node) {
                Point point = (Point) entry.getValue();
                Point newLocation = new Point(point.x + dx, point.y + dy);
                ((Node) entry.getKey()).setPosition(newLocation);
            } else if (entry.getKey() instanceof Primitive) {
                Point point = (Point) entry.getValue();
                Point newLocation = new Point(point.x + dx, point.y + dy);
                ((Primitive) entry.getKey()).setPosition(newLocation);
            }
        }
        for (Map.Entry<Object, Object> entry : originals.entrySet()) {
            if (entry.getKey() instanceof Edge) {
                Edge edge = (Edge) entry.getKey();
                ConnectionWidget w = (ConnectionWidget) scene.findWidget(edge);
                if (w != null) {
                    List<Point> oldPoints = (List<Point>) entry.getValue();
                    Point sourcePoint = w.getSourceAnchor().compute(w.getSourceAnchorEntry()).getAnchorSceneLocation();
                    Point targetPoint = w.getTargetAnchor().compute(w.getTargetAnchorEntry()).getAnchorSceneLocation();
                    int xoff = 0, yoff = 0;
                    if (originals.containsKey(edge.getSource().getNode())) {
                        Point firstControlPoint = oldPoints.get(0);
                        xoff = sourcePoint.x - firstControlPoint.x;
                        yoff = sourcePoint.y - firstControlPoint.y;
                    } else if (originals.containsKey(edge.getTarget().getNode())) {
                        Point lastControlPoint = oldPoints.get(oldPoints.size() - 1);
                        xoff = targetPoint.x - lastControlPoint.x;
                        yoff = targetPoint.y - lastControlPoint.y;
                    }
                    List<Point> newPoints = new ArrayList<Point>();
                    for (Point p : oldPoints) {
                        newPoints.add(new Point(p.x + xoff, p.y + yoff));
                    }
                    edge.setControlPoints(newPoints);
                }
            }
        }
    }
}
