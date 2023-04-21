package jp.hpl.map.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import src.backend.Level;
import src.backend.LinsEntry;
import src.backend.PolyEntry;
import src.backend.exception.kajiya.NoDataException;
import src.backend.math.GeometoryTool;
import src.backend.math.MapMath;
import src.backend.math.optimizer.MoveOptimizer;
import src.backend.math.select.SelectContainer;
import src.backend.wad.WadTool;
import jp.hpl.map.data.undo.AbstractDo;
import jp.hpl.map.data.undo.MoveDo;

public class MouseEventContainer {

    private SelectContainer selectContainer = new SelectContainer();

    List doList = new ArrayList();

    /**
	 * for undo/redo
	 */
    private int doingIndex = -1;

    public void clearUndoList() {
        doingIndex = -1;
        this.doList.clear();
    }

    public void addDo(AbstractDo doing) {
        this.removeDoFromNow();
        this.doList.add(doing);
        this.doingIndex = doList.size() - 1;
    }

    public AbstractDo getDo() throws NoDataException {
        if (doList.size() == 0 || doingIndex < 0) {
            throw new NoDataException();
        } else {
            AbstractDo doing = (AbstractDo) doList.get(doingIndex);
            doingIndex--;
            return doing;
        }
    }

    /**
	 * delete the doings after  
	 */
    public void removeDoFromNow() {
        int from = doingIndex;
        if (from < 0) {
            from = 0;
        }
        int max = doList.size();
        for (int i = from + 1; i < max && i < doList.size(); ) {
            doList.remove(i);
        }
    }

    /** remembering whether the user grabs and holds map items */
    private boolean isHold = false;

    private Point dragStartPoint = null;

    public static int MODE_NOPE = 0;

    public static int MODE_ITEM_GRAB = 1;

    public static int MODE_MAP_SCROLL = 2;

    public static int MODE_RECTANGLE_SELECT = 3;

    public static int MODE_READY_TO_MOVE = 4;

    public static int MODE_CREATING_LINE = 5;

    public static int MODE_MAP_ZOOM = 6;

    /**
	 * when using line tool, this remembers before clicked point index
	 * when next clicking, create line between before point and new one.
	 */
    private int beforeClickEndpointIndex = -1;

    private int mode = MODE_NOPE;

    public SelectContainer getSelectContainer() {
        return selectContainer;
    }

    public boolean isHold() {
        return isHold;
    }

    public void setHold(boolean isHold) {
        this.isHold = isHold;
    }

    private void setSelectContainer(SelectContainer oneSelectionFromPoint) {
        this.selectContainer = oneSelectionFromPoint;
    }

    private void setDragStartPoint(Point dragStartPoint) {
        this.dragStartPoint = dragStartPoint;
    }

    /**
	 * 
	 * (mouse grabbed some map items)
	 * 
	 * -ready to move them (remember start point of dragging)
	 * -remember 
	 *  -holding
	 *  -pressed mouse button
	 * 
	 * @param mouseP mouse position 
	 */
    public void startItemMove(Point mouseP, SelectContainer container) {
        this.setSelectContainer(container);
        this.setDragStartPoint(mouseP);
        this.setMode(MODE_ITEM_GRAB);
    }

    /**
	 * after drag'n drop items, this is called.
	 * 
	 * (for programmers: do not write code in MapCanvas. use this.)
	 * 
	 * @param mouseP
	 */
    public void endItemMove(Level level, short floor, short ceiling) {
        MoveOptimizer opt = new MoveOptimizer(floor, ceiling);
        try {
            opt.optimize(level, this.getSelectContainer());
        } catch (NoDataException e) {
            e.printStackTrace();
        }
        MoveDo moveDo = new MoveDo((SelectContainer) this.getSelectContainer().clone());
        addDo(moveDo);
        this.readyItemMove(this.getSelectContainer());
    }

    /**
	 * map scrolling
	 * 
	 * @param mouseP start point of scroll 
	 */
    public void startMapScroll(Point mouseP) {
        this.setDragStartPoint(mouseP);
        this.setMode(MODE_MAP_SCROLL);
    }

