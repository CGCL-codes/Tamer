package simtools.ui;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * This class aggregates a list of file filters, and matches the files accepted by the list.
 * @author nicolas brodu
 */
public class AggregateFileFilter extends FileFilter {

    public static MenuResourceBundle resources = ResourceFinder.getMenu(AggregateFileFilter.class);

    FileFilter[] filters;

    /**
	 * 
	 */
    public AggregateFileFilter(FileFilter[] list) {
        filters = list;
    }

    /**
	 * Accepts the file iff one of the filter in the list does
	 */
    public boolean accept(File f) {
        for (int i = 0; i < filters.length; ++i) {
            if (filters[i].accept(f)) return true;
        }
        return false;
    }

    public String getDescription() {
        return resources.getString("AllKnownFiles");
    }

    public FileFilter getFilterForFile(Frame owner, File f) {
        if (f == null) return null;
        Vector matchList = new Vector();
        for (int i = 0; i < filters.length; ++i) {
            if (filters[i] instanceof MenuResourceBundle.FileFilter) {
                if (((MenuResourceBundle.FileFilter) filters[i]).canSelect(f)) matchList.add(filters[i]);
            } else {
                if (filters[i].accept(f)) matchList.add(filters[i]);
            }
        }
        if (matchList.size() > 1) return new SelectFileFilterDialog(owner, matchList).getSelection(); else if (matchList.size() == 1) return (FileFilter) matchList.get(0); else return null;
    }

    public class SelectFileFilterDialog extends JDialog {

        Vector filterList;

        int chosenIndex;

        public SelectFileFilterDialog(Frame owner, Vector list) {
            super(owner, resources.getString("SelectAFileFormat"), true);
            filterList = list;
            chosenIndex = -1;
            TableModel dataModel = new AbstractTableModel() {

                public String getColumnName(int columnIndex) {
                    return resources.getString("AvailableFilters");
                }

                public int getColumnCount() {
                    return 1;
                }

                public int getRowCount() {
                    return (filterList == null) ? 0 : filterList.size();
                }

                public Object getValueAt(int row, int col) {
                    return ((FileFilter) filterList.get(row)).getDescription();
                }
            };
            JTable table = new JTable(dataModel);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            ListSelectionModel rowSM = table.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) return;
                    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                    if (lsm.isSelectionEmpty()) {
                    } else {
                        chosenIndex = lsm.getMinSelectionIndex();
                        SelectFileFilterDialog.this.dispose();
                    }
                }
            });
            JScrollPane scrollpane = new JScrollPane(table);
            getContentPane().add(scrollpane);
            addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    dispose();
                }
            });
            pack();
            show();
        }

        public FileFilter getSelection() {
            if (chosenIndex == -1) return null;
            return (FileFilter) filterList.get(chosenIndex);
        }
    }
}
