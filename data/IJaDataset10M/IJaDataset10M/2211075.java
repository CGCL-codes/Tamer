package org.fife.ui.rsyntaxtextarea;

import java.awt.*;
import javax.swing.text.*;
import javax.swing.event.*;

/**
 * The view used by {@link RSyntaxTextArea} when word wrap is enabled.<p>
 *
 * This class is a work-in-progress and will eventually replace
 * {@link WrappedSyntaxView} (which works, but ignores token styles).
 *
 * @author Robert Futrell
 * @version 0.2
 */
public class WrappedSyntaxView extends BoxView implements TabExpander {

    boolean widthChanging;

    int tabBase;

    int tabSize;

    /**
	 * This is reused to keep from allocating/deallocating.
	 */
    private Segment s, drawSeg;

    /**
	 * Another variable initialized once to keep from allocating/deallocating.
	 */
    private Rectangle tempRect;

    /**
	 * Cached for each paint() call so each drawView() call has access to it.
	 */
    private RSyntaxTextArea host;

    private FontMetrics metrics;

    /**
	 * The width of this view cannot be below this amount, as if the width
	 * is ever 0 (really a bug), we'll go into an infinite loop.
	 */
    private static final int MIN_WIDTH = 20;

    /**
	 * Creates a new WrappedSyntaxView.  Lines will be wrapped
	 * on character boundaries.
	 *
	 * @param elem the element underlying the view
	 */
    public WrappedSyntaxView(Element elem) {
        super(elem, Y_AXIS);
        s = new Segment();
        drawSeg = new Segment();
        tempRect = new Rectangle();
    }

    /**
	 * This is called by the nested wrapped line
	 * views to determine the break location.  This can
	 * be reimplemented to alter the breaking behavior.
	 * It will either break at word or character boundaries
	 * depending upon the break argument given at
	 * construction.
	 */
    protected int calculateBreakPosition(int p0, Token tokenList, float x0) {
        int p = p0;
        RSyntaxTextArea textArea = (RSyntaxTextArea) getContainer();
        float currentWidth = getWidth();
        if (currentWidth == Integer.MAX_VALUE) currentWidth = getPreferredSpan(X_AXIS);
        currentWidth = Math.max(currentWidth, MIN_WIDTH);
        Token t = tokenList;
        while (t != null && t.isPaintable()) {
            float tokenWidth = t.getWidth(textArea, this, x0);
            if (tokenWidth > currentWidth) {
                if (p == p0) {
                    return t.getOffsetBeforeX(textArea, this, 0, currentWidth);
                }
                return (t.type == Token.WHITESPACE) ? p + t.textCount : p;
            }
            currentWidth -= tokenWidth;
            x0 += tokenWidth;
            p += t.textCount;
            t = t.getNextToken();
        }
        return p + 1;
    }

    /**
	 * Gives notification from the document that attributes were changed
	 * in a location that this view is responsible for.
	 *
	 * @param e the change information from the associated document
	 * @param a the current allocation of the view
	 * @param f the factory to use to rebuild if the view has children
	 * @see View#changedUpdate
	 */
    public void changedUpdate(DocumentEvent e, Shape a, ViewFactory f) {
        updateChildren(e, a);
    }

    /**
	 * Draws a single view (i.e., a line of text for a wrapped view),
	 * wrapping the text onto multiple lines if necessary.
	 *
	 * @param g The graphics context in which to paint.
	 * @param r The rectangle in which to paint.
	 * @param view The <code>View</code> to paint.
	 * @param fontHeight The height of the font being used.
	 * @param y The y-coordinate at which to begin painting.
	 */
    protected void drawView(Graphics2D g, Rectangle r, View view, int fontHeight, int y) {
        float x = r.x;
        LayeredHighlighter dh = (LayeredHighlighter) host.getHighlighter();
        RSyntaxDocument document = (RSyntaxDocument) getDocument();
        Element map = getElement();
        int p0 = view.getStartOffset();
        int lineNumber = map.getElementIndex(p0);
        int p1 = view.getEndOffset();
        setSegment(p0, p1 - 1, document, drawSeg);
        int start = p0 - drawSeg.offset;
        Token token = document.getTokenListForLine(lineNumber);
        if (token != null && token.type == Token.NULL) {
            dh.paintLayeredHighlights(g, p0, p1, r, host, this);
            return;
        }
        while (token != null && token.isPaintable()) {
            int p = calculateBreakPosition(p0, token, x);
            dh.paintLayeredHighlights(g, p0, p, r, host, this);
            while (token != null && token.isPaintable() && token.offset + token.textCount - 1 < p) {
                x = token.paint(g, x, y, host, this);
                token = token.getNextToken();
            }
            if (token != null && token.isPaintable() && token.offset < p) {
                int tokenOffset = token.offset;
                Token temp = new DefaultToken(drawSeg, tokenOffset - start, p - 1 - start, tokenOffset, token.type);
                temp.paint(g, x, y, host, this);
                temp = null;
                token.makeStartAt(p);
            }
            p0 = (p == p0) ? p1 : p;
            x = r.x;
            y += fontHeight;
        }
    }

