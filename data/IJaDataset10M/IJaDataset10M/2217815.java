package de.psychomatic.mp3db.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import de.psychomatic.mp3db.core.dblayer.Album;
import de.psychomatic.mp3db.utils.GuiStrings;

/**
 * Model for albumtable in CDAdder dialog
 * @author Kykal
 */
public class CDAddTableModel extends AbstractTableModel {

    /**
     * List of keys (paths)
     */
    private List<String> _keys;

    /**
     * Map of albums (path/album)
     */
    private Map<String, Album> _map;

    /**
     * Creates the model
     */
    public CDAddTableModel() {
        super();
        _keys = new ArrayList<String>();
        _map = new HashMap<String, Album>();
    }

    /**
     * Gets the column type
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    @Override
    public Class getColumnClass(final int arg0) {
        Class result = null;
        switch(arg0) {
            case 0:
                result = String.class;
                break;
            case 1:
                result = String.class;
                break;
            case 2:
                result = Boolean.class;
                break;
        }
        return result;
    }

    /**
     * Table has only three columns
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return 3;
    }

    /**
     * Gets the title of a column
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(final int arg0) {
        String result = "";
        switch(arg0) {
            case 0:
                result = GuiStrings.getString("model.path");
                break;
            case 1:
                result = GuiStrings.getString("model.album");
                break;
            case 2:
                result = GuiStrings.getString("model.sampler");
                break;
        }
        return result;
    }

    /**
     * Gets the content of table
     * @return Map of content
     */
    public Map<String, Album> getContent() {
        return _map;
    }

    /**
     * Get the number of cds
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return _keys.size();
    }

    /**
     * Gets the value of a table cell
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(final int arg0, final int arg1) {
        Object result = null;
        switch(arg1) {
            case 0:
                result = _keys.get(arg0);
                break;
            case 1:
                result = (_map.get(_keys.get(arg0))).getAlbum();
                break;
            case 2:
                result = new Boolean((_map.get(_keys.get(arg0))).getAlbumType() == 1);
                break;
        }
        return result;
    }

    /**
     * Only title and sampler column is editable
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(final int arg0, final int arg1) {
        return arg1 == 1 | arg1 == 2;
    }

    /**
     * Sets a new content in table
     * @param map Map of content
     */
    public void setContent(final Map<String, Album> map) {
        _map = map;
        _keys = new ArrayList<String>();
        _keys.addAll(map.keySet());
        Collections.sort(_keys);
        fireTableDataChanged();
    }

    /**
     * Sets values in an album
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    @Override
    public void setValueAt(final Object arg0, final int arg1, final int arg2) {
        switch(arg2) {
            case 0:
                break;
            case 1:
                (_map.get(_keys.get(arg1))).setAlbum((String) arg0);
                break;
            case 2:
                (_map.get(_keys.get(arg1))).setAlbumType((short) (((Boolean) arg0).booleanValue() ? 1 : 0));
                break;
        }
        fireTableCellUpdated(arg1, arg2);
    }
}
