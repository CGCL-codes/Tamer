package core;

import java.awt.Toolkit;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableColumn;

/**
 * @version $Id: LibraryFrame.java 1160 2011-09-23 00:40:25Z frankster $
 */
class LibraryFrame extends AbstractLibraryFrame {

    private static int openFrameCount = 0;

    private static final int SYNTH = 0;

    private static final int TYPE = 1;

    private static final int PATCH_NAME = 2;

    private static final int FIELD1 = 3;

    private static final int FIELD2 = 4;

    private static final int COMMENT = 5;

    static final String FILE_EXTENSION = ".patchlib";

    private static final FileFilter FILE_FILTER = new Actions.ExtensionFilter("PatchEdit Library Files (*" + FILE_EXTENSION + ")", FILE_EXTENSION);

    private static final PatchTransferHandler pth = new PatchListTransferHandler();

    LibraryFrame(File file) {
        super(file.getName(), "Library", pth);
    }

    LibraryFrame() {
        super("Unsaved Library #" + (++openFrameCount), "Library", pth);
    }

    PatchTableModel createTableModel() {
        return new PatchListModel();
    }

    void setupColumns() {
        TableColumn column = null;
        column = table.getColumnModel().getColumn(SYNTH);
        column.setPreferredWidth(50);
        column = table.getColumnModel().getColumn(TYPE);
        column.setPreferredWidth(50);
        column = table.getColumnModel().getColumn(PATCH_NAME);
        column.setPreferredWidth(100);
        column = table.getColumnModel().getColumn(FIELD1);
        column.setPreferredWidth(50);
        column = table.getColumnModel().getColumn(FIELD2);
        column.setPreferredWidth(50);
        column = table.getColumnModel().getColumn(COMMENT);
        column.setPreferredWidth(200);
    }

    void frameActivated() {
        Actions.setEnabled(false, Actions.EN_ALL);
        Actions.setEnabled(true, Actions.EN_GET | Actions.EN_IMPORT | Actions.EN_IMPORT_ALL | Actions.EN_NEW_PATCH);
        enableActions();
    }

