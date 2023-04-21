package com.jpatch.afw.ui.laf;

import com.jpatch.afw.ui.Background;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.metal.MetalSplitPaneUI;

public class SplitPaneUI extends BasicSplitPaneUI {

    private static final int oneTouchSize = 8;

    /**
      * Creates a new SplitPaneUI instance
      */
    public static ComponentUI createUI(JComponent x) {
        return new SplitPaneUI();
    }

    /**
      * Creates the default divider.
      */
    public BasicSplitPaneDivider createDefaultDivider() {
        return new SplitPaneDivider(this);
    }

    @SuppressWarnings("serial")
    private static final class SplitPaneDivider extends BasicSplitPaneDivider {

        public SplitPaneDivider(BasicSplitPaneUI ui) {
            super(ui);
        }

        @Override
        public void paint(Graphics g) {
            Background.fillComponent(this, g);
            paintComponents(g);
        }

        /**
	     * Creates and return an instance of JButton that can be used to
	     * collapse the left component in the split pane.
	     */
        protected JButton createLeftOneTouchButton() {
            JButton b = new JButton() {

                public void setBorder(Border b) {
                }

                public void paint(Graphics g) {
                    if (splitPane != null) {
                        int[] xs = new int[3];
                        int[] ys = new int[3];
                        int blockSize;
                        g.setColor(Color.black);
                        if (orientation == JSplitPane.VERTICAL_SPLIT) {
                            blockSize = Math.min(getHeight(), oneTouchSize);
                            xs[0] = blockSize;
                            xs[1] = 0;
                            xs[2] = blockSize << 1;
                            ys[0] = 1;
                            ys[1] = ys[2] = blockSize + 1;
                            g.drawPolygon(xs, ys, 3);
                        } else {
                            blockSize = Math.min(getWidth(), oneTouchSize);
                            xs[0] = xs[2] = blockSize + 1;
                            xs[1] = 1;
                            ys[0] = 0;
                            ys[1] = blockSize;
                            ys[2] = blockSize << 1;
                        }
                        g.fillPolygon(xs, ys, 3);
                    }
                }

                public boolean isFocusTraversable() {
                    return false;
                }
            };
            b.setMinimumSize(new Dimension(oneTouchSize, oneTouchSize));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            b.setFocusPainted(false);
            b.setBorderPainted(false);
            b.setRequestFocusEnabled(false);
            return b;
        }

        /**
	     * Creates and return an instance of JButton that can be used to
	     * collapse the right component in the split pane.
	     */
        protected JButton createRightOneTouchButton() {
            JButton b = new JButton() {

                public void setBorder(Border border) {
                }

                public void paint(Graphics g) {
                    if (splitPane != null) {
                        int[] xs = new int[3];
                        int[] ys = new int[3];
                        int blockSize;
                        if (orientation == JSplitPane.VERTICAL_SPLIT) {
                            blockSize = Math.min(getHeight(), oneTouchSize);
                            xs[0] = blockSize - 1;
                            xs[1] = blockSize * 2 - 2;
                            xs[2] = 1;
                            ys[0] = blockSize;
                            ys[1] = ys[2] = 1;
                        } else {
                            blockSize = Math.min(getWidth(), oneTouchSize);
                            xs[0] = xs[2] = 1;
                            xs[1] = blockSize;
                            ys[0] = 0;
                            ys[1] = blockSize - 1;
                            ys[2] = blockSize * 2 - 2;
                        }
                        g.setColor(Color.black);
                        g.fillPolygon(xs, ys, 3);
                    }
                }

                public boolean isFocusTraversable() {
                    return false;
                }
            };
            b.setMinimumSize(new Dimension(oneTouchSize, oneTouchSize));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            b.setFocusPainted(false);
            b.setBorderPainted(false);
            b.setRequestFocusEnabled(false);
            return b;
        }
    }
}
