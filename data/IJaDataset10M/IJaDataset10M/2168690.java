package net.sf.reactionlab;

import java.awt.*;
import java.awt.event.*;
import java.awt.Rectangle;
import java.util.*;
import java.util.List;
import javax.swing.*;
import java.io.Serializable;

class NetworkView extends JLayeredPane implements NetworkListener, ItemSelectable {

    private Calculator calc;

    private Environment env;

    private Network net;

    private Map nodes = new HashMap();

    private Map suggest = new HashMap();

    private Map arrows = new HashMap();

    private Node selected;

    private List listeners = Collections.synchronizedList(new ArrayList());

    private Comparator arrowSorter = new ArrowSorter();

    private static final Integer NODE_LAYER = new Integer(1);

    private static final Integer ARROW_LAYER = new Integer(2);

    private static final Integer DRAG_LAYER = new Integer(3);

    private static final Color selectColor = new Color(0x99, 0x99, 0xcc);

    NetworkView(Calculator calc) {
        this.calc = calc;
        env = calc.getEnvironment();
        calcPreferredSize();
        setSize(new Dimension(640, 400));
        net = env.getNetwork();
        net.addNetworkListener(this);
        setOpaque(true);
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private class Node extends JComponent implements MouseListener, MouseMotionListener {

        private Reactor r;

        private ReactorInfo info;

        private ReactorDrawer drawer;

        private Rectangle rect;

        private Rectangle selectRect;

        private boolean draggingNode;

        private Point dragPoint;

        private Arrow draggingArrow;

        private Connection draggingFrom;

        private ArrayList arrowsFrom = new ArrayList(4);

        private ArrayList arrowsTo = new ArrayList(4);

        Node(Reactor r) {
            this.r = r;
            info = env.getReactorInfo(r.getId());
            drawer = env.getReactorDrawer(r.getId());
            rect = drawer.getDraggableBounds();
            selectRect = new Rectangle(rect);
            selectRect.grow(6, 6);
            selectRect.translate(1, 1);
            this.setToolTipText(info.getName());
            this.setSize(drawer.getSize());
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
        }

        public void mouseClicked(MouseEvent e) {
            if (rect.contains(e.getPoint())) {
                processEventStub(e);
            }
        }

        public void mousePressed(MouseEvent e) {
            if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
                Point p = e.getPoint();
                if (rect.contains(p)) {
                    dragPoint = p;
                    draggingNode = true;
                    select(getReactor());
                } else {
                    draggingFrom = getConnectionAt(p);
                    if (draggingFrom != null) {
                        dragPoint = getHotSpot(draggingFrom.getIndex());
                        dragPoint.translate(this.getX(), this.getY());
                        draggingArrow = new Arrow(Color.red, dragPoint, dragPoint);
                        NetworkView.this.add(draggingArrow, DRAG_LAYER);
                    }
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (draggingNode) {
                setLayer(this, NODE_LAYER.intValue());
                calcPreferredSize();
                draggingNode = false;
            } else if (draggingArrow != null) {
                Point p = e.getPoint();
                p.translate(this.getX(), this.getY());
                Component c = NetworkView.this.getComponentAt(p);
                if (c != null && c instanceof Node) {
                    Node node = (Node) c;
                    p.translate(-node.getX(), -node.getY());
                    try {
                        if (node.rect.contains(p)) {
                            net.connect(draggingFrom, node.r);
                        } else {
                            Connection to = node.getConnectionAt(p);
                            if (to != null) draggingFrom.connectTo(to);
                        }
                    } catch (ReactorConnectionException rce) {
                        System.err.println(rce.getMessage());
                    }
                }
                Rectangle b = draggingArrow.getBounds();
                NetworkView.this.remove(draggingArrow);
                NetworkView.this.repaint(b);
                draggingArrow = null;
            }
        }

        public void mouseDragged(MouseEvent e) {
            Point p = e.getPoint();
            if (draggingNode) {
                p.translate(-dragPoint.x, -dragPoint.y);
                for (int i = 0, size = arrowsFrom.size(); i < size; i++) ((Arrow) arrowsFrom.get(i)).translateFrom(p.x, p.y);
                for (int i = 0, size = arrowsTo.size(); i < size; i++) ((Arrow) arrowsTo.get(i)).translateTo(p.x, p.y);
                p.translate(this.getX(), this.getY());
                this.setLocation(p);
            } else if (draggingArrow != null) {
                p.translate(this.getX(), this.getY());
                draggingArrow.setTo(p);
            }
        }

        private Connection getConnectionAt(Point p) {
            int index = drawer.getConnection(p);
            return (index >= 0) ? (Connection) net.getConnections(r).get(index) : null;
        }

        Point getHotSpot(int connectionIndex) {
            return drawer.getHotSpot(connectionIndex);
        }

        Reactor getReactor() {
            return r;
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int iconType;
            if (selected == this) {
                iconType = ReactorDrawer.SELECTED;
                g2.setColor(selectColor);
                g2.fillRoundRect(selectRect.x, selectRect.y, selectRect.width, selectRect.height, 8, 8);
            } else if (net.getError(r) != null) {
                iconType = ReactorDrawer.NOT_READY;
            } else {
                iconType = ReactorDrawer.READY;
            }
            drawer.draw(g2, iconType);
        }

        private void unmarkArrows() {
            for (int i = 0, size = arrowsFrom.size(); i < size; i++) {
                ((Arrow) arrowsFrom.get(i)).setIndex(0);
            }
        }

        private void markArrows() {
            int size = arrowsFrom.size();
            if (size > 1) {
                for (int i = 0; i < size; i++) {
                    ((Arrow) arrowsFrom.get(i)).setIndex(i + 1);
                }
            } else {
                unmarkArrows();
            }
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }
    }

    private void processEventStub(MouseEvent e) {
        processEvent(e);
    }

    public void reactorAdded(Network net, Reactor r) {
        Point p = (Point) suggest.get(r);
        if (p == null) p = new Point(100, 100);
        Node node = new Node(r);
        nodes.put(r, node);
        add(node, NODE_LAYER);
        int w = node.getWidth();
        int h = node.getHeight();
        node.setLocation(p.x - w / 2, p.y - h / 2);
        calcPreferredSize();
    }

    public void reactorRemoved(Network net, Reactor r) {
        Node node = getNode(r);
        if (node == selected) select(null);
        remove(node);
        nodes.remove(r);
        repaint(node.getBounds());
        calcPreferredSize();
    }

    public void reactorStateChanged(Network net, Reactor r, boolean statusChanged) {
        if (statusChanged) {
            getNode(r).repaint();
        }
    }

    public void connectionAdded(Network net, Connection from, Connection to) {
        Node n1 = getNode(from.getReactor());
        Node n2 = getNode(to.getReactor());
        Point p1 = n1.getHotSpot(from.getIndex());
        Point p2 = n2.getHotSpot(to.getIndex());
        p1.translate(n1.getX(), n1.getY());
        p2.translate(n2.getX(), n2.getY());
        Arrow arrow = new Arrow(Color.black, p1, p2);
        n1.arrowsFrom.add(arrow);
        n2.arrowsTo.add(arrow);
        arrows.put(from, arrow);
        arrows.put(arrow, from);
        Collections.sort(n1.arrowsFrom, arrowSorter);
        add(arrow, ARROW_LAYER);
        if (n1 == selected) selected.markArrows();
    }

    public void connectionRemoved(Network net, Connection from, Connection to) {
        Node n1 = getNode(from.getReactor());
        Node n2 = getNode(to.getReactor());
        Arrow arrow = (Arrow) arrows.get(from);
        n1.arrowsFrom.remove(arrow);
        n2.arrowsTo.remove(arrow);
        arrows.remove(from);
        arrows.remove(arrow);
        remove(arrow);
        repaint(arrow.getBounds());
        if (n1 == selected) selected.markArrows();
    }

    private void calcPreferredSize() {
        int max_x = 0;
        int max_y = 0;
        Component[] comps = getComponents();
        if (comps.length == 0) {
            max_x = 640;
            max_y = 400;
        }
        for (int i = 0; i < comps.length; i++) {
            Component c = comps[i];
            if (c instanceof Node) {
                Node node = (Node) c;
                max_x = Math.max(max_x, node.getX() + node.getWidth() + 5);
                max_y = Math.max(max_y, node.getY() + node.getHeight() + 5);
            }
        }
        setPreferredSize(new Dimension(max_x, max_y));
        revalidate();
    }

    private Node getNode(Reactor r) {
        return (Node) nodes.get(r);
    }

    public boolean isSelected(Reactor r) {
        return getNode(r) == selected;
    }

    public Reactor getReactor(MouseEvent e) {
        try {
            return ((Node) e.getSource()).getReactor();
        } catch (ClassCastException ex) {
            return null;
        }
    }

    public void select(Reactor r) {
        if (selected != null && selected.getReactor() == r) {
            return;
        }
        Node old = selected;
        selected = null;
        if (old != null) {
            old.unmarkArrows();
            old.repaint();
            processReactorDeselected(old.getReactor());
        }
        if (r != null) {
            selected = getNode(r);
            selected.markArrows();
            selected.repaint();
            processReactorSelected(r);
        }
    }

    public void suggestLocation(Reactor r, Point p) {
        suggest.put(r, p);
    }

    public void addItemListener(ItemListener l) {
        listeners.add(l);
    }

    public void removeItemListener(ItemListener l) {
        listeners.remove(l);
    }

    public Object[] getSelectedObjects() {
        if (selected == null) return null;
        return new Object[] { selected.getReactor() };
    }

    void processReactorSelected(Reactor r) {
        synchronized (listeners) {
            ItemEvent e = new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, r, ItemEvent.SELECTED);
            for (Iterator it = listeners.iterator(); it.hasNext(); ) ((ItemListener) it.next()).itemStateChanged(e);
        }
    }

    void processReactorDeselected(Reactor r) {
        synchronized (listeners) {
            ItemEvent e = new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, r, ItemEvent.DESELECTED);
            for (Iterator it = listeners.iterator(); it.hasNext(); ) ((ItemListener) it.next()).itemStateChanged(e);
        }
    }

    private class ArrowSorter implements Comparator, Serializable {

        public int compare(Object o1, Object o2) {
            Connection c1 = (Connection) arrows.get(o1);
            Connection c2 = (Connection) arrows.get(o2);
            return c1.getIndex() - c2.getIndex();
        }
    }
}
