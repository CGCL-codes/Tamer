package org.modss.facilitator.ui.result;

import org.swzoo.log2.core.*;
import org.modss.facilitator.model.v1.*;
import org.modss.facilitator.shared.resource.*;
import org.modss.facilitator.shared.singleton.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.List;

/**
 * A component to display a ResultNode.ResultEntry.
 * Clicking on an alternative will toggle its selection.
 *
 * @see ResultModel
 * @author John Farrell
 */
public final class BarGraph extends JPanel implements Scrollable, SortOrderListener {

    private static final int MIN_EXPANSION = 13;

    private static final int PREFERRED_EXPANSION = 25;

    /** The ResultNode that this graph displays */
    private ResultNode model;

    /** Whether we are on top of a glued graph */
    private boolean overGlue = false;

    /** Whether we are a glued graph. */
    private boolean isGlue = false;

    /** Vertical offset of the graph, particularly used if we are glued. */
    private int voffset = 0;

    /** Vertical offset of the graph, particularly used if we are glued. */
    private int hoffset = 0;

    /** Important information about the layout of this component. */
    private BarGraphMetrics metrics;

    /** Whether the alternatives are selected or not. */
    private AlternativeSelectionModel selectionModel;

    /** The width that we know we are going to be rendered in hence must try to fit. */
    private int fitWidth;

    /** Used to listen to selections of alternatives */
    private AlternativeSelectionListener mySelectionListener = new AlternativeSelectionListener() {

        public void alternativeSelectionChanged(AlternativeSelectionEvent event) {
            repaint();
        }
    };

    /** The current sort order */
    private SortOrder sortOrder;

    /** Sorted entries */
    private ResultNode.ResultEntry[] sortedModel;

    /** Index of last entry clicked on. */
    private int lastClickIndex = -1;

