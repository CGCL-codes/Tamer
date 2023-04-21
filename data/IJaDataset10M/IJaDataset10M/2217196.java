package com.ivis.xprocess.ui.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import com.ivis.xprocess.ui.properties.ColorProperties;
import com.ivis.xprocess.ui.util.FontAndColorManager;
import com.ivis.xprocess.ui.util.UIToolkit;
import com.ivis.xprocess.util.NumberUtils;

public class EstimateDiagram implements PaintListener, MouseMoveListener, MouseListener, KeyListener, MouseTrackListener {

    public static final double ESTIMATE_EFFORT_EQUALITY_TOLERANCE = 1.0 / 12.0;

    public static final double DEFAULT_RATIO_OF_BEST_TO_MOST = 0.5;

    public static final double DEFAULT_RATIO_OF_WORST_TO_MOST = 2;

    private static final int MARGIN_WIDTH = 0;

    private static final int MARGIN_HEIGHT = 0;

    private static final double defaultTimeValueWidth = 5;

    private static final int TOOLTIP_Y_OFFSET = 16;

    private static final int BEST_INDEX = 0;

    private static final int MOST_INDEX = 1;

    private static final int WORST_INDEX = 2;

    public final int DEFAULT_WIDTH = 100;

    public final int DEFAULT_HEIGHT = 20;

    private Canvas canvas;

    private Rectangle borderRectangle = new Rectangle(0, 0, 2, 2);

    private IDragableNode bestDragableNode;

    private IDragableNode mostDragableNode;

    private IDragableNode worstDragableNode;

    private IDragableNode estimateDragableNode;

    private IDragableNode[] dragableNodes;

    private IDrawable[] drawables;

    private IDragableNode selectedDragableNode;

    private IDragableNode draggingDragableNode;

    private double[] timeValues;

    private double draggableTimePerPixel;

    private int draggableInitialX;

    private double hoursToDate;

    private ToolTip toolTip;

    private IDrawable progressDrawable;

    private Collection<IEstimateChangeListener> listeners = new ArrayList<IEstimateChangeListener>();

    public EstimateDiagram(Composite parent) {
        canvas = new Canvas(parent, SWT.NONE) {

            @Override
            public Point computeSize(int wHint, int hHint, boolean changed) {
                int width;
                int height;
                if (wHint == SWT.DEFAULT) {
                    width = DEFAULT_WIDTH;
                } else {
                    width = wHint;
                }
                if (hHint == SWT.DEFAULT) {
                    height = DEFAULT_HEIGHT;
                } else {
                    height = hHint;
                }
                return new Point(width, height);
            }
        };
        progressDrawable = new HoursToDateDrawable(this);
        bestDragableNode = new DragablePoint(this, "Best Case");
        mostDragableNode = new DragablePoint(this, "Most Likely");
        worstDragableNode = new DragablePoint(this, "Worst Case");
        estimateDragableNode = new DragableEstimate(this, "Estimate");
        dragableNodes = new IDragableNode[] { estimateDragableNode, bestDragableNode, mostDragableNode, worstDragableNode };
        drawables = new IDrawable[] { progressDrawable, bestDragableNode, mostDragableNode, worstDragableNode, estimateDragableNode };
        canvas.addPaintListener(this);
        canvas.addMouseMoveListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseTrackListener(this);
        canvas.addKeyListener(this);
        toolTip = new ToolTip(canvas);
    }

    public Control getControl() {
        return canvas;
    }

    public void setBestCase(double valueInHours, boolean validate) {
        if (validate) {
            setDraggingTimeValues(bestDragableNode, valueInHours);
            canvas.redraw();
        } else {
            bestDragableNode.setDecimalHours(valueInHours);
            recalculateEstimate();
        }
    }

    public void setMostLikely(double valueInHours, boolean validate) {
        if (validate) {
            setDraggingTimeValues(mostDragableNode, valueInHours);
            canvas.redraw();
        } else {
            mostDragableNode.setDecimalHours(valueInHours);
            recalculateEstimate();
        }
    }

    public void setWorstCase(double valueInHours, boolean validate) {
        if (validate) {
            setDraggingTimeValues(worstDragableNode, valueInHours);
            canvas.redraw();
        } else {
            worstDragableNode.setDecimalHours(valueInHours);
            recalculateEstimate();
        }
    }

