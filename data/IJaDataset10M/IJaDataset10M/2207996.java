package com.objectwave.viewUtility;

import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;

/**
* This class is similar to a grid bag layout, but it automatically adds
* resizing capability.  It resizes components by changing their weight.
* That method of resizing doesn't always provide the behavior expected,
* so be sure to test with your system.
*/
public class ResizableGridBagLayout extends GridBagLayout implements MouseMotionListener, MouseListener {

    Point initDrag = null;

    int arrCount = 0;

    static Panel p = new Panel();

    static ResizableGridBagLayout layout = new ResizableGridBagLayout();

    /**
	*/
    public void addLayoutComponent(Component comp, Object constraints) {
        super.addLayoutComponent(comp, constraints);
        if (comp instanceof MouseTracker) return;
        Container cont = comp.getParent();
        MouseTracker track = new MouseTracker();
        setConstraints(track, getDefaultConstraints());
        cont.add(track);
        track.addMouseListener(this);
        track.addMouseMotionListener(this);
    }

    /**
	*/
    public void adjustHorizontal(int xAdjust, MouseTracker track) {
        Container parent = (Container) track.getParent();
        int totalX = parent.getSize().width;
        Component components[] = parent.getComponents();
        int[][] res = getLayoutDimensions();
        int count = 0;
        int jIdx = track.getColumn();
        double[][] weights = getLayoutWeights();
        Dimension[] mins = new Dimension[components.length];
        for (int i = 0; i < mins.length; i++) {
            mins[i] = components[i].getMinimumSize();
            GridBagConstraints c = lookupConstraints(components[i]);
            int val = mins[i].width + c.insets.left + c.insets.right;
            mins[i] = new Dimension(val, mins[i].height);
        }
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof MouseTracker) {
                count = i;
                MouseTracker track2 = (MouseTracker) components[i];
                if (jIdx == track2.getColumn() && (!track2.isVertical()) && track2.isVisible()) break;
            }
        }
        if (!allowHorizontalResize(res, mins, count, xAdjust, jIdx)) {
            return;
        }
        int minSum = 0;
        double weightSum = 0.0;
        int cnt = count - 1;
        for (int j = 0; j < res[0].length; j++) minSum += mins[cnt++].width;
        for (int j = 0; j < res[0].length; j++) weightSum += weights[0][j];
        double adj = (totalX - minSum) / weightSum;
        int size = res[0][jIdx - 1] + xAdjust;
        double newWeight = (size - mins[count - 1].width) / adj;
        double delta = weights[0][jIdx - 1] - newWeight;
        double newRightWeight = weights[0][jIdx + 1] + delta;
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof MouseTracker) {
                MouseTracker track2 = (MouseTracker) components[i];
                if (jIdx == track2.getColumn() && (!track2.isVertical()) && track2.isVisible()) {
                    GridBagConstraints c = lookupConstraints(components[i - 1]);
                    c.weightx = newWeight;
                    setConstraints(components[i - 1], c);
                    c = lookupConstraints(components[i + 1]);
                    c.weightx = newRightWeight;
                    setConstraints(components[i + 1], c);
                    components[i - 1].doLayout();
                    components[i + 1].doLayout();
                }
            }
        }
        Component comp = track;
        track.getParent().doLayout();
    }

    /**
	*/
    public void adjustVertical(int yAdjust, MouseTracker track) {
        Container parent = (Container) track.getParent();
        int totalY = parent.getSize().height;
        Component components[] = parent.getComponents();
        int[][] res = getLayoutDimensions();
        int count = 0;
        int jIdx = track.getColumn();
        double[][] weights = getLayoutWeights();
        Dimension[] mins = new Dimension[components.length];
        for (int i = 0; i < mins.length; i++) mins[i] = components[i].getMinimumSize();
        if (!allowVerticalResize(res, mins, jIdx, yAdjust)) return;
        int minSum = 0;
        double weightSum = 0.0;
        for (int j = 0; j < res[1].length; j++) minSum += mins[j].height;
        for (int j = 0; j < res[1].length; j++) weightSum += weights[1][j];
        double adj = (totalY - minSum) / weightSum;
        int size = res[1][jIdx - 1] + yAdjust;
        double newWeight = (size - mins[jIdx - 1].height) / adj;
        double delta = weights[1][jIdx - 1] - newWeight;
        double newRightWeight = weights[1][jIdx + 1] + delta;
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof MouseTracker) {
                MouseTracker track2 = (MouseTracker) components[i];
                if (jIdx == track2.getColumn() && (track2.isVertical()) && track2.isVisible()) {
                    GridBagConstraints c = lookupConstraints(components[i - 1]);
                    c.weighty = newWeight;
                    setConstraints(components[i - 1], c);
                    c = lookupConstraints(components[i + 1]);
                    c.weighty = newRightWeight;
                    setConstraints(components[i + 1], c);
                }
            }
        }
        Component comp = track;
        track.getParent().doLayout();
    }

    /**
	*/
    boolean allowHorizontalResize(int[][] actuals, Dimension[] mins, int idx, int xAdjust, int rat) {
        boolean leftMinimum = (actuals[0][rat - 1] == mins[idx - 1].width);
        boolean rightMinimum = (actuals[0][rat + 1] == mins[idx + 1].width);
        if (leftMinimum && rightMinimum) return false;
        if (leftMinimum && xAdjust < 0) return false;
        if (rightMinimum && xAdjust > 0) return false;
        return true;
    }

    /**
	*/
    boolean allowVerticalResize(int[][] actuals, Dimension[] mins, int idx, int yAdjust) {
        boolean leftMinimum = (actuals[1][idx - 1] == mins[idx - 1].height);
        boolean rightMinimum = (actuals[1][idx + 1] == mins[idx + 1].height);
        if (leftMinimum && rightMinimum) return false;
        if (leftMinimum && yAdjust < 0) return false;
        if (rightMinimum && yAdjust > 0) return false;
        return true;
    }

    /**
	*/
    protected void ArrangeGrid(Container parent) {
        if (arrCount++ < 3) {
            Component components[] = parent.getComponents();
            for (int i = 0; i < components.length; i++) if (components[i] instanceof MouseTracker) components[i].setVisible(false);
            super.ArrangeGrid(parent);
            changeLayoutDimensions(parent);
        }
        super.ArrangeGrid(parent);
    }

    /**
	*/
    public int[][] changeLayoutDimensions(Container parent) {
        int[][] res = getLayoutDimensions();
        Component components[] = parent.getComponents();
        int column = 1;
        int row = 0;
        for (int count = 1; count < components.length; count += 2) {
            boolean hide = count < components.length - 1;
            MouseTracker mse = (MouseTracker) components[count];
            mse.setColumn(column);
            GridBagConstraints cc = getConstraints(components[count - 1]);
            hide = hide && (cc.gridwidth != GridBagConstraints.REMAINDER);
            mse.setVisible(hide);
            GridBagConstraints mc = getConstraints(mse);
            if (cc.gridwidth == GridBagConstraints.REMAINDER) {
                row++;
                mse.setVisible(count < components.length - 1);
                mse.setVertical(true);
                mse.setColumn(row++);
                mc.gridwidth = GridBagConstraints.REMAINDER;
                mc.fill = GridBagConstraints.HORIZONTAL;
                setConstraints(mse, mc);
                column = 1;
            } else if (cc.gridwidth == GridBagConstraints.RELATIVE) {
                column = 1;
                mse.setVisible(false);
            } else column += 2;
        }
        return res;
    }

    /**
	*/
    protected void dumpIt(int[][] some) {
        for (int i = 0; i < some.length; i++) for (int j = 0; j < some[i].length; j++) System.out.println("i " + i + " j " + j + " " + some[i][j]);
    }

    /**
	*/
    GridBagConstraints getDefaultConstraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.ipady = c.ipadx = 2;
        c.fill = GridBagConstraints.BOTH;
        return c;
    }

    /**
	* Test routine
	*/
    public static void main(String[] argv) {
        Frame f = new Frame("GridBag Layout Example");
        f.add("Center", testPanel());
        f.pack();
        f.setSize(f.getPreferredSize());
        f.show();
    }

    /**
	*/
    protected static Button makebutton(String name, GridBagLayout gridbag, GridBagConstraints c) {
        Button button = new Button(name);
        gridbag.setConstraints(button, c);
        return button;
    }

    public void mouseClicked(MouseEvent e) {
    }

    /**
	*/
    public void mouseDragged(MouseEvent e) {
        Point p = e.getPoint();
        if (initDrag == null) {
            initDrag = p;
            return;
        }
        int yChange = (p.y - initDrag.y);
        MouseTracker track = (MouseTracker) e.getComponent();
        if (track.isVertical()) adjustVertical(yChange, track); else adjustHorizontal(p.x + initDrag.x, track);
        e.getComponent().getParent().validate();
    }

    public void mouseEntered(MouseEvent e) {
        MouseTracker track = (MouseTracker) e.getComponent();
        track.setEnterCursor();
    }

    public void mouseExited(MouseEvent e) {
        MouseTracker track = (MouseTracker) e.getComponent();
        track.setExitCursor();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    /**
	*/
    public void mouseReleased(MouseEvent e) {
        initDrag = null;
        e.getComponent().getParent().validate();
    }

    /**
	*/
    public void setConstraints(Component comp, GridBagConstraints constraints) {
        super.setConstraints(comp, constraints);
    }

    /**
	*/
    public static Panel testPanel() {
        GridBagConstraints c = new GridBagConstraints();
        p.setLayout(layout);
        Button b = makebutton("One", layout, c);
        p.add(b);
        c.weightx = 1.0;
        c.weighty = 1.0;
        b = makebutton("Two", layout, c);
        p.add(b);
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;
        b = makebutton("Three", layout, c);
        p.add(b);
        c.gridwidth = 0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        b = makebutton("Four A", layout, c);
        p.add(b);
        c.gridwidth = GridBagConstraints.REMAINDER;
        b = makebutton("Four B", layout, c);
        p.add(b);
        c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;
        b = makebutton("One", layout, c);
        p.add(b);
        b = makebutton("Two", layout, c);
        p.add(b);
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;
        b = makebutton("Three", layout, c);
        p.add(b);
        return p;
    }
}