    /** change state of Actions based on the state of the table. */
    void enableActions() {
        Actions.setEnabled(table.getRowCount() > 0, Actions.EN_SAVE | Actions.EN_SAVE_AS | Actions.EN_SEARCH);
        Actions.setEnabled(table.getRowCount() > 1, Actions.EN_DELETE_DUPLICATES | Actions.EN_SORT);
        Actions.setEnabled(table.getSelectedRowCount() > 0, Actions.EN_DELETE);
        Actions.setEnabled(table.getSelectedRowCount() == 1, Actions.EN_COPY | Actions.EN_CUT | Actions.EN_EXPORT | Actions.EN_REASSIGN | Actions.EN_STORE | Actions.EN_UPLOAD | Actions.EN_CROSSBREED);
        Actions.setEnabled(table.getSelectedRowCount() == 1 && myModel.getPatchAt(table.getSelectedRow()).isSinglePatch(), Actions.EN_SEND | Actions.EN_SEND_TO | Actions.EN_PLAY);
        Actions.setEnabled(table.getSelectedRowCount() == 1 && myModel.getPatchAt(table.getSelectedRow()).isBankPatch(), Actions.EN_EXTRACT);
        Actions.setEnabled(table.getSelectedRowCount() == 1 && myModel.getPatchAt(table.getSelectedRow()).hasEditor(), Actions.EN_EDIT);
        Actions.setEnabled(Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this) != null, Actions.EN_PASTE);
    }

    int deleteDuplicates() {
        Collections.sort(myModel.getList(), new SysexSort());
        int numDeleted = 0;
        Iterator it = myModel.getList().iterator();
        byte[] p = ((IPatch) it.next()).getByteArray();
        while (it.hasNext()) {
            byte[] q = ((IPatch) it.next()).getByteArray();
            if (Arrays.equals(p, q)) {
                it.remove();
                numDeleted++;
            } else p = q;
        }
        if (numDeleted > 0) table.clearSelection();
        changed();
        return numDeleted;
    }

    private static class SysexSort implements Comparator {

        public int compare(Object a1, Object a2) {
            String s1 = new String(((IPatch) (a1)).getByteArray());
            String s2 = new String(((IPatch) (a2)).getByteArray());
            return s1.compareTo(s2);
        }
    }

    void sortPatch(Comparator c) {
        Collections.sort(myModel.getList(), c);
        changed();
    }

    FileFilter getFileFilter() {
        return FILE_FILTER;
    }

    String getFileExtension() {
        return FILE_EXTENSION;
    }

    /**
     * Refactored from PerformanceListModel
     * 
     * @author Gerrit Gehnen
     */
    private class PatchListModel extends PatchTableModel {

        private final String[] columnNames = { "Synth", "Type", "Patch Name", "Field 1", "Field 2", "Comment" };

        private ArrayList list = new ArrayList();

        PatchListModel() {
        }

        public int getRowCount() {
            return list.size();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            IPatch myPatch = (IPatch) list.get(row);
            try {
                switch(col) {
                    case SYNTH:
                        return myPatch.getDevice().getSynthName();
                    case TYPE:
                        return myPatch.getType();
                    case PATCH_NAME:
                        return myPatch.getName();
                    case FIELD1:
                        return myPatch.getDate();
                    case FIELD2:
                        return myPatch.getAuthor();
                    case COMMENT:
                        return myPatch.getComment();
                    default:
                        ErrorMsg.reportStatus("LibraryFrame.getValueAt: internal error.");
                        return null;
                }
            } catch (NullPointerException e) {
                ErrorMsg.reportStatus("LibraryFrame.getValueAt: row=" + row + ", col=" + col + ", Patch=" + myPatch);
                ErrorMsg.reportStatus("row count =" + getRowCount());
                return null;
            }
        }

        public Class getColumnClass(int c) {
            return String.class;
        }

        public boolean isCellEditable(int row, int col) {
            return (col > TYPE);
        }

        public void setValueAt(Object value, int row, int col) {
            IPatch myPatch = (IPatch) list.get(row);
            switch(col) {
                case PATCH_NAME:
                    myPatch.setName((String) value);
                    break;
                case FIELD1:
                    myPatch.setDate((String) value);
                    break;
                case FIELD2:
                    myPatch.setAuthor((String) value);
                    break;
                case COMMENT:
                    myPatch.setComment((String) value);
                    break;
                default:
                    ErrorMsg.reportStatus("LibraryFrame.setValueAt: internal error.");
            }
            fireTableCellUpdated(row, col);
        }

        int addPatch(IPatch p) {
            ErrorMsg.reportStatus("LibraryFrame.addPatch: Patch=" + p);
            list.add(p);
            return list.size() - 1;
        }

        int addPatch(IPatch p, int bankNum, int patchNum) {
            ErrorMsg.reportStatus("LibraryFrame.addPatch: Patch=" + p);
            list.add(p);
            return list.size() - 1;
        }

        void setPatchAt(IPatch p, int row, int bankNum, int patchNum) {
            ErrorMsg.reportStatus("LibraryFrame.setPatchAt: row=" + row + ", Patch=" + p);
            list.set(row, p);
        }

        void setPatchAt(IPatch p, int row) {
            ErrorMsg.reportStatus("LibraryFrame.setPatchAt: row=" + row + ", Patch=" + p);
            list.set(row, p);
        }

        IPatch getPatchAt(int row) {
            return (IPatch) list.get(row);
        }

        String getCommentAt(int row) {
            return getPatchAt(row).getComment();
        }

        void removeAt(int row) {
            this.list.remove(row);
        }

        ArrayList getList() {
            return this.list;
        }

        void setList(ArrayList newList) {
            this.list = newList;
        }
    }

    private static class PatchListTransferHandler extends PatchTransferHandler {

        protected boolean storePatch(IPatch p, JComponent c) {
            PatchTableModel model = (PatchTableModel) ((JTable) c).getModel();
            model.addPatch(p);
            model.fireTableDataChanged();
            return true;
        }
    }
}
