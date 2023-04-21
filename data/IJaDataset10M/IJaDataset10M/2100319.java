package mail.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import mail.Errors;
import mail.node.*;

/**
 * 
 * @author Arne Müller
 *
 */
public class ConnectivityGraph extends JComponent implements Runnable {

    private static final long serialVersionUID = -1513857891343949563L;

    Node center;

    Hashtable<Node, Collection<Node>> graph = new Hashtable<Node, Collection<Node>>();

    Vector<Node> primary = new Vector<Node>();

    Hashtable<Node, Integer> positions = new Hashtable<Node, Integer>();

    double rotation = 0;

    Vector<Node> new_primary = new Vector<Node>();

    Hashtable<Node, Integer> new_positions = new Hashtable<Node, Integer>();

    double new_rotation = 0;

    double anim_step = 0;

    boolean animate = false;

    Node new_center = null;

    public ConnectivityGraph(Node n) {
        center = n;
        setPreferredSize(new Dimension(400, 400));
        NodeListener nl = new NodeListener();
        addMouseMotionListener(nl);
        addMouseListener(nl);
        new Thread(this).start();
    }

    private void bubblesort(Vector<Node> lprimary, Hashtable<Node, Integer> lpositions, Comparator<Node> comp) {
        Node old = lprimary.get(lprimary.size() - 1);
        for (int i = 0; i < lprimary.size(); i++) {
            for (int j = 0; j < lprimary.size(); j++) {
                Node next = lprimary.get(j);
                if (comp.compare(next, old) < 0) {
                    lprimary.set(j, old);
                    lprimary.set((j - 1 + lprimary.size()) % lprimary.size(), next);
                    lpositions.put(old, j);
                    lpositions.put(next, (j - 1 + lprimary.size()) % lprimary.size());
                } else {
                    old = next;
                }
            }
        }
    }