    public void setEstimate(double valueInHours, boolean validate) {
        if (validate) {
            setDraggingTimeValues(estimateDragableNode, valueInHours);
            canvas.redraw();
        } else {
            estimateDragableNode.setDecimalHours(valueInHours);
        }
    }

    protected void recalculateEstimate() {
        double estimate = (bestDragableNode.getDecimalHours() + (4.0 * mostDragableNode.getDecimalHours()) + worstDragableNode.getDecimalHours()) / 6.0;
        String estimateAsString = NumberUtils.format(estimate, 1);
        estimate = NumberUtils.parseDouble(estimateAsString);
        estimateDragableNode.setDecimalHours(estimate);
    }

    public void setHoursToDate(double valueInHours) {
        this.hoursToDate = valueInHours;
        ((HoursToDateDrawable) progressDrawable).setHoursToDate(valueInHours);
        if (!canvas.isDisposed()) {
            canvas.redraw();
        }
    }

    public void paintControl(PaintEvent e) {
        Point estimateDiagramSize = canvas.getSize();
        borderRectangle = new Rectangle(MARGIN_WIDTH, MARGIN_HEIGHT, estimateDiagramSize.x - (2 * MARGIN_WIDTH) - 1, estimateDiagramSize.y - (2 * MARGIN_HEIGHT) - 1);
        if (borderRectangle.width < 2) {
            borderRectangle.width = 2;
        }
        if (borderRectangle.height < 2) {
            borderRectangle.height = 2;
        }
        if (canvas.isEnabled()) {
            e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLACK));
        } else {
            e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_DARK_GRAY));
        }
        e.gc.drawRectangle(borderRectangle);
        for (int i = 0; i < drawables.length; i++) drawables[i].paint(e.display, e.gc);
    }

    public Rectangle getDragableBounds(IDragableNode node) {
        double horizontalRatio = node.getDecimalHours() / getTotalTimeValueWidth();
        int x = (int) Math.round(horizontalRatio * getTotalUsableWidth());
        if (node == mostDragableNode) {
            x += bestDragableNode.getSize().x;
        } else if (node == worstDragableNode) {
            x += (bestDragableNode.getSize().x + mostDragableNode.getSize().x);
        } else if (node == estimateDragableNode) {
            x += bestDragableNode.getSize().x;
        }
        if (node.getSize().y == IDragableNode.FILL) {
            return new Rectangle(borderRectangle.x + 1 + x, borderRectangle.y + 1, node.getSize().x - 1, borderRectangle.height - 2);
        }
        int middleY = borderRectangle.y + (borderRectangle.height / 2);
        return new Rectangle(borderRectangle.x + 1 + x, middleY - (node.getSize().y / 2), node.getSize().x - 1, node.getSize().y);
    }

    public int getHoursToDatePos() {
        double horizontalRatio = hoursToDate / getTotalTimeValueWidth();
        int x = (int) Math.round(horizontalRatio * getTotalUsableWidth());
        x += bestDragableNode.getSize().x;
        if ((hoursToDate > mostDragableNode.getDecimalHours()) && (hoursToDate < worstDragableNode.getDecimalHours()) && (estimateDragableNode.getDecimalHours() > mostDragableNode.getDecimalHours())) {
            x += (estimateDragableNode.getSize().x / 2);
        }
        if ((hoursToDate < mostDragableNode.getDecimalHours()) && (hoursToDate > bestDragableNode.getDecimalHours()) && (estimateDragableNode.getDecimalHours() < mostDragableNode.getDecimalHours())) {
            x += (estimateDragableNode.getSize().x / 2);
        }
        return borderRectangle.x + 1 + x;
    }

    private int getTotalUsableWidth() {
        int totalWidth = borderRectangle.width - 1;
        totalWidth -= bestDragableNode.getSize().x;
        totalWidth -= mostDragableNode.getSize().x;
        totalWidth -= worstDragableNode.getSize().x;
        return totalWidth;
    }

    private double getTotalTimeValueWidth() {
        double worstTimeValue = worstDragableNode.getDecimalHours();
        double estimateTimeValue = estimateDragableNode.getDecimalHours();
        return max(worstTimeValue, max(estimateTimeValue, max(hoursToDate, defaultTimeValueWidth)));
    }

    protected double getTimePerPixel() {
        return getTotalTimeValueWidth() / getTotalUsableWidth();
    }

    public double getTimePerPixelRaw() {
        return getTotalTimeValueWidth() / (borderRectangle.width);
    }

    protected Color getColourFor(IDragableNode dragableNode) {
        if (dragableNode == selectedDragableNode) {
            String rgbColor = ColorProperties.getInstance().getProperty(ColorProperties.SELECTED_NODE);
            return FontAndColorManager.getInstance().getColor(UIToolkit.getRGBValue(rgbColor));
        }
        if (dragableNode == draggingDragableNode) {
            String rgbColor = ColorProperties.getInstance().getProperty(ColorProperties.DRAGGED_NODE);
            return FontAndColorManager.getInstance().getColor(UIToolkit.getRGBValue(rgbColor));
        }
        final String colorConstant;
        if (Math.abs(dragableNode.getDecimalHours() - getHoursToDate()) < ESTIMATE_EFFORT_EQUALITY_TOLERANCE) {
            colorConstant = ColorProperties.UNSELECTED_NODE_SAME_AS_EFFORT;
        } else if (dragableNode.getDecimalHours() > getHoursToDate()) {
            colorConstant = ColorProperties.UNSELECTED_NODE_ABOVE;
        } else {
            colorConstant = ColorProperties.UNSELECTED_NODE_BELOW_EFFORT;
        }
        String rgbColor = ColorProperties.getInstance().getProperty(colorConstant);
        return FontAndColorManager.getInstance().getColor(UIToolkit.getRGBValue(rgbColor));
    }

    private IDrawable getDrawable(int x, int y) {
        for (int i = drawables.length - 1; i >= 0; i--) {
            IDrawable drawable = drawables[i];
            Rectangle bounds = drawable.getBounds();
            if (bounds.contains(x, y)) {
                return drawable;
            }
        }
        return null;
    }

    public void mouseDoubleClick(MouseEvent e) {
    }

    public void mouseDown(MouseEvent e) {
        if (!canvas.isFocusControl()) {
            canvas.setFocus();
        }
        if ((selectedDragableNode != null) && (draggingDragableNode == null)) {
            draggingDragableNode = selectedDragableNode;
            selectedDragableNode = null;
            storeTimeValues();
            draggableTimePerPixel = getTimePerPixel();
            draggableInitialX = e.x;
            canvas.redraw();
        }
    }

    public void mouseMove(MouseEvent e) {
        if (draggingDragableNode == null) {
            IDrawable drawable = getDrawable(e.x, e.y);
            if (drawable != null) {
                toolTip.showToolTip(drawable.getLabel(e.x, e.y), e.x, e.y + TOOLTIP_Y_OFFSET);
            } else {
                toolTip.hideToolTip();
            }
            IDragableNode newHoveringOnDragableNode = null;
            if (drawable instanceof IDragableNode) {
                newHoveringOnDragableNode = (IDragableNode) drawable;
            }
            setSelectedDragableNode(newHoveringOnDragableNode);
        } else {
            restoreTimeValues();
            int deltaX = e.x - draggableInitialX;
            double deltaTimeValue = deltaX * draggableTimePerPixel;
            double newTimeValue = draggingDragableNode.getDecimalHours() + deltaTimeValue;
            setDraggingTimeValues(draggingDragableNode, newTimeValue);
            canvas.redraw();
            newTimeValue = draggingDragableNode.getDecimalHours();
            toolTip.showToolTip(draggingDragableNode.getLabel(e.x, e.y), e.x, e.y + TOOLTIP_Y_OFFSET);
        }
    }

    public void mouseUp(MouseEvent e) {
        if (draggingDragableNode != null) {
            draggingDragableNode = null;
            IDrawable drawable = getDrawable(e.x, e.y);
            if (drawable instanceof IDragableNode) {
                setSelectedDragableNode((IDragableNode) drawable);
            } else {
                setSelectedDragableNode(null);
            }
            canvas.redraw();
            toolTip.hideToolTip();
        }
    }

    private void setSelectedDragableNode(IDragableNode newSelectedDragableNode) {
        if (newSelectedDragableNode != null) {
            canvas.getShell().setCursor(canvas.getDisplay().getSystemCursor(SWT.CURSOR_SIZEWE));
        } else {
            canvas.getShell().setCursor(canvas.getDisplay().getSystemCursor(SWT.CURSOR_ARROW));
        }
        if (newSelectedDragableNode != selectedDragableNode) {
            selectedDragableNode = newSelectedDragableNode;
            canvas.redraw();
        }
    }

    private void setDraggingTimeValues(IDragableNode relativeDragableNode, double newTimeValue) {
        if (newTimeValue < 0.0) {
            newTimeValue = 0.0;
        }
        double[] newTimeValues;
        if ((bestDragableNode.getDecimalHours() == 0.0) && (mostDragableNode.getDecimalHours() == 0.0) && (worstDragableNode.getDecimalHours() == 0.0)) {
            newTimeValues = calculateInitialTimeValues(relativeDragableNode, newTimeValue);
        } else {
            if (relativeDragableNode == estimateDragableNode) {
                newTimeValues = calculateEstimateChangeTimeValues(relativeDragableNode, newTimeValue);
            } else {
                newTimeValues = calculateBestOrMostOrWorstChangeTimeValues(relativeDragableNode, newTimeValue);
            }
        }
        bestDragableNode.setDecimalHours(newTimeValues[BEST_INDEX]);
        mostDragableNode.setDecimalHours(newTimeValues[MOST_INDEX]);
        worstDragableNode.setDecimalHours(newTimeValues[WORST_INDEX]);
        recalculateEstimate();
        notifyEstimateChangeListeners(relativeDragableNode);
    }

    private double[] calculateInitialTimeValues(IDragableNode relativeDragableNode, double newTimeValue) {
        double[] newValues = new double[3];
        if (relativeDragableNode == estimateDragableNode) {
            newValues[MOST_INDEX] = (newTimeValue * 12.0) / 13.0;
        } else if (relativeDragableNode == bestDragableNode) {
            newValues[MOST_INDEX] = newTimeValue / getRatioOfBestToMost();
        } else if (relativeDragableNode == mostDragableNode) {
            newValues[MOST_INDEX] = newTimeValue;
        } else if (relativeDragableNode == worstDragableNode) {
            newValues[MOST_INDEX] = newTimeValue / getRatioOfWorstToMost();
        }
        newValues[BEST_INDEX] = newValues[MOST_INDEX] * getRatioOfBestToMost();
        newValues[WORST_INDEX] = newValues[MOST_INDEX] * getRatioOfWorstToMost();
        return newValues;
    }

    private double[] calculateEstimateChangeTimeValues(IDragableNode relativeDragableNode, double newTimeValue) {
        double[] newValues = new double[3];
        double oldEstimate = relativeDragableNode.getDecimalHours();
        double oldBest = bestDragableNode.getDecimalHours();
        double oldMost = mostDragableNode.getDecimalHours();
        newValues[BEST_INDEX] = (newTimeValue * oldBest) / oldEstimate;
        newValues[MOST_INDEX] = (newTimeValue * oldMost) / oldEstimate;
        newValues[WORST_INDEX] = (6.0 * newTimeValue) - newValues[BEST_INDEX] - (4.0 * newValues[MOST_INDEX]);
        return newValues;
    }

    private double[] calculateBestOrMostOrWorstChangeTimeValues(IDragableNode relativeDragableNode, double newTimeValue) {
        double[] newValues = new double[3];
        relativeDragableNode.setDecimalHours(newTimeValue);
        newValues[BEST_INDEX] = bestDragableNode.getDecimalHours();
        newValues[MOST_INDEX] = mostDragableNode.getDecimalHours();
        newValues[WORST_INDEX] = worstDragableNode.getDecimalHours();
        if (relativeDragableNode == bestDragableNode) {
            newValues[MOST_INDEX] = max(newValues[BEST_INDEX], newValues[MOST_INDEX]);
            newValues[WORST_INDEX] = max(newValues[BEST_INDEX], newValues[WORST_INDEX]);
        } else if (relativeDragableNode == mostDragableNode) {
            newValues[BEST_INDEX] = min(newValues[BEST_INDEX], newValues[MOST_INDEX]);
            newValues[WORST_INDEX] = max(newValues[MOST_INDEX], newValues[WORST_INDEX]);
        } else if (relativeDragableNode == worstDragableNode) {
            newValues[MOST_INDEX] = min(newValues[MOST_INDEX], newValues[WORST_INDEX]);
            newValues[BEST_INDEX] = min(newValues[BEST_INDEX], newValues[WORST_INDEX]);
        }
        return newValues;
    }

    private double min(double a, double b) {
        return (a < b) ? a : b;
    }

    private double max(double a, double b) {
        return (a > b) ? a : b;
    }

    private void storeTimeValues() {
        timeValues = new double[dragableNodes.length];
        for (int i = 0; i < dragableNodes.length; i++) timeValues[i] = dragableNodes[i].getDecimalHours();
    }

    private void restoreTimeValues() {
        for (int i = 0; i < dragableNodes.length; i++) dragableNodes[i].setDecimalHours(timeValues[i]);
    }

    protected Rectangle getBorderRectangle() {
        return borderRectangle;
    }

    protected String getFormattedLabel(String text, double timeValue) {
        String hoursText = NumberUtils.format(timeValue, 1);
        return text + ": " + hoursText + " hours";
    }

    public void addEstimateChangeListener(IEstimateChangeListener estimateChangeListener) {
        listeners.add(estimateChangeListener);
    }

    public void removeEstimateChangeListener(IEstimateChangeListener estimateChangeListener) {
        listeners.remove(estimateChangeListener);
    }

    public void notifyEstimateChangeListeners(IDragableNode relativeDragableNode) {
        Iterator<?> listenersIterator = listeners.iterator();
        while (listenersIterator.hasNext()) {
            IEstimateChangeListener estimateChangeListener = (IEstimateChangeListener) listenersIterator.next();
            if (relativeDragableNode == estimateDragableNode) {
                estimateChangeListener.bestCaseChanged(bestDragableNode.getDecimalHours());
                estimateChangeListener.mostLikelyChanged(mostDragableNode.getDecimalHours());
                estimateChangeListener.worstCaseChanged(worstDragableNode.getDecimalHours());
                estimateChangeListener.estimateChanged(estimateDragableNode.getDecimalHours());
            } else if (relativeDragableNode == bestDragableNode) {
                estimateChangeListener.bestCaseChanged(bestDragableNode.getDecimalHours());
                estimateChangeListener.estimateChanged(estimateDragableNode.getDecimalHours());
            } else if (relativeDragableNode == mostDragableNode) {
                estimateChangeListener.mostLikelyChanged(mostDragableNode.getDecimalHours());
                estimateChangeListener.estimateChanged(estimateDragableNode.getDecimalHours());
            } else if (relativeDragableNode == worstDragableNode) {
                estimateChangeListener.worstCaseChanged(worstDragableNode.getDecimalHours());
                estimateChangeListener.estimateChanged(estimateDragableNode.getDecimalHours());
            }
        }
    }

    public double getBestCase() {
        return bestDragableNode.getDecimalHours();
    }

    public double getMostLikely() {
        return mostDragableNode.getDecimalHours();
    }

    public double getWorstCase() {
        return worstDragableNode.getDecimalHours();
    }

    public double getEstimateOfEffort() {
        return estimateDragableNode.getDecimalHours();
    }

    public double getHoursToDate() {
        return hoursToDate;
    }

    public void setEnabled() {
        cancelDrag();
        canvas.redraw();
    }

    public void keyPressed(KeyEvent e) {
        if (e.keyCode == SWT.ESC) {
            cancelDrag();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    private void cancelDrag() {
        if (draggingDragableNode != null) {
            draggingDragableNode = null;
            selectedDragableNode = null;
            restoreTimeValues();
            notifyEstimateChangeListeners(estimateDragableNode);
            canvas.redraw();
            toolTip.hideToolTip();
        }
    }

    public void mouseEnter(MouseEvent e) {
    }

    public void mouseExit(MouseEvent e) {
        selectedDragableNode = null;
        toolTip.hideToolTip();
        canvas.getShell().setCursor(null);
        canvas.redraw();
    }

    public void mouseHover(MouseEvent e) {
    }

    private double getRatioOfBestToMost() {
        try {
            double ratioOfBestToMost = DEFAULT_RATIO_OF_BEST_TO_MOST;
            if ((ratioOfBestToMost > 0.0) && (ratioOfBestToMost <= 1.0)) {
                return ratioOfBestToMost;
            }
        } catch (NumberFormatException e) {
        }
        return DEFAULT_RATIO_OF_BEST_TO_MOST;
    }

    private double getRatioOfWorstToMost() {
        try {
            double ratioOfWorstToMost = DEFAULT_RATIO_OF_WORST_TO_MOST;
            if (ratioOfWorstToMost >= 1.0) {
                return ratioOfWorstToMost;
            }
        } catch (NumberFormatException e) {
        }
        return DEFAULT_RATIO_OF_WORST_TO_MOST;
    }

    public void refresh() {
        if (!canvas.isDisposed()) {
            canvas.redraw();
        }
    }
}
