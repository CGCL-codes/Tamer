package org.sgodden.echo.ext20.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import nextapp.echo.app.Component;
import nextapp.echo.app.table.TableModel;
import org.sgodden.echo.ext20.componentgrid.ComponentGridPanel;
import org.sgodden.echo.ext20.grid.GridCellRenderer;
import org.sgodden.echo.ext20.grid.GridPanel;

/**
 * Adapts a swing {@link TableModel} to an ext {@link SimpleStore}.
 * @author sgodden
 *
 */
public class TableModelAdapter implements SimpleStore, Serializable {

    private static final long serialVersionUID = 1L;

    private Object[][] data;

    private Object[][] renderedData;

    private Integer id;

    private String[] fields;

    private String[] renderFunctions;

    public TableModelAdapter() {
    }

    /**
     * Constructs a new table model adapter.
     */
    public TableModelAdapter(GridPanel gridPanel) {
        TableModel tableModel = gridPanel.getModel();
        doConstruct(gridPanel.getPageOffset(), gridPanel.getPageSize(), tableModel, gridPanel.getGridCellRenderer(), gridPanel);
    }

    public TableModelAdapter(ComponentGridPanel gridPanel) {
        TableModel tableModel = gridPanel.getModel();
        doConstruct(gridPanel.getPageOffset(), gridPanel.getPageSize(), tableModel, gridPanel.getModelValueRenderer(), gridPanel);
    }

    public TableModelAdapter(TableModel tableModel, Component modelComponent, GridCellRenderer renderer, int offset, int pageSize) {
        doConstruct(offset, pageSize, tableModel, renderer, modelComponent);
    }

    private void doConstruct(int offset, int pageSize, TableModel tableModel, GridCellRenderer renderer, Component grid) {
        int rows = tableModel.getRowCount();
        if (pageSize > 0) rows = offset + pageSize < tableModel.getRowCount() ? pageSize : tableModel.getRowCount() - offset;
        int cols = tableModel.getColumnCount();
        data = new String[rows][cols];
        renderedData = new Integer[rows][cols];
        List<String> renderFunctions = new LinkedList<String>();
        for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
            Object[] row = data[rowIndex];
            Object[] renderedRow = renderedData[rowIndex];
            for (int colIndex = 0; colIndex < tableModel.getColumnCount(); colIndex++) {
                row[colIndex] = renderer.getModelValue(grid, tableModel.getValueAt(colIndex, rowIndex + offset), colIndex, rowIndex + offset);
                String renderedFunction = renderer.getClientSideValueRendererScript(grid, tableModel.getValueAt(colIndex, rowIndex + offset), colIndex, rowIndex + offset);
                if (!renderFunctions.contains(renderedFunction)) {
                    renderFunctions.add(renderedFunction);
                }
                renderedRow[colIndex] = Integer.valueOf(renderFunctions.indexOf(renderedFunction));
            }
        }
        this.renderFunctions = renderFunctions.toArray(new String[0]);
        makeFields(tableModel);
    }

    /**
     * Returns the raw model data for use on the client side.
     */
    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    /**
     * Returns the rendered model data for use in presenting
     * the data on the client side.
     * @return
     */
    public Object[][] getRenderedData() {
        return renderedData;
    }

    public void setRenderedData(Object[][] data) {
        this.renderedData = data;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    /**
     * Returns null - it is up to the application to map selected rows
     * to ids.
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSize() {
        return data.length;
    }

    /**
     * Extracts the field (column) information from the passed table model.
     * @param tableModel the table model.
     */
    private void makeFields(TableModel tableModel) {
        fields = new String[tableModel.getColumnCount()];
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            fields[i] = tableModel.getColumnName(i);
        }
    }

    public String[] getRenderFunctions() {
        return renderFunctions;
    }

    public void setRenderFunctions(String[] renderFunctions) {
        this.renderFunctions = renderFunctions;
    }
}
