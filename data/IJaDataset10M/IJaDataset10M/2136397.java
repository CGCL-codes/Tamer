package jscicalc;

/**
 * We use a NavigationFilter on the enty and display panels so that we can move the
 * Caret Left and Right along the expression keeping its position between
 * PObject object strings.
 * This makes it easier to have an editable expression.
 *
 * @author J.&nbsp;D.&nbsp;Lamb 
 * @version $Revision: 14 $
 * @see EntryLabel
 * @see LeftButton
 * @see RightButton
 */
abstract class Navigator extends javax.swing.text.NavigationFilter {

    /**
     * Create a new navigator and set dots to contain only the position one.
     */
    public Navigator() {
        super();
        dots = new java.util.Vector<Integer>();
        dots.add(1);
    }

    /**
     * Override default method. This stops us selecting text.
     * @param fb Used if we want to move the Caret without the move being intercepted
     * by this filter
     * @param dot The Caret position
     * @param bias See Sun&rsquo;s documentation. We pass this on but don&rsquo;t use it.
     */
    public void moveDot(javax.swing.text.NavigationFilter.FilterBypass fb, int dot, javax.swing.text.Position.Bias bias) {
        setDot(fb, dot, bias);
    }

    /**
     * Override default method. Make sure we&rsquo;re always &lsquo;between&rsquo;
     * PObject objects.
     * @param fb Used if we want to move the Caret without the move being intercepted
     * by this filter
     * @param dot The Caret position
     * @param bias See Sun&rsquo;s documentation. We pass this on but don&rsquo;t use it.
     */
    public void setDot(javax.swing.text.NavigationFilter.FilterBypass fb, int dot, javax.swing.text.Position.Bias bias) {
        if (dots.size() == 1 && dots.firstElement() == 1 && (dot > MAX || dot == 1)) {
            fb.setDot(dot, bias);
            return;
        }
        int l = 0;
        for (java.util.ListIterator<Integer> i = dots.listIterator(dots.size()); i.hasPrevious(); ) {
            l = i.previous();
            if (l <= dot) break;
        }
        int u = 0;
        for (Integer i : dots) {
            u = i;
            if (u >= dot) break;
        }
        dot = dot - l > u - dot ? u : l;
        fb.setDot(dot, bias);
    }

    /**
     * Override default method. Make sure we&rsquo;re always &lsquo;between&rsquo;
     * objects.
     * @param text See Sun&rsquo;s documentation. We don&rsquo;t use this except to
     * pass it on
     * @param pos The desired Caret position
     * @param bias See Sun&rsquo;s documentation. We don&rsquo;t use this except to
     * pass it on
     * @param direction See Sun&rsquo;s documentation. We don&rsquo;t use this except to
     * pass it on
     * @param biasRet See Sun&rsquo;s documentation. We don&rsquo;t use this except to
     * pass it on
     * @return A valid position at or near pos
     */
    public int getNextVisualPositionFrom(javax.swing.text.JTextComponent text, int pos, javax.swing.text.Position.Bias bias, int direction, javax.swing.text.Position.Bias[] biasRet) throws javax.swing.text.BadLocationException {
        int p = super.getNextVisualPositionFrom(text, pos, bias, direction, biasRet);
        if (p < 1) return 1; else if (p > dots.lastElement()) return dots.lastElement(); else if (direction == javax.swing.SwingConstants.WEST) {
            for (java.util.ListIterator<Integer> i = dots.listIterator(dots.size()); i.hasPrevious(); ) {
                int q = i.previous();
                if (q <= p) return q;
            }
        } else if (direction == javax.swing.SwingConstants.EAST) {
            for (Integer q : dots) {
                if (q >= p) return q;
            }
        }
        System.err.print("Navigator.getNextVisualPositionFrom(): can't filter ");
        System.err.println(p);
        return p;
    }

    /**
     * Allow access to dots so that it can be set by something else.
     * @return The vector of acceptable Caret positions
     */
    public java.util.Vector<Integer> dots() {
        return dots;
    }

    /**
     * Use this to translate between position 0, 1, ... , end and the
     * acceptable places for the Caret. For example
     * 3+sin 45 as text (even in html) would have vector
     * (1,2,3,7,8,9) indicating that the Caret can be placed at start, end or
     * between any characters, except that "sin " must be treated as it were
     * a single character.
     */
    protected java.util.Vector<Integer> dots;

    /**
     * Used i by setDot;
     */
    protected final int MAX = 34;
}
