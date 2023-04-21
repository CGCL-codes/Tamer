package com.googlecode.g2re.excel;

import com.googlecode.g2re.BuilderArgs;
import com.googlecode.g2re.domain.ReportDefinition;
import com.googlecode.g2re.domain.ReportFormat;
import com.googlecode.g2re.jdbc.DataSet;
import java.io.OutputStream;
import java.util.Dictionary;
import javax.script.ScriptEngine;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Provides an element with details required to Build an excel spreadsheet
 * TODO: Need to use a constructor to set many variables so that they can not be overwritten
 * TODO: Need to make some variables protected that should only be internall set
 * @author Brad Rydzewski
 */
public class ExcelBuilderArgs extends BuilderArgs {

    private WritableWorkbook workbook;

    private WritableSheet worksheet;

    private int column;

    private int row;

    private OutputStream outputStream;

    private static final ReportFormat REPORT_FORMAT = ReportFormat.Excel;

    public ExcelBuilderArgs(ReportDefinition reportDefinition, Dictionary<String, DataSet> results, ScriptEngine scriptEngine, OutputStream outputStream, WritableWorkbook workbook) {
        super(reportDefinition, results, scriptEngine);
        this.outputStream = outputStream;
        this.workbook = workbook;
        init();
    }

    void init() {
        column = 0;
        row = 0;
    }

    /**
     * Increments the current row being built in the Excel document
     * @return
     */
    public int incrementRow() {
        row += 1;
        return row;
    }

    /**
     * Increments the current column being built in the Excel document
     * @return
     */
    public int incrementColumn() {
        column += 1;
        return column;
    }

    /**
     * Resets the current Row and Column back to 0,0
     */
    public void resetRowsAndColumns() {
        setColumn(0);
        setRow(0);
    }

    /**
     * This is a placeholder indicating the current Column
     * being built in the Excel document. NOTE: this is stored as an integer,
     * corresponding to Columns A through ZZ in an excel document.
     * @return
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sets the current Column being built in the Excel document
     * @param column
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * This is a placeholder indicating the current Row
     * being built in the Excel document
     * @return
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the current Row being built in the Excel document
     * @param row
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Gets the current worksheet to be accessed by UI items
     * being built in the excel document
     * @return
     */
    public WritableSheet getWorksheet() {
        return worksheet;
    }

    /**
     * Sets the current worksheet to be accessed by UI items
     * being built in the excel document
     * @param worksheet
     */
    void setWorksheet(WritableSheet worksheet) {
        this.worksheet = worksheet;
    }

    /**
     * Gets the current Excel workbook being rendered
     * @return
     */
    public WritableWorkbook getWorkbook() {
        return workbook;
    }

    /**
     * Gets the Output Stream the Excel document is being written to
     * @return
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Indicates the {@link ReportFormat} type being built, IE excel, pdf
     * and HTML.
     * @return
     */
    @Override
    public ReportFormat getFormat() {
        return REPORT_FORMAT;
    }
}
