package de.gstpl.data.person;

class _VisibleTable extends org.objectstyle.cayenne.CayenneDataObject {

    protected static final String COLUMN_NAME_PROPERTY = "columnName";

    protected static final String TABLE_NAME_PROPERTY = "tableName";

    protected static final String GROUP_PROPERTY = "group";

    protected static final String VISIBLE_TABLE_ID_PK_COLUMN = "VISIBLE_TABLE_ID";

    protected void _setColumnName(String columnName) {
        writeProperty("columnName", columnName);
    }

    protected String _getColumnName() {
        return (String) readProperty("columnName");
    }

    protected void _setTableName(String tableName) {
        writeProperty("tableName", tableName);
    }

    protected String _getTableName() {
        return (String) readProperty("tableName");
    }

    protected void _setGroup(Group group) {
        setToOneTarget("group", group, true);
    }

    protected Group _getGroup() {
        return (Group) readProperty("group");
    }
}
