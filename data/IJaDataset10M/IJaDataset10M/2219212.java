package net.dataforte.canyon.spi.echo2.components;

import java.util.ArrayList;
import java.util.List;
import net.dataforte.canyon.spi.echo2.model.DynaGridColumnModel;
import net.dataforte.canyon.spi.echo2.model.DynaGridModel;
import net.dataforte.canyon.spi.echo2.renderer.CellRenderer;
import net.dataforte.canyon.spi.echo2.renderer.FooterRenderer;
import net.dataforte.canyon.spi.echo2.renderer.HeaderRenderer;
import nextapp.echo2.app.Border;
import nextapp.echo2.app.Color;
import nextapp.echo2.app.Composite;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.list.ListSelectionModel;
import nextapp.echo2.app.table.TableColumnModel;
import nextapp.echo2.app.table.TableModel;
import echopointng.table.DefaultSortableTableModel;
import echopointng.table.SortableTable;

/**
 * DynaGrid is a dynamic grid object which uses annotations to describe its
 * columns
 * 
 * @author Tristan Tarrant
 * 
 * @param <T>
 */
public class DynaGrid<T extends Object> extends Composite {

    SortableTable table;

    DynaGridModel<T> model;

    DynaGridColumnModel<T> columnModel;

    HeaderRenderer headerRenderer;

    CellRenderer cellRenderer;

    FooterRenderer footerRenderer;

    DefaultSortableTableModel sortedModel;

    public DynaGrid() {
        columnModel = new DynaGridColumnModel<T>(this);
        model = new DynaGridModel<T>(columnModel);
        sortedModel = new DefaultSortableTableModel(model, columnModel);
        model.setSortedModel(sortedModel);
        table = new SortableTable(sortedModel);
        table.setAutoCreateColumnsFromModel(false);
        headerRenderer = new HeaderRenderer();
        cellRenderer = new CellRenderer();
        footerRenderer = new FooterRenderer();
        table.setDefaultHeaderRenderer(headerRenderer);
        table.setDefaultRenderer(cellRenderer);
        table.setDefaultFooterRenderer(footerRenderer);
        this.add(table);
    }

    public DynaGrid(Class<T> modelClass) {
        this();
        setModelClass(modelClass);
    }

    public void setModelClass(Class<T> modelClass) {
        columnModel.setModelClass(modelClass);
        table.setColumnModel(columnModel);
    }

    public String getHeaderStyle() {
        return headerRenderer.getCellStyleName();
    }

    public void setHeaderStyle(String headerStyle) {
        headerRenderer.setCellStyleName(headerStyle);
    }

    public String getFooterStyle() {
        return footerRenderer.getCellStyleName();
    }

    public void setFooterStyle(String footerStyle) {
        footerRenderer.setCellStyleName(footerStyle);
    }

    public String getCellStyle() {
        return cellRenderer.getCellStyleName();
    }

    public void setCellStyle(String cellStyle) {
        cellRenderer.setCellStyleName(cellStyle);
    }

    public void setBackground(Color newValue) {
        table.setBackground(newValue);
    }

    public Color getBackground() {
        return table.getBackground();
    }

    public void setBorder(Border newValue) {
        table.setBorder(newValue);
    }

    public Border getBorder() {
        return table.getBorder();
    }

    public void setHeightStretched(boolean newValue) {
        table.setHeightStretched(newValue);
    }

    public boolean isHeightStretched() {
        return table.isHeightStretched();
    }

    public void setResizeable(boolean newValue) {
        table.setResizeable(newValue);
    }

    public boolean isResizeable(boolean newValue) {
        return table.isResizeable();
    }

    public void setRolloverEnabled(boolean newValue) {
        table.setRolloverEnabled(newValue);
    }

    public boolean isRolloverEnabled() {
        return table.isRolloverEnabled();
    }

    public void setScrollable(boolean newValue) {
        table.setScrollable(newValue);
    }

    public boolean isScrollable() {
        return table.isScrollable();
    }

