package org.ceclipse.reporting;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

public class ReportPage extends AbstractReportPage {

    private IReportColumn[] cols;

    private IReportCell[][] rows;

    private Point[][] points;

    private int row;

    /**
   * 默认构造方法.
   */
    public ReportPage() {
        this(new IReportColumn[0]);
    }

    /**
   * 构造方法.
   * 
   * @param cols
   *          报表列字段数组.
   */
    public ReportPage(IReportColumn[] cols) {
        super();
        if (cols == null) {
            throw new IllegalArgumentException("ReportPage columns not found[null]");
        }
        this.row = 0;
        this.cols = cols;
        this.rows = new IReportCell[1][cols.length];
        this.points = new Point[1][cols.length];
    }

    /**
   * 添加报表单元.
   * 
   * @param item
   *          报表单元.
   * @param row
   *          行数.
   * @param col
   *          列数.
   */
    public void addCell(IReportCell item, int row, int col) {
        if (item == null) {
            throw new IllegalArgumentException("ReportPage cell not found[null]");
        }
        if (row < 0) {
            throw new IllegalArgumentException("ReportPage cell row is less than 0");
        }
        if (col < 0) {
            throw new IllegalArgumentException("ReportPage cell column is less than 0");
        }
        int colLength = cols.length;
        int rowLength = rows.length;
        if (row < rowLength && col < colLength) {
            if (points[row][col] != null) {
                throw new IllegalArgumentException("Table item already exists at " + row + "," + col);
            }
            if (item.getVerticalSpan() > 1 && item.getHorizontalSpan() > 1) {
                for (int r = 1, rlen = item.getVerticalSpan(); r < rlen && row + r < rowLength; r++) {
                    int newRow = row + r;
                    for (int c = 1, len = item.getHorizontalSpan(); c < len && c + col < colLength; c++) {
                        int newCol = col + c;
                        if (points[newRow][newCol] != null) {
                            throw new IllegalArgumentException("Table item already exists at " + newRow + "," + newCol);
                        }
                    }
                }
            } else if (item.getVerticalSpan() > 1) {
                for (int r = 1, rlen = item.getVerticalSpan(); r < rlen && row + r < rowLength; r++) {
                    int newRow = row + r;
                    if (points[newRow][col] != null) {
                        throw new IllegalArgumentException("Table item already exists at " + newRow + "," + col);
                    }
                }
            } else if (item.getHorizontalSpan() > 1) {
                for (int c = 1, len = item.getHorizontalSpan(); c < len && c + col < colLength; c++) {
                    int newCol = col + c;
                    if (points[row][newCol] != null) {
                        throw new IllegalArgumentException("Table item already exists at " + row + "," + newCol);
                    }
                }
            }
        }
        int maxRowLength = row + (item.getVerticalSpan() > 0 ? item.getVerticalSpan() - 1 : 0);
        if (maxRowLength >= rowLength) {
            IReportCell[][] swapRows = new IReportCell[maxRowLength + 1][colLength];
            System.arraycopy(rows, 0, swapRows, 0, rowLength);
            Point[][] swapRowBits = new Point[maxRowLength + 1][colLength];
            System.arraycopy(points, 0, swapRowBits, 0, rowLength);
            rows = swapRows;
            points = swapRowBits;
        }
        rows[row][col] = item;
        points[row][col] = new Point(row, col);
        if (item.getVerticalSpan() > 1 && item.getHorizontalSpan() > 1) {
            for (int r = 1, rlen = item.getVerticalSpan(); r < rlen; r++) {
                int newRow = row + r;
                for (int c = 1, len = item.getHorizontalSpan(); c < len && c + col < colLength; c++) {
                    int newCol = col + c;
                    points[newRow][newCol] = new Point(row, col);
                }
            }
        } else {
            if (item.getVerticalSpan() > 1) {
                for (int r = 1, rlen = item.getVerticalSpan(); r < rlen; r++) {
                    points[row + r][col] = new Point(row, col);
                }
            }
            if (item.getHorizontalSpan() > 1) {
                for (int c = 1, len = item.getHorizontalSpan(); c < len && c + col < colLength; c++) {
                    points[row][col + c] = new Point(row, col);
                }
            }
        }
    }

