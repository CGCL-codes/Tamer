package com.peterhi.application.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import javax.swing.*;

public class JDockGlassPane extends JComponent {

    private static final int OFFSET = 1;

    private static final Stroke STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[] { 7, 7 }, 0);

    private static final int PAINT_NOTHING = 0;

    private static final int PAINT_TAB = 1;

    private static final int PAINT_SPLIT = 2;

    private static final int LENGTH = 8;

    private static final Point[] pts = new Point[LENGTH];

    private static final int[] xs = new int[LENGTH];

    private static final int[] ys = new int[LENGTH];

    private JApplicationWindow owner;

    private int whatToPaint = PAINT_NOTHING;

    private JDockTabbedPane tabbedPane;

    private int tabIndex = -1;

    private Object splitConstraint;

    public JDockGlassPane(JApplicationWindow owner) {
        this.owner = owner;
        for (int i = 0; i < pts.length; i++) {
            pts[i] = new Point(0, 0);
        }
    }

    public void paintTab(JDockTabbedPane tabbedPane, int tabIndex) {
        this.tabbedPane = tabbedPane;
        this.tabIndex = tabIndex;
        whatToPaint = PAINT_TAB;
        repaint();
    }

    public void paintSplit(JDockTabbedPane tabbedPane, Object constraint) {
        this.tabbedPane = tabbedPane;
        this.splitConstraint = constraint;
        whatToPaint = PAINT_SPLIT;
        repaint();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        Stroke oldStroke = g.getStroke();
        g.setStroke(STROKE);
        if (tabbedPane != null) {
            if (whatToPaint == PAINT_TAB) {
                if (tabIndex < 0) {
                    g.draw(SwingUtilities.convertRectangle(tabbedPane, tabbedPane.getBounds(), owner.getContentPane()));
                } else {
                    Rectangle bounds = tabbedPane.getBounds();
                    Rectangle tabBounds = tabbedPane.getBoundsAt(tabIndex);
                    tabBounds = SwingUtilities.convertRectangle(tabbedPane, tabBounds, tabbedPane.getParent());
                    int place = tabbedPane.getTabPlacement();
                    if (place == SwingConstants.TOP) {
                        pts[0].x = tabBounds.x;
                        pts[0].y = tabBounds.y + tabBounds.height - OFFSET;
                        pts[1].x = tabBounds.x;
                        pts[1].y = tabBounds.y;
                        pts[2].x = tabBounds.x + tabBounds.width - OFFSET;
                        pts[2].y = tabBounds.y;
                        pts[3].x = tabBounds.x + tabBounds.width - OFFSET;
                        pts[3].y = tabBounds.y + tabBounds.height - OFFSET;
                        pts[4].x = bounds.x + bounds.width - OFFSET;
                        pts[4].y = tabBounds.y + tabBounds.height - OFFSET;
                        pts[5].x = bounds.x + bounds.width - OFFSET;
                        pts[5].y = bounds.y + bounds.height - OFFSET;
                        pts[6].x = bounds.x;
                        pts[6].y = bounds.y + bounds.height - OFFSET;
                        pts[7].x = bounds.x;
                        pts[7].y = tabBounds.y + tabBounds.height - OFFSET;
                    } else if (place == SwingConstants.BOTTOM) {
                        pts[0].x = tabBounds.x;
                        pts[0].y = tabBounds.y;
                        pts[1].x = tabBounds.x;
                        pts[1].y = tabBounds.y + tabBounds.height - OFFSET;
                        pts[2].x = tabBounds.x + tabBounds.width - OFFSET;
                        pts[2].y = tabBounds.y + tabBounds.height - OFFSET;
                        pts[3].x = tabBounds.x + tabBounds.width - OFFSET;
                        pts[3].y = tabBounds.y;
                        pts[4].x = bounds.x + bounds.width - OFFSET;
                        pts[4].y = tabBounds.y;
                        pts[5].x = bounds.x + bounds.width - OFFSET;
                        pts[5].y = bounds.y;
                        pts[6].x = bounds.x;
                        pts[6].y = bounds.y;
                        pts[7].x = bounds.x;
                        pts[7].y = tabBounds.y;
                    } else if (place == SwingConstants.LEFT) {
                        pts[0].x = tabBounds.x + tabBounds.width - OFFSET;
                        pts[0].y = tabBounds.y + tabBounds.height - OFFSET;
                        pts[1].x = tabBounds.x;
                        pts[1].y = tabBounds.y + tabBounds.height - OFFSET;
                        pts[2].x = tabBounds.x;
                        pts[2].y = tabBounds.y;
                        pts[3].x = tabBounds.x + tabBounds.width - OFFSET;
                        pts[3].y = tabBounds.y;
                        pts[4].x = tabBounds.x + tabBounds.width - OFFSET;
                        pts[4].y = bounds.y;
                        pts[5].x = bounds.x + bounds.width - OFFSET;
                        pts[5].y = bounds.y;
                        pts[6].x = bounds.x + bounds.width - OFFSET;
                        pts[6].y = bounds.y + bounds.height - OFFSET;
                        pts[7].x = tabBounds.x + tabBounds.width - OFFSET;
                        pts[7].y = bounds.y + bounds.height - OFFSET;
                    } else if (place == SwingConstants.RIGHT) {
                        pts[0].x = tabBounds.x;
                        pts[0].y = tabBounds.y;
                        pts[1].x = tabBounds.x + tabBounds.width - OFFSET;
                        pts[1].y = tabBounds.y;
                        pts[2].x = tabBounds.x + tabBounds.width - OFFSET;
                        pts[2].y = tabBounds.y + tabBounds.height - OFFSET;
                        pts[3].x = tabBounds.x;
                        pts[3].y = tabBounds.y + tabBounds.height - OFFSET;
                        pts[4].x = tabBounds.x;
                        pts[4].y = bounds.y + bounds.height - OFFSET;
                        pts[5].x = bounds.x;
                        pts[5].y = bounds.y + bounds.height - OFFSET;
                        pts[6].x = bounds.x;
                        pts[6].y = bounds.y;
                        pts[7].x = tabBounds.x;
                        pts[7].y = bounds.y;
                    }
                    for (int i = 0; i < pts.length; i++) {
                        pts[i] = SwingUtilities.convertPoint(tabbedPane, pts[i], owner.getContentPane());
                    }
                    fillPoints();
                    g.drawPolygon(xs, ys, LENGTH);
                }
            } else if (whatToPaint == PAINT_SPLIT) {
                Rectangle bounds = tabbedPane.getBounds();
                if (splitConstraint == BorderLayout.NORTH) {
                    bounds.height = bounds.height / 2;
                    bounds = SwingUtilities.convertRectangle(tabbedPane, bounds, owner.getContentPane());
                } else if (splitConstraint == BorderLayout.SOUTH) {
                    bounds.y = bounds.height / 2;
                    bounds.height /= 2;
                    bounds = SwingUtilities.convertRectangle(tabbedPane, bounds, JDockGlassPane.this);
                } else if (splitConstraint == BorderLayout.WEST) {
                    bounds.width = bounds.width / 2;
                    bounds = SwingUtilities.convertRectangle(tabbedPane, bounds, owner.getContentPane());
                } else if (splitConstraint == BorderLayout.EAST) {
                    bounds.x = bounds.width / 2;
                    bounds.width /= 2;
                    bounds = SwingUtilities.convertRectangle(tabbedPane, bounds, owner.getContentPane());
                } else if (splitConstraint == BorderLayout.CENTER) {
                    bounds = SwingUtilities.convertRectangle(tabbedPane, bounds, owner.getContentPane());
                }
                g.draw(bounds);
            }
        }
        g.setStroke(oldStroke);
    }

    private void fillPoints() {
        for (int i = 0; i < pts.length; i++) {
            xs[i] = pts[i].x;
            ys[i] = pts[i].y;
        }
    }
}
