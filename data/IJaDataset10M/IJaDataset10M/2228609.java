package com.epro.standard.frontend.lov;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class EproLOVColumn implements Serializable {

    private String columnName = "";

    private String columnAlign = "center";

    private int columnWidth = 20;

    private boolean isDisplay = true;

    public EproLOVColumn() {
    }

    public EproLOVColumn(String cName) {
        this.columnName = cName;
    }

    public EproLOVColumn(String cName, String cAlign, int cWidth, boolean isDisplay) {
        this.columnName = cName;
        this.columnAlign = cAlign;
        this.columnWidth = cWidth;
        this.isDisplay = isDisplay;
    }

    public String getColumnAlign() {
        return columnAlign;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public boolean getIsDisplay() {
        return isDisplay;
    }

    public void setColumnAlign(String string) {
        columnAlign = string;
    }

    public void setColumnName(String string) {
        columnName = string;
    }

    public void setColumnWidth(int i) {
        columnWidth = i;
    }

    public void setIsDisplay(boolean b) {
        isDisplay = b;
    }
}
