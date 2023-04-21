package com.ajoniec.sheet.cell;

import com.ajoniec.sheet.commons.SheetPoint;
import com.ajoniec.sheet.commons.SheetRect;
import com.ajoniec.sheet.placement.SheetCellPlacement;
import com.ajoniec.utils.Utils;
import java.util.Date;

/**
 *
 * @author Adam Joniec
 */
public class SheetTimeCell extends SheetCell {

    public long time;

    public SheetTimeCell(SheetCellPlacement placement, long time) {
        super(true);
        this.time = time;
        placement.updateCellPlacement(this);
    }

    public String getTimeString() {
        return Utils.shortTimeString(time);
    }

    public long getTime() {
        return time;
    }

    public boolean setTime(long time) {
        boolean redrawNeeded = ((time / 1000) != (this.time / 1000));
        this.time = time;
        return redrawNeeded;
    }

    public String toString() {
        String ret = "SheetGlobalTimeElement ";
        return ret;
    }
}
