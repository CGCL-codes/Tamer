package ie.iqda.datastructures;

import ie.iqda.io.ErrorLog;
import ie.iqda.io.TextFile;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * An adapter for a JTable.
 *
 * @author Keith Ó Dúlaigh <keith@keithodulaigh.com>
 */
public class JTableUtilities {

    private static final int HEADER_ROW = 1;

    /**
     * Saves a JTable to a file.
     * 
     * @param table
     */
    public static void save(String file, JTable table) {
        ArrayList<ArrayList> data = new ArrayList<ArrayList>();
        for (int row = 0; row < table.getRowCount() + HEADER_ROW; row++) {
            data.add(row, new ArrayList<String>());
        }
        for (int column = 0; column < table.getColumnCount(); column++) {
            data.get(0).add(column, table.getColumnName(column));
        }
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                data.get(i + HEADER_ROW).add(j, table.getValueAt(i, j));
            }
        }
        try {
            XMLEncoder encoder = new XMLEncoder(new FileOutputStream(file));
            encoder.writeObject(data);
            encoder.close();
        } catch (IOException ex) {
            ErrorLog.instance().addEntry(ex);
        }
    }

    /**
     * Exports a JTable to a tab separated text file.
     * 
     * @param file
     * @param table 
     */
    public static void exportTSV(String file, JTable table) {
        StringBuilder fileContents = new StringBuilder();
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                fileContents.append(table.getValueAt(i, j));
                if (j + 1 == table.getColumnCount()) fileContents.append("\n"); else fileContents.append("\t");
            }
        }
        (new TextFile(file)).write(fileContents.toString(), false);
    }

    /**
     * Opens a serialized JTable.
     * 
     * @param file
     * @param table
     */
    public static void open(String file, JTable table) {
        ArrayList<ArrayList> data = null;
        try {
            XMLDecoder decoder = new XMLDecoder(new FileInputStream(file));
            data = (ArrayList<ArrayList>) decoder.readObject();
            decoder.close();
        } catch (Exception ex) {
            ErrorLog.instance().addEntry(ex);
        }
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(data.size() - HEADER_ROW);
        model.setColumnCount(data.get(0).size());
        for (int i = 0; i < data.get(0).size(); i++) {
            table.getColumnModel().getColumn(i).setHeaderValue(data.get(0).get(i));
        }
        for (int i = 1; i < data.size(); i++) {
            for (int j = 0; j < data.get(0).size(); j++) {
                table.setValueAt(data.get(i).get(j), i - 1, j);
            }
        }
        table.getTableHeader().repaint();
        table.repaint();
    }

    /**
     * Reads in a JTable and re-saves it as a persistent map.
     * 
     * @param out
     * @param in
     */
    public static void rewriteAsPersistentMap(String out, String in) {
        final int FIND_COL = 0;
        final int REPLACE_COL = 1;
        JTable tempTable = new JTable();
        PersistentMap tempMap = new PersistentMap(out);
        open(in, tempTable);
        for (int i = 0; i < tempTable.getRowCount(); i++) {
            if (tempTable.getValueAt(i, FIND_COL) != null && tempTable.getValueAt(i, REPLACE_COL) != null) {
                tempMap.put(tempTable.getValueAt(i, FIND_COL).toString(), tempTable.getValueAt(i, REPLACE_COL).toString());
            }
        }
    }
}
