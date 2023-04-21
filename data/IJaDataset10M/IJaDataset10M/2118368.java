package com.angel.io.helpers;

import com.angel.io.annotations.RowChecker;
import com.angel.io.annotations.RowProcessor;

/**
 * @author William
 *
 */
public class Helper {

    private Helper() {
        super();
    }

    public static int calculateQuantityParameters(RowChecker rowChecker, int headerColumnsNamesQuantity) {
        int quantityParameters = rowChecker.columnsParameters().length == 0 ? headerColumnsNamesQuantity : rowChecker.columnsParameters().length;
        if (rowChecker.headerRow()) {
            quantityParameters++;
        }
        if (rowChecker.importRow()) {
            quantityParameters++;
        }
        return quantityParameters;
    }

    public static int calculateQuantityParameters(RowProcessor rowProcessor, int headerColumnsNamesQuantity) {
        int quantityParameters = rowProcessor.columnsParameters().length == 0 ? headerColumnsNamesQuantity : rowProcessor.columnsParameters().length;
        if (rowProcessor.headerRow()) {
            quantityParameters++;
        }
        if (rowProcessor.importRow()) {
            quantityParameters++;
        }
        if (!rowProcessor.object().equals(Object.class)) {
            quantityParameters++;
        }
        return quantityParameters;
    }
}