    /**
	 * Determines the maximum span for this view along an
	 * axis.  This is implemented to provide the superclass
	 * behavior after first making sure that the current font
	 * metrics are cached (for the nested lines which use
	 * the metrics to determine the height of the potentially
	 * wrapped lines).
	 *
	 * @param axis may be either View.X_AXIS or View.Y_AXIS
	 * @return  the span the view would like to be rendered into.
	 *           Typically the view is told to render into the span
	 *           that is returned, although there is no guarantee.  
	 *           The parent may choose to resize or break the view.
	 * @see View#getMaximumSpan
	 */
    public float getMaximumSpan(int axis) {
        updateMetrics();
        return super.getMaximumSpan(axis);
    }

    /**
	 * Determines the minimum span for this view along an
	 * axis.  This is implemented to provide the superclass
	 * behavior after first making sure that the current font
	 * metrics are cached (for the nested lines which use
	 * the metrics to determine the height of the potentially
	 * wrapped lines).
	 *
	 * @param axis may be either View.X_AXIS or View.Y_AXIS
	 * @return  the span the view would like to be rendered into.
	 *           Typically the view is told to render into the span
	 *           that is returned, although there is no guarantee.  
	 *           The parent may choose to resize or break the view.
	 * @see View#getMinimumSpan
	 */
    public float getMinimumSpan(int axis) {
        updateMetrics();
        return super.getMinimumSpan(axis);
    }

    /**
	 * Determines the preferred span for this view along an
	 * axis.  This is implemented to provide the superclass
	 * behavior after first making sure that the current font
	 * metrics are cached (for the nested lines which use
	 * the metrics to determine the height of the potentially
	 * wrapped lines).
	 *
	 * @param axis may be either View.X_AXIS or View.Y_AXIS
	 * @return  the span the view would like to be rendered into.
	 *           Typically the view is told to render into the span
	 *           that is returned, although there is no guarantee.  
	 *           The parent may choose to resize or break the view.
	 * @see View#getPreferredSpan
	 */
    public float getPreferredSpan(int axis) {
        updateMetrics();
        return super.getPreferredSpan(axis);
    }

    /**
	 * Returns the tab size set for the document, defaulting to 5.
	 *
	 * @return the tab size
	 */
    protected int getTabSize() {
        Integer i = (Integer) getDocument().getProperty(PlainDocument.tabSizeAttribute);
        int size = (i != null) ? i.intValue() : 5;
        return size;
    }

