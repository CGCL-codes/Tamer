package jpicedt.graphic.toolkit;

import jpicedt.graphic.PECanvas;
import jpicedt.graphic.PicPoint;
import jpicedt.graphic.SelectionHandler;
import jpicedt.graphic.event.PEMouseEvent;
import jpicedt.graphic.grid.Grid;
import jpicedt.graphic.model.AbstractCurve;
import jpicedt.graphic.model.Drawing;
import jpicedt.graphic.model.EditPointConstraint;
import jpicedt.graphic.model.Element;
import jpicedt.graphic.model.PicCircleFrom3Points;
import jpicedt.graphic.model.PicEllipse;
import jpicedt.graphic.model.PicMultiCurve;
import jpicedt.graphic.model.PicMultiCurveConvertable;
import jpicedt.graphic.model.PicParallelogram;
import jpicedt.graphic.model.PicPsCurve;
import jpicedt.graphic.model.PicSmoothPolygon;
import jpicedt.graphic.view.HitInfo;
import jpicedt.graphic.view.View;
import jpicedt.graphic.view.highlighter.CompositeHighlighter;
import jpicedt.graphic.view.highlighter.DefaultHighlighterFactory;
import jpicedt.ui.dialog.UserConfirmationCache;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Comparator;
import java.util.TreeSet;
import static jpicedt.graphic.view.highlighter.CompositeHighlighter.*;
import static jpicedt.graphic.view.ViewConstants.*;
import static jpicedt.graphic.PECanvas.SelectionBehavior.*;
import static jpicedt.Log.*;
import static jpicedt.Localizer.*;

/**
 * A factory that produces MouseTransform's that may be plugged into the SelectionTool mouse-tool.
 * MouseTransform's created by this factory are dedicated to editing points of Elements
 * which either support a variable number of points (e.g. <code>AbstractCurve</code> and subclasses),
 * and/or whose point possess specific geometric feature (e.g. smoothness/symmetry for PicMultiCurve,
 * smoothness coefficient for PicSmoothPolygon,...).
 * So far, only AbstractCurve's are supported by the current implementation.
 * @author Sylvain Reynal
 * @since jPicEdt 1.4
 * @version $Id: EditPointMouseTransformFactory.java,v 1.27 2011/12/04 07:54:49 vincentb1 Exp $
 */
public class EditPointMouseTransformFactory extends AbstractMouseTransformFactory {

    private CursorFactory cursorFactory = new CursorFactory();

    private SelectedPointsHandler selectedPointsHandler = new SelectedPointsHandler();

    private double scale = 0.0;

    private Rectangle2D.Double circle = new Rectangle2D.Double();

    private BasicStroke lineStroke = new BasicStroke(1.0f);

    private PicPoint ptBuffer = new PicPoint();

    private HighlightingMode oldSelHandlerHighlightingMode;

    /**
	 * @param kit the hosting editor-kit
	 */
    public EditPointMouseTransformFactory(EditorKit kit) {
        super(kit);
    }

    /**
	 * Returns a reference to the current <code>SelectedPointsHandler</code>.
	 */
    public SelectedPointsHandler getSelectedPointsHandler() {
        return selectedPointsHandler;
    }

    /**
	 * Return true is the given element is a valid target for this factory.
	 */
    protected boolean isValidTarget(Element e) {
        return (e instanceof AbstractCurve) || (e instanceof PicParallelogram);
    }

    /**
	 * Allows this <code>EditPointMouseTransformFactory</code> to do specific graphic rendering when it's
	 * installed in a hosting <code>SelectionTool</code>. This implementation renders selected points using a
	 * specific highlighter (which superimposes to the standard highlighter).
	 * @since jpicedt 1.4
	 */
    public void paint(Graphics2D g, Rectangle2D allocation, double scale) {
        if (this.scale != scale) {
            this.scale = scale;
            lineStroke = new BasicStroke((float) (2.0 / scale));
        }
        g.setPaint(Color.blue);
        g.setStroke(lineStroke);
        double barbellSize = 1.3 * BARBELL_SIZE / scale;
        Element elem = selectedPointsHandler.getElement();
        for (Iterator it = selectedPointsHandler.indexIterator(); it.hasNext(); ) {
            int selPtIdx = ((Integer) it.next()).intValue();
            elem.getCtrlPt(selPtIdx, ptBuffer);
            circle.setFrameFromCenter(ptBuffer.x, ptBuffer.y, ptBuffer.x + barbellSize, ptBuffer.y + barbellSize);
            g.draw(circle);
        }
    }

