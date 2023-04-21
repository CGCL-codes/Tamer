package org.openconcerto.sql.users.rights;

import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.model.SQLRow;
import org.openconcerto.sql.model.SQLRowListRSH;
import org.openconcerto.sql.model.SQLRowValues;
import org.openconcerto.sql.model.SQLSelect;
import org.openconcerto.sql.model.SQLTable;
import org.openconcerto.sql.model.SQLTableEvent;
import org.openconcerto.sql.model.SQLTableEvent.Mode;
import org.openconcerto.sql.model.SQLTableModifiedListener;
import org.openconcerto.sql.model.Where;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class UserRightsManagerModel extends AbstractTableModel {

    private SQLTable tableRight = Configuration.getInstance().getRoot().findTable("RIGHTS");

    private SQLTable tableUserRight = Configuration.getInstance().getRoot().findTable("USER_RIGHT");

    private int idUser = -1;

    private Vector<SQLRowValues> listRowValues = new Vector<SQLRowValues>(this.tableRight.getRowCount());

    private List<String> columns = Arrays.asList("Actif", "Libellé");

    List<SQLRow> cache = new ArrayList<SQLRow>();

    public UserRightsManagerModel() {
        super();
        SQLSelect sel2 = new SQLSelect(Configuration.getInstance().getBase());
        sel2.addSelect(this.tableRight.getKey());
        sel2.addFieldOrder(this.tableRight.getField("CODE"));
        List<SQLRow> rowsRights = (List<SQLRow>) Configuration.getInstance().getBase().getDataSource().execute(sel2.asString(), new SQLRowListRSH(this.tableRight, true));
        this.cache.addAll(rowsRights);
        this.tableRight.addTableModifiedListener(new SQLTableModifiedListener() {

            @Override
            public void tableModified(SQLTableEvent evt) {
                if (evt.getMode() == Mode.ROW_ADDED) {
                    UserRightsManagerModel.this.cache.add(evt.getRow());
                } else {
                    final SQLRow row = evt.getRow();
                    for (int i = 0; i < UserRightsManagerModel.this.cache.size(); i++) {
                        final SQLRow row2 = UserRightsManagerModel.this.cache.get(i);
                        if (row2.getID() == row.getID()) {
                            if (!row.isValid()) {
                                UserRightsManagerModel.this.cache.remove(i);
                            } else {
                                UserRightsManagerModel.this.cache.set(i, row2);
                            }
                            break;
                        }
                    }
                }
            }
        });
        this.tableUserRight.addTableModifiedListener(new SQLTableModifiedListener() {

            @Override
            public void tableModified(SQLTableEvent evt) {
                if (evt.getMode() == Mode.ROW_ADDED) {
                    rowAdded(evt);
                } else {
                    rowModified(evt);
                }
            }

            public void rowAdded(SQLTableEvent evt) {
                final SQLRow row = evt.getRow();
                if (row.getInt("ID_USER_COMMON") == UserRightsManagerModel.this.idUser) {
                    SQLRowValues rowVals = getSQLRowValuesFor(row);
                    if (rowVals == null) {
                        UserRightsManagerModel.this.listRowValues.add(row.createUpdateRow());
                        fireTableRowsInserted(UserRightsManagerModel.this.listRowValues.size() - 2, UserRightsManagerModel.this.listRowValues.size() - 1);
                    }
                }
            }

            public void rowModified(SQLTableEvent evt) {
                final SQLRow row = evt.getRow();
                if (row.getInt("ID_USER_COMMON") == UserRightsManagerModel.this.idUser) {
                    SQLRowValues rowVals = getSQLRowValuesFor(row);
                    int index = UserRightsManagerModel.this.listRowValues.indexOf(rowVals);
                    if (!row.isValid()) {
                        UserRightsManagerModel.this.listRowValues.removeElement(rowVals);
                        fireTableRowsDeleted(index - 1, index + 1);
                    } else {
                        rowVals.loadAbsolutelyAll(row);
                        fireTableRowsUpdated(index - 1, index + 1);
                    }
                }
            }
        });
    }

    private SQLRowValues getSQLRowValuesFor(final SQLRow row) {
        final String string2 = row.getString("CODE");
        for (SQLRowValues rowVals : this.listRowValues) {
            final String string = rowVals.getString("CODE");
            if (rowVals.getID() == row.getID() || (string != null && string2 != null && string.equalsIgnoreCase(string2))) {
                return rowVals;
            }
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return this.columns.get(column);
    }

    @Override
    public int getColumnCount() {
        return this.columns.size();
    }

    @Override
    public int getRowCount() {
        return this.listRowValues.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Boolean.class;
        } else {
            return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex == 0);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        this.listRowValues.get(rowIndex).put("HAVE_RIGHT", value);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            Boolean b = this.listRowValues.get(rowIndex).getBoolean("HAVE_RIGHT");
            return (b == null) ? true : b;
        } else {
            return this.listRowValues.get(rowIndex).getString("NOM");
        }
    }

    public SQLRowValues getRowValuesAt(int index) {
        return this.listRowValues.get(index);
    }

    /**
     * Valide les modifications dans la base
     */
    public void commitData() {
        List<SQLRowValues> listRowVals = new ArrayList<SQLRowValues>(this.listRowValues);
        for (SQLRowValues rowVals : listRowVals) {
            try {
                SQLRow row = rowVals.commit();
                rowVals.loadAbsolutelyAll(row);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Charge les droits de l'utilisateur, vide la table si il n'existe pas
     * 
     * @param idUser
     */
    public void loadRightsForUser(int idUser) {
        this.idUser = idUser;
        if (idUser > 1) {
            SQLSelect sel = new SQLSelect(Configuration.getInstance().getBase());
            sel.addSelect(this.tableUserRight.getKey());
            sel.setWhere(new Where(this.tableUserRight.getField("ID_USER_COMMON"), "=", idUser));
            List<SQLRow> rows = (List<SQLRow>) Configuration.getInstance().getBase().getDataSource().execute(sel.asString(), new SQLRowListRSH(this.tableUserRight, true));
            Map<String, SQLRowValues> map = new HashMap<String, SQLRowValues>(rows.size());
            for (SQLRow row : rows) {
                map.put(row.getString("CODE"), row.createUpdateRow());
            }
            this.listRowValues.clear();
            for (SQLRow row : this.cache) {
                final SQLRowValues e = map.get(row.getString("CODE"));
                if (e != null) {
                    e.put("NOM", row.getString("NOM"));
                    e.put("DESCRIPTION", row.getString("DESCRIPTION"));
                    this.listRowValues.add(e);
                } else {
                    SQLRowValues rowVals = new SQLRowValues(this.tableUserRight);
                    rowVals.put("ID_USER_COMMON", idUser);
                    rowVals.put("CODE", row.getString("CODE"));
                    rowVals.put("NOM", row.getString("NOM"));
                    rowVals.put("DESCRIPTION", row.getString("DESCRIPTION"));
                    this.listRowValues.add(rowVals);
                }
            }
        } else {
            this.listRowValues.clear();
        }
        fireTableDataChanged();
    }
}
