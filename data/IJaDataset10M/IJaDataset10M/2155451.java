package com.privilege.model;

import java.util.List;
import com.privilege.displayable.Component;

public interface TableModel extends Model {

    public String getTitle();

    public void setTitle(String title);

    public Component getValueAt(int rowIndex, int columnIndex);

    public void setValueAt(int rowIndex, int columnIndex, Component component);

    public String getColumnName(int column);

    public void setColumnName(int column, String columnName);

    public int getRowSize();

    public int getColumnSize();

    public List getContent();
}