    /**
   * 在最后一行添加报表单元.
   * 
   * @param item
   *          报表单元.
   * @param col
   *          列数.
   */
    public void addCell(IReportCell item, int col) {
        if (item == null) {
            throw new IllegalArgumentException("ReportPage cell not found[null]");
        }
        if (col < 0) {
            throw new IllegalArgumentException("ReportPage cell column is less than 0");
        }
        int row = rows.length - 1;
        int colLength = cols.length;
        int rowLength = rows.length;
        if (col < colLength) {
            if (points[row][col] != null) {
                throw new IllegalArgumentException("Table item already exists at " + row + "," + col);
            }
            if (item.getVerticalSpan() > 1 && item.getHorizontalSpan() > 1) {
                for (int r = 1, rlen = item.getVerticalSpan(); r < rlen && row + r < rowLength; r++) {
                    int newRow = row + r;
                    for (int c = 1, len = item.getHorizontalSpan(); c < len && c + col < colLength; c++) {
                        int newCol = col + c;
                        if (points[newRow][newCol] != null) {
                            throw new IllegalArgumentException("Table item already exists at " + newRow + "," + newCol);
                        }
                    }
                }
            } else if (item.getVerticalSpan() > 1) {
                for (int r = 1, rlen = item.getVerticalSpan(); r < rlen && row + r < rowLength; r++) {
                    int newRow = row + r;
                    if (points[newRow][col] != null) {
                        throw new IllegalArgumentException("Table item already exists at " + newRow + "," + col);
                    }
                }
            } else if (item.getHorizontalSpan() > 1) {
                for (int c = 1, len = item.getHorizontalSpan(); c < len && c + col < colLength; c++) {
                    int newCol = col + c;
                    if (points[row][newCol] != null) {
                        throw new IllegalArgumentException("Table item already exists at " + row + "," + newCol);
                    }
                }
            }
        }
        int maxRowLength = row + (item.getVerticalSpan() > 0 ? item.getVerticalSpan() - 1 : 0);
        if (maxRowLength >= rowLength) {
            IReportCell[][] swapRows = new IReportCell[maxRowLength + 1][colLength];
            System.arraycopy(rows, 0, swapRows, 0, rowLength);
            Point[][] swapRowBits = new Point[maxRowLength + 1][colLength];
            System.arraycopy(points, 0, swapRowBits, 0, rowLength);
            rows = swapRows;
            points = swapRowBits;
        }
        rows[row][col] = item;
        points[row][col] = new Point(row, col);
        if (item.getVerticalSpan() > 1 && item.getHorizontalSpan() > 1) {
            for (int r = 1, rlen = item.getVerticalSpan(); r < rlen; r++) {
                int newRow = row + r;
                for (int c = 1, len = item.getHorizontalSpan(); c < len && c + col < colLength; c++) {
                    int newCol = col + c;
                    points[newRow][newCol] = new Point(row, col);
                }
            }
        } else {
            if (item.getVerticalSpan() > 1) {
                for (int r = 1, rlen = item.getVerticalSpan(); r < rlen; r++) {
                    points[row + r][col] = new Point(row, col);
                }
            }
            if (item.getHorizontalSpan() > 1) {
                for (int c = 1, len = item.getHorizontalSpan(); c < len && c + col < colLength; c++) {
                    points[row][col + c] = new Point(row, col);
                }
            }
        }
    }

    /**
   * 获取报表单元.
   * <p>
   * 该方法调用下面的getCell(row, col, true)方法, 以简化用户调用.
   * </p>
   * 
   * @param row
   *          行数.
   * @param col
   *          列数.
   * @return 返回报表单元, 如果不存在则返回null.
   * @see #getCell(int, int, boolean)
   */
    public IReportCell getCell(int row, int col) {
        return getCell(row, col, true);
    }

