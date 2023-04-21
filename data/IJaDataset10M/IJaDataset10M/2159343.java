package org.vmasterdiff.gui.swing.text;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.logging.Logger;
import javas.swing.text.SegmentCache;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class DiffWrappedPlainView extends javas.swing.text.WrappedPlainView {

    private static final Logger log = Logger.getLogger(DiffWrappedPlainView.class.getName());

    protected int lineNumber = 1;

    /**
     * Creates a new MyWrappedPlainView.  Lines can be wrapped on
     * either character or word boundaries depending upon the
     * setting of the wordWrap parameter.
     *
     * @param elem the element underlying the view
     * @param wordWrap should lines be wrapped on word boundaries?
     */
    public DiffWrappedPlainView(Element elem, boolean wordWrap) {
        super(elem, wordWrap);
    }

    /**
     * Loads all of the children to initialize the view.
     * This is called by the <code>setParent</code> method.
     * Subclasses can reimplement this to initialize their
     * child views in a different manner.  The default
     * implementation creates a child view for each 
     * child element.
     *
     * @param f the view factory
     */
    protected void loadChildren(ViewFactory f) {
        Element e = getElement();
        System.out.println("e=" + e);
        int n = e.getElementCount();
        if (n > 0) {
            View[] added = new View[n];
            for (int i = 0; i < n; i++) {
                System.out.println("element=" + e.getElement(i));
                added[i] = new DiffWrappedLine(e.getElement(i), (lineNumber - 2) / 2);
            }
            replace(0, 0, added);
        }
    }

    /**
     * Update the child views in response to a 
     * document event.
     */
    protected void updateChildren(DocumentEvent e, Shape a) {
        Element elem = getElement();
        DocumentEvent.ElementChange ec = e.getChange(elem);
        if (ec != null) {
            Element[] removedElems = ec.getChildrenRemoved();
            Element[] addedElems = ec.getChildrenAdded();
            View[] added = new View[addedElems.length];
            for (int i = 0; i < addedElems.length; i++) {
                added[i] = new DiffWrappedLine(addedElems[i], (lineNumber - 2) / 2);
                lineNumber++;
            }
            replace(ec.getIndex(), removedElems.length, added);
            if (a != null) {
                preferenceChanged(null, true, true);
                getContainer().repaint();
            }
        }
    }

    /**
     * This is called by the nested wrapped line
     * views to determine the break location.  This can
     * be reimplemented to alter the breaking behavior.
     * It will either break at word or character boundaries
     * depending upon the break argument given at
     * construction.
     */
    protected int calculateBreakPosition(int p0, int p1) {
        int p;
        Segment segment = SegmentCache.getSharedSegment();
        loadText(segment, p0, p1);
        if (wordWrap) {
            p = p0 + Utilities.getBreakLocation(segment, metrics, tabBase, tabBase + getWidth() - 30, this, p0);
        } else {
            p = p0 + Utilities.getTabbedTextOffset(segment, metrics, tabBase, tabBase + getWidth() - 30, this, p0);
        }
        SegmentCache.releaseSharedSegment(segment);
        return p;
    }

    protected class DiffWrappedLine extends WrappedLine {

        protected int lineNumber = -1;

        protected DiffWrappedLine(Element elem) {
            super(elem);
        }

        protected DiffWrappedLine(Element elem, int lineNumber) {
            super(elem);
            this.lineNumber = lineNumber;
        }

        public void paint(Graphics g, Shape a) {
            Rectangle alloc = (Rectangle) a;
            int y = alloc.y + metrics.getAscent();
            int x = alloc.x;
            g.setColor(Color.RED);
            g.drawString("" + lineNumber, x, y);
            x += 30;
            g.fillRect(x, y, 4, 4);
            JTextComponent host = (JTextComponent) getContainer();
            Highlighter h = host.getHighlighter();
            LayeredHighlighter dh = (h instanceof LayeredHighlighter) ? (LayeredHighlighter) h : null;
            int p1 = getEndOffset();
            for (int p0 = getStartOffset(); p0 < p1; ) {
                int p = calculateBreakPosition(p0, p1);
                if (dh != null) {
                    if (p == p1) {
                        dh.paintLayeredHighlights(g, p0, p - 1, a, host, this);
                    } else {
                        dh.paintLayeredHighlights(g, p0, p, a, host, this);
                    }
                }
                drawLine(p0, p, g, x, y);
                p0 = (p == p0) ? p1 : p;
                y += metrics.getHeight();
            }
        }
    }
}
