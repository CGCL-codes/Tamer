package net.sf.webwarp.reports.xls;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

/**
 * Helper to format the cells. Collects/caches all the formates definied for a excel sheet.
 * 
 * @author aul
 */
@SuppressWarnings("deprecation")
public class ExcelCellFormatter implements IExcelCellFormatter {

    private Map<String, HSSFCellStyle> cellStyles;

    private Map<String, ExcelCellType> cellTypes;

    private HSSFCellStyle defaultDateStyle;

    private HSSFCellStyle defaultDateTimeStyle;

    private HSSFCellStyle defaultTimeStyle;

    private IExcelFormat excelFormat;

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(ExcelCellFormatter.class);

    public ExcelCellFormatter(IExcelFormat format, HSSFWorkbook wb) {
        defaultDateStyle = initDefaultDateStyle(wb);
        defaultDateTimeStyle = initDefaultDateTimeStyle(wb);
        defaultTimeStyle = initDefaultTimeStyle(wb);
        excelFormat = format;
        if (excelFormat != null && excelFormat.getExcelCellFormatPool() != null && !excelFormat.getExcelCellFormatPool().isEmpty()) {
            ExcelCellFormatPool formatPool = excelFormat.getExcelCellFormatPool();
            cellStyles = new HashMap<String, HSSFCellStyle>();
            cellTypes = new HashMap<String, ExcelCellType>();
            Iterator<String> it = formatPool.getFormatIdIterator();
            while (it.hasNext()) {
                String formatId = (String) it.next();
                ExcelCellFormat excelCellFormat = formatPool.get(formatId);
                HSSFCellStyle hssfCellStyle = excelCellFormat.getHSSFStyle(wb);
                cellStyles.put(formatId, hssfCellStyle);
                cellTypes.put(formatId, excelCellFormat.getExcelCellType());
            }
        } else {
            cellStyles = null;
            cellTypes = null;
        }
    }

    private boolean hasFormatFor(String formatId) {
        if (cellStyles == null) return false;
        if (cellStyles.get(formatId) == null) return false; else return true;
    }

    public HSSFCell fillTableHeaderCell(Object value, String columnName, HSSFCell cell) {
        if (excelFormat == null) return fillCell(value, cell, null);
        ExcelFormatIds excelFormatIds = excelFormat.getTableHeaderCellFormatIds();
        if (excelFormatIds == null) return fillCell(value, cell, null);
        return fillCell(value, excelFormatIds.getFormatId(columnName), String.class, cell);
    }

    public HSSFCell fillDataCell(Object value, String columnName, HSSFCell cell) {
        if (value == null) {
            return cell;
        }
        if (excelFormat == null) return fillCell(value, cell, null);
        ExcelFormatIds excelFormatIds = excelFormat.getDataCellFormatIds();
        if (excelFormatIds == null) return fillCell(value, cell, null);
        return fillCell(value, excelFormatIds.getFormatId(columnName), value.getClass(), cell);
    }

    public HSSFCell fillCell(Object value, HSSFCell cell, HSSFCellStyle style) {
        return ReportDataUtil.fillCell(value, cell, style, defaultDateTimeStyle, defaultDateStyle, defaultTimeStyle);
    }

    private HSSFCell fillCellByFormatId(Object value, String formatId, HSSFCell cell) {
        HSSFCellStyle style = cellStyles.get(formatId);
        return fillCell(value, cell, style);
    }

    private HSSFCell fillCell(Object value, ExcelFormatId excelFormatId, Class<?> valueClass, HSSFCell cell) {
        if (excelFormatId == null) return fillCell(value, cell, null);
        String formatId = excelFormatId.getFormatId();
        if (!hasFormatFor(formatId)) return fillCell(value, cell, null);
        return fillCellByFormatId(value, formatId, cell);
    }

    private HSSFCellStyle initDefaultDateTimeStyle(HSSFWorkbook wb) {
        HSSFDataFormat df = wb.createDataFormat();
        HSSFCellStyle dateStyle = wb.createCellStyle();
        dateStyle.setDataFormat(df.getFormat("DD.MM.YYYY hh:mm:ss"));
        return dateStyle;
    }

    private HSSFCellStyle initDefaultDateStyle(HSSFWorkbook wb) {
        HSSFDataFormat df = wb.createDataFormat();
        HSSFCellStyle dateStyle = wb.createCellStyle();
        dateStyle.setDataFormat(df.getFormat("DD.MM.YYYY"));
        return dateStyle;
    }

    private HSSFCellStyle initDefaultTimeStyle(HSSFWorkbook wb) {
        HSSFDataFormat df = wb.createDataFormat();
        HSSFCellStyle dateStyle = wb.createCellStyle();
        dateStyle.setDataFormat(df.getFormat("hh:mm:ss"));
        return dateStyle;
    }

    public int addReportHeader(HSSFSheet sheet) {
        int headerRowCount = 0;
        List<ExcelReportHeaderCell> excelReportHeaderCells = excelFormat.getReportHeaderCells();
        if (excelReportHeaderCells == null) return 0;
        for (ExcelReportHeaderCell headerCell : excelReportHeaderCells) {
            if (headerCell.needsColspan()) {
                sheet.addMergedRegion(new Region(headerCell.getRow(), (short) headerCell.getColStart(), headerCell.getRow(), (short) headerCell.getColEnd()));
            }
            HSSFCell cell = ReportDataUtil.createCell(headerCell.getRow(), headerCell.getColStart(), sheet);
            fillCellByFormatId(headerCell.getValue(), headerCell.getFormatId(), cell);
            if (headerCell.getRow() > headerRowCount) {
                headerRowCount = headerCell.getRow();
            }
        }
        return headerRowCount;
    }
}
