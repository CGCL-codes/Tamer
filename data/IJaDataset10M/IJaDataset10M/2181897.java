package lab.core;

import lab.graphics.Disk;
import lab.graphics.Obstacle;
import lab.ui.LabCanvas;
import lab.ui.UIConfig;
import acm.graphics.GPoint;
import java.util.Vector;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class CanvasData {

    private World world;

    private LabCanvas canvas;

    private Disk startDisk, endDisk;

    private Vector<GPoint> obstacleBuffer;

    private Vector<Disk> obstacleGraphicsBuffer;

    private Object lock;

    private boolean bounded;

    public CanvasData(LabCanvas c) {
        canvas = c;
        world = new World(canvas.getWidth(), canvas.getHeight());
        canvas.addComponentListener(new ResizeListener());
        obstacleBuffer = new Vector<GPoint>();
        obstacleGraphicsBuffer = new Vector<Disk>();
        lock = new Object();
    }

    public void setStartPoint(GPoint pt) {
        if (startDisk != null) {
            startDisk.setLocation(pt);
        } else {
            startDisk = new Disk(pt, UIConfig.START_DISK_RADIUS, UIConfig.START_DISK_COLOR);
            canvas.add(startDisk);
        }
        world.setStart(pt);
    }

    public void setEndPoint(GPoint pt) {
        if (endDisk != null) {
            endDisk.setLocation(pt);
        } else {
            endDisk = new Disk(pt, UIConfig.END_DISK_RADIUS, UIConfig.END_DISK_COLOR);
            canvas.add(endDisk);
        }
        world.setGoal(pt);
    }

    /**
     * Adds a vertex to the current internal <code>Obstacle</code> buffer.  Note that
     * the <code>Obstacle</code> will not actually be constructed until the client calls
     * <code>registerObstacle()</code>.
     * @param pt
     */
    public void addObstacleVertex(GPoint pt) {
        synchronized (lock) {
            int index = obstacleBuffer.size();
            Disk vertex = new Disk(pt, UIConfig.ACTIVE_OBSTACLE_VERTEX_RADIUS, UIConfig.ACTIVE_OBSTACLE_VERTEX_COLOR);
            vertex.addMouseListener(new VertexDiskMouseListener(index));
            if (index > 0) {
                Disk last = obstacleGraphicsBuffer.get(index - 1);
                last.setRadius(UIConfig.OBSTACLE_VERTEX_RADIUS);
                last.setFillColor(UIConfig.OBSTACLE_VERTEX_COLOR);
            }
            canvas.add(vertex);
            obstacleGraphicsBuffer.add(vertex);
            obstacleBuffer.add(pt);
        }
    }

    public void removeObstacleVertex(int index) {
        synchronized (lock) {
            if (obstacleBuffer.isEmpty()) return;
            obstacleBuffer.remove(index);
            Disk gv = obstacleGraphicsBuffer.remove(index);
            canvas.remove(gv);
            for (int i = index, len = obstacleGraphicsBuffer.size(); i < len; i++) ((VertexDiskMouseListener) obstacleGraphicsBuffer.get(i).getLastAddedMouseListener()).shiftIndex();
            if (index == obstacleBuffer.size() && index > 0) {
                Disk newActv = obstacleGraphicsBuffer.get(index - 1);
                newActv.setRadius(UIConfig.ACTIVE_OBSTACLE_VERTEX_RADIUS);
                newActv.setFillColor(UIConfig.ACTIVE_OBSTACLE_VERTEX_COLOR);
            }
        }
    }

    public void registerObstacle() {
        synchronized (lock) {
            if (obstacleBuffer.isEmpty()) return;
            if (obstacleBuffer.size() <= 2) {
                discardObstacle();
                return;
            }
            Obstacle obs = new Obstacle(obstacleBuffer.toArray(new GPoint[obstacleBuffer.size()]));
            world.addObstacle(obs);
            canvas.add(obs);
            discardObstacle();
        }
    }

    public void discardObstacle() {
        synchronized (lock) {
            obstacleBuffer.clear();
            for (int i = 0, len = obstacleGraphicsBuffer.size(); i < len; i++) canvas.remove(obstacleGraphicsBuffer.get(i));
            obstacleGraphicsBuffer.clear();
        }
    }

    public boolean isObstacleRegistered() {
        return obstacleBuffer.isEmpty();
    }

    public void setWorldBounded(boolean bound) {
        bounded = bound;
        world.setBounded(bound);
    }

    public World getWorld() {
        return world;
    }

    /**
     * Clear everything (graphics and underlying <code>World</code>).  Note that
     * <code>canvas.removeAll()</code> need not be called after calling this.
     */
    public void clear() {
        canvas.removeAll();
        world.clear();
        startDisk = null;
        endDisk = null;
        obstacleBuffer.clear();
        obstacleGraphicsBuffer.clear();
        if (bounded) setWorldBounded(true);
    }

    /**
     * Alias of <code>clear()</code>.
     */
    public void reset() {
        clear();
    }

    private void resize() {
        world.setDimensions(canvas.getWidth(), canvas.getHeight());
    }

    private class ResizeListener implements ComponentListener {

        public void componentResized(ComponentEvent e) {
            resize();
        }

        public void componentHidden(ComponentEvent e) {
        }

        public void componentMoved(ComponentEvent e) {
        }

        public void componentShown(ComponentEvent e) {
        }
    }

    private class VertexDiskMouseListener implements MouseListener {

        private int bufferIndex;

        private Color colorCache;

        private Disk me;

        public VertexDiskMouseListener(int index) {
            bufferIndex = index;
        }

        public void shiftIndex() {
            bufferIndex--;
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
            me = obstacleGraphicsBuffer.get(bufferIndex);
            colorCache = me.getColor();
            me.setColor(UIConfig.MOUSEOVER_OBSTACLE_VERTEX_COLOR);
        }

        public void mouseExited(MouseEvent e) {
            if (me == null) me = obstacleGraphicsBuffer.get(bufferIndex);
            if (colorCache == null) {
                if (bufferIndex == (obstacleBuffer.size() - 1)) colorCache = UIConfig.ACTIVE_OBSTACLE_VERTEX_COLOR; else colorCache = UIConfig.OBSTACLE_VERTEX_COLOR;
            }
            me.setColor(colorCache);
        }

        public void mousePressed(MouseEvent e) {
            switch(e.getButton()) {
                case MouseEvent.BUTTON1:
                    removeObstacleVertex(bufferIndex);
                    break;
                case MouseEvent.BUTTON3:
                    break;
            }
        }

        public void mouseReleased(MouseEvent e) {
        }
    }
}
