package AccordionSequenceDrawer;

import AccordionDrawer.*;

/**
 * @author jslack
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SequenceQuadGridCell extends QuadGridCell {

    /**
	 * @param thedrawer
	 * @param thelevel
	 * @param therow
	 * @param thecol
	 * @param isQuad
	 */
    int seqmin, seqmax;

    public SequenceQuadGridCell(AccordionDrawer thedrawer, int thelevel, int therow, int thecol, boolean isQuad, int seqmin, int seqmax) {
        super(thedrawer, thelevel, therow, thecol, isQuad);
        this.setSeqMin(seqmin);
        this.setSeqMax(seqmax);
    }

    public GridCell createCell(int level, int row, int col, boolean isQuad) {
        if (level == 0) return new SequenceLeafGridCell(drawer, level, row, col, isQuad, row - 1, row - 1); else return new SequenceQuadGridCell(drawer, level, row, col, isQuad, row + 1, row + 1);
    }

    public int getSeqMin() {
        return seqmin;
    }

    public int getSeqMax() {
        return seqmax;
    }

    public void setSeqMin(int n) {
        seqmin = n;
    }

    public void setSeqMax(int n) {
        seqmax = n;
    }

    public void draw() {
        int curr = drawer.getFrameNum();
        if (curr <= drawnFrame) return;
        drawnFrame = curr;
        GridCell cell;
        if (drawer.colorgrid || drawBackground) drawBackgroundBox(null);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                GridCell kid = kidCells[i][j];
                if (null != kid) kid.possiblyEnqueue();
            }
        }
        drawKidsGrid();
    }

    public void possiblyEnqueue() {
        int frameNum = drawer.getFrameNum();
        if (null != this && drawnFrame < frameNum && enqueuedFrame < frameNum) {
            if (bigEnough()) {
                drawer.addCellToQueue(this);
                countRecurse++;
            } else {
                drawer.subpixelDraw(this);
                countSubpixelRecurse++;
            }
        }
    }

    public void addGeom(CellGeom g) {
        cellGeoms.add(g);
        if (level > 0 && null == cellGeomDummy) cellGeomDummy = g;
        int gkey = g.getKey();
        objmin = (gkey < objmin) ? gkey : objmin;
        objmax = (gkey > objmax) ? gkey : objmax;
        int seqkey = ((SiteNode) g).getSequence().key;
        setSeqMin((seqkey < getSeqMin()) ? seqkey : getSeqMin());
        setSeqMax((seqkey > getSeqMax()) ? seqkey : getSeqMax());
        SequenceQuadGridCell temp = (SequenceQuadGridCell) parentCell;
        while (temp != null) {
            temp.setObjMin((gkey < temp.getObjMin()) ? gkey : temp.getObjMin());
            temp.setObjMax((gkey > temp.getObjMax()) ? gkey : temp.getObjMax());
            temp.setSeqMin((seqkey < temp.getSeqMin()) ? seqkey : temp.getSeqMin());
            temp.setSeqMax((seqkey > temp.getSeqMax()) ? seqkey : temp.getSeqMax());
            if (null == temp.cellGeomDummy) {
                temp.cellGeomDummy = g;
            }
            temp = (SequenceQuadGridCell) temp.getParent();
        }
    }

    public boolean bigEnough() {
        boolean tooSmallX = (getMaxPix(AccordionDrawer.X, true) - getMinPix(AccordionDrawer.X, false)) < drawer.getMinCellSize(AccordionDrawer.X);
        return (!tooSmallX);
    }

    public int compareTo(Object o) {
        double oArea = ((GridCell) o).area;
        if (area == oArea && level == ((GridCell) o).getLevel() && rowcol[0] == ((GridCell) o).rowcol[0] && rowcol[1] == ((GridCell) o).rowcol[1]) {
            return 0;
        } else if (area >= oArea) {
            return -1;
        } else {
            return 1;
        }
    }

    public boolean equals(Object o) {
        double oArea = ((GridCell) o).area;
        if (oArea == area && ((GridCell) o).getLevel() == level && ((GridCell) o).rowcol[0] == rowcol[0] && ((GridCell) o).rowcol[1] == rowcol[1]) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        return isQuadTree() + " L " + level + " R " + rowcol[1] + " C " + rowcol[0] + " omin " + getObjMin() + " objmax " + getObjMax() + " smin " + getSeqMin() + " smax " + getSeqMax() + " X: " + nf.format(getMinPix(0, true)) + ", " + nf.format(getMaxPix(0, false)) + "Y: " + nf.format(getMinPix(1, false)) + ", " + nf.format(getMaxPix(1, false));
    }
}
