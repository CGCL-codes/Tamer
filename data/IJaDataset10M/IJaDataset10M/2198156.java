package com.peterhi.application.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.Point;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;

public class JGlass extends JComponent implements SwingConstants, MouseListener {

    protected static final int INTEREST_NONE = 0;

    protected static final int INTEREST_TAB = 1;

    protected static final int INTEREST_EDGE_DROP = 2;

    private static final Stroke STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[] { 7, 7 }, 0);

    private static final int OFFSET = 0;

    private static final int LENGTH = 8;

    private static final Point[] pts = new Point[LENGTH];

    private static final int[] xs = new int[LENGTH];

    private static final int[] ys = new int[LENGTH];

    private JApplicationWindow window;

    private int interest;

    private Component component;

    private Point mouseLocation;

    public JGlass(JApplicationWindow window) {
        this.window = window;
        for (int i = 0; i < pts.length; i++) pts[i] = new Point(0, 0);
        window.setGlassPane(this);
        setVisible(true);
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int value) {
        interest = value;
    }

    public Point getMouseLocation() {
        return mouseLocation;
    }

    public void setMouseLocation(Point value) {
        mouseLocation = value;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component value) {
        component = value;
    }

    public int parsePosition(JTabbedPane tab, Point location) {
        if (isInTabs(tab, location)) return CENTER; else return getPosition(tab, location);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        Stroke oldStroke = g.getStroke();
        g.setStroke(STROKE);
        if (INTEREST_TAB == interest) paintTab(g); else if (INTEREST_EDGE_DROP == interest) paintEdgeDrop(g);
        g.setStroke(oldStroke);
    }

    private void paintTab(Graphics2D g) {
        JTabbedPane tab = (JTabbedPane) component;
        if (isInTabs(tab, mouseLocation)) {
            int tabIndex = getTabIndexFromPoint(tab);
            if (tabIndex < 0) g.draw(SwingUtilities.convertRectangle(tab, tab.getBounds(), this)); else {
                Rectangle tabBounds = SwingUtilities.convertRectangle(tab, tab.getBoundsAt(tabIndex), this);
                g.draw(tabBounds);
            }
        } else {
            Rectangle top = new Rectangle(0, 0, tab.getWidth(), tab.getHeight() / 4);
            Rectangle bottom = new Rectangle(0, tab.getHeight() / 4 * 3, tab.getWidth(), tab.getHeight() / 4);
            Rectangle left = new Rectangle(0, tab.getHeight() / 4, tab.getWidth() / 3, tab.getHeight() / 2);
            Rectangle right = new Rectangle(tab.getWidth() / 3 * 2, tab.getHeight() / 4, tab.getWidth() / 3, tab.getHeight() / 2);
            Rectangle topHalf = new Rectangle(0, 0, tab.getWidth(), tab.getHeight() / 2);
            Rectangle bottomHalf = new Rectangle(0, tab.getHeight() / 2, tab.getWidth(), tab.getHeight() / 2);
            Rectangle leftHalf = new Rectangle(0, 0, tab.getWidth() / 2, tab.getHeight());
            Rectangle rightHalf = new Rectangle(tab.getWidth() / 2, 0, tab.getWidth() / 2, tab.getHeight());
            if (top.contains(mouseLocation)) g.draw(SwingUtilities.convertRectangle(tab, topHalf, this)); else if (bottom.contains(mouseLocation)) g.draw(SwingUtilities.convertRectangle(tab, bottomHalf, this)); else if (left.contains(mouseLocation)) g.draw(SwingUtilities.convertRectangle(tab, leftHalf, this)); else if (right.contains(mouseLocation)) g.draw(SwingUtilities.convertRectangle(tab, rightHalf, this)); else g.draw(SwingUtilities.convertRectangle(tab, new Rectangle(0, 0, tab.getWidth(), tab.getHeight()), this));
        }
    }

    private void paintEdgeDrop(Graphics2D g) {
        JPanel panel = (JPanel) component;
        int w = getWidth();
        int h = getHeight();
        int w3 = getWidth() / 3;
        int h3 = getHeight() / 3;
        Rectangle rect;
        if (panel == window.getTopDock()) rect = new Rectangle(0, 0, w, h3); else if (panel == window.getBottomDock()) rect = new Rectangle(0, h - h3, w, h3); else if (panel == window.getLeftDock()) rect = new Rectangle(0, 0, w3, h); else rect = new Rectangle(w - w3, 0, w3, h);
        if (rect != null) g.draw(rect);
    }

    private int getTabIndexFromPoint(JTabbedPane tab) {
        if (tab == null) return -1;
        int index = -1;
        for (int i = 0; i < tab.getTabCount(); i++) {
            Rectangle rect = tab.getBoundsAt(i);
            if (rect.contains(mouseLocation)) index = i;
        }
        return index;
    }

    private Rectangle getTabBounds(JTabbedPane tab) {
        if (tab == null) return null;
        return SwingUtilities.convertRectangle(tab, new Rectangle(0, 0, tab.getWidth(), tab.getHeight()), this);
    }

    private void fillPoints() {
        for (int i = 0; i < pts.length; i++) {
            xs[i] = pts[i].x;
            ys[i] = pts[i].y;
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    private static boolean isInTabs(JTabbedPane tab, Point location) {
        boolean paintTab = false;
        for (int i = 0; i < tab.getTabCount(); i++) {
            Rectangle rect = tab.getBoundsAt(i);
            if (rect.contains(location)) paintTab = true;
        }
        return paintTab;
    }

    private static int getPosition(JTabbedPane tab, Point location) {
        Rectangle top = new Rectangle(0, 0, tab.getWidth(), tab.getHeight() / 4);
        Rectangle bottom = new Rectangle(0, tab.getHeight() / 4 * 3, tab.getWidth(), tab.getHeight() / 4);
        Rectangle left = new Rectangle(0, tab.getHeight() / 4, tab.getWidth() / 3, tab.getHeight() / 2);
        Rectangle right = new Rectangle(tab.getWidth() / 3 * 2, tab.getHeight() / 4, tab.getWidth() / 3, tab.getHeight() / 2);
        if (top.contains(location)) return TOP; else if (bottom.contains(location)) return BOTTOM; else if (left.contains(location)) return LEFT; else if (right.contains(location)) return RIGHT; else return CENTER;
    }
}
