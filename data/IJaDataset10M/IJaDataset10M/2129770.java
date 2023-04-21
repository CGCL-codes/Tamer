package org.butu.gui.widgets;

import java.util.LinkedHashMap;
import org.butu.gui.widgets.table.CursorTableViewer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.TColumn;

public class Columns implements IColumnModel {

    public class ColumnInfo implements IColumnInfo {

        private ColumnData columnData;

        private CellEditor editor;

        private TColumn column;

        private boolean hidden = false;

        private Object data;

        public ColumnInfo(String property, String name, int width) {
            columnData = new ColumnData(property, name, width);
            columnCount++;
            visibleColumnCount++;
        }

        /**
         * �������� ColumnInfo �� ������ ������� ColumnData. ��� ���� ColumnData �����������.
         * @param columnData
         */
        public ColumnInfo(ColumnData columnData) {
            this.columnData = new ColumnData(columnData);
            columnCount++;
            visibleColumnCount++;
        }

        public void init() {
            if (!hidden && column == null) {
                column = viewer.createColumn(columnData.style);
                column.setWidth(columnData.width);
                column.setText(columnData.name);
            }
        }

        public int getEffectiveIndex() {
            int index = 1;
            for (ColumnInfo info : columns.values()) {
                if (info.equals(this)) {
                    break;
                } else if (!info.isHidden()) {
                    index++;
                }
            }
            return index;
        }

        public int getWidth() {
            if (column != null) columnData.width = column.getWidth();
            return columnData.width;
        }

        public void setWidth(int width) {
            columnData.width = width;
            if (column != null) column.setWidth(width);
        }

        public int getStyle() {
            return columnData.style;
        }

        public void setStyle(int style) {
            this.columnData.style = style;
        }

        public boolean isHidden() {
            return hidden;
        }

        public void setHidden(boolean hidden) {
            if (this.hidden != hidden) {
                this.hidden = hidden;
                visibleColumnCount += hidden ? -1 : 1;
            }
            if (hidden) {
                if (column != null) {
                    column.dispose();
                    column = null;
                }
            } else if (column == null) {
                column = viewer.createColumn(columnData.style, getEffectiveIndex());
                column.setWidth(columnData.width);
                column.setText(columnData.name);
            }
        }

        public String getId() {
            return columnData.property;
        }

        public void setId(String property) {
            this.columnData.property = property;
        }

        public String getText() {
            return columnData.name;
        }

        public void setText(String name) {
            this.columnData.name = name;
        }

        public CellEditor getEditor() {
            return editor;
        }

        public void setEditor(CellEditor editor) {
            this.editor = editor;
        }

        public Color getBackgroundColor() {
            return columnData.backgroundColor;
        }

        public void setBackgroundColor(Color backgroundColor) {
            this.columnData.backgroundColor = backgroundColor;
        }

