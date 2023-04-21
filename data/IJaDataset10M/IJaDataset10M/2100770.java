package net.sf.vfsjfilechooser.utils;

import org.apache.commons.vfs.FileObject;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.table.TableCellRenderer;

/**
 * Copy of some functions in SwingUtilities2
 * to avoid using sun proprietary classes
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public final class SwingCommonsUtilities {

    private static final javax.swing.JFileChooser fr = new javax.swing.JFileChooser();

    private static final javax.swing.filechooser.FileSystemView fw = fr.getFileSystemView();

    private static FileObject defaultDir;

    private SwingCommonsUtilities() {
        throw new AssertionError("Trying to instanciate SwingCommonsUtilities");
    }

    public static FileObject getVFSFileChooserDefaultDirectory() {
        if (defaultDir == null) {
            try {
                defaultDir = VFSUtils.getFileSystemManager().toFileObject(fw.getDefaultDirectory());
            } catch (Exception e) {
                defaultDir = VFSUtils.resolveFileObject(System.getProperty("user.home"));
            }
        }
        return defaultDir;
    }

    /**
     *
     * @param table
     * @param row
     * @param column
     * @param p
     * @return
     */
    public static boolean pointOutsidePrefSize(JTable table, int row, int column, Point p) {
        if ((table.convertColumnIndexToModel(column) != 0) || (row == -1)) {
            return true;
        }
        TableCellRenderer tcr = table.getCellRenderer(row, column);
        Object value = table.getValueAt(row, column);
        Component cell = tcr.getTableCellRendererComponent(table, value, false, false, row, column);
        Dimension itemSize = cell.getPreferredSize();
        Rectangle cellBounds = table.getCellRect(row, column, false);
        cellBounds.width = itemSize.width;
        cellBounds.height = itemSize.height;
        assert ((p.x >= cellBounds.x) && (p.y >= cellBounds.y));
        if ((p.x > (cellBounds.x + cellBounds.width)) || (p.y > (cellBounds.y + cellBounds.height))) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param list
     * @param index
     * @param point
     * @return
     */
    public static boolean pointIsInActualBounds(JList list, int index, Point point) {
        ListCellRenderer renderer = list.getCellRenderer();
        ListModel dataModel = list.getModel();
        Object value = dataModel.getElementAt(index);
        Component item = renderer.getListCellRendererComponent(list, value, index, false, false);
        Dimension itemSize = item.getPreferredSize();
        Rectangle cellBounds = list.getCellBounds(index, index);
        if (!item.getComponentOrientation().isLeftToRight()) {
            cellBounds.x += (cellBounds.width - itemSize.width);
        }
        cellBounds.width = itemSize.width;
        return cellBounds.contains(point);
    }

    /**
     *
     * @param list
     * @param point
     * @return
     */
    public static int loc2IndexFileList(JList list, Point point) {
        int index = list.locationToIndex(point);
        if (index != -1) {
            Object bySize = list.getClientProperty("List.isFileList");
            if (bySize instanceof Boolean && ((Boolean) bySize).booleanValue() && !pointIsInActualBounds(list, index, point)) {
                index = -1;
            }
        }
        return index;
    }
}
