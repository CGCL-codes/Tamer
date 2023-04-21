package net.sourceforge.jd.gui;

import java.io.File;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import net.sourceforge.jd.lang.Lang;

public class CPModel extends DefaultTableModel {

    private ArrayList<File> fileList;

    public CPModel(ArrayList<File> fileList) {
        this.fileList = fileList;
        addColumn("Type");
        addColumn("Path");
        for (File file : fileList) {
            addRow(file);
        }
    }

    public boolean addFile(File file) {
        addRow(file);
        return fileList.add(file);
    }

    private void addRow(File file) {
        Object[] rowData = new Object[2];
        if (file.isDirectory()) {
            rowData[0] = Lang.CLASS_FRAME_FOLDER;
        } else {
            rowData[0] = Lang.CLASS_FRAME_FILE;
        }
        rowData[1] = file.getPath();
        addRow(rowData);
    }

    public void removeFile(int index) {
        fileList.remove(index);
        removeRow(index);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
