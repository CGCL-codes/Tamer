package edu.sdsc.grid.gui.applet;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

class UploadTableModel extends DefaultTableModel {

    static int ICON_COLUMN = 0;

    static int SOURCE_COLUMN = 1;

    static int DESTINATION_COLUMN = 2;

    static int RESOURCE_COLUMN = 3;

    static int STATUS_COLUMN = 4;

    private int directoryFileCount;

    private static String fileIconPath = "/image/file.png";

    private static String folderIconPath = "/image/folder.png";

    private ImageIcon fileIcon;

    private ImageIcon folderIcon;

    static AppletLogger logger = AppletLogger.getInstance();

    UploadTableModel() {
        this.addColumn("");
        this.addColumn("Source");
        this.addColumn("Destination");
        this.addColumn("Resource");
        this.addColumn("Status");
        try {
            fileIcon = new ImageIcon(this.getClass().getResource(fileIconPath));
            folderIcon = new ImageIcon(this.getClass().getResource(folderIconPath));
        } catch (Exception e) {
            logger.log("file icon exception. " + e);
        }
    }

    public boolean isCellEditable(int row, int col) {
        if (col == SOURCE_COLUMN || col == DESTINATION_COLUMN || col == RESOURCE_COLUMN) return true;
        return false;
    }

    public Class getColumnClass(int c) {
        Object obj = getValueAt(0, c);
        if (obj == null) {
            return new Object().getClass();
        } else if (c == ICON_COLUMN) {
            return Icon.class;
        } else if (c == STATUS_COLUMN) {
            return JProgressBar.class;
        } else if (c == SOURCE_COLUMN) {
            return JTextField.class;
        } else if (c == DESTINATION_COLUMN) {
            return JTextField.class;
        } else if (c == RESOURCE_COLUMN) {
            return JComboBox.class;
        } else {
            return this.getValueAt(0, c).getClass();
        }
    }

    public void addFile(List fileList) {
        int rowCount = this.getRowCount();
        UploadItem item = null;
        for (int k = 0; k < fileList.size(); k++) {
            item = (UploadItem) fileList.get(k);
            addToTable(item);
        }
    }

    public void removeFile(int[] selectedRows) {
        List itemList = new ArrayList();
        try {
            for (int k = selectedRows.length - 1; k >= 0; k--) {
                String source = ((JTextField) this.getValueAt(selectedRows[k], SOURCE_COLUMN)).getText();
                String destination = ((JTextField) this.getValueAt(selectedRows[k], DESTINATION_COLUMN)).getText();
                String resource = (String) ((JComboBox) this.getValueAt(selectedRows[k], RESOURCE_COLUMN)).getSelectedItem();
                UploadItem item = new UploadItem(source, destination, resource);
                itemList.add(item);
                this.removeRow(selectedRows[k]);
            }
        } catch (IOException e) {
            logger.log("file grid exception. " + e);
        } catch (URISyntaxException e) {
            logger.log("file name/uri exception. " + e);
        }
        DBUtil.getInstance().delete(itemList);
    }

    private JTextFieldListener tfListener = new JTextFieldListener();

    private JTextFieldMouseListener tfMouseListener = new JTextFieldMouseListener();

    private void addToTable(UploadItem item) {
        JTextField tfSource = new JTextField(item.getSource());
        JTextField tfDestination = new JTextField(item.getDestination());
        JComboBox comboBox = new JComboBox(item.getResourceList().toArray());
        comboBox.setSelectedItem(item.getSelectedResource());
        tfSource.addFocusListener(tfListener);
        tfDestination.addFocusListener(tfListener);
        tfSource.setBorder(new EmptyBorder(0, 8, 0, 8));
        tfDestination.setBorder(new EmptyBorder(0, 8, 0, 8));
        tfSource.setDragEnabled(false);
        tfDestination.setDragEnabled(false);
        ImageIcon icon = null;
        if (item.getType().equals(UploadItem.TYPE_FILE)) icon = fileIcon; else icon = folderIcon;
        this.addRow(new Object[] { icon, tfSource, tfDestination, comboBox, null });
    }

    private void calculateFileCount(File file, int row, String folderName) {
        File[] fileList = file.listFiles();
        int len = fileList.length;
        for (int i = 0; i < len; i++) {
            if (fileList[i].isFile()) {
                directoryFileCount++;
            } else if (fileList[i].isDirectory()) {
                calculateFileCount(fileList[i], row, folderName);
            }
        }
    }
}
