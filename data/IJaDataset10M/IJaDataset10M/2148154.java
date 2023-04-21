package ircam.jmax.editors.mat;

import ircam.jmax.fts.*;
import ircam.jmax.toolkit.*;
import java.util.Enumeration;
import java.awt.*;

public interface MatDataModel {

    public abstract int getSize();

    public abstract int getRows();

    public abstract String getColumnName(int col_id);

    public abstract String getType();

    public abstract String getName();

    public abstract boolean haveRowIdCol();

    public abstract boolean canAppendColumn();

    public abstract int getColumns();

    public abstract void setSize(int m, int n);

    public abstract Dimension getDefaultSize();

    public abstract void setRows(int m);

    public abstract void setColumns(int n);

    public abstract Object getValueAt(int m, int n);

    public abstract void setValueAt(int m, int n, Object value);

    public abstract void addMatListener(MatDataListener theListener);

    public abstract void removeMatListener(MatDataListener theListener);

    public abstract void addJMaxTableListener(JMaxTableListener theListener);

    public abstract void removeJMaxTableListener(JMaxTableListener theListener);

    public abstract void requestSetValue(java.lang.Object aValue, int rowIndex, int columnIndex);

    public abstract void requestAppendRow();

    public abstract void requestInsertRow(int index);

    public abstract void requestAppendColumn();

    public abstract void requestInsertColumn(int index);

    public abstract void requestDeleteRows(int startIndex, int size);

    public abstract void requestDeleteCols(int startIndex, int size);

    public abstract void requestExport();

    public abstract void requestImport();

    public FtsGraphicObject getFtsMatrixObject();
}
