package org.cilogon.service.storage.impl.postgres;

import edu.uiuc.ncsa.security.storage.sql.ColumnDescriptorEntry;
import static java.sql.Types.LONGVARCHAR;
import static java.sql.Types.TIMESTAMP;

/**
 * A little clarification is needed. The ArchivedUser object wraps a User. The SQL that stores this, however, has one
 * archived user and the user information as a single row. Therefore, logically it makes sense that this class
 * extends the {@link org.cilogon.service.storage.impl.postgres.UsersTable} where the user and archived user are
 * independent of each other. This fact is mostly recorded here for internal information.
 * <p>Created by Jeff Gaynor<br>
 * on Apr 13, 2010 at  2:07:56 PM
 */
public class ArchivedUsersTable extends UsersTable {

    public String archivedUserTablename = "old_user";

    public ArchivedUsersTable(String schema, String tablenamePrefix) {
        super(schema, tablenamePrefix);
        userTablename = getTablename();
        setTablename(archivedUserTablename);
    }

    public ArchivedUsersTable() {
        super();
        userTablename = getTablename();
        setTablename(archivedUserTablename);
    }

    String userTablename;

    public String getUserTablename() {
        return userTablename;
    }

    public void setTablename(String tablename) {
        super.setTablename(tablename);
    }

    @Override
    public String getPrimaryKeyColumnName() {
        return getArchivedUserIDColumn();
    }

    public String getArchivedUserIDColumn() {
        return archivedUserIDColumn;
    }

    public void setArchivedUserIDColumn(String archivedUserIDColumn) {
        this.archivedUserIDColumn = archivedUserIDColumn;
    }

    String archivedUserIDColumn = "archived_user_id";

    String archivedTimestampColumn = "archive_time";

    public String getArchivedTimestampColumn() {
        return archivedTimestampColumn;
    }

    public void setArchivedTimestampColumn(String col) {
        archivedTimestampColumn = col;
    }

    public String userExistsStatement() {
        return "SELECT * FROM " + getTablename() + " WHERE " + getArchivedUserIDColumn() + " = ?";
    }

    public String getArchivedUserByUID() {
        return "SELECT * FROM " + getTablename() + " WHERE " + getArchivedUserIDColumn() + "= ? ";
    }

    @Override
    public void createColumnDescriptors() {
        super.createColumnDescriptors();
        getColumnDescriptor().add(new ColumnDescriptorEntry(getArchivedUserIDColumn(), LONGVARCHAR));
        getColumnDescriptor().add(new ColumnDescriptorEntry(getArchivedTimestampColumn(), TIMESTAMP));
        getColumnDescriptor().get(getUserIDColumn()).setPrimaryKey(false);
    }

    public String addArchiveUser() {
        return "insert into " + getTablename() + " " + "(" + getArchivedUserIDColumn() + "," + getRemoteUserColumn() + "," + getIDPColumn() + "," + getIDPDisplayNameColumn() + "," + getUserIDColumn() + "," + getSerialStringColumn() + "," + getFirstnameColumn() + "," + getLastnameColumn() + "," + getEmailColumn() + "," + getCreationTimestampColumn() + "," + getArchivedTimestampColumn() + ")" + "select ?," + getRemoteUserColumn() + "," + getIDPColumn() + "," + getIDPDisplayNameColumn() + "," + getUserIDColumn() + "," + getSerialStringColumn() + "," + getFirstnameColumn() + "," + getLastnameColumn() + "," + getEmailColumn() + "," + getCreationTimestampColumn() + "," + "CURRENT_TIMESTAMP FROM " + getUserTablename() + " where " + getUserIDColumn() + " = ?";
    }

    public String getArchivedUser() {
        return "SELECT * FROM " + getTablename() + " WHERE " + getUserIDColumn() + " = ? ORDER BY " + getArchivedTimestampColumn();
    }
}