    /**
	 * called when the associated <code>SelectionTool</code> is being activated in the hosting
	 * <code>EditorKit</code>.  This cleans up the state of the underlying <code>SelectionHandler</code>, and
	 * checks for the state of the current selection (ie <code>EditorKit</code>'s selection handler) to be
	 * adequate with the mouse-transform's created by this factory. This means in particular that no
	 * <code>Element</code> must be selected. Besides, <code>highlightingMode</code> is forced to
	 * <code>LOCAL_MODE</code>, so that curve's control-points are visible ("green squares").
	 *
	 */
    public void init(UserConfirmationCache ucc) {
        selectedPointsHandler.clear();
        PECanvas canvas = getEditorKit().getCanvas();
        if (canvas == null) return;
        switch(canvas.getSelectionSize()) {
            case 0:
                break;
            default:
                canvas.unSelectAll();
                break;
        }
        SelectionHandler sh = getEditorKit().getSelectionHandler();
        if (sh instanceof DefaultSelectionHandler) {
            DefaultSelectionHandler dsh = (DefaultSelectionHandler) sh;
            this.oldSelHandlerHighlightingMode = dsh.getHighlightingMode();
            dsh.setHighlightingMode(HighlightingMode.LOCAL);
        }
    }

    /**
	 * Called when the associated <code>SelectionTool</code> is being deactivated in the hosting
	 * <code>EditorKit</code>.  This simply cleans up the state of the underlying
	 * <code>SelectionHandler</code>.
	 */
    public void flush() {
        SelectionHandler sh = getEditorKit().getSelectionHandler();
        if (sh instanceof DefaultSelectionHandler) {
            DefaultSelectionHandler dsh = (DefaultSelectionHandler) sh;
            dsh.setHighlightingMode(this.oldSelHandlerHighlightingMode);
        }
    }