        public TColumn getColumn() {
            return column;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

    private int columnCount;

    private int visibleColumnCount;

    private LinkedHashMap<String, ColumnInfo> columns;

    private ColumnInfo[] columnsArray;

    private String[] properties;

    private CellEditor[] editors;

    private IColumnable viewer;

    private boolean fixedFirst;

    public Columns(IColumnable viewer, boolean fixedFirst) {
        columns = new LinkedHashMap<String, ColumnInfo>();
        this.viewer = viewer;
        this.fixedFirst = fixedFirst;
    }

    public Columns(IColumnable viewer) {
        this(viewer, true);
    }

    public ColumnInfo getColumnAt(int index) {
        if (columnsArray != null) {
            return columnsArray[index];
        } else {
            return null;
        }
    }

    public void validate() {
        initPropertiesAndEditors();
        viewer.setColumnProperties(getProperties());
        viewer.setCellEditors(getEditors());
        int i;
        if (fixedFirst) {
            columnsArray = new ColumnInfo[columns.size() + 1];
            columnsArray[0] = null;
            i = 1;
        } else {
            columnsArray = new ColumnInfo[columns.size()];
            i = 0;
        }
        for (ColumnInfo value : columns.values()) {
            columnsArray[i] = value;
            i++;
        }
    }

    /**
     * ������������� �������. �������� ��������.
     */
    public void init() {
        for (ColumnInfo value : columns.values()) {
            value.init();
        }
        validate();
    }

    private void initPropertiesAndEditors() {
        int i;
        if (viewer instanceof CursorTableViewer) {
            properties = new String[visibleColumnCount + 1];
            editors = new CellEditor[visibleColumnCount + 1];
            properties[0] = "";
            editors[0] = null;
            i = 1;
        } else {
            properties = new String[visibleColumnCount];
            editors = new CellEditor[visibleColumnCount];
            i = 0;
        }
        for (ColumnInfo value : columns.values()) {
            if (!value.isHidden()) {
                properties[i] = value.getId();
                editors[i] = value.getEditor();
                i++;
            }
        }
    }

    public String[] getProperties() {
        return properties;
    }

    public CellEditor[] getEditors() {
        return editors;
    }

    public void setColumnHidden(String columnName, boolean isHidden) {
        ColumnInfo info = columns.get(columnName);
        if (isHidden != info.isHidden()) {
            if (isHidden) {
                correctCursor();
            }
            info.setHidden(isHidden);
            validate();
        }
    }

    public void removeColumn(String property) {
        ColumnInfo info = columns.get(property);
        if (info != null) {
            correctCursor();
            columns.remove(property);
            info.column.dispose();
            info.column = null;
            visibleColumnCount--;
            columnCount--;
        }
    }

    private void correctCursor() {
        if (viewer instanceof ICursorable) {
            ICursorable cur = (ICursorable) viewer;
            int curCol = cur.getCurrentColIndex();
            if (curCol + 1 == visibleColumnCount) {
                cur.moveTo(curCol - 1, cur.getCurrentRowIndex());
            }
        }
    }

    /**
     * @param property ������������� �������.
     * @return ������-���� ������� �� ���������� ���������������. ���� ���� �� ������, null.
     */
    public ColumnInfo get(String property) {
        return columns.get(property);
    }

    public ColumnInfo addColumn(String property, String name, int width) {
        ColumnInfo info = new ColumnInfo(property, name, width);
        columns.put(property, info);
        return info;
    }

    public ColumnInfo addColumn(String property, String name, int width, CellEditor editor) {
        ColumnInfo info = new ColumnInfo(property, name, width);
        info.setEditor(editor);
        columns.put(property, info);
        return info;
    }

    public ColumnInfo addColumn(ColumnData columnData) {
        ColumnInfo info = new ColumnInfo(columnData);
        columns.put(columnData.property, info);
        return info;
    }

    /**
     * �������� <b>ColumnData</b>, �������� � ������������ ������ �� �����������, � ������������ � ����
     * ������ �� �������� �������, �������� � ��������� <b>ColumnInfo</b>.
     * �� ��� �������� ��������� <b>ColumnInfo</b> �� ��������� <b>ColumnData</b> ������������ ��������������
     * (��� ����� ��������� � � ���������� ������� � ����� � ������� �������� <i>addColumn</i>). �������,
     * ��������� ������� � ������ ���������� �� ��������� ����������� ������� �����. ��������� �������
     * ��������� ������ � ����������������� ��������� ������ � �������.
     * @return ������ �������� ColumnData, ��������� �� ������ ���� ������� ������� ������� ������.
     */
    public ColumnData[] getColumnDatas() {
        ColumnData[] columnDatas = new ColumnData[visibleColumnCount];
        int i = 0;
        for (ColumnInfo columnInfo : columns.values()) {
            if (!columnInfo.hidden) {
                columnDatas[i++] = columnInfo.columnData;
            }
        }
        return columnDatas;
    }
}
