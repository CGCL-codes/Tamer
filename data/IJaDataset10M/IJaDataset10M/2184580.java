package org.jmesa.limit;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Used to figure out the row information so the proper page of information can be retrieved.
 * </p>
 *
 * @since 2.0
 * @author Jeff Johnston
 */
public class RowSelect implements Serializable {

    private int page;

    private int maxRows;

    private int rowEnd;

    private int rowStart;

    private int totalRows;

    public RowSelect(int page, int maxRows, int totalRows) {
        this.maxRows = maxRows;
        this.totalRows = totalRows;
        init(page);
    }

    /**
     * @return The current page that is being displayed.
     */
    public int getPage() {
        return page;
    }

    /**
     * Set the page and recalculate the row information.
     *
     * @param page The page that should be displayed.
     */
    public void setPage(int page) {
        init(page);
    }

    /**
     * @return The first row to display.
     */
    public int getRowStart() {
        return rowStart;
    }

    /**
     * @return The last row to display.
     */
    public int getRowEnd() {
        return rowEnd;
    }

    /**
     * @return The maximum possible rows that could be displayed on one page.
     */
    public int getMaxRows() {
        return maxRows;
    }

    /**
     * Set the max rows and recalculate the row information.
     *
     * @param maxRows The maxRows that should be displayed.
     */
    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
        init(page);
    }

    /**
     * @return The total possible rows, including those that are paginated.
     */
    public int getTotalRows() {
        return totalRows;
    }

    /**
     * Set the total rows and recalculate the row information.
     *
     * @param totalRows The totalRows that should be displayed.
     */
    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
        init(page);
    }

    private void init(int page) {
        page = getValidPage(page, maxRows, totalRows);
        int rs = (page - 1) * maxRows;
        int re = rs + maxRows;
        if (re > totalRows) {
            re = totalRows;
        }
        this.page = page;
        this.rowStart = rs;
        this.rowEnd = re;
    }

    /**
     * The page returned that is not greater than the pages that can display.
     */
    private int getValidPage(int page, int maxRows, int totalRows) {
        while (!isValidPage(page, maxRows, totalRows)) {
            --page;
        }
        return page;
    }

    /**
     * Testing that the page returned is not greater than the pages that are able to be displayed.
     * The problem arises if using the state feature and rows are deleted.
     */
    private boolean isValidPage(int page, int maxRows, int totalRows) {
        if (page == 1) {
            return true;
        }
        int rs = (page - 1) * maxRows;
        int re = rs + maxRows;
        if (re > totalRows) {
            re = totalRows;
        }
        return re > rs;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("page", page);
        builder.append("maxRows", maxRows);
        builder.append("rowEnd", rowEnd);
        builder.append("rowStart", rowStart);
        builder.append("totalRows", totalRows);
        return builder.toString();
    }
}
