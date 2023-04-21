package com.jchy.graph.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import com.jchy.graph.Edge;
import com.jchy.graph.Vertex;
import com.jchy.graph.core.GraphItData;
import com.jchy.graph.core.Pixel;

/** 
 * Main Screen Label
 * Used for rendering the actual graph vertices and edges and 
 * for handling mouse actions.
 * 
 */
public class MainScreenLabel extends JLabel implements MouseListener, MouseMotionListener {

    public static final long serialVersionUID = 999;

    private GraphItData gid;

    private int mousePressedX, mousePressedY, mouseReleasedX, mouseReleasedY;

    private List<DragVertexInfo> dragVertexInfoList;

    private String mode;

    public MainScreenLabel() {
        mode = "Select Vertices By Box";
        addMouseListener(this);
        addMouseMotionListener(this);
        gid = new GraphItData();
        gid.createSampleGraph();
        dragVertexInfoList = new ArrayList<DragVertexInfo>();
    }

    public GraphItData getGraphItData() {
        return gid;
    }

    public void open(String inFileName) {
        gid.open(inFileName);
        if (gid.getMap().entrySet().size() == 0) {
            gid.castCircle(200, 200);
        }
        for (Vertex aVertex : gid.getAGraph().getVertices()) {
            if (gid.getColors().get(aVertex) == null) {
                gid.getColors().put(aVertex, Color.BLACK);
            }
        }
        repaint();
    }

    public void save() {
        gid.save();
    }

    public void saveAs(String inFileName) {
        gid.saveAs(inFileName);
    }

    public void setMode(String inMode) {
        mode = inMode;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ArrayList<Vertex> vertices = gid.getAGraph().getVertices();
        ArrayList<Edge> edges = gid.getAGraph().getEdges();
        Pixel fromPixel, toPixel;
        boolean fromSelected, toSelected;
        double arrowAngle;
        int arrowLength;
        double angle;
        double radius;
        int arrow1X, arrow1Y, arrow2X, arrow2Y;
        double arrow1angle, arrow2angle;
        arrowAngle = 170 * Math.PI / 180;
        arrowLength = 15;
        for (Edge anEdge : edges) {
            fromPixel = gid.getMap().get(anEdge.getFrom());
            toPixel = gid.getMap().get(anEdge.getTo());
            fromSelected = gid.getSelected().contains(anEdge.getFrom());
            toSelected = gid.getSelected().contains(anEdge.getTo());
            g.setColor((fromSelected && toSelected) ? Color.RED : Color.BLACK);
            g.drawLine((int) fromPixel.getX(), (int) fromPixel.getY(), (int) toPixel.getX(), (int) toPixel.getY());
            radius = Math.sqrt(Math.pow(toPixel.getX() - fromPixel.getX(), 2) + Math.pow(toPixel.getY() - fromPixel.getY(), 2));
            if (radius > 0) {
                if (toPixel.getX() >= fromPixel.getX()) angle = Math.asin((toPixel.getY() - fromPixel.getY()) / radius); else angle = Math.PI - Math.asin((toPixel.getY() - fromPixel.getY()) / radius);
                arrow1angle = angle + arrowAngle;
                arrow1X = (int) (toPixel.getX() + arrowLength * Math.cos(arrow1angle));
                arrow1Y = (int) (toPixel.getY() + arrowLength * Math.sin(arrow1angle));
                g.drawLine(arrow1X, arrow1Y, (int) toPixel.getX(), (int) toPixel.getY());
                arrow2angle = angle - arrowAngle;
                arrow2X = (int) (toPixel.getX() + arrowLength * Math.cos(arrow2angle));
                arrow2Y = (int) (toPixel.getY() + arrowLength * Math.sin(arrow2angle));
                g.drawLine(arrow2X, arrow2Y, (int) toPixel.getX(), (int) toPixel.getY());
            }
        }
        for (Vertex aVertex : vertices) {
            Pixel aPixel = gid.getMap().get(aVertex);
            Color aColor = gid.getColors().get(aVertex);
            if (gid.getSelected().contains(aVertex)) {
                g.setColor(Color.RED);
                g.drawArc((int) aPixel.getX() - 3, (int) aPixel.getY() - 3, 6, 6, 0, 350);
            }
            g.setColor(aColor);
            g.drawArc((int) aPixel.getX() - 5, (int) aPixel.getY() - 5, 10, 10, 0, 354);
            g.drawString(String.valueOf(aVertex.getId()), (int) aPixel.getX() + 5, (int) aPixel.getY() - 5);
        }
    }

    public void mouseDragged(MouseEvent me) {
        int currX, currY;
        Pixel newPos;
        Pixel cursor;
        currX = me.getX();
        currY = me.getY();
        if (mode.equals("Move Closest Vertex") || mode.equals("Move Selected Vertices") || mode.equals("Move Vertices Connected to Closest") || mode.equals("Move Vertices Connected to Closest Same Color")) {
            for (DragVertexInfo dragVertexInfo : dragVertexInfoList) {
                cursor = dragVertexInfo.getCursor();
                newPos = new Pixel(cursor.getX() + currX, cursor.getY() + currY);
                gid.moveVertexTo(dragVertexInfo.getVertex(), newPos);
            }
        }
        repaint();
    }

    public void mouseMoved(MouseEvent me) {
    }

