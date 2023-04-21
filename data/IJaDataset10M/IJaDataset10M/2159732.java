package de.benedetto.obj;

import java.sql.*;
import de.benedetto.database.*;

public abstract class IndexEntry extends DB_TupleWithIDandName {

    static String sequenceName = new String("b_index_seq");

    protected DB_IntegerField id;

    protected DB_StringField content;

    protected IndexEntryType entryType;

    protected DB_StringField comment;

    protected IndexEntry reference;

    public abstract String getTableName();

    public abstract IndexEntry getNewIndexEntry();

    public String getSequenceName() {
        return sequenceName;
    }

    protected IndexEntry(Database p_database) {
        super(p_database);
        id = new DB_IntegerField("id");
        content = new DB_StringField("i_content");
        comment = new DB_StringField("i_comment");
        UserDB db = (UserDB) database;
        entryType = db.getIndexType(getTableName()).getEntryType("eintrag");
        reference = null;
    }

    public String getContent() {
        return content.getString();
    }

    public void setContent(String str) {
        content.setString(str);
        if (content.getChanged() == true) changed = true;
    }

    public void resetId() {
        id = new DB_IntegerField("id");
    }

    public int getId() {
        return id.getInt();
    }

    public String getComment() {
        return comment.getString();
    }

    public void setComment(String str) {
        comment.setString(str);
        if (comment.getChanged()) changed = true;
    }

    public IndexEntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(String typeName) {
        UserDB db = (UserDB) database;
        IndexType indexType = db.getIndexType(getTableName());
        if (indexType != null) {
            IndexEntryType type = indexType.getEntryType(typeName);
            if (type != null) {
                changed = true;
                entryType = type;
            }
        }
    }

    public void setEntryType(IndexEntryType type) {
        if (type != entryType) {
            changed = true;
            entryType = type;
        }
    }

    public IndexEntry getReference() {
        return reference;
    }

