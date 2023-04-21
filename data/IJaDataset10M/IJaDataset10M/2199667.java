package tufts.vue.gui;

import tufts.vue.VUE;
import tufts.vue.MapViewer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.BorderFactory;

/**
 * Scroll pane for MapViewer / MapViewport with a focus indicator.
 *
 * @version $Revision: 1.12 $ / $Date: 2010-02-03 19:15:47 $ / $Author: mike $
 * @author Scott Fraize
 */
public class MapScrollPane extends javax.swing.JScrollPane {

    public static final boolean UseMacFocusBorder = false;

    private FocusIndicator mFocusIndicator;

    private final MapViewer mViewer;

    public MapScrollPane(MapViewer viewer) {
        super(viewer);
        mViewer = viewer;
        setFocusable(false);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
        setWheelScrollingEnabled(true);
        getVerticalScrollBar().setUnitIncrement(16);
        getHorizontalScrollBar().setUnitIncrement(16);
        mFocusIndicator = new FocusIndicator(viewer);
        setCorner(LOWER_RIGHT_CORNER, mFocusIndicator);
        if (UseMacFocusBorder) {
        } else if (GUI.isMacAqua()) {
            if (GUI.isMacBrushedMetal()) setBorder(BorderFactory.createLineBorder(new Color(155, 155, 155), 1)); else setBorder(null);
        }
    }

    @Override
    protected javax.swing.JViewport createViewport() {
        return new tufts.vue.MapViewport(this);
    }

    public java.awt.Component getFocusIndicator() {
        return mFocusIndicator;
    }

    /** a little box for the lower right of a JScrollPane indicating this viewer's focus state */
    private static class FocusIndicator extends javax.swing.JComponent {

        final Color fill;

        final Color line;

        static final int inset = 4;

        final MapViewer mViewer;

        FocusIndicator(MapViewer viewer) {
            mViewer = viewer;
            if (GUI.isMacAqua()) {
                fill = GUI.AquaFocusBorderLight;
                line = GUI.AquaFocusBorderLight.darker();
            } else {
                fill = GUI.getToolbarColor();
                line = fill.darker();
            }
        }

        public void paintComponent(Graphics g) {
            paintIcon(g);
        }

        void paintIcon(Graphics g) {
            int w = getWidth();
            int h = getHeight();
            if (VUE.getActiveViewer() == mViewer) {
                g.setColor(fill);
                g.fillRect(inset, inset, w - inset * 2, h - inset * 2);
            }
            if (mViewer.isFocusOwner()) {
                g.setColor(line);
                w--;
                h--;
                g.drawRect(inset, inset, w - inset * 2, h - inset * 2);
            }
        }
    }
}
