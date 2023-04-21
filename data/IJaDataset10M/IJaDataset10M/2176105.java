package com.pyrphoros.erddb.gui.windows.edit.table;

import com.pyrphoros.erddb.gui.windows.edit.constraint.*;
import com.pyrphoros.erddb.gui.windows.edit.*;
import com.pyrphoros.erddb.Designer;
import com.pyrphoros.erddb.gui.windows.edit.index.EditIndexDialog;
import com.pyrphoros.erddb.model.data.Index;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JTable;

/**
 *
 * @author Tyler
 */
public class EditIndexAction implements ActionListener {

    private JTable indexTable;

    private JTable columnsTable;

    private ArrayList<Index> tempIndices;

    private String tableName;

    public EditIndexAction(JTable indexTable, JTable columnsTable, ArrayList<Index> tempIndices, String tableName) {
        this.indexTable = indexTable;
        this.columnsTable = columnsTable;
        this.tempIndices = tempIndices;
        this.tableName = tableName;
    }

    public void actionPerformed(ActionEvent e) {
        int[] rows = indexTable.getSelectedRows();
        if (rows.length == 1) {
            JDialog dialog = new EditIndexDialog(Designer.getMainFrame(), Designer.getResource("gui.dialog.editindices.title"), true, indexTable, columnsTable, tempIndices, tableName, rows[0]);
            dialog.pack();
            dialog.setVisible(true);
        }
    }
}
