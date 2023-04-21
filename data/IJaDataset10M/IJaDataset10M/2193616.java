package org.plazmaforge.framework.datawarehouse.convert.dataexport.odt;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;
import org.plazmaforge.framework.core.exception.ApplicationException;
import org.plazmaforge.framework.datawarehouse.IDataSetColumn;
import org.plazmaforge.framework.datawarehouse.convert.dataexport.AbstractExporter;
import org.plazmaforge.framework.datawarehouse.convert.dataexport.odt.zip.FileBufferedOasisZip;
import org.plazmaforge.framework.datawarehouse.convert.dataexport.odt.zip.FileBufferedOasisZipEntry;
import org.plazmaforge.framework.datawarehouse.convert.dataexport.odt.zip.OasisZip;
import org.plazmaforge.framework.datawarehouse.convert.dataexport.odt.zip.OasisZipEntry;
import org.plazmaforge.framework.util.StringUtils;

public class ODTExporter extends AbstractExporter {

    private Writer tempBodyWriter;

    private Writer tempStyleWriter;

    private Map fontMap;

    private StyleCache styleCache;

    private TableBuilder tableBuilder;

    public void execute() throws ApplicationException {
        if (!isValid()) {
            return;
        }
        init();
        OutputStream os = null;
        try {
            os = createOutputStream();
            exportToOasisZip(os);
        } catch (Exception ex) {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (Exception e) {
            }
        }
    }

    protected void flushOutput() throws ApplicationException {
    }

    protected void exportToOasisZip(OutputStream os) throws Exception, IOException {
        OasisZip oasisZip = new FileBufferedOasisZip();
        OasisZipEntry tempBodyEntry = new FileBufferedOasisZipEntry(null);
        OasisZipEntry tempStyleEntry = new FileBufferedOasisZipEntry(null);
        tempBodyWriter = tempBodyEntry.getWriter();
        tempStyleWriter = tempStyleEntry.getWriter();
        styleCache = new StyleCache(tempStyleWriter, fontMap);
        Writer stylesWriter = oasisZip.getStylesEntry().getWriter();
        StyleBuilder styleBuilder = new StyleBuilder(null, stylesWriter);
        styleBuilder.build();
        stylesWriter.close();
        exportTable();
        tempBodyWriter.flush();
        tempStyleWriter.flush();
        tempBodyWriter.close();
        tempStyleWriter.close();
        ContentBuilder contentBuilder = new ContentBuilder(oasisZip.getContentEntry(), tempStyleEntry, tempBodyEntry, styleCache.getFontFaces());
        contentBuilder.build();
        tempStyleEntry.dispose();
        tempBodyEntry.dispose();
        oasisZip.zipEntries(os);
        oasisZip.dispose();
    }

    protected String getFileExt() {
        return ".odt";
    }

    protected String getFileEncoding() {
        return null;
    }

    protected void exportTable() throws IOException {
        tableBuilder = new TableBuilder(0, 0, tempBodyWriter, tempStyleWriter);
        tableBuilder.buildTableStyle();
        tableBuilder.buildTableHeader();
        int columnCount = getDataSet().getColumnCount();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            tableBuilder.buildColumnStyle(columnIndex, 0);
            tableBuilder.buildColumnHeader(columnIndex);
            tableBuilder.buildColumnFooter();
        }
        if (isIncludeHeaders()) exportColumnHeader();
        int rowCount = getDataSet().getRowCount();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            this.rowIndex = rowIndex;
            exportRow(rowIndex);
        }
        tableBuilder.buildTableFooter();
    }

    protected void exportRow(int rowIndex) throws IOException {
        exportStartRow(rowIndex, 0);
        int columnCount = getDataSet().getColumnCount();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            IDataSetColumn column = getDataSet().getColumn(columnIndex);
            Object value = getDataSet().getValue(rowIndex, columnIndex);
            this.columnIndex = columnIndex;
            exportRowCell(column, value);
        }
        exportEndRow();
        onProcessDataRow();
    }

    protected void exportColumnHeader() throws IOException {
        exportStartRow();
        int columnCount = getDataSet().getColumnCount();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            IDataSetColumn column = getDataSet().getColumn(columnIndex);
            Object value = column.getDisplayName();
            this.columnIndex = columnIndex;
            exportHeaderCell(column, value);
        }
        exportEndRow();
    }

    protected void exportStartRow(int rowIndex, int rowHeight) throws IOException {
        tableBuilder.buildRowHeader(rowIndex);
    }

    protected void exportStartRow() throws IOException {
        exportStartRow(-1, 0);
    }

    protected void exportEndRow() throws IOException {
        tableBuilder.buildRowFooter();
    }

    protected void exportHeaderCell(IDataSetColumn column, Object value) throws IOException {
        String text = value == null ? "" : value.toString();
        tableBuilder.buildColumnStyle(columnIndex, 0);
        exportText(text, tableBuilder);
    }

    protected void exportRowCell(IDataSetColumn column, Object value) throws IOException {
        String text = value == null ? "" : value.toString();
        tableBuilder.buildColumnStyle(columnIndex, 0);
        exportText(text, tableBuilder);
    }

    private void writeEmptyCell(int emptyCellColSpan, int emptyCellWidth, int rowHeight) throws IOException {
        tempBodyWriter.write("<table:table-cell");
        tempBodyWriter.write(" table:style-name=\"empty-cell\"");
        if (emptyCellColSpan > 1) {
            tempBodyWriter.write(" table:number-columns-spanned=\"" + emptyCellColSpan + "\"");
        }
        tempBodyWriter.write("/>\n");
        writeOccupiedCells(emptyCellColSpan - 1);
    }

    private void writeOccupiedCells(int count) throws IOException {
        for (int i = 0; i < count; i++) {
            tempBodyWriter.write("<table:covered-table-cell/>\n");
        }
    }

    protected void exportText(String text, TableBuilder tableBuilder, byte backgroundMode, Color backgroundColor, byte hAlign, byte vAlign, byte rotation, int colSpan, int rowSpan) throws IOException {
        tableBuilder.buildCellHeader(styleCache.getCellStyle(backgroundMode, backgroundColor, hAlign, vAlign, rotation), colSpan, rowSpan);
        tempBodyWriter.write("<text:p text:style-name=\"");
        tempBodyWriter.write(styleCache.getParagraphStyle(hAlign, vAlign, rotation));
        tempBodyWriter.write("\">");
        tempBodyWriter.write("<text:span");
        tempBodyWriter.write(">");
        if (text != null) {
            tempBodyWriter.write(Utils.replaceNewLineWithLineBreak(StringUtils.xmlEncode(text)));
        }
        tempBodyWriter.write("</text:span>");
        tempBodyWriter.write("</text:p>\n");
        tableBuilder.buildCellFooter();
    }

    protected void exportText(String text, TableBuilder tableBuilder) throws IOException {
        tableBuilder.buildCellHeader();
        tempBodyWriter.write("<text:p text:style-name=\"");
        tempBodyWriter.write("\">");
        tempBodyWriter.write("<text:span");
        tempBodyWriter.write(">");
        if (text != null) {
            tempBodyWriter.write(Utils.replaceNewLineWithLineBreak(StringUtils.xmlEncode(text)));
        }
        tempBodyWriter.write("</text:span>");
        tempBodyWriter.write("</text:p>\n");
        tableBuilder.buildCellFooter();
    }
}
