package org.ceclipse.reporting;

public abstract class AbstractReportCell implements IReportCell {

    private ReportCellData cellData;

    private int horizontalSpan;

    private int verticalSpan;

    public AbstractReportCell() {
        this(1, 1);
    }

    public AbstractReportCell(ReportCellData cData) {
        this(new ReportCellData(), 1, 1);
    }

    public AbstractReportCell(int hSpan, int vSpan) {
        this(new ReportCellData(), hSpan, vSpan);
    }

    public AbstractReportCell(ReportCellData cData, int hSpan, int vSpan) {
        if (cData == null) {
            throw new IllegalArgumentException("Report page cell data not found[null]");
        }
        cellData = cData;
        setHorizontalSpan(hSpan);
        setVerticalSpan(vSpan);
    }

    public ReportCellData getCellData() {
        return cellData;
    }

    public void setCellData(ReportCellData cellData) {
        if (cellData == null) {
            throw new IllegalArgumentException("Report cell data not found[null]");
        }
        this.cellData = cellData;
    }

    public int getHorizontalSpan() {
        return horizontalSpan;
    }

    /**
   * 设置该单元的跨列数.
   * 
   * @param horizontalSpan
   *          列数, 从1开始.
   */
    public void setHorizontalSpan(int horizontalSpan) {
        if (horizontalSpan < 1) {
            throw new IllegalArgumentException("Horizontal span is less than 1");
        }
        this.horizontalSpan = horizontalSpan;
    }

    public int getVerticalSpan() {
        return verticalSpan;
    }

    /**
   * 设置该单元的跨行数.
   * 
   * @param verticalSpan
   *          行数, 从1开始.
   */
    public void setVerticalSpan(int verticalSpan) {
        if (verticalSpan < 1) {
            throw new IllegalArgumentException("Vertical span is less than 1");
        }
        this.verticalSpan = verticalSpan;
    }
}
