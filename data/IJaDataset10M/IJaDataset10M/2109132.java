package net.ar.guia.own.interfaces;

import java.util.*;

public interface Table {

    public abstract void setHeaderAt(int aColumn, VisualComponent aVisualComponent);

    public abstract VisualComponent getHeaderAt(int aColumn);

    public abstract void setElementAt(int aRow, int aColumn, VisualComponent aVisualComponent);

    public abstract VisualComponent getElementAt(int aRow, int aColumn);

    public abstract int getColumnCount();

    public abstract int getRowCount();

    public abstract List getRows();

    public abstract void setRows(List aRows);

    public abstract List getRowAt(int aPosition);

    public abstract void clear();
}
