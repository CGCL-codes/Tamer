package com.apelon.beans.apeldiff;

import javax.swing.ImageIcon;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDropEvent;
import javax.swing.event.TableModelListener;
import java.util.Vector;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public class DefaultDiffModel implements ApelDiffModel {

    private String[][] m_stTableData = null;

    private String[] m_stColHeaders = null;

    private String[] m_stRowHeaders = null;

    private ImageIcon[] m_pRowHeaderIcons = null;

    public DefaultDiffModel() {
    }

    public void setTableSize(int rows, int cols) {
        m_stTableData = new String[rows][cols];
        m_stRowHeaders = new String[rows];
        m_stColHeaders = new String[cols];
    }

    public void setHasIcons(boolean bVal) {
        if (bVal) m_pRowHeaderIcons = new ImageIcon[m_stRowHeaders.length]; else m_pRowHeaderIcons = null;
    }

    public void setValueAt(String stVal, int row, int col) {
        m_stTableData[row][col] = stVal;
    }

    public void setRowHeader(String stVal, int row) {
        m_stRowHeaders[row] = stVal;
    }

    public void setRowHeaderIcon(ImageIcon iVal, int row) {
        m_pRowHeaderIcons[row] = iVal;
    }

    public void setColHeader(String stVal, int col) {
        m_stColHeaders[col] = stVal;
    }

    public String getRowHeaderName(int iRow) {
        return m_stRowHeaders[iRow];
    }

    public boolean hasRowHeaderIcons() {
        return (m_pRowHeaderIcons != null);
    }

    public ImageIcon getRowHeaderIcon(int iRow) {
        return m_pRowHeaderIcons[iRow];
    }

    public Transferable getTransferableValueAt(int iRow, int iCol) {
        return null;
    }

    public int getRowCount() {
        return m_stRowHeaders.length;
    }

    public int getColumnCount() {
        return m_stColHeaders.length;
    }

    public String getColumnName(int columnIndex) {
        return m_stColHeaders[columnIndex];
    }

    public Class getColumnClass(int columnIndex) {
        return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return m_stTableData[rowIndex][columnIndex];
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }

    public int getCellDiff(int iRow, int iCol) {
        if (iCol == 0) return ApelDiffModel.SAME; else return getDiff(iRow, iCol);
    }

    private int getDiff(int iRow, int iCol) {
        String stCol;
        String stCompCol;
        int iRet = 0;
        Object pObj = getValueAt(iRow, iCol);
        if (pObj != null) stCol = (String) pObj; else stCol = new String("");
        pObj = getValueAt(iRow, iCol - 1);
        if (pObj != null) stCompCol = (String) pObj; else stCompCol = new String("");
        if (stCol.equals(stCompCol)) iRet = SAME; else if (stCol.length() == 0 && stCompCol.length() > 0) iRet = DELETED; else if (stCompCol.length() == 0 && stCol.length() > 0) iRet = NEW; else iRet = CHANGED;
        return iRet;
    }

    public String getTooltipTextValueAt(int iRow, int iCol) {
        return " ";
    }
}