    public boolean setReference(String str) {
        str = str.trim();
        if (str.length() == 0) str = null;
        if (reference == null) {
            if (str != null) {
                reference = getNewIndexEntry();
                if (reference.read(str)) {
                    changed = true;
                    return true;
                } else {
                    reference = null;
                    return false;
                }
            }
        } else {
            if (str == null) {
                changed = true;
                reference = null;
                return true;
            } else {
                if (reference.getContent().compareTo(str) != 0) {
                    changed = true;
                    reference = getNewIndexEntry();
                    if (reference.read(str)) {
                        return true;
                    } else {
                        reference = null;
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void store() {
        updateContent();
        if (content.getString() == null) return;
        if (changed) {
            try {
                database.execute("BEGIN");
                int newContentID = readContentID(content.getString());
                if (id.isNull()) {
                    if (newContentID == -1) {
                        add();
                    } else {
                        id.setInt(newContentID);
                    }
                } else {
                    if ((newContentID != id.getInt()) && newContentID != -1) {
                        remapAddedEntry(newContentID);
                        delete();
                    } else {
                        update();
                    }
                }
                database.execute("COMMIT");
            } catch (SQLException e) {
                Database.handleException(null, e);
            }
        }
    }

    public int readContentID(String content) {
        try {
            SQL_Command cmd = new SQL_Command();
            cmd.append("SELECT id FROM " + getTableName() + " WHERE i_content = '");
            cmd.appendData(content);
            cmd.append("';");
            ResultSet rs = database.executeQuery(cmd.toString());
            if (rs.next()) {
                return rs.getInt("id");
            }
            rs.close();
        } catch (SQLException e) {
            Database.handleException(null, e);
        }
        return -1;
    }

    public boolean read(int p_id) {
        UserDB db = (UserDB) database;
        try {
            String cmd = "SELECT r.*, t.name" + " FROM " + getTableName() + " r, b_indexentrytype t" + " WHERE r.id = " + p_id + " AND r.i_entrytype = t.id" + ";";
            ResultSet rs = database.executeQuery(cmd);
            if (rs.next()) {
                changed = false;
                content.setValue(rs);
                id.setInt(p_id);
                IndexType indexType = db.getIndexType(getTableName());
                entryType = indexType.getEntryType(rs.getString("name"));
                comment.setValue(rs);
                int referenceId = rs.getInt("i_reference");
                if (referenceId > 0) {
                    reference = getNewIndexEntry();
                    if (reference.read(referenceId) == false) {
                        reference = null;
                    }
                }
                readAdditionalFields(rs);
                rs.close();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Database.handleException(null, e);
        }
        return false;
    }

    public boolean read(String p_content) {
        UserDB db = (UserDB) database;
        try {
            SQL_Command cmd = new SQL_Command();
            cmd.append("SELECT r.*, t.name FROM ");
            cmd.append(getTableName());
            cmd.append(" r, b_indexentrytype t WHERE r.i_content = '");
            cmd.appendData(p_content);
            cmd.append("' AND r.i_entrytype = t.id;");
            ResultSet rs = database.executeQuery(cmd.toString());
            if (rs.next()) {
                changed = false;
                content.setString(p_content);
                id.setValue(rs);
                IndexType indexType = db.getIndexType(getTableName());
                entryType = indexType.getEntryType(rs.getString("name"));
                comment.setValue(rs);
                int referenceId = rs.getInt("i_reference");
                if (referenceId > 0) {
                    reference = getNewIndexEntry();
                    if (reference.read(referenceId) == false) {
                        reference = null;
                    }
                }
                readAdditionalFields(rs);
                rs.close();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Database.handleException(null, e);
        }
        return false;
    }

    public void add() {
        try {
            ResultSet rs = database.executeQuery("SELECT nextval('b_index_seq');");
            if (rs.next()) {
                id.setInt(rs.getInt("nextval"));
            }
            rs.close();
            SQL_Command command = new SQL_Command();
            SQL_Command cmdfield = new SQL_Command();
            SQL_Command cmddata = new SQL_Command();
            command.append("INSERT INTO ");
            command.append(getTableName());
            id.dbAdd(cmdfield, cmddata);
            content.dbAdd(cmdfield, cmddata);
            cmdfield.append(", i_entrytype");
            cmddata.append(", ");
            cmddata.append(String.valueOf(entryType.getId()));
            comment.dbAdd(cmdfield, cmddata);
            if (reference != null) {
                cmdfield.append(", i_reference");
                cmddata.append(", " + reference.getId());
            }
            addAdditionalFields(cmdfield, cmddata);
            cmdfield.append(")");
            cmddata.append(");");
            command.append(cmdfield.toString());
            command.append(cmddata.toString());
            database.execute(command.toString());
            changed = false;
        } catch (SQLException e) {
            Database.handleException(null, e);
        }
    }

    public void update() {
        SQL_Command command = new SQL_Command();
        command.append("UPDATE ");
        command.append(getTableName());
        command.append(" SET ");
        command.append("i_entrytype = ");
        command.append(String.valueOf(entryType.getId()));
        content.dbUpdate(command);
        comment.dbUpdate(command);
        if (reference != null) {
            command.append(", i_reference = ");
            command.append(String.valueOf(reference.getId()));
        } else {
            command.append(", i_reference = 0");
        }
        updateAdditionalFields(command);
        command.append(" WHERE id = ");
        command.append(id.getString());
        command.append(";");
        try {
            database.execute(command.toString());
            changed = false;
        } catch (SQLException e) {
            Database.handleException(null, e);
        }
    }

    public void delete() {
        try {
            SQL_Command cmd = new SQL_Command();
            cmd.append("DELETE FROM " + getTableName() + " WHERE id = " + id.getString() + ";");
            database.execute(cmd.toString());
        } catch (SQLException e) {
            Database.handleException(null, e);
        }
    }

    public void remapAddedEntry(int newContentID) throws SQLException {
        SQL_Command cmd = new SQL_Command();
        cmd.append("UPDATE b_addedentry SET ae_index = ");
        cmd.append(String.valueOf(newContentID));
        cmd.append(" WHERE ae_index = ");
        cmd.append(String.valueOf(id.getInt()));
        cmd.append(";");
        database.execute(cmd.toString());
    }

    public void updateContent() {
    }

    protected void addAdditionalFields(SQL_Command cmd, SQL_Command dat) {
    }

    protected void updateAdditionalFields(SQL_Command cmd) {
    }

    protected void readAdditionalFields(ResultSet rs) throws SQLException {
    }
}
