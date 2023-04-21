package org.cantaloop.jiomask.factory.javacode.layout;

import java.util.Iterator;
import java.util.List;

public class Helper {

    /**
   * Returns the maximum count of cells in a row.
   */
    public static final int getMaximumCellCount(List rowList) {
        int maxCellCount = 0;
        for (Iterator it = rowList.iterator(); it.hasNext(); ) {
            RowDescriptor row = (RowDescriptor) it.next();
            maxCellCount = Math.max(maxCellCount, row.getCellCount());
        }
        return maxCellCount;
    }
}
