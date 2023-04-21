package AccordionTreeDrawer;

import java.util.Iterator;
import java.util.ArrayList;
import java.awt.Color;
import AccordionDrawer.*;

/**
 * @author jslack
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TreeQuadGridCell extends QuadGridCell {

    /**
	 * @param thedrawer
	 * @param thelevel
	 * @param therow
	 * @param thecol
	 */
    public TreeQuadGridCell(AccordionDrawer thedrawer, int thelevel, int therow, int thecol, boolean isQuad) {
        super(thedrawer, thelevel, therow, thecol, isQuad);
    }

    public GridCell createCell(int level, int row, int col, boolean isQuad) {
        if (level == 0) return new TreeLeafGridCell(drawer, level, row, col, isQuad); else return new TreeQuadGridCell(drawer, level, row, col, isQuad);
    }

    public void draw() {
        int curr = drawer.getFrameNum();
        if (curr <= drawnFrame) return;
        drawnFrame = curr;
        drawAttachedObjects();
        GridCell cell;
        if (drawer.colorgrid || drawBackground) drawBackgroundBox(null);
        Iterator iter = cellGeoms.iterator();
        while (iter.hasNext()) {
            CellGeom obj = (CellGeom) iter.next();
            ((AccordionTreeDrawer) drawer).drawNextFancy(obj);
        }
        drawKidsGrid();
    }

    public void possiblyEnqueue() {
        if (null != this && drawnFrame < drawer.getFrameNum() && enqueuedFrame < drawer.getFrameNum() && level > 0) {
            computePlaceThisFrame();
            if (bigEnough()) {
                drawer.addCellToQueue(this);
                countRecurse++;
            } else {
                ArrayList colorList = null;
                if (!drawer.colorgrid) {
                    colorList = drawer.getColorsForGridCell(this, objmin);
                    if (colorList == null || colorList.size() == 0) {
                        GridCell par = getParent();
                        while (!par.bigEnough() && par.drewMarkedAttachedFrame < drawer.getFrameNum()) par = par.getParent();
                        if (par.bigEnough() && (par.drewMarkedAttachedFrame < drawer.getFrameNum())) {
                            drawer.addCellToQueue(this);
                            countSubpixelRecurse++;
                        }
                    }
                }
            }
        }
    }

    public void addGeom(CellGeom g) {
        cellGeoms.add(g);
        if (level > 0 && null == cellGeomDummy) cellGeomDummy = g;
        int gkey = g.getKey();
        objmin = (gkey < objmin) ? gkey : objmin;
        objmax = (gkey > objmax) ? gkey : objmax;
        TreeQuadGridCell temp = (TreeQuadGridCell) parentCell;
        while (temp != null) {
            temp.objmin = (objmin < temp.objmin) ? objmin : temp.objmin;
            temp.objmax = (objmax > temp.objmax) ? objmax : temp.objmax;
            if (null == temp.cellGeomDummy) {
                temp.cellGeomDummy = g;
            }
            temp = (TreeQuadGridCell) temp.getParent();
        }
    }

    public boolean bigEnough() {
        boolean tooSmallX = (getMaxPix(AccordionDrawer.X, true) - getMinPix(AccordionDrawer.X, false)) < drawer.getMinCellSize(AccordionDrawer.X);
        boolean tooSmallY = (getMaxPix(AccordionDrawer.Y, false) - getMinPix(AccordionDrawer.Y, false)) < drawer.getMinCellSize(AccordionDrawer.Y);
        return (!tooSmallX && !tooSmallY);
    }

    public int compareTo(Object other) {
        GridCell o = (GridCell) other;
        if (level == ((GridCell) o).getLevel() && rowcol[0] == ((GridCell) o).rowcol[0] && rowcol[1] == ((GridCell) o).rowcol[1]) {
            return 0;
        } else if (rowcol[1] > ((GridCell) o).rowcol[1]) {
            return -1;
        } else if (rowcol[1] == o.rowcol[1]) {
            if (rowcol[0] > o.rowcol[0]) {
                return -1;
            }
            if (rowcol[0] == o.rowcol[0]) {
                if (level > ((GridCell) o).getLevel()) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    public boolean equals(Object other) {
        GridCell o = (GridCell) other;
        if (((GridCell) o).getLevel() == level && ((GridCell) o).rowcol[0] == rowcol[0] && ((GridCell) o).rowcol[1] == rowcol[1]) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        return print(-1);
    }
}