    /**
	 * Return a MouseTransform whose type is adequate with the given mouse-event.
	 * This can be null if no MouseTransform matches the given event.
	 * <p>
	 * Basically, we work with the following modifiers : Shift, Control, Alt. Other modifiers
	 * must be excluded, given their poor support on MacOS platforms, and their odd behaviours
	 * on some Unices. Similarly, double-click events should be avoided since these are rather hard to deal with
	 * seeing that a single-click event is ALWAYS posted beforehands.
	 */
    public MouseTransform createMouseTransform(PEMouseEvent e) {
        HitInfo hitInSelection = getEditorKit().hitTest(e, true);
        if (hitInSelection == null) {
            HitInfo hitInDrawing = getEditorKit().hitTest(e, false);
            if (hitInDrawing == null) {
                if (e.getCanvas().getSelectionSize() == 1) return new SelectPointsInAreaTransform(e.isShiftDown());
                if (e.getCanvas().getSelectionSize() > 1) return new SelectElementTransform(null);
                return new HelpMessageMouseTransform("help-message.SelectACurve");
            }
            if (isValidTarget(hitInDrawing.getTarget())) return new SelectElementTransform(hitInDrawing.getTarget());
            if (e.getCanvas().getSelectionSize() > 1) return new SelectElementTransform(null);
            return null;
        }
        if (!isValidTarget(hitInSelection.getTarget())) return new SelectElementTransform(null);
        if (e.getCanvas().getSelectionSize() > 1) return new SelectElementTransform(hitInSelection.getTarget());
        if (!e.isShiftDown() && !e.isControlDown() && !e.isAltDown()) {
            if (hitInSelection instanceof HitInfo.Point) {
                HitInfo.Point hip = (HitInfo.Point) hitInSelection;
                if (selectedPointsHandler.isSelected(hip)) return new RemovePointTransform();
                return new SelectPointsInAreaTransform(hip, false);
            }
            return new SelectPointsInAreaTransform(false);
        }
        if (e.isShiftDown() && !e.isControlDown() && !e.isAltDown()) {
            if (hitInSelection instanceof HitInfo.Point) {
                HitInfo.Point hip = (HitInfo.Point) hitInSelection;
                if (selectedPointsHandler.isSelected(hip)) return new UnSelectPointTransform(hip);
                return new SelectPointsInAreaTransform(hip, true);
            }
            return new SelectPointsInAreaTransform(true);
        }
        if (!e.isShiftDown() && e.isControlDown() && !e.isAltDown()) {
            if (hitInSelection.getTarget() instanceof PicMultiCurve) {
                PicMultiCurve curve = (PicMultiCurve) hitInSelection.getTarget();
                if (hitInSelection instanceof HitInfo.Point) {
                    HitInfo.Point hip = (HitInfo.Point) hitInSelection;
                    return null;
                }
                if (hitInSelection instanceof HitInfo.Stroke) {
                    HitInfo.Stroke his = (HitInfo.Stroke) hitInSelection;
                    return new SplitSegmentTransform(curve, his.getClickedSegment(), e.getCanvas().getGrid());
                }
            } else if (hitInSelection.getTarget() instanceof PicSmoothPolygon) {
                PicSmoothPolygon curve = (PicSmoothPolygon) hitInSelection.getTarget();
                if (hitInSelection instanceof HitInfo.Point) {
                    HitInfo.Point hip = (HitInfo.Point) hitInSelection;
                    return null;
                }
                if (hitInSelection instanceof HitInfo.HighlighterStroke) {
                    HitInfo.HighlighterStroke his = (HitInfo.HighlighterStroke) hitInSelection;
                    return new SplitSegmentTransform(curve, his.getClickedSegment(), e.getCanvas().getGrid());
                }
            } else if (hitInSelection.getTarget() instanceof PicPsCurve) {
                PicPsCurve curve = (PicPsCurve) hitInSelection.getTarget();
                if (hitInSelection instanceof HitInfo.Point) {
                    HitInfo.Point hip = (HitInfo.Point) hitInSelection;
                    return null;
                } else if (hitInSelection instanceof HitInfo.HighlighterStroke) {
                    HitInfo.HighlighterStroke his = (HitInfo.HighlighterStroke) hitInSelection;
                    switch(his.getClickedSegment()) {
                        case 0:
                            return new SplitSegmentTransform(curve, 0, e.getCanvas().getGrid());
                        case 1:
                            return new SplitSegmentTransform(curve, curve.getLastPointIndex() - 1, e.getCanvas().getGrid());
                        default:
                    }
                } else if (hitInSelection instanceof HitInfo.Stroke) {
                    HitInfo.Stroke his = (HitInfo.Stroke) hitInSelection;
                    if (curve.isClosed()) return new SplitSegmentTransform(curve, his.getClickedSegment(), e.getCanvas().getGrid()); else return new SplitSegmentTransform(curve, his.getClickedSegment() + 1, e.getCanvas().getGrid());
                }
            }
            return null;
        }
        if (!e.isShiftDown() && e.isControlDown() && e.isAltDown()) {
            if (hitInSelection instanceof HitInfo.Point) {
                HitInfo.Point hip = (HitInfo.Point) hitInSelection;
                if (hitInSelection.getTarget() instanceof PicMultiCurve) {
                    return new InvalidMouseTransform();
                }
                if (hitInSelection.getTarget() instanceof PicPsCurve) {
                    return new InvalidMouseTransform();
                }
                if (hitInSelection.getTarget() instanceof PicSmoothPolygon) {
                    return new EditSmoothCoeffTransform();
                }
                return null;
            }
        }
        return null;
    }

    /**
	 * a mouse-transform that selects all elements inside a rectangle dragged by the user
	 */
    protected class SelectPointsInAreaTransform extends SelectAreaTransform {

        private boolean addToSelection;

        private HitInfo.Point hip;

        /**
		 * @param addToSelection if true, selection of points is incremental.
		 */
        public SelectPointsInAreaTransform(boolean addToSelection) {
            this.addToSelection = addToSelection;
            this.hip = null;
        }