    public void mouseReleased(MouseEvent me) {
        mouseReleasedX = me.getX();
        mouseReleasedY = me.getY();
        if (mode.equals("Select Vertices By Box")) {
            gid.selectVerticesByBox(mousePressedX, mousePressedY, mouseReleasedX, mouseReleasedY);
        } else if (mode.equals("Select Vertices By Closest")) {
            gid.selectVerticesByNearest(mouseReleasedX, mouseReleasedY);
        } else if (mode.equals("Add Vertex")) {
            gid.addVertex(mouseReleasedX, mouseReleasedY);
        } else if (mode.equals("Add Edges By Closest")) {
            gid.addEdgesByClosest(mouseReleasedX, mouseReleasedY);
        } else if (mode.equals("Remove Edges By Closest")) {
            gid.removeEdgesByClosest(mouseReleasedX, mouseReleasedY);
        } else if (mode.equals("Select Vertices Connected to Closest")) {
            gid.selectVerticesConnectedToNearest(mouseReleasedX, mouseReleasedY);
        } else if (mode.equals("Remove Vertex By Closest")) {
            gid.removeClosestVertex(mouseReleasedX, mouseReleasedY);
        } else if (mode.equals("Select Components With Same Size as Closest")) gid.selectComponentsWithSameSizeAsSelected(mouseReleasedX, mouseReleasedY); else if (mode.equals("Color Closest Vertex")) gid.colorClosestVertex(mouseReleasedX, mouseReleasedY); else if (mode.equals("Sort Vertices From Closest")) gid.sortVerticesByClosest(mouseReleasedX, mouseReleasedY);
        repaint();
    }

    public void mousePressed(MouseEvent me) {
        Vertex closestVertex;
        Pixel closestVertexPosition;
        Pixel cursorRelative;
        DragVertexInfo dragVertexInfo;
        mousePressedX = me.getX();
        mousePressedY = me.getY();
        dragVertexInfoList.clear();
        if (mode.equals("Move Closest Vertex")) {
            closestVertex = gid.getClosestVertex(mousePressedX, mousePressedY);
            closestVertexPosition = gid.getMap().get(closestVertex);
            cursorRelative = new Pixel(closestVertexPosition.getX() - mousePressedX, closestVertexPosition.getY() - mousePressedY);
            dragVertexInfo = new DragVertexInfo(closestVertex, cursorRelative);
            dragVertexInfoList.add(dragVertexInfo);
        } else if (mode.equals("Move Selected Vertices")) {
            for (Vertex selected : gid.getSelected()) {
                closestVertexPosition = gid.getMap().get(selected);
                cursorRelative = new Pixel(closestVertexPosition.getX() - mousePressedX, closestVertexPosition.getY() - mousePressedY);
                dragVertexInfo = new DragVertexInfo(selected, cursorRelative);
                dragVertexInfoList.add(dragVertexInfo);
            }
        } else if (mode.equals("Move Vertices Connected to Closest")) {
            closestVertex = gid.getClosestVertex(mousePressedX, mousePressedY);
            closestVertexPosition = gid.getMap().get(closestVertex);
            cursorRelative = new Pixel(closestVertexPosition.getX() - mousePressedX, closestVertexPosition.getY() - mousePressedY);
            dragVertexInfo = new DragVertexInfo(closestVertex, cursorRelative);
            dragVertexInfoList.add(dragVertexInfo);
            for (Vertex comp : gid.getAGraph().getComponentVertices(gid.getClosestVertex(mousePressedX, mousePressedY))) {
                closestVertexPosition = gid.getMap().get(comp);
                cursorRelative = new Pixel(closestVertexPosition.getX() - mousePressedX, closestVertexPosition.getY() - mousePressedY);
                dragVertexInfo = new DragVertexInfo(comp, cursorRelative);
                dragVertexInfoList.add(dragVertexInfo);
            }
        } else if (mode.equals("Move Vertices Connected to Closest Same Color")) {
            closestVertex = gid.getClosestVertex(mousePressedX, mousePressedY);
            closestVertexPosition = gid.getMap().get(closestVertex);
            cursorRelative = new Pixel(closestVertexPosition.getX() - mousePressedX, closestVertexPosition.getY() - mousePressedY);
            dragVertexInfo = new DragVertexInfo(closestVertex, cursorRelative);
            dragVertexInfoList.add(dragVertexInfo);
            for (Vertex comp : gid.getAGraph().getComponentVertices(gid.getClosestVertex(mousePressedX, mousePressedY))) {
                if (gid.getColors().get(comp).equals(gid.getColors().get(closestVertex))) {
                    closestVertexPosition = gid.getMap().get(comp);
                    cursorRelative = new Pixel(closestVertexPosition.getX() - mousePressedX, closestVertexPosition.getY() - mousePressedY);
                    dragVertexInfo = new DragVertexInfo(comp, cursorRelative);
                    dragVertexInfoList.add(dragVertexInfo);
                }
            }
        }
    }

    public void mouseClicked(MouseEvent me) {
    }

    public void mouseExited(MouseEvent me) {
    }

    public void mouseEntered(MouseEvent me) {
    }
}

class DragVertexInfo {

    private Vertex vertex;

    private Pixel cursor;

    public DragVertexInfo(Vertex inVertex, Pixel inCursor) {
        vertex = inVertex;
        cursor = inCursor;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public Pixel getCursor() {
        return cursor;
    }
}
