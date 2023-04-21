package net.sf.webwarp.util.excel.hssf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * A Collection of usefull static helper methods for reading Excel-Data.
 * 
 * @author mos
 * 
 */
public class HssfUtils {

    private HssfUtils() {
    }

    /**
     * Returns a List of Maps assuming that the first row contains the map keys (i.e. column names).
     * 
     * @param sheet
     * @return
     */
    public static List<Map<String, Object>> getRows(HSSFSheet sheet) {
        return getRows(sheet, 0);
    }

    /**
     * Returns a List of Maps assuming that the first row contains the map keys (i.e. column names). Starting at the given rowIndex.
     * 
     * @param sheet
     * @param rowIndex
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getRows(HSSFSheet sheet, int rowIndex) {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        Iterator<HSSFRow> rowIter = sheet.rowIterator();
        Map<Short, String> headerMap = new HashMap<Short, String>();
        while (rowIter.hasNext()) {
            HSSFRow row = rowIter.next();
            if (row.getRowNum() == rowIndex) {
                Iterator<HSSFCell> cellIter = row.cellIterator();
                while (cellIter.hasNext()) {
                    HSSFCell cell = cellIter.next();
                    headerMap.put(cell.getCellNum(), ((String) HssfUtils.getCellValue(cell)).trim());
                }
            } else if (row.getRowNum() > rowIndex) {
                Iterator<HSSFCell> cellIter = row.cellIterator();
                Map<String, Object> rowMap = new HashMap<String, Object>();
                while (cellIter.hasNext()) {
                    HSSFCell cell = cellIter.next();
                    rowMap.put(headerMap.get(cell.getCellNum()), HssfUtils.getCellValue(cell));
                }
                rows.add(rowMap);
            }
        }
        return rows;
    }

    /**
     * Returns the Values of the specific Row.
     * 
     * @param sheet
     * @param rowIndex
     *            rowIndex
     * @return a list containing the values in the specific row
     */
    @SuppressWarnings("unchecked")
    public static List<Object> getRowValues(HSSFSheet sheet, int rowIndex) {
        if (sheet == null) {
            return Collections.EMPTY_LIST;
        }
        return getRowValues(sheet.getRow(rowIndex));
    }

    /**
     * Returns the Values of the specific Row.
     * 
     * @param sheet
     * @param rowIndex
     * @param columnNames
     * @return
     */
    public static Map<String, Object> getRowValues(HSSFSheet sheet, int rowIndex, List<String> columnNames) {
        if (sheet == null || columnNames == null) {
            return Collections.EMPTY_MAP;
        }
        return getRowValues(sheet.getRow(rowIndex), columnNames);
    }

    /**
     * Returns the Values of the specific Row.
     * 
     * @param row
     * @return
     */
    public static List<Object> getRowValues(HSSFRow row) {
        if (row == null) {
            return Collections.EMPTY_LIST;
        }
        List<Object> values = new ArrayList<Object>();
        Iterator<HSSFCell> it = row.cellIterator();
        while (it.hasNext()) {
            values.add(HssfUtils.getCellValue(it.next()));
        }
        return values;
    }

    /**
     * Returns the values of the specific row in a map containing the columnNames as keys.
     * 
     * @param row
     * @param columnNames
     * @return
     */
    public static Map<String, Object> getRowValues(HSSFRow row, List<String> columnNames) {
        if (row == null || columnNames == null) {
            return Collections.EMPTY_MAP;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<HSSFCell> it = row.cellIterator();
        int i = 0;
        while (it.hasNext()) {
            try {
                map.put(columnNames.get(i++), HssfUtils.getCellValue(it.next()));
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        return map;
    }

    /**
     * Returns the Value of the specific cell.
     * 
     * @param sheet
     * @param rowIndex
     *            rowIndex
     * @param columnIndex
     *            columnIndex
     * @return the column value
     */
    public static Object getCellValue(HSSFSheet sheet, int rowIndex, int columnIndex) {
        if (sheet != null) {
            return getCellValue(sheet.getRow(rowIndex), columnIndex);
        }
        return null;
    }

    /**
     * Returns the Object of the specific cell.
     * 
     * @param row
     * @param columnIndex
     * @return the Object of the specific Cell
     */
    public static Object getCellValue(HSSFRow row, int columnIndex) {
        Validate.isTrue(columnIndex <= 255, "column index may not by less the 255");
        if (row != null) {
            return getCellValue(row.getCell((short) columnIndex));
        }
        return null;
    }

    /**
     * This Method tries to get the right Java-Type out of the Excel Cell.
     * <ul>
     * <li>it never returns an empty String; allways null.
     * <li>if the cell-type is nummeric it returns a {@link BigDecimal}
     * <li>if the cell-type is nummeric and the cell is date-formatted it returns a java.util.Date
     * <li>if the cell-type is formula it tries to return a {@link BigDecimal}. if this fails it tries to return a {@link String}. At least it tries to return a
     * {@link Boolean}
     * 
     * @param cell
     * @return the Object of the specific Cell.
     */
    public static Object getCellValue(HSSFCell cell) {
        if (cell != null) {
            if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                String value = cell.getStringCellValue();
                if (StringUtils.isNotBlank(value)) {
                    return value;
                }
            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
                return cell.getBooleanCellValue();
            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    return HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                } else if (!Double.isNaN(cell.getNumericCellValue())) {
                    return BigDecimal.valueOf(cell.getNumericCellValue());
                }
            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    try {
                        return HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                    } catch (Exception e) {
                    }
                }
                try {
                    double doubleValue = cell.getNumericCellValue();
                    if (!Double.isNaN(doubleValue)) {
                        try {
                            return BigDecimal.valueOf(cell.getNumericCellValue());
                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e) {
                }
                try {
                    return cell.getBooleanCellValue();
                } catch (Exception e) {
                }
                try {
                    String value = cell.getStringCellValue();
                    if (StringUtils.isNotBlank(value)) {
                        return value;
                    }
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

    /**
     * Converts the value to a {@link Date} Object. If it is an Instance of {@link Number} it converts it to {@link Date} according the excel Number to Date
     * Method. Excel stores the dates internally in doubles. The integer numbers representing the days since 1.1.1900 and the fraction digits representing the
     * second since 00:00:00.
     * <p>
     * It is basically a wrapper function arround {@link HSSFDateUtil#getJavaDate(double)}
     * 
     * @param value
     *            the value to be converted
     * @return
     */
    public static Date toUtilDate(Object value) {
        if (value instanceof Date) {
            return (Date) value;
        } else if (value instanceof Number) {
            return new Date(HSSFDateUtil.getJavaDate(((Number) value).doubleValue()).getTime());
        }
        return null;
    }

    /**
     * Converts the value to a {@link java.sql.Date} Object. If it is an Instance of {@link Number} it converts it to {@link java.sql.Date} according the excel
     * Number to Date Method. Excel stores the dates internally in doubles. The integer numbers representing the days since 1.1.1900 and the fraction digits
     * representing the second since 00:00:00.
     * <p>
     * It is basically a wrapper function arround {@link HSSFDateUtil#getJavaDate(double)}
     * 
     * @param value
     *            the value to be converted
     * @return
     */
    public static java.sql.Date toSQLDate(Object value) {
        if (value instanceof java.sql.Date) {
            return (java.sql.Date) value;
        } else if (value instanceof java.util.Date) {
            return new java.sql.Date(((Date) value).getTime());
        } else if (value instanceof Number) {
            return new java.sql.Date(HSSFDateUtil.getJavaDate(((Number) value).doubleValue()).getTime());
        }
        return null;
    }
}