    /**
   * 获取报表单元.
   * <p>
   * <ul>
   * 如果一个元素位置为(1,2)且跨三列(Horizontal Span = 3), 则它的位置标记有(1,2),(1,3)和(1,4), 其中(1,2)是它的绝对位置, (1,3)和(1,4)是它的映射位置:
   * <li>当使用映射位置: 则获取getCell(1,3,true)时返回null.</li>
   * <li>当不使用映射位置: 则获取getCell(1,3,true)时返回该元素对象.</li>
   * </ul>
   * </p>
   * 
   * @param row
   *          行数.
   * @param col
   *          列数.
   * @param abs
   *          是否使用映射位置.
   * @return 返回报表单元, 如果不存在则返回null.
   */
    public IReportCell getCell(int row, int col, boolean abs) {
        int colLength = cols.length;
        int rowLength = rows.length;
        if (row < rowLength && col < colLength) {
            if (abs) {
                Point point = points[row][col];
                if (point != null) {
                    return rows[point.x][point.y];
                } else {
                    return null;
                }
            } else {
                return rows[row][col];
            }
        } else {
            return null;
        }
    }

    /**
   * 检查指定单元是否存在.
   * <p>
   * 该方法调用下面的hasCell(row, col, true)方法, 以简化用户调用.
   * </p>
   * 
   * @param row
   *          行数.
   * @param col
   *          列数.
   * @return 返回true表示已存在, 返回false表示不存在.
   */
    public boolean hasCell(int row, int col) {
        return hasCell(row, col, true);
    }

    /**
   * 检查指定单元是否存在.
   * <p>
   * <ul>
   * 如果一个元素位置为(1,2)且跨三列(Horizontal Span = 3), 则它的位置标记有(1,2),(1,3)和(1,4), 其中(1,2)是它的绝对位置, (1,3)和(1,4)是它的映射位置:
   * <li>当使用映射位置: 则获取hasCell(1,3,true)时返回false.</li>
   * <li>当不使用映射位置: 则获取hasCell(1,3,true)时返回true.</li>
   * </ul>
   * </p>
   * 
   * @param row
   *          行数.
   * @param col
   *          列数.
   * @param abs
   *          是否使用映射位置.
   * @return 返回true表示已存在, 返回false表示不存在.
   */
    public boolean hasCell(int row, int col, boolean abs) {
        int colLength = cols.length;
        int rowLength = rows.length;
        if (row < rowLength && col < colLength) {
            if (abs) {
                Point point = points[row][col];
                return point != null;
            } else {
                return rows[row][col] != null;
            }
        } else {
            return false;
        }
    }

    /**
   * 获取报表列字段.
   * 
   * @param col
   *          列数.
   * @return 列字段,如果不存在,则返回null.
   */
    public IReportColumn getColumn(int col) {
        int colLength = cols.length;
        if (col < colLength) {
            return cols[col];
        } else {
            return null;
        }
    }

    /**
   * 获取报表当前行数.
   * 
   * @return 返回行数.
   */
    public int getRowCount() {
        return rows.length;
    }

    /**
   * 获取报表列数.
   * 
   * @return 返回列数.
   */
    public int getColumnCount() {
        return cols.length;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (int row = 0; row < rows.length; row++) {
            for (int col = 0; col < cols.length; col++) {
                buffer.append(rows[row][col] == null ? "0 " : "1 ");
            }
            buffer.append("+ ");
            for (int col = 0; col < cols.length; col++) {
                buffer.append(points[row][col] != null ? "1 " : "0 ");
            }
            buffer.append(System.getProperty("line.separator"));
        }
        return buffer.toString();
    }

