package org.jcvi.glk.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;

public final class ExcelFileUtils {

    private ExcelFileUtils() {
        throw new IllegalStateException("not instantiable");
    }

    /**
     * Depending on how the excel file was created
     * the values for integer may be stored as 
     * text or as numbers.  This method
     * can handle both cases
     * @param cell
     * @return
     */
    public static int getIntValueFrom(Cell cell) {
        switch(cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return Integer.parseInt(cell.getRichStringCellValue().getString());
            case Cell.CELL_TYPE_NUMERIC:
                return (int) cell.getNumericCellValue();
            default:
                throw new IllegalArgumentException("could not parse int from cell " + cell);
        }
    }

    public static boolean hasValue(Cell cell) {
        if (cell == null) {
            return false;
        }
        RichTextString string = cell.getRichStringCellValue();
        if (string == null || string.getString().trim().isEmpty()) {
            return false;
        }
        return true;
    }
}
