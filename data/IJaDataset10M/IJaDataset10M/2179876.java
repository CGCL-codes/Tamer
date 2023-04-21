package net.nothinginteresting.tablelayout.layout.math;

import java.util.List;
import net.nothinginteresting.tablelayout.layout.ITableLayoutData;
import net.nothinginteresting.tablelayout.layout.IWidgetAdapter;
import net.nothinginteresting.tablelayout.layout.TableLayout.HORIZONTAL_ALIGN;
import net.nothinginteresting.tablelayout.layout.TableLayout.VERTICAL_ALIGN;
import net.nothinginteresting.tablelayout.logging.Logger;

/**
 * @author Dmitri Gorbenko
 * 
 */
public class CellCalculator {

    private final Rectangle[][] cellRectangles;

    private final Rectangle clientArea;

    /**
	 * @param cellRectangles
	 * @param clientArea
	 */
    public CellCalculator(Rectangle[][] cellRectangles, Rectangle clientArea) {
        this.cellRectangles = cellRectangles;
        this.clientArea = clientArea;
    }

    public Rectangle getChildRectangle(IWidgetAdapter widgetAdapter) {
        ITableLayoutData tableLayoutData = widgetAdapter.getLayoutData();
        Rectangle cellRectangle = new Rectangle(getCellX(clientArea, tableLayoutData), getCellY(clientArea, tableLayoutData), Math.max(0, getCellWidth(tableLayoutData)), Math.max(0, getCellHeight(tableLayoutData)));
        return getWidgetRectangle(widgetAdapter, cellRectangle);
    }

    /**
	 * @param cellRectangle
	 * @param tableLayoutData
	 * @return
	 */
    private Rectangle getWidgetRectangle(IWidgetAdapter widgetAdapter, Rectangle cellRectangle) {
        ITableLayoutData tableLayoutData = widgetAdapter.getLayoutData();
        Rectangle result = new Rectangle(0, 0, 0, 0);
        Point widgetSize = new Point(0, 0);
        if (tableLayoutData.getHorizontalAlign() != HORIZONTAL_ALIGN.FILL || tableLayoutData.getVerticalAlign() != VERTICAL_ALIGN.FILL) {
            widgetSize = widgetAdapter.getSize();
        }
        Point hValues = calculateWidgetAlignValuesH(tableLayoutData.getHorizontalAlign(), cellRectangle.getX(), cellRectangle.getWidth(), widgetSize.getX());
        result.setX(hValues.getX());
        result.setWidth(hValues.getY());
        Point vValues = calculateWidgetAlignValuesV(tableLayoutData.getVerticalAlign(), cellRectangle.getY(), cellRectangle.getHeight(), widgetSize.getY());
        result.setY(vValues.getX());
        result.setHeight(vValues.getY());
        return result;
    }

    private Point calculateWidgetAlignValuesH(HORIZONTAL_ALIGN horizontal_align, int cellFrom, int cellLength, int widgetLength) {
        int from = 0;
        int length = 0;
        if (horizontal_align == HORIZONTAL_ALIGN.FILL) {
            from = cellFrom;
            length = cellLength;
        } else {
            if (horizontal_align == HORIZONTAL_ALIGN.LEFT) {
                length = Math.min(widgetLength, cellLength);
                from = cellFrom;
            }
            if (horizontal_align == HORIZONTAL_ALIGN.RIGHT) {
                length = Math.min(widgetLength, cellLength);
                from = cellFrom + cellLength - length;
            }
            if (horizontal_align == HORIZONTAL_ALIGN.CENTER) {
                length = Math.min(widgetLength, cellLength);
                from = cellFrom + (cellLength - length) / 2;
            }
        }
        return new Point(from, length);
    }

    private Point calculateWidgetAlignValuesV(VERTICAL_ALIGN align, int cellFrom, int cellLength, int widgetLength) {
        int from = 0;
        int length = 0;
        if (align == VERTICAL_ALIGN.FILL) {
            from = cellFrom;
            length = cellLength;
        } else {
            if (align == VERTICAL_ALIGN.TOP) {
                length = Math.min(widgetLength, cellLength);
                from = cellFrom;
            }
            if (align == VERTICAL_ALIGN.BOTTOM) {
                length = Math.min(widgetLength, cellLength);
                from = cellFrom + cellLength - length;
            }
            if (align == VERTICAL_ALIGN.CENTER) {
                length = Math.min(widgetLength, cellLength);
                from = cellFrom + (cellLength - length) / 2;
            }
        }
        return new Point(from, length);
    }

    private int getCellHeight(ITableLayoutData tableLayoutData) {
        int result = 0;
        Logger.debug("cellRectangles.length = " + cellRectangles.length);
        Logger.debug("data.col = " + tableLayoutData.getCol() + " data.row = " + tableLayoutData.getRow());
        for (int row = tableLayoutData.getRow(); row < tableLayoutData.getRow() + tableLayoutData.getRowSpan(); row++) {
            Logger.debug("row = " + row + "cellRectangles.length.length = " + cellRectangles[tableLayoutData.getCol()].length);
            result += cellRectangles[tableLayoutData.getCol()][row].getHeight();
        }
        result = Math.max(0, result - tableLayoutData.getTopPadding() - tableLayoutData.getBottomPadding());
        return result;
    }

    private int getCellWidth(ITableLayoutData tableLayoutData) {
        int result = 0;
        for (int col = tableLayoutData.getCol(); col < tableLayoutData.getCol() + tableLayoutData.getColSpan(); col++) {
            result += cellRectangles[col][tableLayoutData.getRow()].getWidth();
        }
        result = Math.max(0, result - tableLayoutData.getLeftPadding() - tableLayoutData.getRightPadding());
        return result;
    }

    private int getCellY(Rectangle clientArea, ITableLayoutData tableLayoutData) {
        return clientArea.getY() + cellRectangles[tableLayoutData.getCol()][tableLayoutData.getRow()].getY() + tableLayoutData.getTopPadding();
    }

    private int getCellX(Rectangle clientArea, ITableLayoutData tableLayoutData) {
        return clientArea.getX() + cellRectangles[tableLayoutData.getCol()][tableLayoutData.getRow()].getX() + tableLayoutData.getLeftPadding();
    }

    /**
     * @param widgetAdapters
     */
    public void applyCalculations(List<IWidgetAdapter> widgetAdapters) {
        for (IWidgetAdapter widgetAdapter : widgetAdapters) {
            widgetAdapter.setRectangle(getChildRectangle(widgetAdapter));
        }
    }
}