    /**
	 * Gives notification that something was inserted into the 
	 * document in a location that this view is responsible for.
	 * This is implemented to simply update the children.
	 *
	 * @param changes The change information from the associated document.
	 * @param a the current allocation of the view
	 * @param f the factory to use to rebuild if the view has children
	 * @see View#insertUpdate
	 */
    public void insertUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
        updateChildren(changes, a);
        Rectangle alloc = ((a != null) && isAllocationValid()) ? getInsideAllocation(a) : null;
        int pos = changes.getOffset();
        View v = getViewAtPosition(pos, alloc);
        if (v != null) v.insertUpdate(changes, alloc, f);
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
        int n = e.getElementCount();
        if (n > 0) {
            View[] added = new View[n];
            for (int i = 0; i < n; i++) added[i] = new WrappedLine(e.getElement(i));
            replace(0, 0, added);
        }
    }

    /**
	 * Provides a mapping, for a given region, from the document model
	 * coordinate space to the view coordinate space. The specified region is
	 * created as a union of the first and last character positions.<p>
	 *
	 * This is implemented to subtract the width of the second character, as
	 * this view's <code>modelToView</code> actually returns the width of the
	 * character instead of "1" or "0" like the View implementations in
	 * <code>javax.swing.text</code>.  Thus, if we don't override this method,
	 * the <code>View</code> implementation will return one character's width
	 * too much for its consumers (implementations of
	 * <code>javax.swing.text.Highlighter</code>).
	 *
	 * @param p0 the position of the first character (>=0)
	 * @param b0 The bias of the first character position, toward the previous
	 *        character or the next character represented by the offset, in
	 *        case the position is a boundary of two views; <code>b0</code>
	 *        will have one of these values:
	 * <ul>
	 *    <li> <code>Position.Bias.Forward</code>
	 *    <li> <code>Position.Bias.Backward</code>
	 * </ul>
	 * @param p1 the position of the last character (>=0)
	 * @param b1 the bias for the second character position, defined
	 *		one of the legal values shown above
	 * @param a the area of the view, which encompasses the requested region
	 * @return the bounding box which is a union of the region specified
	 *		by the first and last character positions
	 * @exception BadLocationException  if the given position does
	 *   not represent a valid location in the associated document
	 * @exception IllegalArgumentException if <code>b0</code> or
	 *		<code>b1</code> are not one of the
	 *		legal <code>Position.Bias</code> values listed above
	 * @see View#viewToModel
	 */
    public Shape modelToView(int p0, Position.Bias b0, int p1, Position.Bias b1, Shape a) throws BadLocationException {
        Shape s0 = modelToView(p0, a, b0);
        Shape s1;
        if (p1 == getEndOffset()) {
            try {
                s1 = modelToView(p1, a, b1);
            } catch (BadLocationException ble) {
                s1 = null;
            }
            if (s1 == null) {
                Rectangle alloc = (a instanceof Rectangle) ? (Rectangle) a : a.getBounds();
                s1 = new Rectangle(alloc.x + alloc.width - 1, alloc.y, 1, alloc.height);
            }
        } else {
            s1 = modelToView(p1, a, b1);
        }
        Rectangle r0 = s0.getBounds();
        Rectangle r1 = (s1 instanceof Rectangle) ? (Rectangle) s1 : s1.getBounds();
        if (r0.y != r1.y) {
            Rectangle alloc = (a instanceof Rectangle) ? (Rectangle) a : a.getBounds();
            r0.x = alloc.x;
            r0.width = alloc.width;
        }
        r0.add(r1);
        if (p1 > p0) r0.width -= r1.width;
        return r0;
    }

    /**
	 * Returns the next tab stop position after a given reference position.
	 * This implementation does not support things like centering so it
	 * ignores the tabOffset argument.
	 *
	 * @param x the current position >= 0
	 * @param tabOffset the position within the text stream
	 *   that the tab occurred at >= 0.
	 * @return the tab stop, measured in points >= 0
	 */
    public float nextTabStop(float x, int tabOffset) {
        if (tabSize == 0) return x;
        int ntabs = ((int) x - tabBase) / tabSize;
        return tabBase + ((ntabs + 1) * tabSize);
    }

    /**
	 * Paints the word-wrapped text.
	 *
	 * @param g The graphics context in which to paint.
	 * @param a The shape (usually a rectangle) in which to paint.
	 */
    public void paint(Graphics g, Shape a) {
        Rectangle alloc = (a instanceof Rectangle) ? (Rectangle) a : a.getBounds();
        tabBase = alloc.x;
        Graphics2D g2d = (Graphics2D) g;
        host = (RSyntaxTextArea) getContainer();
        int ascent = host.getMaxAscent();
        int fontHeight = host.getLineHeight();
        int n = getViewCount();
        int x = alloc.x + getLeftInset();
        int y = alloc.y + getTopInset();
        Rectangle clip = g.getClipBounds();
        for (int i = 0; i < n; i++) {
            tempRect.x = x + getOffset(X_AXIS, i);
            tempRect.y = y + getOffset(Y_AXIS, i);
            tempRect.width = getSpan(X_AXIS, i);
            tempRect.height = getSpan(Y_AXIS, i);
            if (tempRect.intersects(clip)) {
                View view = getView(i);
                drawView(g2d, alloc, view, fontHeight, tempRect.y + ascent);
            }
        }
    }

    /**
	 * Gives notification that something was removed from the 
	 * document in a location that this view is responsible for.
	 * This is implemented to simply update the children.
	 *
	 * @param changes The change information from the associated document.
	 * @param a the current allocation of the view
	 * @param f the factory to use to rebuild if the view has children
	 * @see View#removeUpdate
	 */
    public void removeUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
        updateChildren(changes, a);
        Rectangle alloc = ((a != null) && isAllocationValid()) ? getInsideAllocation(a) : null;
        int pos = changes.getOffset();
        View v = getViewAtPosition(pos, alloc);
        if (v != null) v.removeUpdate(changes, alloc, f);
    }

    /**
	 * Makes a <code>Segment</code> point to the text in our
	 * document between the given positions.  Note that the positions MUST be
	 * valid positions in the document.
	 *
	 * @param p0 The first position in the document.
	 * @param p1 The second position in the document.
	 * @param document The document from which you want to get the text.
	 * @param seg The segment in which to load the text.
	 */
    private void setSegment(int p0, int p1, Document document, Segment seg) {
        try {
            document.getText(p0, p1 - p0, seg);
        } catch (BadLocationException ble) {
            ble.printStackTrace();
            System.exit(0);
        }
    }

    /**
	 * Sets the size of the view.  This should cause layout of the view along
	 * the given axis, if it has any layout duties.
	 *
	 * @param width the width >= 0
	 * @param height the height >= 0
	 */
    public void setSize(float width, float height) {
        updateMetrics();
        if ((int) width != getWidth()) {
            preferenceChanged(null, true, true);
            widthChanging = true;
        }
        super.setSize(width, height);
        widthChanging = false;
    }

    /**
	 * Update the child views in response to a 
	 * document event.
	 */
    void updateChildren(DocumentEvent e, Shape a) {
        Element elem = getElement();
        DocumentEvent.ElementChange ec = e.getChange(elem);
        if (e.getType() == DocumentEvent.EventType.CHANGE) {
            getContainer().repaint();
        } else if (ec != null) {
            Element[] removedElems = ec.getChildrenRemoved();
            Element[] addedElems = ec.getChildrenAdded();
            View[] added = new View[addedElems.length];
            for (int i = 0; i < addedElems.length; i++) added[i] = new WrappedLine(addedElems[i]);
            replace(ec.getIndex(), removedElems.length, added);
            if (a != null) {
                preferenceChanged(null, true, true);
                getContainer().repaint();
            }
        }
        updateMetrics();
    }

    final void updateMetrics() {
        Component host = getContainer();
        Font f = host.getFont();
        metrics = host.getFontMetrics(f);
        tabSize = getTabSize() * metrics.charWidth('m');
    }

    /**
	 * Simple view of a line that wraps if it doesn't
	 * fit withing the horizontal space allocated.
	 * This class tries to be lightweight by carrying little 
	 * state of it's own and sharing the state of the outer class 
	 * with it's siblings.
	 */
    class WrappedLine extends View {

        int nlines;

        WrappedLine(Element elem) {
            super(elem);
        }

        /**
		 * Calculate the number of lines that will be rendered
		 * by logical line when it is wrapped.
		 */
        final int calculateLineCount() {
            int nlines = 0;
            int startOffset = getStartOffset();
            int p1 = getEndOffset();
            RSyntaxTextArea textArea = (RSyntaxTextArea) getContainer();
            RSyntaxDocument doc = (RSyntaxDocument) getDocument();
            Element map = doc.getDefaultRootElement();
            int line = map.getElementIndex(startOffset);
            Token tokenList = doc.getTokenListForLine(line);
            float x0 = 0;
            for (int p0 = startOffset; p0 < p1; ) {
                nlines += 1;
                x0 = RSyntaxUtilities.makeTokenListStartAt(tokenList, p0, WrappedSyntaxView.this, textArea, x0);
                int p = calculateBreakPosition(p0, tokenList, x0);
                p0 = (p == p0) ? ++p : p;
            }
            return nlines;
        }

        /**
		 * Determines the preferred span for this view along an
		 * axis.
		 *
		 * @param axis may be either X_AXIS or Y_AXIS
		 * @return   the span the view would like to be rendered into.
		 *           Typically the view is told to render into the span
		 *           that is returned, although there is no guarantee.  
		 *           The parent may choose to resize or break the view.
		 * @see View#getPreferredSpan
		 */
        public float getPreferredSpan(int axis) {
            switch(axis) {
                case View.X_AXIS:
                    float width = getWidth();
                    if (width == Integer.MAX_VALUE) {
                        return 100f;
                    }
                    return width;
                case View.Y_AXIS:
                    if (nlines == 0 || widthChanging) nlines = calculateLineCount();
                    int h = nlines * ((RSyntaxTextArea) getContainer()).getLineHeight();
                    return h;
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }

        /**
		 * Renders using the given rendering surface and area on that
		 * surface.  The view may need to do layout and create child
		 * views to enable itself to render into the given allocation.
		 *
		 * @param g the rendering surface to use
		 * @param a the allocated region to render into
		 * @see View#paint
		 */
        public void paint(Graphics g, Shape a) {
        }

        /**
		 * Provides a mapping from the document model coordinate space
		 * to the coordinate space of the view mapped to it.
		 *
		 * @param pos the position to convert
		 * @param a the allocated region to render into
		 * @return the bounding box of the given position is returned
		 * @exception BadLocationException  if the given position does not
		 *            represent a valid location in the associated document.
		 */
        public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
            Rectangle alloc = a.getBounds();
            RSyntaxTextArea textArea = (RSyntaxTextArea) getContainer();
            alloc.height = textArea.getLineHeight();
            alloc.width = 1;
            int p0 = getStartOffset();
            int p1 = getEndOffset();
            int testP = (b == Position.Bias.Forward) ? pos : Math.max(p0, pos - 1);
            RSyntaxDocument doc = (RSyntaxDocument) getDocument();
            Element map = doc.getDefaultRootElement();
            int line = map.getElementIndex(p0);
            Token tokenList = doc.getTokenListForLine(line);
            float x0 = alloc.x;
            while (p0 < p1) {
                x0 = RSyntaxUtilities.makeTokenListStartAt(tokenList, p0, WrappedSyntaxView.this, textArea, x0);
                int p = calculateBreakPosition(p0, tokenList, x0);
                if ((pos >= p0) && (testP < p)) {
                    alloc = RSyntaxUtilities.getLineWidthUpTo(textArea, s, p0, pos, WrappedSyntaxView.this, alloc, alloc.x);
                    return alloc;
                }
                if (p == p1 - 1 && pos == p1 - 1) {
                    if (pos > p0) {
                        alloc = RSyntaxUtilities.getLineWidthUpTo(textArea, s, p0, pos, WrappedSyntaxView.this, alloc, alloc.x);
                    }
                    return alloc;
                }
                p0 = (p == p0) ? p1 : p;
                alloc.y += alloc.height;
            }
            throw new BadLocationException(null, pos);
        }

        /**
		 * Provides a mapping from the view coordinate space to the logical
		 * coordinate space of the model.
		 *
		 * @param x the X coordinate
		 * @param y the Y coordinate
		 * @param a the allocated region to render into
		 * @return the location within the model that best represents the
		 *  given point in the view
		 * @see View#viewToModel
		 */
        public int viewToModel(float fx, float fy, Shape a, Position.Bias[] bias) {
            bias[0] = Position.Bias.Forward;
            Rectangle alloc = (Rectangle) a;
            RSyntaxDocument doc = (RSyntaxDocument) getDocument();
            int x = (int) fx;
            int y = (int) fy;
            if (y < alloc.y) {
                return getStartOffset();
            } else if (y > alloc.y + alloc.height) {
                return getEndOffset() - 1;
            } else {
                RSyntaxTextArea textArea = (RSyntaxTextArea) getContainer();
                alloc.height = textArea.getLineHeight();
                int p1 = getEndOffset();
                Element map = doc.getDefaultRootElement();
                int p0 = getStartOffset();
                int line = map.getElementIndex(p0);
                Token tlist = doc.getTokenListForLine(line);
                while (p0 < p1) {
                    RSyntaxUtilities.makeTokenListStartAt(tlist, p0, WrappedSyntaxView.this, textArea, alloc.x);
                    int p = calculateBreakPosition(p0, tlist, alloc.x);
                    if ((y >= alloc.y) && (y < (alloc.y + alloc.height))) {
                        if (x < alloc.x) {
                            return p0;
                        } else if (x > alloc.x + alloc.width) {
                            return p - 1;
                        } else {
                            int n = tlist.getListOffset(textArea, WrappedSyntaxView.this, alloc.x, x);
                            return Math.max(Math.min(n, p1 - 1), p0);
                        }
                    }
                    p0 = (p == p0) ? p1 : p;
                    alloc.y += alloc.height;
                }
                return getEndOffset() - 1;
            }
        }

        public void insertUpdate(DocumentEvent e, Shape a, ViewFactory f) {
            int n = calculateLineCount();
            if (this.nlines != n) {
                this.nlines = n;
                WrappedSyntaxView.this.preferenceChanged(this, false, true);
                getContainer().repaint();
            } else if (a != null) {
                Component c = getContainer();
                Rectangle alloc = (Rectangle) a;
                c.repaint(alloc.x, alloc.y, alloc.width, alloc.height);
            }
        }

        public void removeUpdate(DocumentEvent e, Shape a, ViewFactory f) {
            int n = calculateLineCount();
            if (this.nlines != n) {
                this.nlines = n;
                WrappedSyntaxView.this.preferenceChanged(this, false, true);
                getContainer().repaint();
            } else if (a != null) {
                Component c = getContainer();
                Rectangle alloc = (Rectangle) a;
                c.repaint(alloc.x, alloc.y, alloc.width, alloc.height);
            }
        }
    }
}
