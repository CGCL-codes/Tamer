package davidlauzon.activerecord.nodes;

import java.sql.SQLException;
import java.util.Vector;
import davidlauzon.activerecord.connection.ConnectionAdapter;
import davidlauzon.activerecord.manager.CreateTableStatementManager;
import davidlauzon.activerecord.manager.DropTableStatementManager;
import davidlauzon.activerecord.nodes.field.Field;
import davidlauzon.activerecord.visitor.SqlSerializer;

public class Table extends Node implements Cloneable, AliasableTableIF {

    /*********************************************************************************************
     * VARIABLES
     *********************************************************************************************/
    private Database _db;

    private String _name;

    private String _alias = null;

    private Field[] _fields;

    private Field[] _fieldsWithoutPrimaryKeys;

    /*********************************************************************************************
     * ONE-LINER METHODS
     *********************************************************************************************/
    @Override
    public String name() {
        return _name;
    }

    @Override
    public String alias() {
        return _alias;
    }

    @Override
    public boolean isAlias() {
        return _alias != null;
    }

    public Field[] fields() {
        return _fields;
    }

    public ConnectionAdapter getConnection() {
        return _db.getConnection();
    }

    public void setDatabase(Database db) {
        _db = db;
    }

    /*********************************************************************************************
     * PUBLIC METHODS
     *********************************************************************************************/
    public Table(String name, Field[] fields) {
        _name = name;
        _fields = fields;
    }

    public void recreate() throws SQLException {
        drop();
        create();
    }

    public long create() throws SQLException {
        return new CreateTableStatementManager().table(this).columns(_fields).ifNotExists().execute();
    }

    public String createAsSqlString() {
        return new CreateTableStatementManager().table(this).columns(_fields).ifNotExists().toSql();
    }

    public long drop() throws SQLException {
        return new DropTableStatementManager().table(this).ifExists().execute();
    }

    public String dropAsSqlString() {
        return new DropTableStatementManager().table(this).ifExists().toSql();
    }

    public Table as(String alias) {
        Table other = null;
        try {
            other = (Table) clone();
            other._alias = alias;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return other;
    }

    public Field[] getFieldsWithoutPK() {
        if (_fieldsWithoutPrimaryKeys == null) {
            Field[] allFields = fields();
            Vector<Field> nonPkFields = new Vector<Field>(allFields.length);
            for (int i = 0; i < allFields.length; i++) {
                if (!allFields[i].isPrimaryKey()) nonPkFields.add(allFields[i]);
            }
            _fieldsWithoutPrimaryKeys = (Field[]) nonPkFields.toArray(new Field[nonPkFields.size()]);
        }
        return _fieldsWithoutPrimaryKeys;
    }

    @Override
    public String accept(SqlSerializer visitor) {
        return visitor.visit(this);
    }
}