        /**
		 * @param addToSelection if true, selection of points is incremental.
		 * @param hip if non-null, indices contained therein will be selected before
		 * starting to draw the selection rectangle (click on an Element's point) */
        public SelectPointsInAreaTransform(HitInfo.Point hip, boolean addToSelection) {
            this.addToSelection = addToSelection;
            this.hip = hip;
        }

        /** called by mousePressed */
        public void start(PEMouseEvent e) {
            super.start(e);
            if (e.getCanvas().getSelectionSize() != 1) return;
            if (!addToSelection) selectedPointsHandler.clear();
            if (hip != null) {
                if (selectedPointsHandler.getElement() == null || selectedPointsHandler.getElement() != hip.getTarget()) selectedPointsHandler.setElement(hip.getTarget());
                for (int i = 0; i < hip.getNbHitPoints(); i++) {
                    selectedPointsHandler.selectPoint(hip.getIndex(i));
                }
            }
        }

        /**
		 * Called when the mouse is released. Selects every elements inside the selection area,
		 * including the element being currently under the cursor.
		 */
        public boolean next(PEMouseEvent e) {
            super.next(e);
            Rectangle2D rectArea = getSelectionRectangle();
            if (e.getCanvas().getSelectionSize() != 1) return false;
            Element elem;
            if (hip != null) elem = hip.getTarget(); else elem = (Element) e.getCanvas().selection().next();
            if (selectedPointsHandler.getElement() == null || selectedPointsHandler.getElement() != elem) {
                selectedPointsHandler.setElement(elem);
            }
            for (int ptIndex = elem.getFirstPointIndex(); ptIndex <= elem.getLastPointIndex(); ptIndex++) {
                ptBuffer = elem.getCtrlPt(ptIndex, ptBuffer);
                if (rectArea.contains(ptBuffer.x, ptBuffer.y)) {
                    selectedPointsHandler.selectPoint(ptIndex);
                }
            }
            if (DEBUG) debug(selectedPointsHandler.toString());
            return false;
        }

        /**
		 * @return a help-message for the UI, that makes sense with this transform.
		 */
        public String getHelpMessage() {
            return "help-message.SelectPointsInArea";
        }

        /** @return a textual representation of this transform for debugging purpose */
        public String toString() {
            return "[SelectPointInAreaTransform]";
        }
    }

    /**
	 * a mouse-transform that unselects points
	 */
    protected class UnSelectPointTransform implements MouseTransform {

        private HitInfo.Point hip;

        /**
		 * @param hip indices contained therein will be unselected
		 */
        public UnSelectPointTransform(HitInfo.Point hip) {
            this.hip = hip;
        }

        /** called by mousePressed */
        public void start(PEMouseEvent e) {
            if (e.getCanvas().getSelectionSize() != 1) return;
            if (hip != null) {
                if (selectedPointsHandler.getElement() == null || selectedPointsHandler.getElement() != hip.getTarget()) selectedPointsHandler.setElement(hip.getTarget());
                for (int i = 0; i < hip.getNbHitPoints(); i++) {
                    selectedPointsHandler.unSelectPoint(hip.getIndex(i));
                }
            }
        }

        public boolean next(PEMouseEvent e) {
            return false;
        }

        public void process(PEMouseEvent e) {
        }

        public void paint(Graphics2D g, Rectangle2D allocation, double scale) {
        }

        public Cursor getCursor() {
            return cursorFactory.getPECursor(CursorFactory.SELECT);
        }

        /**
		 * @return a help-message for the UI, that makes sense with this transform.
		 */
        public String getHelpMessage() {
            return "help-message.UnSelectPoint";
        }

        /** @return a textual representation of this transform for debugging purpose */
        public String toString() {
            return "[UnSelectPointTransform]";
        }
    }

    /**
	 * Helper class for SelectPointsInAreaTransform.
	 * This is a typesafe container for ONE Element and the indices of its selected-points
	 * <p>
	 * Indices are ALWAYS sorted in descending order :
	 * this helps e.g. RemovePointTransform removing curve's points starting from the end (this is made necessary
	 * so as to preserve the meaning of yet-to-be-removed points indices ; removing points starting from
	 * index "0" would surely lead to odd behaviour since ensuing indices would be shifted to the "left").
	 */
    protected class SelectedPointsHandler {