    /** Create a new BarGraph, recording selection in the given AlternativeSelectionModel. */
    public BarGraph(SortOrder sortOrder) {
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {
                mouseClick(evt);
            }
        });
        setOpaque(false);
        setModel(null, null);
        this.sortOrder = sortOrder;
    }

    /**
     * Our preferred size is the width we are given (unless it is smaller than
     * the minimum allowed), and as much height as is required.
     */
    public Dimension getPreferredSize() {
        Graphics g = getGraphics();
        if (model == null || g == null) return new Dimension(0, 0);
        BarGraphMetrics preferredMetrics = calculateMetrics(null);
        Rectangle complete = preferredMetrics.getComplete();
        Dimension d = new Dimension(complete.width, complete.height);
        d.width = fitWidth;
        if (d.width < preferredMetrics.getMinWidth()) d.width = preferredMetrics.getMinWidth();
        return d;
    }

    /**
     * Fit into the given width unless that is smaller than our minimum size.
     */
    private void fitToWidth(int width) {
        this.fitWidth = width;
    }

    /**
     * @param altMetrics the font that will be used to render the alternative names.
     * @return width of longest piece of text describing alternative.
     */
    private int calculateLongestAltPixels(FontMetrics altMetrics) {
        int largestAlt = 0;
        if (model == null) return largestAlt;
        ResultNode.ResultEntry[] results = model.getResults();
        for (int i = 0; i < results.length; i++) {
            Alternative alt = results[i].getAlternative();
            int width = altMetrics.stringWidth(alt.getShortDescription());
            largestAlt = (largestAlt < width) ? width : largestAlt;
        }
        return largestAlt;
    }

    /**
     * Change the vertical offset. This is used to make a glued graph move vertically
     * before it is visible, to stay under a vertically scrolling graph, so that
     * when it is revealed it is glued in the right place.
     */
    void setVOffset(int voffset) {
        this.voffset = voffset;
        setLocation(-hoffset, -voffset);
        repaint();
    }

    /**
     * Change the horizontal offset. This is used to make a glued graph move horizontally to
     * stay under a horizontally scrolling graph.
     */
    void setHOffset(int hoffset) {
        this.hoffset = hoffset;
        setLocation(-hoffset, -voffset);
        repaint();
    }

    /**
     * Set whether this graph is drawn over a glued graph or not.
     * A graph over the top of a glued graph is more transparent.
     */
    void setOverGlue(boolean overGlue) {
        this.overGlue = overGlue;
        repaint();
    }

    /** Set whether this graph is glued down or not. **/
    void setGlue(boolean isGlue) {
        this.isGlue = isGlue;
        repaint();
    }

    /**
     * Handle a click on an alternative to select/deselect it.
     */
    private void mouseClick(MouseEvent evt) {
        Point p = new Point(evt.getX(), evt.getY());
        int index = getResultIndexAt(p);
        if (index < 0) return;
        ResultNode.ResultEntry entry = sortedModel[index];
        if ((evt.getModifiers() & MouseEvent.SHIFT_MASK) > 0 && lastClickIndex >= 0 && lastClickIndex != index) {
            int lower = Math.min(index, lastClickIndex);
            int higher = Math.max(index, lastClickIndex);
            boolean selected = selectionModel.isSelected(sortedModel[lastClickIndex].getAlternative());
            for (int i = lower; i <= higher; i++) {
                selectionModel.setSelected(sortedModel[i].getAlternative(), selected);
            }
        } else {
            lastClickIndex = index;
            selectionModel.toggleSelected(entry.getAlternative());
        }
    }

    /**
     * Change the model for this graph.
     * @param model the new model for this graph.
     */
    public void setModel(ResultNode model, AlternativeSelectionModel selectionModel) {
        if (this.selectionModel != null) {
            selectionModel.removeSelectionListener(mySelectionListener);
        }
        this.model = model;
        this.selectionModel = selectionModel;
        if (selectionModel != null) {
            selectionModel.addSelectionListener(mySelectionListener);
        }
        sort();
    }

    /**
     * Return displayed results in sorted order. Used to support reporting.
     */
    ResultNode.ResultEntry[] getSortedResults() {
        return sortedModel;
    }

    /** Image of graph, for printing. The size of this image is the preferred size. **/
    Image getImage() {
        BarGraphMetrics metrics = calculateMetrics(null);
        Rectangle complete = metrics.getComplete();
        Image img = createImage(complete.width, complete.height);
        Graphics g = img.getGraphics();
        g.setClip(0, 0, complete.width, complete.height);
        Rectangle r = g.getClipBounds();
        g.setColor(getColour("background", Color.lightGray));
        g.fillRect(r.x, r.y, r.width, r.height);
        paintComponent(g, metrics);
        return img;
    }

    /**
     * Recalculate the BarGraphMetrics according to the given actual size.
     * This is THE method that figures out where things go.
     */
    BarGraphMetrics calculateMetrics(Dimension actual) {
        metrics = null;
        if (model == null) return null;
        Font alt = ResourceUtils.getFont(resources, "dss.gui.result.view.bar.alternative");
        Font index = ResourceUtils.getFont(resources, "dss.gui.result.view.bar.index");
        FontMetrics altMetrics = getGraphics().getFontMetrics(alt);
        FontMetrics indexMetrics = getGraphics().getFontMetrics(index);
        int scale = resources.getIntProperty("dss.gui.result.view.bar.graph.scale", 16);
        int divisions = resources.getIntProperty("dss.gui.result.view.bar.graph.divisions", 4);
        int largestAlt = calculateLongestAltPixels(altMetrics);
        int afterAlt = scale * 3 / 2;
        int preferredResultWidth = scale * PREFERRED_EXPANSION;
        int minResultWidth = scale * MIN_EXPANSION;
        int borderWidth = scale;
        int altSeparator = scale;
        int altHeight = (int) Math.max(altMetrics.getHeight(), scale * 4 / 3);
        int altSize = altHeight + altSeparator;
        int modelSize = model.getResults().length;
        int resultPad = scale / 2;
        Rectangle resultBox = new Rectangle();
        resultBox.x = borderWidth + largestAlt + afterAlt + boxBorder + resultPad;
        resultBox.y = borderWidth + altSize + boxBorder + resultPad;
        resultBox.width = preferredResultWidth;
        resultBox.height = modelSize * altSize;
        Rectangle box = new Rectangle();
        box.x = resultBox.x - resultPad;
        box.y = resultBox.y - resultPad;
        box.width = resultBox.width + resultPad + resultPad;
        box.height = resultBox.height + resultPad + resultPad;
        Rectangle border = new Rectangle();
        border.x = box.x - boxBorder - afterAlt - largestAlt;
        border.y = box.y - boxBorder - altSize;
        border.width = box.width + boxBorder + boxBorder + afterAlt + largestAlt;
        border.height = box.height + boxBorder + boxBorder + altSize;
        Rectangle complete = new Rectangle();
        complete.x = border.x - borderWidth;
        complete.y = border.y - borderWidth;
        complete.width = border.width + borderWidth + borderWidth;
        complete.height = border.height + borderWidth + borderWidth;
        int smallest = complete.width - preferredResultWidth + minResultWidth;
        if (actual != null) {
            int cw = actual.width;
            if (cw < smallest) cw = smallest;
            if (cw != complete.width) {
                int shrinkage = complete.width - cw;
                resultBox.width -= shrinkage;
                box.width -= shrinkage;
                border.width -= shrinkage;
                complete.width -= shrinkage;
            }
        }
        return new BarGraphMetrics(resultBox, box, border, complete, altSize, divisions, smallest);
    }

    static class BarGraphMetrics {

        /** The drawn box that the results go in. */
        private Rectangle box;

        /** The vertical distance between alternative baselines */
        private int altSize;

        /** The actual box that the results go in */
        private Rectangle resultBox;

        /** Number of index lines */
        private int divisions;

        /** All space inside the blank border */
        private Rectangle border;

        /** The complete area of the BarGraph */
        private Rectangle complete;

        /** The minimum width of the graph */
        private int minWidth;

        private BarGraphMetrics(Rectangle resultBox, Rectangle box, Rectangle border, Rectangle complete, int altSize, int divisions, int minWidth) {
            this.resultBox = resultBox;
            this.box = box;
            this.border = border;
            this.complete = complete;
            this.altSize = altSize;
            this.divisions = divisions;
            this.minWidth = minWidth;
        }

        private Rectangle getBox() {
            return box;
        }

        private int getAlternativeSize() {
            return altSize;
        }

        private Rectangle getResultBox() {
            return resultBox;
        }

        Rectangle getComplete() {
            return complete;
        }

        private int getDivisions() {
            return divisions;
        }

        private Rectangle getBorder() {
            return border;
        }

        private int getMinWidth() {
            return minWidth;
        }

        public String toString() {
            return "BarGraphMetrics[" + complete + "]";
        }
    }

    private int getResultIndexAt(Point p) {
        if (metrics == null || model == null) return -1;
        if ((p.y < metrics.getBox().y) || (p.y > metrics.getBox().y + metrics.getBox().height)) {
            return -1;
        }
        int i = (p.y - metrics.getResultBox().y) / metrics.getAlternativeSize();
        if (i < 0) {
            i = 0;
        } else if (i == model.getResults().length) {
            i = i - 1;
        }
        return i;
    }

    /**
     * @param divisions the number of divisions to use
     * @return the formatted strings which are labels for the index lines.
     */
    private static String[] calculateIndexText(int divisions) {
        NumberFormat fmt = NumberFormat.getInstance();
        fmt.setMinimumFractionDigits(1);
        fmt.setMaximumFractionDigits(2);
        String[] indexText = new String[divisions + 1];
        for (int i = 0; i < divisions + 1; i++) {
            double val = (double) i / (double) divisions;
            indexText[i] = fmt.format(val);
        }
        return indexText;
    }

    /** Draw the bar graph onto this graphics. **/
    public void paintComponent(Graphics g) {
        BarGraphMetrics metrics = this.metrics = calculateMetrics(getSize());
        if (metrics == null) return;
        paintComponent(g, metrics);
    }

    /**
     * Draw the bar graph using the specified metrics.
     */
    void paintComponent(Graphics g, BarGraphMetrics metrics) {
        Rectangle box = metrics.getBox();
        Rectangle resultBox = metrics.getResultBox();
        Rectangle border = metrics.getBorder();
        if (!isGlue) {
            Color frameColour = getColour("frame", Color.black);
            g.setColor(frameColour);
            for (int i = 0; i < boxBorder; i++) {
                g.drawRect(box.x - 1 - i, box.y - 1 - i, i + i + box.width + 1, i + i + box.height + 1);
            }
        }
        if (!overGlue && !isGlue) {
            Color internalColour = getColour("internal", Color.yellow);
            g.setColor(internalColour);
            g.fillRect(box.x, box.y, box.width, box.height);
        }
        int divisions = metrics.getDivisions();
        if (!isGlue && (divisions > 0)) {
            Color linesColour = getColour("lines", Color.darkGray);
            g.setColor(linesColour);
            for (int i = 0; i <= divisions; i++) {
                int x = i * resultBox.width / divisions + resultBox.x;
                g.drawLine(x, box.y, x, box.y + box.height);
            }
            String[] indexText = calculateIndexText(divisions);
            Font indexFont = ResourceUtils.getFont(resources, "dss.gui.result.view.bar.index");
            FontMetrics indexMetrics = g.getFontMetrics(indexFont);
            Color indexColour = getColour("text", Color.black);
            g.setColor(indexColour);
            g.setFont(indexFont);
            int y = border.y + indexMetrics.getHeight() + ((indexMetrics.getAscent() - indexMetrics.getDescent()) / 2);
            for (int i = 0; i < (divisions + 1); i++) {
                int width = indexMetrics.stringWidth(indexText[i]);
                int x = resultBox.x - width / 2 + i * resultBox.width / divisions;
                if (x <= box.x) {
                    x = box.x + 1;
                } else if (x + width >= box.x + box.width) {
                    x = box.x + box.width - width;
                }
                g.drawString(indexText[i], x, y);
            }
        }
        Font altFont = ResourceUtils.getFont(resources, "dss.gui.result.view.bar.alternative");
        FontMetrics altMetrics = g.getFontMetrics(altFont);
        g.setFont(altFont);
        Rectangle clipRect = g.getClipBounds();
        int imin = 0, imax = 0;
        if (clipRect.y >= resultBox.y) {
            imin = (clipRect.y - resultBox.y) / metrics.getAlternativeSize();
        }
        if (clipRect.y + clipRect.height >= resultBox.y) {
            imax = (clipRect.y + clipRect.height - resultBox.y) / metrics.getAlternativeSize();
        }
        if (isGlue) {
            Font f = ResourceUtils.getFont(resources, "dss.gui.result.view.bar.alternative");
            Font small = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
            g.setFont(small);
        }
        int modelSize = model.getResults().length;
        Rectangle[] fullRects = new Rectangle[modelSize];
        Rectangle[] altRects = new Rectangle[modelSize];
        for (int i = imin; i <= imax && i < modelSize; i++) {
            Rectangle altRect = new Rectangle(resultBox.x, resultBox.y + i * metrics.getAlternativeSize(), resultBox.width, metrics.getAlternativeSize());
            Rectangle fullAltRect = (Rectangle) altRect.clone();
            fullAltRect.x = border.x;
            fullAltRect.width = border.width;
            if (isGlue) {
                altRect.x = altRect.x + border.x;
            }
            fullRects[i] = fullAltRect;
            altRects[i] = altRect;
        }
        for (int i = imin; i <= imax && i < modelSize; i++) {
            boolean dark = i % 2 == 0;
            if (fullRects[i].intersects(clipRect)) {
                paintAlternativeBackground(g, sortedModel[i], metrics, altRects[i], dark);
            }
        }
        for (int i = imin; i <= imax && i < modelSize; i++) {
            boolean dark = i % 2 == 0;
            if (fullRects[i].intersects(clipRect)) {
                paintAlternativeForeground(g, sortedModel[i], metrics, altRects[i]);
            }
        }
    }

    /** Keys of colours which change when we are glued. */
    private static final String[] glueChangeColourKeys = { "internal", "darker", "fill", "value" };

    /** Same as glueChangeColourKeys but a List so we can call contains. */
    private static final List glueChangeColourKeysList = Arrays.asList(glueChangeColourKeys);

    private Color getColour(String key, Color defaultColour) {
        boolean useGlue = isGlue && glueChangeColourKeysList.contains(key);
        String resourceKey = "dss.gui.result.view.bar.graph." + (useGlue ? "glued." : "") + key + ".colour";
        LogTools.trace(logger, 25, "getColour() - resourceKey=" + resourceKey);
        Color c = resources.getColorProperty(resourceKey, null);
        if (c == null) {
            if (isGlue) {
                c = greyer(defaultColour);
            } else {
                c = defaultColour;
            }
        }
        return c;
    }

    /** @return a colour similar to c but more like grey. */
    private static Color greyer(Color c) {
        return new Color((128 + c.getRed()) / 2, (128 + c.getGreen()) / 2, (128 + c.getBlue()) / 2);
    }

    /**
     * Paint the background of a single alternative.
     * Called only from paintComponent(Graphics)
     */
    private void paintAlternativeBackground(Graphics g, ResultNode.ResultEntry entry, BarGraphMetrics metrics, Rectangle altRect, boolean dark) {
        Font altFont = ResourceUtils.getFont(resources, "dss.gui.result.view.bar.alternative");
        FontMetrics altMetrics = g.getFontMetrics(altFont);
        String name = entry.getAlternative().getShortDescription();
        boolean shading = resources.getBooleanProperty("dss.gui.result.view.bar.graph.shading", true);
        Rectangle box = metrics.getBox();
        Rectangle resultBox = metrics.getResultBox();
        Rectangle border = metrics.getBorder();
        Color background;
        if (!shading || !dark) {
            background = getColour("internal", Color.yellow);
        } else {
            background = getColour("darker", Color.yellow.darker());
        }
        g.setColor(background);
        if (!overGlue) {
            g.fillRect(metrics.getBox().x, altRect.y, box.width, altRect.height);
        }
        if (selectionModel.isSelected(entry.getAlternative())) {
            if (!overGlue) {
                g.fillRect(0, altRect.y, box.x - boxBorder, altRect.height);
            } else {
                int height = altFont.getSize() + 2;
                int nameLength = altMetrics.stringWidth(name);
                g.fillRect(border.x - 2, altRect.y + altRect.height / 2 - altMetrics.getHeight() / 2 + 1, nameLength + 4, height);
            }
        }
        g.setColor(getColour("lines", Color.darkGray));
        for (int j = 0; j <= metrics.getDivisions(); j++) {
            int ix = j * resultBox.width / metrics.getDivisions() + metrics.getResultBox().x;
            g.drawLine(ix, altRect.y, ix, altRect.y + altRect.height);
        }
    }

    /**
     * Paint the foreground of a single alternative.
     * Called only from paintComponent(Graphics)
     */
    private void paintAlternativeForeground(Graphics g, ResultNode.ResultEntry entry, BarGraphMetrics metrics, Rectangle altRect) {
        Font altFont = ResourceUtils.getFont(resources, "dss.gui.result.view.bar.alternative");
        FontMetrics altMetrics = g.getFontMetrics(altFont);
        String name = entry.getAlternative().getShortDescription();
        boolean shading = resources.getBooleanProperty("dss.gui.result.view.bar.graph.shading", true);
        Rectangle box = metrics.getBox();
        Rectangle resultBox = metrics.getResultBox();
        Rectangle border = metrics.getBorder();
        g.setColor(getColour("text", Color.black));
        int x = border.x;
        int y = altRect.y + altRect.height / 2 + altMetrics.getHeight() / 2;
        if (isGlue) {
            x += border.x;
            y += altMetrics.getHeight();
        }
        g.drawString(name, x, y);
        int pmin = (int) (entry.getResult().getMin() * (double) resultBox.width);
        int xlen = (int) (entry.getResult().getMax() * (double) resultBox.width) - pmin;
        if (xlen > minMiddleLineSize) {
            xlen = (xlen % 2 == 0) ? (xlen - 1) : xlen;
        }
        if (xlen == 0) {
            pmin = pmin - singleWidth / 2;
            xlen = singleWidth;
            g.setColor(getColour("value", Color.red));
        } else {
            g.setColor(getColour("fill", Color.green));
        }
        Rectangle coloured = new Rectangle(resultBox.x + pmin, altRect.y + (altRect.height - altMetrics.getHeight()) / 2, xlen, altMetrics.getHeight());
        if (isGlue) {
            coloured.y += altMetrics.getHeight() + 2;
            coloured.height -= 4;
        }
        g.fillRect(coloured.x, coloured.y, coloured.width, coloured.height);
        g.setColor(getColour("border", Color.black));
        g.drawRect(coloured.x, coloured.y, coloured.width, coloured.height);
        if (xlen > minMiddleLineSize) {
            g.drawLine(coloured.x + xlen / 2, coloured.y - middleLineExtension, coloured.x + xlen / 2, coloured.y + coloured.height + middleLineExtension);
        }
    }

    public Dimension getPreferredScrollableViewportSize() {
        Dimension d = getPreferredSize();
        return d;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return 1;
        } else {
            if (metrics == null) {
                return 1;
            } else {
                return metrics.getAlternativeSize();
            }
        }
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width;
        } else {
            return visibleRect.height;
        }
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    /**
     * A ScrollPane specially for putting a BarGraph in.
     */
    public class BarGraphScrollPane extends JScrollPane {

        public BarGraphScrollPane() {
            setLayout(new BarGraphScrollPaneLayout());
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            setViewport(createViewport());
            setVerticalScrollBar(createVerticalScrollBar());
            setHorizontalScrollBar(createHorizontalScrollBar());
            setViewportView(BarGraph.this);
            setOpaque(true);
            updateUI();
        }
    }

    /**
     * A layout for the BarGraphScrollPane. To ensure that the BarGraph reports
     * a preferred size which bears some relation to what is actually available
     * (because Swing doesn't understand particularly well that some components
     * may vary their preferred size according to how much is available), we
     * find out how much room we are going to need to fit in, and then when we
     * are asked how much we would like, we give an appropriate answer.
     */
    public class BarGraphScrollPaneLayout extends ScrollPaneLayout.UIResource {

        public void layoutContainer(Container parent) {
            Insets i = parent.getInsets();
            int width = parent.getSize().width - getVerticalScrollBar().getSize().width - i.left - i.right;
            BarGraph.this.fitToWidth(width);
            super.layoutContainer(parent);
        }
    }

    /** Sort according to the current sort order. */
    private void sort() {
        lastClickIndex = -1;
        if (model == null) {
            sortedModel = null;
        } else {
            sortedModel = Sorting.sort(model.getResults(), sortOrder);
            repaint();
        }
    }

    public void orderChanged(SortOrderEvent ev) {
        this.sortOrder = ev.getNewSortOrder();
        sort();
    }

    /** Logger. */
    private static final Logger logger = LogFactory.getLogger();

    /** Resource provider. */
    private static final ResourceProvider resources = Singleton.Factory.getInstance().getResourceProvider();

    /** width of the border of the box **/
    private static final int boxBorder = resources.getIntProperty("dss.gui.result.view.bar.graph.boxBorder", 3);

    /** minimum size of the box to bother drawing the centre line **/
    private static final int minMiddleLineSize = resources.getIntProperty("dss.gui.result.view.bar.graph.minMiddleLineWidth", 4);

    /** the width of an alternative when it is a single value **/
    private static final int singleWidth = resources.getIntProperty("dss.gui.result.view.bar.graph.singleWidth", 7);

    /** the height a line across an alternative box extends above and below **/
    private static final int middleLineExtension = resources.getIntProperty("dss.gui.result.view.bar.graph.middleLineExt", 3);
}
