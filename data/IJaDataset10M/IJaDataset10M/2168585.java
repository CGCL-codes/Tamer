package net.sourceforge.squirrel_sql.plugins.mssql.sql.constraint;

import java.util.ArrayList;

public class MssqlConstraint {

    /**
     * Holds value of property constraintName.
     */
    private String _constraintName;

    private ArrayList<String> _constraintColumns;

    /** Creates a new instance of MssqlConstraint */
    public MssqlConstraint() {
        _constraintColumns = new ArrayList<String>();
    }

    /**
     * Getter for property constraintName.
     * @return Value of property constraintName.
     */
    public String getConstraintName() {
        return this._constraintName;
    }

    /**
     * Setter for property constraintName.
     * @param constraintName New value of property constraintName.
     */
    public void setConstraintName(String constraintName) {
        this._constraintName = constraintName;
    }

    public void addConstraintColumn(String columnName) {
        _constraintColumns.add(columnName);
    }

    public Object[] getConstraintColumns() {
        return _constraintColumns.toArray();
    }

    public boolean constrainsColumn(String columnName) {
        for (int i = 0; i < _constraintColumns.size(); i++) if (columnName.equals(_constraintColumns.get(i))) return true;
        return false;
    }
}
