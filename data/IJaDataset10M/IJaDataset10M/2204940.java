package org.sqlexp.sql;

/**
 * Sql view representation.
 * @author Matthieu Réjou
 */
public class SqlView extends SqlObject<SqlViewGroup, SqlObjectGroup<SqlView, ?>> {

    private final SqlColumnGroup columnGroup;

    /**
	 * @param parent to add the object to
	 * @param name of the object
	 */
    public SqlView(final SqlViewGroup parent, final String name) {
        super(parent, name);
        columnGroup = new SqlColumnGroup(this);
    }

    /**
	 * @return the columnGroup
	 */
    public final SqlColumnGroup getColumnGroup() {
        return columnGroup;
    }

    /**
	 * @param name of the object
	 * @return column
	 */
    public final SqlColumn getColumn(final String name) {
        return columnGroup.getChild(name);
    }

    /**
	 * <b>For vendor specific needs only</b>.
	 * @param vendorId vendor specific identifier
	 * @return column
	 */
    public final SqlColumn getColumn(final int vendorId) {
        return columnGroup.getChild(vendorId);
    }
}