    public void run() {
        while (true) {
            if (new_center == null) new_center = center;
            Collection<Node> neighbors;
            try {
                neighbors = new_center.getNeighbors();
            } catch (RequestFailedException e1) {
                Errors.addException("ConnectivityGraph: disconnected Node", e1);
                neighbors = new ArrayList<Node>();
            }
            graph.put(new_center, neighbors);
            for (Node n : neighbors) {
                Collection<Node> toAdd;
                try {
                    toAdd = n.getNeighbors();
                    graph.put(n, toAdd);
                } catch (RequestFailedException e) {
                    Errors.addException("failed to get neighbors for ConnectivityGraph", e);
                }
            }
            Vector<Node> lprimary = new Vector<Node>(neighbors);
            Hashtable<Node, Integer> lpositions = new Hashtable<Node, Integer>();
            for (int i = 0; i < lprimary.size(); i++) {
                lpositions.put(lprimary.get(i), i);
            }
            if (lprimary.size() > 0) bubblesort(lprimary, lpositions, new ConnectionComparator(lprimary, lpositions));
            if (primary.equals(lprimary)) {
                primary = lprimary;
                positions = lpositions;
                center = new_center;
                animate = false;
                repaint();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            } else {
                double angle = 2 * Math.PI / lprimary.size();
                double old_angle = 2 * Math.PI / primary.size();
                int num = 0;
                new_rotation = 0;
                for (int i = 0; i < lprimary.size(); i++) {
                    Node n = lprimary.get(i);
                    Integer old_i = positions.get(n);
                    if (old_i != null) {
                        new_rotation += i * angle - old_i * old_angle;
                        num++;
                    }
                }
                if (num != 0) new_rotation = -new_rotation / num + rotation;
                new_primary = lprimary;
                new_positions = lpositions;
                anim_step = 0;
                animate = true;
                for (int i = 0; i <= 8 * num + 20; i++) {
                    anim_step = ((double) i) / (8 * num + 20);
                    repaint();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                animate = false;
                primary = new_primary;
                positions = new_positions;
                rotation = new_rotation;
                center = new_center;
                repaint();
            }
        }
    }

    private int kreisPosX(int radius, double angle) {
        return ((int) (Math.cos(angle) * radius));
    }

    private int kreisPosY(int radius, double angle) {
        return ((int) (Math.sin(angle) * radius));
    }

    public void paintComponent(Graphics g) {
        int center_x = getWidth() / 2;
        int center_y = getHeight() / 2;
        int radius = Math.min(center_x, center_y) - 30;
        if (animate) {
            double angle = 2 * Math.PI / new_primary.size();
            double old_angle = 2 * Math.PI / primary.size();
            for (int i = 0; i < new_primary.size(); i++) {
                Node ni = new_primary.get(i);
                Integer old_i = positions.get(ni);
                if (old_i != null) {
                    int x = center_x + (int) (kreisPosX(radius, angle * i + new_rotation) * anim_step + kreisPosX(radius, old_angle * old_i + rotation) * (1 - anim_step));
                    int y = center_y + (int) (kreisPosY(radius, angle * i + new_rotation) * anim_step + kreisPosY(radius, old_angle * old_i + rotation) * (1 - anim_step));
                    for (Node n : graph.get(ni)) {
                        Integer jI = positions.get(n);
                        Integer kI = new_positions.get(n);
                        if (jI != null && kI != null) {
                            int j = jI;
                            int k = kI;
                            int ox = center_x + (int) (kreisPosX(radius, angle * k + new_rotation) * anim_step + kreisPosX(radius, old_angle * j + rotation) * (1 - anim_step));
                            int oy = center_y + (int) (kreisPosY(radius, angle * k + new_rotation) * anim_step + kreisPosY(radius, old_angle * j + rotation) * (1 - anim_step));
                            g.setColor(Color.WHITE);
                            g.drawLine(x, y, ox, oy);
                        } else if (jI != null && n.equals(new_center)) {
                            int j = jI;
                            int ox = center_x + (int) (kreisPosX(radius, old_angle * j + rotation) * (1 - anim_step));
                            int oy = center_y + (int) (kreisPosY(radius, old_angle * j + rotation) * (1 - anim_step));
                            g.setColor(Color.BLACK);
                            g.drawLine(x, y, ox, oy);
                        } else if (kI != null && n.equals(center)) {
                            int k = kI;
                            int ox = center_x + (int) (kreisPosX(radius, angle * k + rotation) * anim_step);
                            int oy = center_y + (int) (kreisPosY(radius, angle * k + rotation) * anim_step);
                            g.setColor(Color.BLACK);
                            g.drawLine(x, y, ox, oy);
                        } else if (n.equals(center) && n.equals(new_center)) {
                            int ox = center_x;
                            int oy = center_y;
                            g.setColor(Color.BLACK);
                            g.drawLine(x, y, ox, oy);
                        }
                    }
                } else if (ni.equals(center)) {
                    int x = center_x + (int) (kreisPosX(radius, angle * i + new_rotation) * anim_step);
                    int y = center_y + (int) (kreisPosY(radius, angle * i + new_rotation) * anim_step);
                    for (Node n : graph.get(ni)) {
                        Integer jI = positions.get(n);
                        Integer kI = new_positions.get(n);
                        if (jI != null && kI != null) {
                            int j = jI;
                            int k = kI;
                            int ox = center_x + (int) (kreisPosX(radius, angle * k + new_rotation) * anim_step + kreisPosX(radius, old_angle * j + rotation) * (1 - anim_step));
                            int oy = center_y + (int) (kreisPosY(radius, angle * k + new_rotation) * anim_step + kreisPosY(radius, old_angle * j + rotation) * (1 - anim_step));
                            g.setColor(Color.WHITE);
                            g.drawLine(x, y, ox, oy);
                        } else if (jI != null && n.equals(new_center)) {
                            int j = jI;
                            int ox = center_x + (int) (kreisPosX(radius, old_angle * j + rotation) * (1 - anim_step));
                            int oy = center_y + (int) (kreisPosY(radius, old_angle * j + rotation) * (1 - anim_step));
                            g.setColor(Color.BLACK);
                            g.drawLine(x, y, ox, oy);
                        }
                    }
                }
            }
            for (int i = 0; i < new_primary.size(); i++) {
                Node ni = new_primary.get(i);
                Integer old_i = positions.get(ni);
                if (old_i != null) {
                    int x = center_x + (int) (kreisPosX(radius, angle * i + new_rotation) * anim_step + kreisPosX(radius, old_angle * old_i + rotation) * (1 - anim_step));
                    int y = center_y + (int) (kreisPosY(radius, angle * i + new_rotation) * anim_step + kreisPosY(radius, old_angle * old_i + rotation) * (1 - anim_step));
                    if (!graph.get(ni).contains(new_center)) {
                        g.setColor(Color.RED);
                    }
                    g.setColor(Color.BLACK);
                    g.fillOval(x - 2, y - 2, 4, 4);
                } else if (ni.equals(center)) {
                    int x = center_x + (int) (kreisPosX(radius, angle * i + new_rotation) * anim_step);
                    int y = center_y + (int) (kreisPosY(radius, angle * i + new_rotation) * anim_step);
                    if (!graph.get(ni).contains(new_center)) {
                        g.setColor(Color.RED);
                    }
                    g.setColor(Color.BLACK);
                    g.fillOval(x - 3, y - 3, 6, 6);
                }
            }
            Integer ci = positions.get(new_center);
            if (ci != null) {
                int x = center_x + (int) (kreisPosX(radius, old_angle * ci + rotation) * (1 - anim_step));
                int y = center_y + (int) (kreisPosY(radius, old_angle * ci + rotation) * (1 - anim_step));
                g.setColor(Color.BLACK);
                g.fillOval(x - 4, y - 4, 8, 8);
            } else if (new_center.equals(center)) {
                g.setColor(Color.BLACK);
                g.fillOval(center_x - 4, center_y - 4, 8, 8);
            }
        } else {
            double angle = 2 * Math.PI / primary.size();
            for (int i = 0; i < primary.size(); i++) {
                int x = center_x + kreisPosX(radius, angle * i + rotation);
                int y = center_y + kreisPosY(radius, angle * i + rotation);
                for (Node n : graph.get(primary.get(i))) {
                    Integer jI = positions.get(n);
                    if (jI != null) {
                        int j = jI;
                        int ox = center_x + kreisPosX(radius, angle * j + rotation);
                        int oy = center_y + kreisPosY(radius, angle * j + rotation);
                        g.setColor(Color.WHITE);
                        g.drawLine(x, y, ox, oy);
                    }
                }
            }
            g.setColor(Color.BLACK);
            String id = center.getID().toString();
            g.drawString(id, center_x - g.getFontMetrics().stringWidth(id) / 2, center_y);
            for (int i = 0; i < primary.size(); i++) {
                int x = center_x + kreisPosX(radius, angle * i + rotation);
                int y = center_y + kreisPosY(radius, angle * i + rotation);
                if (!graph.get(primary.get(i)).contains(center)) {
                    g.setColor(Color.RED);
                }
                g.setColor(Color.BLACK);
                g.fillOval(x - 2, y - 2, 4, 4);
            }
        }
    }

    public void setCenter(Node n) {
        new_center = n;
    }

    public static void showGraph(Node n) {
        JFrame f = new JFrame("Connectivity");
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(new ConnectivityGraph(n));
        f.pack();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }

    private class ConnectionComparator implements Comparator<Node> {

        Vector<Node> lprimary;

        Hashtable<Node, Integer> lpositions;

        public ConnectionComparator(Vector<Node> prim, Hashtable<Node, Integer> pos) {
            lprimary = prim;
            lpositions = pos;
        }

        private int value(Node n) {
            int out = 0;
            int myindex = lpositions.get(n);
            int numNodes = lprimary.size();
            Collection<Node> neighbors = graph.get(n);
            if (neighbors == null) return (0);
            for (Node neighbor : neighbors) {
                Integer n_index = lpositions.get(neighbor);
                if (n_index != null) {
                    int dist = (n_index - myindex + numNodes) % numNodes;
                    if (dist > numNodes / 2) dist = dist - numNodes;
                    if (Math.abs(dist) > Math.abs(out)) {
                        out = dist;
                    }
                }
            }
            return (out);
        }

        public int compare(Node o1, Node o2) {
            return (value(o1) - value(o2));
        }
    }

    private class NodeListener implements MouseListener, MouseMotionListener {

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
        }

        private Node getNodeAtPoint(int x, int y) {
            if (primary.size() == 0) return (null);
            double angle = Math.atan2(-y + getHeight() / 2, -x + getWidth() / 2) + Math.PI;
            angle -= rotation;
            int i = ((int) (angle * primary.size() / (2 * Math.PI) + 0.5) + primary.size()) % primary.size();
            return (primary.get(i));
        }

        public void mousePressed(MouseEvent e) {
            if (!animate) {
                setCenter(getNodeAtPoint(e.getX(), e.getY()));
            }
        }

        public void mouseMoved(MouseEvent e) {
            if (!animate) {
                Node n = getNodeAtPoint(e.getX(), e.getY());
                if (n != null) setToolTipText("Dist: " + center.distance(n));
            }
        }
    }
}