        private Element target;

        private TreeSet<Integer> indexList;

        public SelectedPointsHandler() {
            indexList = new TreeSet<Integer>(new ReverseIntegerComparator());
        }

        /** clears the state of this handler by removing the reference to the current Element and its selected points */
        public void clear() {
            target = null;
            indexList.clear();
        }

        /** sets the current Element and clears the list of selected-points */
        public void setElement(Element e) {
            this.target = e;
            indexList.clear();
        }

        /** returns the current target Element */
        public Element getElement() {
            return target;
        }

        /** adds the given point index to the selection */
        public void selectPoint(int idx) {
            indexList.add(idx);
        }

        /** removes the given point index to the selection */
        public void unSelectPoint(int idx) {
            indexList.remove(idx);
        }

        /**
		 * Returns true if the point with the given index is selected.
		 */
        public boolean isSelected(int idx) {
            return indexList.contains(idx);
        }

        /**
		 * Return true if at least one point in the given HitInfo.Point is selected
		 */
        public boolean isSelected(HitInfo.Point hip) {
            for (int i = 0; i < hip.getNbHitPoints(); i++) {
                if (isSelected(hip.getIndex(i))) return true;
            }
            return false;
        }

        /** return the number of selected points for the current element */
        public int getSelectionSize() {
            return indexList.size();
        }

        /** return an Iterator over the set of selected-points indices (wrapped in Integer's) */
        public Iterator indexIterator() {
            return indexList.iterator();
        }

        public String toString() {
            String str = "[SelectedPointsHandler : target=" + target + ", indices=";
            for (Iterator it = indexList.iterator(); it.hasNext(); ) {
                str += it.next().toString() + " ";
            }
            return str;
        }
    }

    /** a comparator that may be used to sort integers in descending order using standard "sort" methods in Java's Collection Framework */
    class ReverseIntegerComparator implements Comparator<Integer> {

        public int compare(Integer o1, Integer o2) {
            int i1 = o1;
            int i2 = o2;
            return (i2 - i1);
        }
    }

    /**
	 * a mouse-transform that add points to extensible curves
	 */
    protected class SplitSegmentTransform implements MouseTransform {

        private AbstractCurve target;

        private Grid grid;

        private int segIdx;

        private int ctrlPtIdx;

        private int seqIndex = 0;

        /**
		 * @param target the element upon which is transform will act
		 * @param segIdx index of the segment where the new point is to be inserted.
		 * @param grid the Grid instance used for alignment (if it's snap-on)
		 */
        public SplitSegmentTransform(AbstractCurve target, int segIdx, Grid grid) {
            this.target = target;
            this.segIdx = segIdx;
            this.grid = grid;
        }

        /**
		 * Called when the mouse is pressed for the first time.
		 * @since jpicedt 1.4
		 */
        public void start(PEMouseEvent e) {
            PicPoint pt = e.getPicPoint();
            grid.nearestNeighbour(pt, pt);
            e.getCanvas().beginUndoableUpdate(localize("action.editorkit.SplitSegment"));
            ctrlPtIdx = target.splitSegment(segIdx, pt);
        }

        /**
		 * Called when the mouse is dragged. This moves the newly added point and/or sets its control-point when
		 * applicable.
		 */
        public void process(PEMouseEvent e) {
            PicPoint pt = e.getPicPoint();
            grid.nearestNeighbour(pt, pt);
            if (target instanceof PicMultiCurve) {
                switch(seqIndex) {
                    case 0:
                        target.setCtrlPt(ctrlPtIdx, pt, BasicEditPointConstraint.SMOOTHNESS_SYMMETRY);
                        break;
                    case 1:
                        target.setCtrlPt(target.getPBCBezierIndex(ctrlPtIdx + 1), pt, BasicEditPointConstraint.SMOOTHNESS_SYMMETRY);
                        break;
                    default:
                }
            } else if (target instanceof PicSmoothPolygon) {
                target.setCtrlPt(ctrlPtIdx, pt, null);
            } else if (target instanceof PicPsCurve) {
                target.setCtrlPt(ctrlPtIdx, pt, null);
            }
        }

