package org.awelements.table.web;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.awelements.table.Column;
import org.awelements.table.Filter;
import org.awelements.table.ISetValueCommand;
import org.awelements.table.TableCommand;
import org.awelements.table.TableController;
import org.awelements.table.TableUtils;

public class WebTableControllerAdapter {

    private TableController mTableController;

    private HttpServletRequest mRequest;

    private HttpServletResponse mResponse;

    public WebTableControllerAdapter(TableController tableController, HttpServletRequest request, HttpServletResponse response) {
        mTableController = tableController;
        mResponse = response;
        mRequest = request;
    }

    private String getParameter(String name) {
        return mRequest.getParameter(name);
    }

    private String getParameter(String name, String defaultValue) {
        final String value = mRequest.getParameter(name);
        return value != null ? value : defaultValue;
    }

    private Integer getIntegerParameter(String name) {
        return TableUtils.toInteger(getParameter(name));
    }

    private int getIntegerParameter(String name, int defaultValue) {
        final Integer value = TableUtils.toInteger(getParameter(name));
        return value != null ? value : defaultValue;
    }

    private Map getParameterMap() {
        return mRequest.getParameterMap();
    }

    private Writer getWriter() {
        try {
            return mResponse.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void renderTable() {
        mTableController.render(getWriter());
    }

    public void refresh() {
        renderTable();
    }

    public void sort() {
        final String colIdx = getParameter("column");
        mTableController.changeSortDirection(Integer.parseInt(colIdx));
        renderTable();
    }

    public void reset() {
        mTableController.removeSort();
        renderTable();
    }

    public void check() {
        final String rowIdx = getParameter("rowIndex");
        mTableController.invertSelection(Integer.parseInt(rowIdx));
        renderTable();
    }

    public void checkAllOrNone() {
        mTableController.selectAllOrNoneVisible();
        renderTable();
    }

    public void firstPage() {
        mTableController.setPageIndex(0);
        renderTable();
    }

    public void lastPage() {
        mTableController.setPageIndex(mTableController.getLastPageIndex());
        renderTable();
    }

    public void prevPage() {
        mTableController.prevPage();
        renderTable();
    }

    public void nextPage() {
        mTableController.nextPage();
        renderTable();
    }

    public void setPage() {
        final String pageIndex = getParameter("pageIndex");
        System.out.println("setPage(" + pageIndex + ")");
        mTableController.setPageIndex(Integer.parseInt(pageIndex));
        renderTable();
    }

    public void toggleTableFilter() {
        final String filterName = getParameter("filterName");
        try {
            if (mTableController.isTableFilterOpen(filterName)) {
                closeFilter();
            } else {
                final Filter filter = mTableController.openTableFilter(filterName);
                if (filter != null) {
                    mTableController.applyTableFilter(filterName, null);
                    mTableController.renderFilter(filter, getWriter());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openFilter() {
        final String columnIndex = getParameter("columnIndex");
        final String filterName = getParameter("filterName");
        try {
            Filter filter = null;
            if (columnIndex != null && columnIndex.trim().length() != 0) filter = mTableController.openColumnFilter(Integer.parseInt(columnIndex), filterName); else filter = mTableController.openTableFilter(filterName);
            if (filter != null) mTableController.renderFilter(filter, getWriter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void applyFilter() {
        final String filterName = getParameter("filterName");
        final String columnIndexStack = getParameter("columnIndexStack");
        if (columnIndexStack != null) {
            try {
                long s = (new Date()).getTime();
                mTableController.applyColumnFilter(parseColumnIndexStack(columnIndexStack), filterName, getParameterMap());
                System.out.println("filter application took " + (new Date().getTime() - s) / 1000.0 + " secs");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        renderTable();
    }

    public void applyTableFilter() {
        final String filterName = getParameter("filterName");
        try {
            long s = (new Date()).getTime();
            mTableController.applyTableFilter(filterName, getParameterMap());
            System.out.println("table filter application took " + (new Date().getTime() - s) / 1000.0 + " secs");
        } catch (Exception e) {
            e.printStackTrace();
        }
        renderTable();
    }

    public void executeTableCommand(HttpServletRequest request) {
        try {
            final String commandName = getParameter("command");
            final TableCommand tableCommand = mTableController.getTableCommand(commandName);
            if (tableCommand != null && tableCommand instanceof HttpServletTableCommand) {
                final HttpServletTableCommand httpServletTableCommand = (HttpServletTableCommand) tableCommand;
                initHttpServletTableCommand(httpServletTableCommand, request);
                httpServletTableCommand.execute();
            } else {
                System.err.println("Table '" + mTableController.getTable().getName() + "' has no command '" + commandName + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        renderTable();
    }

    private void initHttpServletTableCommand(HttpServletTableCommand httpServletTableCommand, HttpServletRequest request) {
        final Integer rowIdx = TableUtils.toInteger(getParameter("rowIndex"));
        Object rowObject = null;
        if (rowIdx != null) rowObject = mTableController.getRowObject(rowIdx);
        httpServletTableCommand.setTableController(mTableController);
        httpServletTableCommand.setRowIndex(rowIdx);
        httpServletTableCommand.setRowObject(rowObject);
        httpServletTableCommand.setRequest(request);
    }

    public void executeSetValueCommand() {
        final Integer rowIndex = getIntegerParameter("rowIndex");
        final Integer columnIndex = getIntegerParameter("columnIndex");
        final String newValue = getParameter("newValue");
        String columnName = null;
        try {
            final Column column = mTableController.getTable().getColumn(columnIndex);
            columnName = column.getName();
            final Object rowObject = mTableController.getRowObject(rowIndex);
            if (rowObject != null) {
                final ISetValueCommand setValueCommand = column.getSetValueCommand();
                if (setValueCommand != null) {
                    setValueCommand.setValue(rowIndex, rowObject, newValue, mRequest);
                } else {
                    TableUtils.setProperty(rowObject, columnName, columnIndex, newValue);
                }
                mTableController.refreshAll();
            }
        } catch (Exception e) {
            System.err.println("cannot set value " + columnName + "='" + newValue + "'");
        }
        renderTable();
    }

    public void executeDownloadCommand(HttpServletRequest request) {
        try {
            final String commandName = getParameter("command");
            final TableCommand tableCommand = mTableController.getTableCommand(commandName);
            if (tableCommand != null && tableCommand instanceof HttpServletDownloadCommand) {
                final HttpServletDownloadCommand httpServletDownloadCommand = (HttpServletDownloadCommand) tableCommand;
                initHttpServletTableCommand(httpServletDownloadCommand, request);
                httpServletDownloadCommand.setResponse(mResponse);
                httpServletDownloadCommand.execute();
            } else {
                System.err.println("Table '" + mTableController.getTable().getName() + "' has no command '" + commandName + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeRenderTableInfoBox() {
        try {
            final String name = getParameter("name");
            final Integer rowIndex = getIntegerParameter("rowIndex");
            final Object rowObject = mTableController.getRowObject(rowIndex);
            final TableHtmlInfoBox infoBox = mTableController.getTable().getInfoBox(name);
            new HtmlInfoBoxRenderer().renderTableHtmlInfoBox(infoBox, rowIndex, rowObject, mRequest, mResponse.getWriter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int[] parseColumnIndexStack(String columnIndexStack) {
        final String[] tok = columnIndexStack.split("\\.");
        final int[] result = new int[tok.length];
        for (int i = 0; i < tok.length; ++i) result[i] = Integer.parseInt(tok[i]);
        return result;
    }

    public void closeFilter() {
        final String filterName = getParameter("filterName");
        final String columnIndex = getParameter("columnIndex");
        if (columnIndex != null && columnIndex.trim().length() != 0) mTableController.removeColumnFilter(Integer.parseInt(columnIndex), filterName); else mTableController.removeTableFilter(filterName);
        renderTable();
    }

    public void resetFilter() {
        final String filterName = getParameter("filterName");
        final String columnIndexStack = getParameter("columnIndexStack");
        if (columnIndexStack != null) mTableController.resetColumnFilter(parseColumnIndexStack(columnIndexStack), filterName); else mTableController.resetTableFilter(filterName);
        renderTable();
    }

    public void expand() {
        final String rowIdx = getParameter("rowIndex");
        mTableController.expand(Integer.parseInt(rowIdx));
        renderTable();
    }

    public void collapse() {
        final String rowIdx = getParameter("rowIndex");
        mTableController.collapse(Integer.parseInt(rowIdx));
        renderTable();
    }

    public void setRowsPerPage() {
        final String rowsPerPage = getParameter("rowsPerPage");
        mTableController.setRowsPerPage(Integer.parseInt(rowsPerPage));
        renderTable();
    }

    public void minimizeColumn() {
        final String columnIndex = getParameter("columnIndex");
        mTableController.minimizeColumn(Integer.parseInt(columnIndex));
        renderTable();
    }

    public void maximizeColumn() {
        final String columnIndex = getParameter("columnIndex");
        mTableController.maximizeColumn(Integer.parseInt(columnIndex));
        renderTable();
    }

    public void setColumnVisibility() {
        final String columnIndexes = getParameter("columns");
        final String visible = getParameter("visible");
        final int[] indexes = TableUtils.splitInteters(columnIndexes, ",");
        mTableController.setColumnsVisible(indexes, TableUtils.isTrue(visible));
        renderTable();
    }
}
