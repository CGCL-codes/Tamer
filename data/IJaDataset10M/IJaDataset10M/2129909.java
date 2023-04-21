package org.proteomecommons.MSExpedite.Graph;

import java.awt.Color;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import javax.swing.SwingUtilities;

public class ZoomController extends AbstractGraphControllerMouseUser {

    static int LINE_SIZE = 10;

    final TrackBall tb = new TrackBall();

    MouseMotionListener mouseMotionListener;

    MouseListener mouseListener;

    AbstractShape line = new LineShape();

    LinkedList<GraphicsState> history = new LinkedList<GraphicsState>();

    public ZoomController(IObjectDrawer objRenderer, Graph graph, boolean bOn) {
        this(objRenderer, graph);
        enable(bOn);
        line.setDrawingColor(Color.red.darker());
    }

    public ZoomController(IObjectDrawer objRenderer, Graph graph) {
        set(objRenderer);
        set(graph);
        line.setDrawingColor(Color.red.darker());
    }

    protected void eventDatapointsSet(Object newValue, Object oldValue) {
        clearZoomHistory();
    }

    private void buildEvents() {
        graph.addMouseMotionListener(tb);
        graph.addMouseListener(tb);
    }

    public void setHistory(LinkedList<GraphicsState> history) {
        this.history = history;
    }

    public LinkedList copyHistory() {
        LinkedList l = new LinkedList();
        for (int i = 0; i < history.size(); i++) {
            l.add(new GraphicsState(history.get(i)));
        }
        return l;
    }

    protected synchronized void zoomBack() {
        final int size = history.size();
        if (size == 0) {
            return;
        }
        final GraphicsState tmpState = history.removeLast();
        graph.set(tmpState);
        graph.reconstructImage();
    }

    private synchronized void clearZoomHistory() {
        history.clear();
    }

    public synchronized void zoomOff() {
        int endIndex, startIndex;
        startIndex = 0;
        Array2D datapoints = graph.getDataPoints();
        if (datapoints == null) {
            return;
        }
        if (datapoints != null) {
            endIndex = datapoints.length();
        } else {
            endIndex = startIndex;
        }
        if (datapoints.length() == 0) {
            return;
        }
        GraphicsState gs = new GraphicsState();
        gs.startIndex = startIndex;
        gs.endIndex = endIndex;
        float min = datapoints.x[0];
        float max = datapoints.x[endIndex - 1];
        gs.xMax = max;
        gs.xMin = min;
        graph.set(gs);
        isOn = false;
    }

    public void enable(boolean b) {
        isOn = b;
        String propertyChanged = null;
        if (b) {
            addListeners();
        } else {
            removeListeners();
        }
    }

    public synchronized void zoom(Range p) {
        history.add(new GraphicsState(graph.getGraphicsState()));
        int ix = -1;
        int peakIndex = -1;
        float peak = 0.0f;
        setViewport(p);
        graph.reconstructImage();
    }

    public void setViewport(Range p) {
        Object[] r = graph.getDrawers();
        if (r.length == 1 && r[0] instanceof BarDrawer) {
            graph.setViewportFromScreen(p, Graph.CENTER_DATAPOINTS);
            return;
        }
        boolean b = false;
        for (int i = 0; i < r.length; i++) {
            if (r[i] instanceof BarDrawer) {
                b = true;
            }
        }
        if (b) {
            graph.setViewportFromScreen(p, Graph.CENTER_DATAPOINTS);
        } else {
            graph.setViewportFromScreen(p);
        }
    }

    public final void enableQuickUnZoom(boolean b) {
        if (!b) {
            graph.removeMouseMotionListener(tb);
        } else {
            graph.addMouseMotionListener(tb);
        }
    }

    private void addListeners() {
        graph.removeMouseListener(tb);
        graph.removeMouseMotionListener(tb);
        graph.addMouseMotionListener(tb);
        graph.addMouseListener(tb);
    }

    private void removeListeners() {
        graph.removeMouseListener(tb);
        graph.removeMouseMotionListener(tb);
    }

    public int getMouseButtonUsed() {
        return InputEvent.BUTTON1_MASK | InputEvent.BUTTON3_MASK;
    }

    class TrackBall implements MouseListener, MouseMotionListener {

        boolean mousePressed = false;

        int xPixel = 0, yPixel = 0, w = 0, h = 0, lastX = 0;

        Range range = new Range();

        public TrackBall() {
        }

        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                Array2D datapoints = graph.getDataPoints();
                if (datapoints == null) {
                    return;
                }
                mousePressed = true;
                lastX = e.getX();
                xPixel = e.getX();
                yPixel = e.getY();
                line.init(xPixel, yPixel);
                objRenderer.add(line);
                graph.repaint();
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (mousePressed) {
                mousePressed = false;
                lastX = 0;
                objRenderer.remove(line);
                if (w > 1) {
                    zoom(getRange(e));
                }
                w = h = 0;
            }
            if (SwingUtilities.isRightMouseButton(e)) {
                zoomBack();
            }
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public final void mouseDragged(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                lastX = e.getX();
                w = e.getX() - xPixel;
                h = e.getY() - yPixel;
                lastX = e.getX();
                line = buildShape(line, e);
                int constrainedY = line.getStartPosition().y;
                line.setCurrentPosition(e.getX(), constrainedY);
                graph.repaint();
            }
        }

        AbstractShape buildShape(AbstractShape l, MouseEvent e) {
            w = Math.abs(e.getX() - xPixel);
            h = Math.abs(e.getY() - yPixel);
            if (e.getX() < xPixel) {
                l.init(e.getX(), yPixel);
                l.setCurrentPosition(e.getX() + w, yPixel + h);
                return line;
            }
            l.init(xPixel, yPixel);
            l.setCurrentPosition(xPixel + w, yPixel + h);
            return line;
        }

        Range getRange(MouseEvent e) {
            AbstractShape shape = buildShape(line, e);
            int start = shape.getStartPosition().x;
            int end = shape.getCurrentPosition().x;
            range.setStart(start);
            range.setEnd(end);
            return range;
        }

        public void mouseMoved(MouseEvent e) {
        }
    }
}
