package com.ajoniec.sheet.grid;

import com.ajoniec.sheet.cell.SheetCell;
import com.ajoniec.sheet.commons.SheetPoint;
import com.ajoniec.sheet.commons.SheetRect;

/**
 *
 * @author Adam Joniec
 */
public class SheetScrolling {

    private int translationX = 0, translationY = 0;

    private SheetRect rcContent;

    public SheetScrolling() {
    }

    public SheetScrolling(SheetRect rc) {
        this.rcContent = rc;
    }

    public void setScrollRect(SheetRect rc) {
        this.rcContent = rc;
    }

    public boolean isScrollingNeeded(SheetCell cell) {
        SheetPoint ptTranslation = getScrollTranslation(cell);
        return !(translationY == ptTranslation.y);
    }

    public SheetPoint getScrollTranslation(SheetCell cell) {
        int y = 0;
        if (!isCellVisible(cell)) {
            if (cell.getPos().y2 <= (rcContent.y1 + translationY) || cell.getPos().y1 <= (rcContent.y1 + translationY)) {
                y = cell.getPos().y1 - 15;
            } else if (cell.getPos().y1 >= rcContent.y2 + translationY) {
                y = cell.getPos().y2 - rcContent.h - 15;
            } else {
                y = 0;
            }
        }
        return new SheetPoint(0, y);
    }

    public SheetPoint scrollToCell(SheetCell cell) {
        if (cell == null) return new SheetPoint(translationX, translationY);
        SheetPoint ptTranslation = getScrollTranslation(cell);
        translationX = ptTranslation.x;
        translationY = ptTranslation.y;
        return ptTranslation;
    }

    public boolean isCellVisible(SheetCell cell) {
        SheetRect cellPos = cell.getPos(), translatedCellPos = new SheetRect(cellPos.x - translationX, cellPos.y - translationY, cellPos.w, cellPos.h);
        return cell.getPos().inside(rcContent.offset(translationX, translationY));
    }
}
