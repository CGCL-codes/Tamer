package com.healthmarketscience.sqlbuilder.dbspec.basic;

import java.util.ArrayList;
import java.util.List;
import com.healthmarketscience.sqlbuilder.dbspec.Constraint;

/**
 * Representation of a (table or column) constraint in a database schema.
 *
 * @author James Ahlborn
 */
public class DbConstraint extends DbObject<DbObject<?>> implements Constraint {

    /** the type for this constraint */
    private final Type _type;

    /** constrained columns */
    private List<DbColumn> _columns = new ArrayList<DbColumn>();

    public DbConstraint(DbColumn parent, String name, Type type) {
        this((DbObject<?>) parent, name, type);
        _columns.add(parent);
    }

    public DbConstraint(DbTable parent, String name, Type type, String... colNames) {
        this(parent, name, type, parent.findColumns(colNames));
    }

    public DbConstraint(DbTable parent, String name, Type type, DbColumn... columns) {
        this(parent, name, type);
        addObjects(_columns, parent, columns);
    }

    private DbConstraint(DbObject<?> parent, String name, Type type) {
        super(parent, name);
        _type = type;
    }

    public Type getType() {
        return _type;
    }

    public String getConstraintNameSQL() {
        return getName();
    }

    public List<DbColumn> getColumns() {
        return _columns;
    }

    @Override
    public String toString() {
        String name = super.toString();
        return "" + getType() + ((name != null) ? (" " + name) : "");
    }
}
