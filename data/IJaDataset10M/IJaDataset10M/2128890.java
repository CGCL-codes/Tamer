package de.kout.wlFxp.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.*;

/**
 *  Description of the Class
 *
 *@author     Alexander Kout
 *@created    29. Juli 2003
 */
class TableButtonMouseListener implements MouseListener {

    private MainTable table;

    JTableHeader header;

    TableButtonRenderer renderer;

    /**
	 *  Constructor for the TableButtonMouseListener object
	 *
	 *@param  table     Description of the Parameter
	 *@param  renderer  Description of the Parameter
	 */
    public TableButtonMouseListener(MainTable table, TableButtonRenderer renderer) {
        this.table = table;
        header = table.getTableHeader();
        this.renderer = renderer;
    }

    /**
	 *  Description of the Method
	 *
	 *@param  e  Description of the Parameter
	 */
    public void mouseClicked(MouseEvent e) {
    }

    /**
	 *  Description of the Method
	 *
	 *@param  e  Description of the Parameter
	 */
    public void mouseEntered(MouseEvent e) {
    }

    /**
	 *  Description of the Method
	 *
	 *@param  e  Description of the Parameter
	 */
    public void mouseExited(MouseEvent e) {
    }

    /**
	 *  Description of the Method
	 *
	 *@param  e  Description of the Parameter
	 */
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        int col = table.columnAtPoint(p);
        int space = 0;
        for (int i = 0; i < col; i++) space += table.getColumnModel().getColumn(i).getWidth();
        if (Math.abs(space + table.getColumnModel().getColumn(col).getWidth() - p.getX()) < 4 || Math.abs(space - p.getX()) < 3) return;
        int sortCol = table.convertColumnIndexToModel(col);
        renderer.setPressedColumn(col);
        renderer.setSelectedColumn(col);
        header.repaint();
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        boolean isAscent;
        if (TableButtonRenderer.DOWN == renderer.getState(col)) {
            isAscent = true;
        } else {
            isAscent = false;
        }
        ((MainTableModel) table.getModel()).sortBy(sortCol, isAscent);
    }

    /**
	 *  Description of the Method
	 *
	 *@param  e  Description of the Parameter
	 */
    public void mouseReleased(MouseEvent e) {
        int col = table.columnAtPoint(e.getPoint());
        renderer.setPressedColumn(-1);
        header.repaint();
    }
}
