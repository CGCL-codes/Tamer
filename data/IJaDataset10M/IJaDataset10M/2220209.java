package com.tarpri.preferences;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class MatchTableModel implements TableModel {

    private String[] _col_names;

    private Vector<TarpriMatch> _matches;

    private TableModelListener _listener;

    public MatchTableModel() {
        super();
        _col_names = new String[] { "Find", "Regular Exp.", "Case Sensitive" };
        _matches = new Vector<TarpriMatch>();
    }

    public void addTableModelListener(TableModelListener l) {
        _listener = l;
    }

    public Object[] toArray() {
        return _matches.toArray();
    }

    public Class<String> getColumnClass(int columnIndex) {
        return String.class;
    }

    public int getColumnCount() {
        return _col_names.length;
    }

    public String getColumnName(int columnIndex) {
        return _col_names[columnIndex];
    }

    public int getRowCount() {
        return _matches.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        String value = null;
        if (columnIndex == 0) value = ((TarpriMatch) _matches.get(rowIndex)).getContents(); else if (columnIndex == 1) value = ((TarpriMatch) _matches.get(rowIndex)).isRegular_expression() ? "True" : "False"; else if (columnIndex == 2) value = ((TarpriMatch) _matches.get(rowIndex)).isCase_sensitive() ? "True" : "False";
        return value;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void removeTableModelListener(TableModelListener l) {
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    public void add(TarpriMatch new_match) {
        _matches.add(new_match);
        _listener.tableChanged(new TableModelEvent(this));
    }

    public void add(List<TarpriMatch> new_matches) {
        _matches.addAll(new_matches);
        _listener.tableChanged(new TableModelEvent(this));
    }

    public LinkedList<TarpriMatch> toList() {
        LinkedList<TarpriMatch> list_matches = new LinkedList<TarpriMatch>();
        for (int match_idx = 0; match_idx < _matches.size(); match_idx++) {
            list_matches.add(_matches.get(match_idx));
        }
        return list_matches;
    }

    public void remove(int index) {
        _matches.remove(index);
        _listener.tableChanged(new TableModelEvent(this));
    }

    public void remove(Object o) {
        _matches.remove(o);
        _listener.tableChanged(new TableModelEvent(this));
    }

    public void removeAllElements() {
        _matches.removeAllElements();
        _listener.tableChanged(new TableModelEvent(this));
    }

    public TarpriMatch get(int index) {
        return _matches.get(index);
    }

    public TarpriMatch set(int index, TarpriMatch element) {
        return _matches.set(index, element);
    }
}