    protected final boolean printBody(ReportContext context, int x, int y, int width, int height) {
        Device device = context.getDevice();
        GC gc = context.getGraphicsContext();
        ReportPageData pageData = getPageData();
        Font oldPageFont = gc.getFont();
        Color oldPageBackground = gc.getBackground();
        Color oldPageForeground = gc.getForeground();
        Font newPageFont = pageData.getFont();
        Color newPageBackground = pageData.getBackground();
        Color newPageForeground = pageData.getForeground();
        if (newPageBackground != null) {
            newPageBackground = new Color(device, newPageBackground.getRGB());
            gc.setBackground(newPageBackground);
            gc.fillRectangle(x, y, width, height);
        }
        if (newPageForeground != null) {
            newPageForeground = new Color(device, newPageForeground.getRGB());
            gc.setForeground(newPageForeground);
        }
        if (newPageFont != null) {
            newPageFont = new Font(device, newPageFont.getFontData());
            gc.setFont(newPageFont);
        }
        try {
            int cellSpacingPixels = ReportUtil.computeWidth(context.getDevice(), pageData.getCellSpacing());
            gc.setLineWidth(cellSpacingPixels);
            int tableWidth = 0;
            for (IReportColumn col : cols) {
                tableWidth += col.getWidth(context);
            }
            int colX = x;
            int colY = y;
            if (pageData.isHeaderVisible()) {
                int maxColHeight = 0;
                for (IReportColumn col : cols) {
                    int colHeight = col.getHeight(context);
                    if (height > colHeight) {
                        maxColHeight = colHeight;
                    }
                }
                for (IReportColumn col : cols) {
                    int colWidth = (int) Math.floor(width * (double) col.getWidth(context) / tableWidth);
                    ReportCellData cellData = col.getCellData();
                    int colSeparatorX1 = colX;
                    int colSeparatorY1 = colY;
                    int colSeparatorX2 = colSeparatorX1;
                    int colSeparatorY2 = colSeparatorY1 + maxColHeight;
                    int colSeparatorX3 = colSeparatorX1 + colWidth;
                    int colSeparatorY3 = colSeparatorY1;
                    int colSeparatorX4 = colSeparatorX2 + colWidth;
                    int colSeparatorY4 = colSeparatorY2;
                    int rowSeparatorX1 = colX;
                    int rowSeparatorY1 = colY;
                    int rowSeparatorX2 = colX + colWidth;
                    int rowSeparatorY2 = colY;
                    int rowSeparatorX3 = colX;
                    int rowSeparatorY3 = colY + maxColHeight;
                    int rowSeparatorX4 = colX + colWidth;
                    int rowSeparatorY4 = colY + maxColHeight;
                    Font oldColumnFont = gc.getFont();
                    Color oldColumnBackground = gc.getBackground();
                    Color oldColumnForeground = gc.getForeground();
                    Font newColumnFont = cellData.getFont();
                    Color newColumnBackground = cellData.getBackground();
                    Color newColumnForeground = cellData.getForeground();
                    if (newColumnFont != null) {
                        newColumnFont = new Font(device, newColumnFont.getFontData());
                        gc.setFont(newColumnFont);
                    }
                    if (newColumnBackground != null) {
                        newColumnBackground = new Color(device, newColumnBackground.getRGB());
                        gc.setBackground(newColumnBackground);
                        gc.fillRectangle(colX, colY, colWidth, maxColHeight);
                    }
                    if (newColumnForeground != null) {
                        newColumnForeground = new Color(device, newColumnForeground.getRGB());
                        gc.setForeground(newColumnForeground);
                    }
                    if (pageData.isLineVisible()) {
                        gc.drawLine(colSeparatorX1, colSeparatorY1, colSeparatorX2, colSeparatorY2);
                        gc.drawLine(colSeparatorX3, colSeparatorY3, colSeparatorX4, colSeparatorY4);
                        gc.drawLine(rowSeparatorX1, rowSeparatorY1, rowSeparatorX2, rowSeparatorY2);
                        gc.drawLine(rowSeparatorX3, rowSeparatorY3, rowSeparatorX4, rowSeparatorY4);
                    } else {
                        if (cellData.getLeftBorderVisible()) {
                            int oldBorderSize = gc.getLineWidth();
                            Color oldBorderColor = gc.getForeground();
                            Color newBorderColor = cellData.getLeftBorderColor();
                            if (cellData.getLeftBorderSize() > 0) {
                                gc.setLineWidth(cellData.getLeftBorderSize());
                            }
                            if (newBorderColor != null) {
                                newBorderColor = new Color(device, newBorderColor.getRGB());
                                gc.setForeground(newBorderColor);
                            }
                            gc.drawLine(colSeparatorX1, colSeparatorY1, colSeparatorX2, colSeparatorY2);
                            if (cellData.getLeftBorderSize() > 0) {
                                gc.setLineWidth(oldBorderSize);
                            }
                            if (newBorderColor != null) {
                                gc.setForeground(oldBorderColor);
                                newBorderColor.dispose();
                            }
                        }
                        if (cellData.getRightBorderVisible()) {
                            int oldBorderSize = gc.getLineWidth();
                            Color oldBorderColor = gc.getForeground();
                            Color newBorderColor = cellData.getRightBorderColor();
                            if (cellData.getRightBorderSize() > 0) {
                                gc.setLineWidth(cellData.getRightBorderSize());
                            }
                            if (newBorderColor != null) {
                                newBorderColor = new Color(device, newBorderColor.getRGB());
                                gc.setForeground(newBorderColor);
                            }
                            gc.drawLine(colSeparatorX3, colSeparatorY3, colSeparatorX4, colSeparatorY4);
                            if (cellData.getRightBorderSize() > 0) {
                                gc.setLineWidth(oldBorderSize);
                            }
                            if (newBorderColor != null) {
                                gc.setForeground(oldBorderColor);
                                newBorderColor.dispose();
                            }
                        }
                        if (cellData.getTopBorderVisible()) {
                            int oldBorderSize = gc.getLineWidth();
                            Color oldBorderColor = gc.getForeground();
                            Color newBorderColor = cellData.getTopBorderColor();
                            if (cellData.getTopBorderSize() > 0) {
                                gc.setLineWidth(cellData.getTopBorderSize());
                            }
                            if (newBorderColor != null) {
                                newBorderColor = new Color(device, newBorderColor.getRGB());
                                gc.setForeground(newBorderColor);
                            }
                            gc.drawLine(rowSeparatorX1, rowSeparatorY1, rowSeparatorX2, rowSeparatorY2);
                            if (cellData.getTopBorderSize() > 0) {
                                gc.setLineWidth(oldBorderSize);
                            }
                            if (newBorderColor != null) {
                                gc.setForeground(oldBorderColor);
                                newBorderColor.dispose();
                            }
                        }
                        if (cellData.getBottomBorderVisible()) {
                            int oldBorderSize = gc.getLineWidth();
                            Color oldBorderColor = gc.getForeground();
                            Color newBorderColor = cellData.getBottomBorderColor();
                            if (cellData.getBottomBorderSize() > 0) {
                                gc.setLineWidth(cellData.getBottomBorderSize());
                            }
                            if (newBorderColor != null) {
                                newBorderColor = new Color(device, newBorderColor.getRGB());
                                gc.setForeground(newBorderColor);
                            }
                            gc.drawLine(rowSeparatorX3, rowSeparatorY3, rowSeparatorX4, rowSeparatorY4);
                            if (cellData.getBottomBorderSize() > 0) {
                                gc.setLineWidth(oldBorderSize);
                            }
                            if (newBorderColor != null) {
                                gc.setForeground(oldBorderColor);
                                newBorderColor.dispose();
                            }
                        }
                    }
                    col.output(context, colX, colY, colWidth, maxColHeight);
                    if (newColumnFont != null) {
                        gc.setFont(oldColumnFont);
                        newColumnFont.dispose();
                    }
                    if (newColumnBackground != null) {
                        gc.setBackground(oldColumnBackground);
                        newColumnBackground.dispose();
                    }
                    if (newColumnForeground != null) {
                        gc.setForeground(oldColumnForeground);
                        newColumnForeground.dispose();
                    }
                    colX += colWidth;
                }
                colY += maxColHeight;
            }
            for (int rowX = x, rowY = colY; row < getRowCount(); row++) {
                int maxRowHeight = 0;
                for (int col = 0; col < getColumnCount(); col++) {
                    IReportCell cell = getCell(row, col, false);
                    if (cell != null) {
                        int rowHeight = cell.getHeight(context);
                        if (rowHeight > maxRowHeight) {
                            if (cell.getVerticalSpan() <= 1) {
                                maxRowHeight = rowHeight;
                            }
                        }
                    }
                }
                for (int col = 0; col < getColumnCount(); col++) {
                    IReportCell cell = getCell(row, col, false);
                    if (cell != null) {
                        int vSpan = cell.getVerticalSpan();
                        if (vSpan < 1) {
                            vSpan = 1;
                        }
                        if (rowY - y + maxRowHeight * vSpan > height) {
                            return true;
                        }
                    }
                }
                rowX = x;
                for (int col = 0; col < getColumnCount(); col++) {
                    IReportColumn cellCol = getColumn(col);
                    if (cellCol != null) {
                        int colWidth = (int) (width * ((double) cellCol.getWidth(context) / tableWidth));
                        IReportCell cell = getCell(row, col, false);
                        if (cell != null) {
                            int hSpan = cell.getHorizontalSpan() > 1 ? cell.getHorizontalSpan() : 1;
                            int vSpan = cell.getVerticalSpan() > 1 ? cell.getVerticalSpan() : 1;
                            int rowWidth = colWidth * hSpan;
                            int rowHeight = maxRowHeight * vSpan;
                            ReportCellData cellData = cell.getCellData();
                            int colSeparatorX1 = rowX;
                            int colSeparatorY1 = rowY;
                            int colSeparatorX2 = colSeparatorX1;
                            int colSeparatorY2 = colSeparatorY1 + (maxRowHeight) * vSpan;
                            int colSeparatorX3 = colSeparatorX1 + colWidth;
                            int colSeparatorY3 = colSeparatorY1;
                            int colSeparatorX4 = colSeparatorX2 + colWidth;
                            int colSeparatorY4 = colSeparatorY2;
                            for (int c = 1; c < hSpan; c++) {
                                IReportColumn sepCol = getColumn(c + col);
                                if (sepCol != null) {
                                    int cWidth = (int) (width * ((double) sepCol.getWidth(context) / tableWidth));
                                    colSeparatorX3 = colSeparatorX4 += cWidth;
                                }
                            }
                            int rowSeparatorX1 = rowX;
                            int rowSeparatorY1 = rowY;
                            int rowSeparatorX2 = rowX + colWidth;
                            int rowSeparatorY2 = rowY;
                            for (int c = 1; c < hSpan; c++) {
                                IReportColumn sepCol = getColumn(c + col);
                                if (sepCol != null) {
                                    int cWidth = (int) (width * ((double) sepCol.getWidth(context) / tableWidth));
                                    rowSeparatorX2 += cWidth;
                                }
                            }
                            int rowSeparatorX3 = rowX;
                            int rowSeparatorY3 = rowY + maxRowHeight * vSpan;
                            int rowSeparatorX4 = rowX + colWidth;
                            int rowSeparatorY4 = rowY + maxRowHeight * vSpan;
                            for (int c = 1; c < hSpan; c++) {
                                IReportColumn sepCol = getColumn(c + col);
                                if (sepCol != null) {
                                    int cWidth = (int) (width * ((double) sepCol.getWidth(context) / tableWidth));
                                    rowSeparatorX4 += cWidth;
                                }
                            }
                            Font oldCellFont = gc.getFont();
                            Color oldCellBackground = gc.getBackground();
                            Color oldCellForeground = gc.getForeground();
                            Font newCellFont = cellData.getFont();
                            Color newCellBackground = cellData.getBackground();
                            Color newCellForeground = cellData.getForeground();
                            if (newCellFont != null) {
                                newCellFont = new Font(device, newCellFont.getFontData());
                                gc.setFont(newCellFont);
                            }
                            if (newCellBackground != null) {
                                newCellBackground = new Color(device, newCellBackground.getRGB());
                                gc.setBackground(newCellBackground);
                                gc.fillRectangle(rowX, rowY, colSeparatorX3 - colSeparatorX1, (maxRowHeight) * vSpan);
                            }
                            if (newCellForeground != null) {
                                newCellForeground = new Color(device, newCellForeground.getRGB());
                                gc.setForeground(newCellForeground);
                            }
                            if (pageData.isLineVisible()) {
                                gc.drawLine(colSeparatorX1, colSeparatorY1, colSeparatorX2, colSeparatorY2);
                                gc.drawLine(colSeparatorX3, colSeparatorY3, colSeparatorX4, colSeparatorY4);
                                gc.drawLine(rowSeparatorX1, rowSeparatorY1, rowSeparatorX2, rowSeparatorY2);
                                gc.drawLine(rowSeparatorX3, rowSeparatorY3, rowSeparatorX4, rowSeparatorY4);
                            } else {
                                if (cellData.getLeftBorderVisible()) {
                                    int oldBorderSize = gc.getLineWidth();
                                    Color oldBorderColor = gc.getForeground();
                                    Color newBorderColor = cellData.getLeftBorderColor();
                                    if (cellData.getLeftBorderSize() > 0) {
                                        gc.setLineWidth(cellData.getLeftBorderSize());
                                    }
                                    if (newBorderColor != null) {
                                        newBorderColor = new Color(device, newBorderColor.getRGB());
                                        gc.setForeground(newBorderColor);
                                    }
                                    gc.drawLine(colSeparatorX1, colSeparatorY1, colSeparatorX2, colSeparatorY2);
                                    if (cellData.getLeftBorderSize() > 0) {
                                        gc.setLineWidth(oldBorderSize);
                                    }
                                    if (newBorderColor != null) {
                                        gc.setForeground(oldBorderColor);
                                        newBorderColor.dispose();
                                    }
                                }
                                if (cellData.getRightBorderVisible()) {
                                    int oldBorderSize = gc.getLineWidth();
                                    Color oldBorderColor = gc.getForeground();
                                    Color newBorderColor = cellData.getRightBorderColor();
                                    if (cellData.getRightBorderSize() > 0) {
                                        gc.setLineWidth(cellData.getRightBorderSize());
                                    }
                                    if (newBorderColor != null) {
                                        newBorderColor = new Color(device, newBorderColor.getRGB());
                                        gc.setForeground(newBorderColor);
                                    }
                                    gc.drawLine(colSeparatorX3, colSeparatorY3, colSeparatorX4, colSeparatorY4);
                                    if (cellData.getRightBorderSize() > 0) {
                                        gc.setLineWidth(oldBorderSize);
                                    }
                                    if (newBorderColor != null) {
                                        gc.setForeground(oldBorderColor);
                                        newBorderColor.dispose();
                                    }
                                }
                                if (cellData.getTopBorderVisible()) {
                                    int oldBorderSize = gc.getLineWidth();
                                    Color oldBorderColor = gc.getForeground();
                                    Color newBorderColor = cellData.getTopBorderColor();
                                    if (cellData.getTopBorderSize() > 0) {
                                        gc.setLineWidth(cellData.getTopBorderSize());
                                    }
                                    if (newBorderColor != null) {
                                        newBorderColor = new Color(device, newBorderColor.getRGB());
                                        gc.setForeground(newBorderColor);
                                    }
                                    gc.drawLine(rowSeparatorX1, rowSeparatorY1, rowSeparatorX2, rowSeparatorY2);
                                    if (cellData.getTopBorderSize() > 0) {
                                        gc.setLineWidth(oldBorderSize);
                                    }
                                    if (newBorderColor != null) {
                                        gc.setForeground(oldBorderColor);
                                        newBorderColor.dispose();
                                    }
                                }
                                if (cellData.getBottomBorderVisible()) {
                                    int oldBorderSize = gc.getLineWidth();
                                    Color oldBorderColor = gc.getForeground();
                                    Color newBorderColor = cellData.getBottomBorderColor();
                                    if (cellData.getBottomBorderSize() > 0) {
                                        gc.setLineWidth(cellData.getBottomBorderSize());
                                    }
                                    if (newBorderColor != null) {
                                        newBorderColor = new Color(device, newBorderColor.getRGB());
                                        gc.setForeground(newBorderColor);
                                    }
                                    gc.drawLine(rowSeparatorX3, rowSeparatorY3, rowSeparatorX4, rowSeparatorY4);
                                    if (cellData.getBottomBorderSize() > 0) {
                                        gc.setLineWidth(oldBorderSize);
                                    }
                                    if (newBorderColor != null) {
                                        gc.setForeground(oldBorderColor);
                                        newBorderColor.dispose();
                                    }
                                }
                            }
                            cell.output(context, rowX, rowY, rowWidth, rowHeight);
                            if (newCellFont != null) {
                                gc.setFont(oldCellFont);
                                newCellFont.dispose();
                            }
                            if (newCellBackground != null) {
                                gc.setBackground(oldCellBackground);
                                newCellBackground.dispose();
                            }
                            if (newCellForeground != null) {
                                gc.setForeground(oldCellForeground);
                                newCellForeground.dispose();
                            }
                        }
                        rowX += colWidth;
                    }
                }
                rowY += maxRowHeight;
            }
            if (row == getRowCount()) {
                row = 0;
                return false;
            } else {
                return true;
            }
        } finally {
            if (newPageFont != null) {
                gc.setFont(oldPageFont);
                newPageFont.dispose();
            }
            if (newPageBackground != null) {
                gc.setBackground(oldPageBackground);
                newPageBackground.dispose();
            }
            if (newPageForeground != null) {
                gc.setForeground(oldPageForeground);
                newPageForeground.dispose();
            }
        }
    }

