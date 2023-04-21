package unikrakadmin;

import java.util.*;
import unikrakadmin.graph.*;
import javax.swing.*;
import java.awt.*;

class DrawingPanel extends JPanel {

    private int xOffset = 0, yOffset = 0;

    private int width = 300, height = 300;

    private Rectangle clip;

    private EditorData d = null;

    private Point drawOffset;

    public DrawingPanel() {
        this.setOpaque(false);
        this.clip = new Rectangle(0, 0, 0, 0);
    }

    public void paintComponent(Graphics g) {
        g.setClip(clip.x, clip.y, clip.width, clip.height);
        g.translate(-xOffset + drawOffset.x, -yOffset + drawOffset.y);
        drawpoints(g);
        drawWalls(g);
        drawBox(g);
    }

    private double scaleD(double a) {
        return a * d.zoom;
    }

    private int scaleI(double a) {
        return (int) (a * d.zoom);
    }

    private void drawpoints(Graphics g) {
        if (d != null) {
            g.setColor(Color.black);
            Enumeration<EditorNode> nodeEnum = d.g.nodes.elements();
            while (nodeEnum.hasMoreElements()) {
                EditorNode n = nodeEnum.nextElement();
                if (n != null && n.floor == d.g.showFloor) {
                    if (d.g.selectedNodes.contains(new Integer(n.id))) g.setColor(Color.red); else if (n.id == d.g.edgemakeSelectedNode) g.setColor(Color.green); else g.setColor(Color.black);
                    g.fillOval(scaleI(n.pos.x - 3.0), scaleI(n.pos.y - 3.0), scaleI(7), scaleI(7));
                }
            }
            g.setColor(Color.blue);
            Enumeration<EditorEdge> edgeEnum = d.g.edges.elements();
            while (edgeEnum.hasMoreElements()) {
                EditorEdge e = edgeEnum.nextElement();
                if (e != null) {
                    if (d.g.selectedEdges.contains(new Integer(e.id))) continue;
                    try {
                        EditorNode n1 = (EditorNode) d.g.nodes.get(e.v1);
                        EditorNode n2 = (EditorNode) d.g.nodes.get(e.v2);
                        if (n1 != null && n2 != null && n1.floor == d.g.showFloor && n2.floor == d.g.showFloor) g.drawLine(scaleI(n1.pos.x), scaleI(n1.pos.y), scaleI(n2.pos.x), scaleI(n2.pos.y));
                    } catch (ClassCastException ex) {
                    }
                }
            }
            if (d.g.selectedEdges.size() > 0) {
                try {
                    Iterator<Integer> edgeIter = d.g.selectedEdges.iterator();
                    while (edgeIter.hasNext()) {
                        EditorEdge e = (EditorEdge) d.g.edges.get(edgeIter.next().intValue());
                        if (e != null) {
                            g.setColor(Color.red);
                            EditorNode n1 = (EditorNode) d.g.nodes.get(e.v1);
                            EditorNode n2 = (EditorNode) d.g.nodes.get(e.v2);
                            g.drawLine(scaleI(n1.pos.x), scaleI(n1.pos.y), scaleI(n2.pos.x), scaleI(n2.pos.y));
                        }
                    }
                } catch (ClassCastException ex) {
                }
            }
        }
    }

    private void drawWalls(Graphics g) {
        g.setColor(Color.blue);
        if (d != null && d.walls != null) {
            for (int i = 0; i < d.walls.length; i++) {
                g.drawLine(scaleI(d.walls[i].v1.x), scaleI(d.walls[i].v1.y), scaleI(d.walls[i].v2.x), scaleI(d.walls[i].v2.y));
            }
        }
    }

    private void drawDoors(Graphics g) {
        g.setColor(Color.green);
        if (d != null && d.doors != null) {
            for (int i = 0; i < d.doors.length; i++) {
                g.drawLine(scaleI(d.doors[i].v1.x), scaleI(d.doors[i].v1.y), scaleI(d.doors[i].v2.x), scaleI(d.doors[i].v2.y));
            }
        }
    }

    private void drawCorners(Graphics g) {
        if (d != null && d.corners != null) {
            g.setColor(Color.ORANGE);
            for (int i = 0; i < d.corners.length; i++) {
                if (d.corners[i].specialCorner) {
                    g.fillOval(scaleI(d.corners[i].pos.x - 3), scaleI(d.corners[i].pos.y - 3), scaleI(7), scaleI(7));
                }
            }
        }
    }

    private void drawBox(Graphics g) {
        if (d != null && d.boxStart != null && d.boxEnd != null) {
            int left = Math.min(scaleI(d.boxStart.x), scaleI(d.boxEnd.x));
            int right = Math.max(scaleI(d.boxStart.x), scaleI(d.boxEnd.x));
            int top = Math.min(scaleI(d.boxStart.y), scaleI(d.boxEnd.y));
            int bottom = Math.max(scaleI(d.boxStart.y), scaleI(d.boxEnd.y));
            g.setColor(Color.RED);
            g.drawLine(left, top, right, top);
            g.drawLine(right, top, right, bottom);
            g.drawLine(right, bottom, left, bottom);
            g.drawLine(left, bottom, left, top);
        }
    }

    public void setOffset(int x, int y) {
        xOffset = x;
        xOffset = y;
    }

    public void setOffset(Point p) {
        xOffset = p.x;
        yOffset = p.y;
    }

    public void setDrawOffset(Point p) {
        drawOffset = p;
    }

    public void setClip(Rectangle clip) {
        this.clip = clip;
    }

    public void setEditorData(EditorData d) {
        this.d = d;
    }
}
