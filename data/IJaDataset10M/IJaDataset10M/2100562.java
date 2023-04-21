package org.gocha.grid.rowheight;

import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.gocha.grid.IGridRow;

/**
 * Таблица с поддержкой изменения высоты строки
 * @author  gocha
 */
public class RowHeightTable extends javax.swing.JPanel {

    private RowHeader rowHeader = null;

    private class TableHeightCleaner implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
        }
    }

    private TableHeightCleaner cleaner = new TableHeightCleaner();

    private RowHeaderPanel mainPanel = null;

    private Dimension savedHeadersize = null;

    private boolean headerIsVisible = true;

    /** Конструктор */
    public RowHeightTable() {
        initComponents();
        mainPanel = new RowHeaderPanel();
        rowHeader = new RowHeader(myTable1, jScrollPane1);
        mainPanel.getContent().add(rowHeader);
        mainPanel.setJTable(myTable1);
        jSplitPane1.setLeftComponent(mainPanel);
    }

    public boolean isHeaderVisible() {
        return headerIsVisible;
    }

    public void setHeaderVisible(boolean visible) {
        if (headerIsVisible && !visible) {
            savedHeadersize = getJTable().getTableHeader().getPreferredSize();
        }
        if (!headerIsVisible && visible) {
            if (savedHeadersize != null) {
                getJTable().getTableHeader().setPreferredSize(savedHeadersize);
            } else {
                getJTable().getTableHeader().setPreferredSize(new Dimension(0, 20));
            }
        }
        headerIsVisible = visible;
    }

    /**
     * Устанавливает модель таблицы
     * @param model модель таблицы
     */
    public void setModel(TableModel model) {
        if (getModel() != null) {
            getModel().removeTableModelListener(cleaner);
        }
        getJTable().setModel(model);
        if (model instanceof IGridRow) {
            RowsHeightInfo rhi = new RowsHeightInfo((IGridRow) model);
            getJTable().setRowsHeightInfo(rhi);
        }
        if (model != null) {
            model.addTableModelListener(cleaner);
        }
    }

    /**
     * Возвращает модель таблицы
     * @return модель таблицы
     */
    public TableModel getModel() {
        return getJTable().getModel();
    }

    /**
     * Изменяет размеры всех строк подгоняя под размер занимемых данных
     */
    public void resizeAllRowToData() {
        myTable1.resizeAllRowToData();
    }

    /**
     * Изменяет размеры строки подгоняя под размер занимемых данных
     * @param row строка
     */
    public void resizeRowToData(int row) {
        myTable1.resizeRowToData(row);
    }

    /**
     * @return Возвращает Java Таблицу
     */
    public RowHeightSupportJTable getJTable() {
        return myTable1;
    }

    /**
     * @return Возвращает ScrollPane на которой расположена Java таблица
     */
    public JScrollPane getJScrollPane() {
        return jScrollPane1;
    }

    /**
     * @return Возвращает JSplitPane
     */
    public JSplitPane getJSplitPane() {
        return jSplitPane1;
    }

    /**
     * @return Возвращает RowHeader управляющий высотой строк
     */
    public RowHeader getRowHeader() {
        return rowHeader;
    }

    private void initComponents() {
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        myTable1 = new org.gocha.grid.rowheight.RowHeightSupportJTable();
        setLayout(new java.awt.BorderLayout());
        jSplitPane1.setDividerLocation(25);
        jSplitPane1.setDividerSize(5);
        myTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        myTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        myTable1.setIntercellSpacing(new java.awt.Dimension(0, 0));
        myTable1.setShowHorizontalLines(false);
        myTable1.setShowVerticalLines(false);
        jScrollPane1.setViewportView(myTable1);
        jSplitPane1.setRightComponent(jScrollPane1);
        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JSplitPane jSplitPane1;

    private org.gocha.grid.rowheight.RowHeightSupportJTable myTable1;
}
