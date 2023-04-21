package org.sqlexp.sql;

/**
 * Stored procedure group.
 * @author Matthieu Réjou
 */
public class SqlProcedureGroup extends SqlObjectGroup<SqlDatabase, SqlProcedure> {

    /**
	 * @param parent to add the object to
	 */
    protected SqlProcedureGroup(final SqlDatabase parent) {
        super(parent);
    }

    @Override
    protected final String getInternalPathName() {
        return "procedures";
    }
}