    protected final boolean ignoreBody(ReportContext context, int x, int y, int width, int height) {
        ReportPageData pageData = getPageData();
        int maxColHeight = 0;
        if (pageData.isHeaderVisible()) {
            for (IReportColumn col : cols) {
                int colHeight = col.getHeight(context);
                if (height > colHeight) {
                    maxColHeight = colHeight;
                }
            }
        }
        for (int rowY = y + maxColHeight; row < getRowCount(); row++) {
            int maxRowHeight = 0;
            for (int col = 0; col < getColumnCount(); col++) {
                IReportCell cell = getCell(row, col, false);
                if (cell != null) {
                    int rowHeight = cell.getHeight(context);
                    if (rowHeight > maxRowHeight) {
                        if (cell.getVerticalSpan() <= 1) {
                            maxRowHeight = rowHeight;
                        }
                    }
                }
            }
            for (int col = 0; col < getColumnCount(); col++) {
                IReportCell cell = getCell(row, col, false);
                if (cell != null) {
                    int vSpan = cell.getVerticalSpan();
                    if (vSpan < 1) {
                        vSpan = 1;
                    }
                    if (rowY - y + maxRowHeight * vSpan > height) {
                        return true;
                    }
                }
            }
            rowY += maxRowHeight;
        }
        if (row == getRowCount()) {
            row = 0;
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected final int countPage(ReportContext context, int x, int y, int width, int height) {
        int pageCount;
        ReportPageData pageData = getPageData();
        Device device = context.getDevice();
        GC gc = context.getGraphicsContext();
        Font oldPageFont = gc.getFont();
        Font newPageFont = pageData.getFont();
        if (newPageFont != null) {
            newPageFont = new Font(device, newPageFont.getFontData());
            gc.setFont(newPageFont);
        }
        int maxColHeight = 0;
        if (pageData.isHeaderVisible()) {
            for (IReportColumn col : cols) {
                int colHeight = col.getHeight(context);
                if (height > colHeight) {
                    maxColHeight = colHeight;
                }
            }
        }
        int row = 0;
        loopPage: for (pageCount = 1; row < getRowCount(); pageCount++) {
            for (int rowY = y + maxColHeight; row < getRowCount(); row++) {
                int maxRowHeight = 0;
                for (int col = 0; col < getColumnCount(); col++) {
                    IReportCell cell = getCell(row, col, false);
                    if (cell != null) {
                        int rowHeight = cell.getHeight(context);
                        if (rowHeight > maxRowHeight) {
                            if (cell.getVerticalSpan() <= 1) {
                                maxRowHeight = rowHeight;
                            }
                        }
                    }
                }
                for (int col = 0; col < getColumnCount(); col++) {
                    IReportCell cell = getCell(row, col, false);
                    if (cell != null) {
                        int vSpan = cell.getVerticalSpan();
                        if (vSpan < 1) {
                            vSpan = 1;
                        }
                        if (rowY - y + maxRowHeight * vSpan > height) {
                            continue loopPage;
                        }
                    }
                }
                rowY += maxRowHeight;
            }
            if (row == getRowCount()) {
                break;
            } else {
                continue;
            }
        }
        if (newPageFont != null) {
            gc.setFont(oldPageFont);
            newPageFont.dispose();
        }
        return pageCount;
    }
}
