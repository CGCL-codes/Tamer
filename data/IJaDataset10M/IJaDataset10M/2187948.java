package uk.ac.essex.common.gui.file;

import javax.swing.table.AbstractTableModel;
import javax.swing.*;
import java.io.File;
import java.io.FilenameFilter;

/**
 * This is a model of a directory
 * <br>
 * <br>
 *
 * @author Eric Armstrong, Tom Santos, Steve Wilson and Laurence Smith
 * @version $Revision: 1.1 $
 * You should have received a copy of GNU public license with this code.
 * If not please visit <a href="www.gnu.org/copyleft/gpl.html">this site </a>
 */
public class DirectoryModel extends AbstractTableModel {

    protected File directory;

    protected String[] children;

    protected int rowCount;

    protected Icon dirIcon;

    protected Icon fileIcon;

    protected FilenameFilter filenameFilter = new AllFileFilter();

    public DirectoryModel() {
        init();
    }

    public DirectoryModel(FilenameFilter filenameFilter) {
        this.filenameFilter = filenameFilter;
        init();
    }

    public DirectoryModel(File dir) {
        init();
        if (dir.isDirectory()) directory = dir;
        children = dir.list();
        rowCount = children.length;
    }

    protected void init() {
        dirIcon = (Icon) UIManager.get("FileView.directoryIcon");
        fileIcon = (Icon) UIManager.get("FileView.fileIcon");
    }

    public void setDirectory(File dir) {
        if (dir != null) {
            if (dir.isDirectory()) {
                directory = dir;
                children = dir.list(filenameFilter);
                rowCount = children.length;
            }
        } else {
            directory = null;
            children = null;
            rowCount = 0;
        }
        fireTableDataChanged();
    }

    public int getRowCount() {
        return children != null ? rowCount : 0;
    }

    public int getColumnCount() {
        return children != null ? 3 : 0;
    }

    public File getRow(int row) {
        if (directory == null || children == null) {
            return null;
        }
        File fileSysEntity = new File(directory, children[row]);
        return fileSysEntity;
    }

    public Object getValueAt(int row, int column) {
        File fileSysEntity = getRow(row);
        switch(column) {
            case 0:
                return getIcon(fileSysEntity);
            case 1:
                return fileSysEntity.getName();
            case 2:
                if (fileSysEntity.isDirectory()) {
                    return "--";
                } else {
                    return new Long(fileSysEntity.length());
                }
            case 3:
            default:
                return "";
        }
    }

    private Icon getIcon(File file) {
        return file.isDirectory() ? dirIcon : fileIcon;
    }

    public String getColumnName(int column) {
        switch(column) {
            case 0:
                return "Type";
            case 1:
                return "Name";
            case 2:
                return "Bytes";
            default:
                return "unknown";
        }
    }

    public Class getColumnClass(int column) {
        if (column == 0) {
            return getValueAt(0, column).getClass();
        } else {
            return super.getColumnClass(column);
        }
    }

    private class AllFileFilter implements FilenameFilter {

        public boolean accept(File file, String s) {
            return true;
        }
    }
}
