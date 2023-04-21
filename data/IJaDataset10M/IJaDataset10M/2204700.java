package kth.csc.inda.sempc.model;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.table.AbstractTableModel;
import kth.csc.inda.sempc.SEMPC;
import kth.csc.inda.sempc.Utils;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDDatabaseException;

/**
 * Data model for the table to the right of the browse tree.
 * 
 * @author Sebastian Sjögren
 * @version 2009-05-27
 */
@SuppressWarnings("serial")
public class BrowseTableModel extends AbstractTableModel {

    private String path;

    private ArrayList<String> data;

    private String[] columnNames = { "Files" };

    /**
	 * Will populate this data model with the filenames (not directories)
	 * of the files found directly in given path.
	 * @param p A directory in MPD database
	 */
    public synchronized void updateBrowseList(String p) {
        if (SEMPC.getMPD() == null) return;
        path = p;
        Collection<String> tmp;
        try {
            tmp = SEMPC.getMPD().getMPDDatabase().listAllFiles(path);
            if (data == null) data = new ArrayList<String>(); else data.clear();
            Utils.log(this, "Populating table with contents of " + path);
            int depth = path.split("/").length;
            for (String file : tmp) {
                if (file.contains("file: ") && file.split("/").length == depth + 1) data.add(file.substring("file: ".length()));
            }
            fireTableDataChanged();
        } catch (MPDDatabaseException e) {
            e.printStackTrace();
        } catch (MPDConnectionException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Returns the name of the column at given index.
	 * @return the name of the column.
	 */
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
	 * Returns number of columns
	 * @return number of columns (always 1)
	 */
    @Override
    public int getColumnCount() {
        return 1;
    }

    /**
	 * Returns the number of rows.
	 * @return number of rows.
	 */
    @Override
    public int getRowCount() {
        return data == null ? 0 : data.size();
    }

    /**
	 * Returns the value at given position
	 * @param row Row index
	 * @param col Column index
	 * @return the value
	 */
    @Override
    public Object getValueAt(int row, int col) {
        if (data == null) return "";
        switch(col) {
            case 0:
                return data.get(row).substring(path.length() + 1);
            default:
                return null;
        }
    }
}
