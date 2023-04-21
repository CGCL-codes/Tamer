package org.jmeld.ui.swing;

import javax.swing.text.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/** A highlighter that paints in layers.
 */
public class JMHighlighter implements Highlighter {

    public static final Integer LAYER0 = new Integer(1);

    public static final Integer LAYER1 = new Integer(2);

    public static final Integer LAYER2 = new Integer(3);

    public static final Integer LAYER3 = new Integer(4);

    public static final Integer UPPER_LAYER;

    private static ArrayList<Integer> layers;

    static {
        layers = new ArrayList<Integer>();
        layers.add(LAYER0);
        layers.add(LAYER1);
        layers.add(LAYER2);
        layers.add(LAYER3);
        UPPER_LAYER = layers.get(layers.size() - 1);
    }

    private Map<Integer, List<Highlighter.Highlight>> highlights;

    private JTextComponent component;

    private boolean doNotRepaint;

    public JMHighlighter() {
        highlights = new HashMap<Integer, List<Highlighter.Highlight>>();
    }

    public void setDoNotRepaint(boolean doNotRepaint) {
        this.doNotRepaint = doNotRepaint;
    }

    /**
   * Renders the highlights.
   *
   * @param g the graphics context
   */
    public void paint(Graphics g) {
        int upperLayer;
        List<Highlighter.Highlight> list;
        Rectangle a;
        Insets insets;
        Rectangle clip;
        int startOffset;
        int endOffset;
        int lineHeight;
        LineNumberBorder lineNumberBorder;
        clip = g.getClipBounds();
        lineHeight = component.getFontMetrics(component.getFont()).getHeight();
        startOffset = component.viewToModel(new Point(clip.x - lineHeight, clip.y));
        endOffset = component.viewToModel(new Point(clip.x, clip.y + clip.height + lineHeight));
        lineNumberBorder = null;
        if (component.getBorder() instanceof LineNumberBorder) {
            lineNumberBorder = (LineNumberBorder) component.getBorder();
        }
        if (lineNumberBorder != null) {
            lineNumberBorder.paintBefore(g);
        }
        a = null;
        for (Integer layer : layers) {
            list = highlights.get(layer);
            if (list == null) {
                continue;
            }
            if (list.size() == 0) {
                continue;
            }
            if (a == null) {
                a = component.getBounds();
                insets = component.getInsets();
                a.x = insets.left;
                a.y = insets.top;
                a.width -= insets.left + insets.right;
                a.height -= insets.top + insets.bottom;
            }
            for (Highlighter.Highlight hli : list) {
                if (hli.getStartOffset() > endOffset || hli.getEndOffset() < startOffset) {
                    continue;
                }
                hli.getPainter().paint(g, hli.getStartOffset(), hli.getEndOffset(), a, component);
            }
        }
        if (lineNumberBorder != null) {
            lineNumberBorder.paintAfter(g, startOffset, endOffset);
        }
    }

    public void install(JTextComponent c) {
        component = c;
        removeAllHighlights();
    }

    public void deinstall(JTextComponent c) {
        component = null;
    }

    public Object addHighlight(int p0, int p1, Highlighter.HighlightPainter painter) throws BadLocationException {
        return addHighlight(UPPER_LAYER, p0, p1, painter);
    }

    public Object addHighlight(Integer layer, int p0, int p1, Highlighter.HighlightPainter painter) throws BadLocationException {
        Document doc;
        HighlightInfo hli;
        doc = component.getDocument();
        hli = new HighlightInfo();
        hli.painter = painter;
        hli.p0 = doc.createPosition(p0);
        hli.p1 = doc.createPosition(p1);
        getLayer(layer).add(hli);
        repaint();
        return hli;
    }

    public void removeHighlight(Object object) {
        removeHighlight(UPPER_LAYER, object);
    }

    public void removeHighlight(Integer layer, Object object) {
        getLayer(layer).remove(object);
        repaint();
    }

    public void removeHighlights(Integer layer) {
        getLayer(layer).clear();
        repaint();
    }

    /**
   * Removes all highlights.
   */
    public void removeAllHighlights() {
        for (Integer layer : layers) {
            getLayer(layer).clear();
        }
        repaint();
    }

    public void changeHighlight(Object object, int p0, int p1) throws BadLocationException {
        changeHighlight(UPPER_LAYER, object, p0, p1);
    }

    public void changeHighlight(Integer layer, Object object, int p0, int p1) throws BadLocationException {
        Document doc;
        HighlightInfo hli;
        doc = component.getDocument();
        hli = (HighlightInfo) object;
        hli.p0 = doc.createPosition(p0);
        hli.p1 = doc.createPosition(p1);
        repaint();
    }

    /**
   * Makes a copy of the highlights.  Does not actually clone each highlight,
   * but only makes references to them.
   *
   * @return the copy
   * @see Highlighter#getHighlights
   */
    public Highlighter.Highlight[] getHighlights() {
        int size;
        Highlighter.Highlight[] result;
        int index;
        size = 0;
        for (Integer layer : layers) {
            size += getLayer(layer).size();
        }
        result = new Highlighter.Highlight[size];
        index = 0;
        for (Integer layer : layers) {
            for (Highlighter.Highlight hli : getLayer(layer)) {
                result[index] = hli;
                index++;
            }
        }
        return result;
    }

    private List<Highlighter.Highlight> getLayer(Integer layer) {
        List<Highlighter.Highlight> result;
        result = highlights.get(layer);
        if (result == null) {
            result = new ArrayList<Highlighter.Highlight>();
            highlights.put(layer, result);
        }
        return result;
    }

    public void repaint() {
        if (doNotRepaint) {
            return;
        }
        component.repaint();
    }

    class HighlightInfo implements Highlighter.Highlight {

        Position p0;

        Position p1;

        Highlighter.HighlightPainter painter;

        public int getStartOffset() {
            return p0.getOffset();
        }

        public int getEndOffset() {
            return p1.getOffset();
        }

        public Highlighter.HighlightPainter getPainter() {
            return painter;
        }
    }
}