    public void setSelectionEnabled(boolean newValue) {
        table.setSelectionEnabled(newValue);
    }

    public boolean isSelectionEnabled() {
        return table.isSelectionEnabled();
    }

    public void setSelectionMode(int mode) {
        table.getSelectionModel().setSelectionMode(mode);
    }

    public int getSelectionMode() {
        return table.getSelectionModel().getSelectionMode();
    }

    public void setWidth(Extent newValue) {
        table.setWidth(newValue);
    }

    public Extent getWidth() {
        return table.getHeight();
    }

    public void setHeight(Extent newValue) {
        table.setHeight(newValue);
    }

    public Extent getHeight() {
        return table.getHeight();
    }

    public TableModel getModel() {
        return model;
    }

    public TableColumnModel getColumnModel() {
        return columnModel;
    }

    public void addActionListener(ActionListener l) {
        final ActionListener al = l;
        table.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ActionEvent ae = new ActionEvent(DynaGrid.this, e.getActionCommand());
                al.actionPerformed(ae);
            }
        });
    }

    public void removeActionListener(ActionListener l) {
    }

    public void setSelectionModel(ListSelectionModel newValue) {
        table.setSelectionModel(newValue);
    }

    public ListSelectionModel getSelectionModel() {
        return table.getSelectionModel();
    }

    public HeaderRenderer getHeaderRenderer() {
        return headerRenderer;
    }

    public void setHeaderRenderer(HeaderRenderer headerRenderer) {
        this.headerRenderer = headerRenderer;
    }

    public CellRenderer getCellRenderer() {
        return cellRenderer;
    }

    public void setCellRenderer(CellRenderer cellRenderer) {
        this.cellRenderer = cellRenderer;
    }

    public FooterRenderer getFooterRenderer() {
        return footerRenderer;
    }

    public void setFooterRenderer(FooterRenderer footerRenderer) {
        this.footerRenderer = footerRenderer;
    }

    /**
     * Returns the maximum selected index.
     *
     * @return the maximum selected index
     */
    public int getMaxSelectedIndex() {
        return getSelectionModel().getMaxSelectedIndex();
    }

    /**
     * Returns the minimum selected index.
     *
     * @return The minimum selected index
     */
    public int getMinSelectedIndex() {
        return getSelectionModel().getMinSelectedIndex();
    }

    public T getSelectedValue() {
        int selectedIndex = getMinSelectedIndex();
        return (selectedIndex < 0) ? null : model.getRow(selectedIndex);
    }

    public int[] getSelectedIndices() {
        ListSelectionModel selectionModel = table.getSelectionModel();
        int min = selectionModel.getMinSelectedIndex();
        if (min == -1) {
            return new int[0];
        }
        int selectionCount = 0;
        int max = selectionModel.getMaxSelectedIndex();
        int size = model.getRowCount();
        if (max >= size - 1) {
            max = size - 1;
        }
        for (int index = min; index <= max; ++index) {
            if (selectionModel.isSelectedIndex(index)) {
                ++selectionCount;
            }
        }
        int[] selectedIndices = new int[selectionCount];
        selectionCount = 0;
        for (int index = min; index <= max; ++index) {
            if (selectionModel.isSelectedIndex(index)) {
                selectedIndices[selectionCount] = index;
                ++selectionCount;
            }
        }
        return selectedIndices;
    }

    public List<T> getSelectedValues() {
        ListSelectionModel selectionModel = table.getSelectionModel();
        int min = selectionModel.getMinSelectedIndex();
        List<T> selectedValues = new ArrayList<T>();
        if (min == -1) {
            return selectedValues;
        }
        int max = selectionModel.getMaxSelectedIndex();
        int size = model.getRowCount();
        if (max >= size - 1) {
            max = size - 1;
        }
        for (int index = min; index <= max; ++index) {
            if (selectionModel.isSelectedIndex(index)) {
                selectedValues.add(model.getRow(index));
            }
        }
        return selectedValues;
    }
}