    private void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    /**
	 * rectangle selecting
	 * 
	 * @param mouseP
	 */
    public void startRectangleSelect(Point mouseP) {
        this.setDragStartPoint(mouseP);
        this.getSelectContainer().clear();
        this.setMode(MODE_RECTANGLE_SELECT);
    }

    public Point getDragStartPoint() {
        return dragStartPoint;
    }

    /**
	 * ready to move items
	 * no pointing
	 * 
	 * @param container
	 */
    public void readyItemMove(SelectContainer container) {
        this.selectContainer = container;
        this.setMode(MODE_READY_TO_MOVE);
    }

    /**
	 * 
	 */
    public void resetMode() {
        this.setMode(MODE_NOPE);
        this.setBeforeClickEndpointIndex(-1);
    }

    public void startCreatingLine(Point mouseViewPoint, Point offset, double zoom, int endpointIndex, boolean isExist, Level level) {
        System.out.println("Start creating line-------------------------");
        setMode(MODE_CREATING_LINE);
        if (isExist) {
        } else {
            Point mouseWorldPoint = MapMath.getWorldPointFromViewPoint(mouseViewPoint, offset, zoom);
            boolean isEPNT = false;
            short newIndex = MapMath.createEndpoint(mouseWorldPoint.x, mouseWorldPoint.y, isEPNT, level);
            System.out.println("DEBUG:Endpoint(PNTS)[ID = " + newIndex + "] has been created.");
            endpointIndex = newIndex;
        }
        if (this.beforeClickEndpointIndex != -1) {
            if (this.beforeClickEndpointIndex == endpointIndex) {
                this.resetMode();
                System.out.println("DEBUG:Clicked same point as before clicked [ID=" + endpointIndex + "]");
            } else {
                short startPointIndex = (short) this.beforeClickEndpointIndex;
                short endPointIndex = (short) endpointIndex;
                boolean found = false;
                for (int i = 0; i < level.getLinsChunk().getNumEntries(); i++) {
                    LinsEntry line = level.getLinsChunk().getEntry(i);
                    if (MapMath.isTwoLinesHaveSameEndpoints(line.getEndpoints()[0], line.getEndpoints()[1], startPointIndex, endPointIndex)) {
                        found = true;
                        JOptionPane.showMessageDialog(null, "Another line already exists.");
                        System.out.println("DEBUG:Another line already exists [ID=" + endpointIndex + "]");
                        break;
                    }
                }
                if (found == false) {
                    for (int p = 0; p < level.getPolyChunk().getNumEntries(); p++) {
                        PolyEntry poly = level.getPolyChunk().getEntry(p);
                        int count = 0;
                        for (int ep = 0; ep < poly.getVertex_count(); ep++) {
                            if (poly.getEndpoint_indexes()[ep] == beforeClickEndpointIndex || poly.getEndpoint_indexes()[ep] == endpointIndex) {
                                count++;
                            }
                            if (count >= 2) {
                                found = true;
                                break;
                            }
                        }
                    }
                    if (found == false) {
                        short index = MapMath.createLine(startPointIndex, endPointIndex, level);
                        this.setBeforeClickEndpointIndex(endpointIndex);
                        System.out.println("Line:[ID=" + index + "] has created.");
                    }
                }
            }
        } else {
            System.out.println("DEBUG:Create only endpoint.");
            this.setBeforeClickEndpointIndex(endpointIndex);
            System.out.println("DEBUG:set before click endpoint:" + beforeClickEndpointIndex);
        }
        System.out.println("End creating line-------------------------\n");
    }

    public int getBeforeClickEndpointIndex() {
        return beforeClickEndpointIndex;
    }

    public void startMapZoom(Point mousePoint) {
        this.setDragStartPoint(mousePoint);
        this.setMode(MODE_MAP_ZOOM);
    }

    private void setBeforeClickEndpointIndex(int beforeClickEndpointIndex) {
        this.beforeClickEndpointIndex = beforeClickEndpointIndex;
    }
}