        /**
		 * Called when the mouse is released.
		 */
        public boolean next(PEMouseEvent e) {
            if (target instanceof PicMultiCurve) {
                seqIndex++;
                if (seqIndex < 2) return true;
            }
            e.getCanvas().endUndoableUpdate();
            return false;
        }

        /**
		 * @return a help-message for the UI, that makes sense with this transform.
		 */
        public String getHelpMessage() {
            return "help-message.SplitSegment";
        }

        /**
		 * Allows the MouseTransform to do specific graphic rendering when it's operating.
		 * This implementation does nothing.
		 * @since jpicedt 1.3.2
		 */
        public void paint(Graphics2D g, Rectangle2D allocation, double scale) {
        }

        /** @return a textual representation of this transform for debugging purpose */
        public String toString() {
            return "[SplitSegmentTransform : \n\tsegIdx = " + segIdx + "\n\ttarget = " + target;
        }

        /**
		 * @return a cursor adequate with this mouse-transform, delegating to CursorFactory.
		 */
        public Cursor getCursor() {
            return cursorFactory.getPECursor(CursorFactory.ADD_ENDPT);
        }
    }

    /**
	 * a mouse-transform that removes selected points to/from extensible curves or parallelogram/ellipses after conversion
	 * to a multicurve.
	 * Target element is fetched from the selectedPointHandler.
	 */
    protected class RemovePointTransform implements MouseTransform {

        /**
		 * Called when the mouse is pressed. The transform should do the initialization work here.
		 * @since jpicedt 1.3.3
		 */
        public void start(PEMouseEvent e) {
            Element elem = selectedPointsHandler.getElement();
            if (elem == null) return;
            if (!isValidTarget(elem)) return;
            PicPoint pt = e.getPicPoint();
            e.getCanvas().getGrid().nearestNeighbour(pt, pt);
            e.getCanvas().beginUndoableUpdate(localize("action.editorkit.RemovePoint"));
            for (Iterator it = selectedPointsHandler.indexIterator(); it.hasNext(); ) {
                int index = ((Integer) it.next()).intValue();
                if (elem instanceof PicMultiCurve) {
                    PicMultiCurve c = (PicMultiCurve) elem;
                    if (c.isControlPoint(index)) {
                        c.removePoint(index);
                        it.remove();
                    }
                } else if (elem instanceof PicSmoothPolygon) {
                    ((PicSmoothPolygon) elem).removePoint(index);
                    it.remove();
                } else if (elem instanceof PicPsCurve) {
                    ((PicPsCurve) elem).removePoint(index);
                    it.remove();
                }
            }
            if (elem instanceof PicMultiCurve) {
                PicMultiCurve c = (PicMultiCurve) elem;
                for (Iterator it = selectedPointsHandler.indexIterator(); it.hasNext(); ) {
                    int index = ((Integer) it.next()).intValue();
                    c.removePoint(index);
                    it.remove();
                }
            }
            e.getCanvas().endUndoableUpdate();
        }

        /**
		 * Called when the mouse is dragged. This implementation does nothing.
		 */
        public void process(PEMouseEvent e) {
        }

        /**
		 * Called when the mouse is released.
		 */
        public boolean next(PEMouseEvent e) {
            return false;
        }

        /**
		 * @return a help-message for the UI, that makes sense with this transform.
		 */
        public String getHelpMessage() {
            return "help-message.RemovePoint";
        }

        /**
		 * Allows the MouseTransform to do specific graphic rendering when it's operating.
		 * This implementation does nothing.
		 * @since jpicedt 1.3.2
		 */
        public void paint(Graphics2D g, Rectangle2D allocation, double scale) {
        }

        /** @return a textual representation of this transform for debugging purpose */
        public String toString() {
            return "[RemovePointTransform : \n\tselectionHandler = " + selectedPointsHandler.toString() + "\n";
        }

        /**
		 * @return a cursor adequate with this mouse-transform, delegating to CursorFactory.
		 */
        public Cursor getCursor() {
            return cursorFactory.getPECursor(CursorFactory.REMOVE_ENDPT);
        }
    }

    /**
	 * a mouse-transform that edit smooth-coefficients for a PicSmoothPolygon (edit ALL selected points at once)
	 */
    protected class EditSmoothCoeffTransform implements MouseTransform {

        double lastYMousePosition;

        PicSmoothPolygon target;

        double increment;

        /**
		 * Called when the mouse is pressed. The transform should do the initialization work here.
		 * @since jpicedt 1.4
		 */
        public void start(PEMouseEvent e) {
            Element elem = selectedPointsHandler.getElement();
            if (elem == null) return;
            if (!(elem instanceof PicSmoothPolygon)) return;
            this.target = (PicSmoothPolygon) elem;
            PicPoint pt = e.getPicPoint();
            e.getCanvas().beginUndoableUpdate(localize("action.editorkit.EditSmoothCoeffs"));
            lastYMousePosition = e.getAwtMouseEvent().getPoint().y;
        }

        /**
		 * Called when the mouse is dragged. This implementation updates the current smooth coefficient value from the vertical
		 * mouse position, and update the target polygon accordingly.
		 */
        public void process(PEMouseEvent e) {
            double newYMousePosition = e.getAwtMouseEvent().getPoint().y;
            this.increment = (newYMousePosition - lastYMousePosition) / 100.0;
            for (Iterator it = selectedPointsHandler.indexIterator(); it.hasNext(); ) {
                int index = ((Integer) it.next()).intValue();
                double coeff = target.getSmoothCoefficient(index);
                coeff += increment;
                System.out.println(coeff);
                target.setSmoothCoefficient(index, coeff);
            }
            lastYMousePosition = newYMousePosition;
        }

        /**
		 * Called when the mouse is released. Fires an end-undoable-event.
		 */
        public boolean next(PEMouseEvent e) {
            e.getCanvas().endUndoableUpdate();
            return false;
        }

        /**
		 * @return a help-message for the UI, that makes sense with this transform.
		 */
        public String getHelpMessage() {
            return "help-message.EditSmoothCoeffs";
        }

        /**
		 * Allows the MouseTransform to do specific graphic rendering when it's operating.
		 * This implementation does nothing.
		 * @since jpicedt 1.3.2
		 */
        public void paint(Graphics2D g, Rectangle2D allocation, double scale) {
        }

        /** @return a textual representation of this transform for debugging purpose */
        public String toString() {
            return "[EditSmoothCoeffs : \n\tselectionHandler = " + selectedPointsHandler.toString() + "\n";
        }

        /**
		 * @return a cursor adequate with this mouse-transform, delegating to CursorFactory.
		 */
        public Cursor getCursor() {
            return cursorFactory.getPECursor(CursorFactory.N_RESIZE);
        }
    }

    protected class SelectElementTransform implements MouseTransform {

        Element target;

        /**
		 * @param target the Element to be selected ; if null, all Element's get deselected.
		 */
        public SelectElementTransform(Element target) {
            this.target = target;
        }

        public void start(PEMouseEvent e) {
            if (target == null) {
                e.getCanvas().unSelectAll();
                selectedPointsHandler.clear();
            } else {
                boolean needConversion = (target instanceof PicMultiCurveConvertable) && !(target instanceof AbstractCurve);
                if (needConversion) {
                    e.getCanvas().beginUndoableUpdate(localize("action.editorkit.ConvertParallelogramToMulticurve"));
                    PicMultiCurve curve = ((PicMultiCurveConvertable) target).convertToMultiCurve();
                    Drawing dr = target.getDrawing();
                    if (dr != null) {
                        dr.replace(target, curve);
                        target = curve;
                    }
                    e.getCanvas().endUndoableUpdate();
                }
                e.getCanvas().select(target, REPLACE);
                selectedPointsHandler.setElement(target);
            }
        }

        public boolean next(PEMouseEvent e) {
            return false;
        }

        public void process(PEMouseEvent e) {
        }

        public void paint(Graphics2D g, Rectangle2D allocation, double scale) {
        }

        public Cursor getCursor() {
            return cursorFactory.getPECursor(CursorFactory.SELECT);
        }

        public String getHelpMessage() {
            return "help-message.SelectACurveTransform";
        }
    }
}
