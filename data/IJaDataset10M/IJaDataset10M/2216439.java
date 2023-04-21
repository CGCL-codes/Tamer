package sqlTools.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import sqlTools.TypesUtil;
import function.sql.SQLFunction;

public abstract class ReflectionORMObject extends ORMObject {

    /**
		Returns the name of the table that represents this
		object in the database. Or, if you like, the name of the
		database table this object is a representation of. The 
		tablename gets dynamically detected at runtime, but you could override
		this function in case you can't or won't follow the naming 
		conventions or you'd like to do performance optimizations and
		want to save some initialization/reflection/db-exploration time.
	*/
    public String getTableName() {
        initTableName();
        return tableName;
    }

    /**
		@returns the number of DB-rows affected by inserting this record. Should be wary
		if != 1
	*/
    public boolean insert() {
        if (existsInDB(this)) {
            System.err.println("[ORMObject.insert] can't insert: " + toString() + " already exists in DB.");
            return false;
        }
        Executer exe = new Executer(this);
        String result = getSQLExecuter().executePreparedStatement(getInsertStatement(), exe);
        if (result != null) {
            throw new RuntimeException("[ORMObject.insert] trouble inserting: " + toString() + " : " + result);
        }
        setLoadedFromDatabase(true);
        return exe.getNumAffected() == 1;
    }

    /**
		@returns the number of DB-rows affected by inserting this record. Should be wary
		if != 1
	*/
    public boolean update() {
        Executer exe = new Executer(this);
        String result = getSQLExecuter().executePreparedStatement(getUpdateStatement(), exe);
        if (result != null) {
            throw new RuntimeException("[ORMObject.update] trouble updating: " + toString() + " : " + result);
        }
        return exe.getNumAffected() == 1;
    }

    public boolean delete() {
        DeleteExecuter exe = new DeleteExecuter(this);
        String result = getSQLExecuter().connection(exe);
        if (result != null) {
            throw new RuntimeException("[ORMObject.delete] trouble deleting: " + toString() + " : " + result);
        }
        return exe.getNumAffected() == 1;
    }

    /**
		This is a utility method to retrieve a list of all the
		fields in this object that can be mapped to database fields. This function 
		returns a list of fieldnames as they appear in the database table.
	*/
    protected ORMField[] getORMFields() {
        initFields();
        LinkedList list = new LinkedList(publicFields.values());
        Collections.sort(list);
        return (ORMField[]) list.toArray(ORMFIELD_ARR_CAST);
    }

    protected abstract OneToOneRelation[] getOneToOneRelations();

    protected ORMField getField(String key) {
        initFields();
        ORMField field = publicFields.getField(key);
        if (field == null) field = publicFields.getField(Utils.convertToJavaName(key));
        if (field == null) {
            field = publicFields.getField(Utils.convertToDBName(key));
        }
        if (field == null) {
            System.err.println("[Warning] trying to access non-existant field: " + key);
        }
        return field;
    }

    protected ORMField[] getPrimaryKeys() {
        initPrimaryKeys();
        return primKeys;
    }

    private ORMField[] primKeys;

    private String tableName;

    private ORMHashMap publicFields = new ORMHashMap();

    private boolean initialized;

    private void initFields() {
        if (initialized) return;
        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i != fields.length; ++i) {
            if (sqlTools.TypesUtil.getSqlType(fields[i].getType()) == (java.sql.Types.NULL) || !(Modifier.isPublic(fields[i].getModifiers()) || Modifier.isProtected(fields[i].getModifiers())) || Modifier.isStatic(fields[i].getModifiers())) {
                continue;
            } else {
                ORMField oField = new ORMField(fields[i], Utils.convertToDBName(fields[i].getName()));
                publicFields.put(fields[i].getName(), oField);
            }
        }
        initialized = true;
        initFieldsDB();
    }

    private void initFieldsDB() {
        String result = getSQLExecuter().metaData(new SQLFunction() {

            public void apply(DatabaseMetaData meta) throws SQLException {
                int position = 0;
                ResultSet rset = null;
                List markedForRemoval = new LinkedList();
                for (Iterator it = publicFields.values().iterator(); it.hasNext(); ) {
                    ORMField oField = (ORMField) it.next();
                    rset = meta.getColumns(null, null, getTableName(), oField.getNameInTable());
                    int rows = 0;
                    while (rset.next()) {
                        ++rows;
                        oField.setType(rset.getInt("DATA_TYPE"));
                        ++position;
                        oField.setPosition(position);
                    }
                    if (rows > 1) {
                        throw new RuntimeException("[ORMObject.initDB] ambiguous column definition for: " + oField.getNameInTable());
                    } else if (rows == 0) {
                        rset.close();
                        oField.setNameInTable(oField.getNameInTable().toLowerCase());
                        rset = meta.getColumns(null, null, getTableName(), oField.getNameInTable());
                        while (rset.next()) {
                            ++rows;
                            oField.setType(rset.getInt("DATA_TYPE"));
                            ++position;
                            oField.setPosition(position);
                        }
                        if (rows > 1) {
                            throw new RuntimeException("[ORMObject.initDB] ambiguous column definition for: " + oField.getNameInTable());
                        }
                        if (rows == 0) {
                            markedForRemoval.add(oField.getName());
                        }
                    }
                }
                if (rset != null) {
                    rset.close();
                }
                for (Iterator it = markedForRemoval.iterator(); it.hasNext(); ) {
                    publicFields.remove(it.next());
                }
                rset = meta.getColumns(null, null, getTableName(), null);
                while (rset.next()) {
                    String col = rset.getString("COLUMN_NAME");
                    boolean error = false;
                    if ("NO".equals(rset.getString("IS_NULLABLE")) && getField(col) == null) {
                        System.err.println("[Warning] table : " + getTableName() + " has no field-mapping for the non-null " + "column: " + col + " should be: " + TypesUtil.getJavaType(rset.getInt("DATA_TYPE")));
                        error = true;
                    }
                    if (error) {
                        throw new RuntimeException("[ReflectionORMObject.initFieldsDB] incomplete implementation, no field for: " + col);
                    }
                }
            }
        });
        if (result != null) {
            throw new RuntimeException("[ORMObject]" + result);
        }
    }

    private void initTableName() {
        if (this.tableName != null) return;
        this.tableName = Utils.convertToDBName(this.getClass().getName());
        getSQLExecuter().metaData(new SQLFunction() {

            public void apply(DatabaseMetaData meta) throws SQLException {
                ResultSet rset = meta.getTables(null, null, tableName, null);
                int rows = numRows(rset);
                if (rows == 0) {
                    rset.close();
                    tableName = tableName.toLowerCase();
                    rset = meta.getTables(null, null, tableName, null);
                    rows = numRows(rset);
                    if (rows == 0) {
                        throw new RuntimeException("[ORMObject.initDB] no such DB obj:" + getTableName());
                    }
                } else if (rows != 1) {
                    throw new RuntimeException("[ORMObject.initDB] ambiguous DB objs for:" + getTableName());
                }
                rset.close();
            }
        });
    }

    private void initPrimaryKeys() {
        if (primKeys != null) return;
        getSQLExecuter().metaData(new SQLFunction() {

            public void apply(DatabaseMetaData meta) throws SQLException {
                ResultSet rset = meta.getPrimaryKeys(null, null, getTableName());
                int rows = 0;
                LinkedList list = new LinkedList();
                while (rset.next()) {
                    ++rows;
                    String col = rset.getString("COLUMN_NAME");
                    ORMField f = getField(col);
                    if (f == null) throw new RuntimeException("[ORMObject.initPrimaryKeys] primary key: " + col + " isn't present as field in class.");
                    list.add(f);
                }
                if (rows == 0) {
                    throw new RuntimeException("[ORMObject.initPrimaryKeys] no primary keys found for: " + getTableName());
                }
                primKeys = (ORMField[]) list.toArray(ORMFIELD_ARR_CAST);
            }
        });
    }

    private static final Object[] ORMFIELD_ARR_CAST = new ORMField[0];

    private static final String[] STRING_CAST = new String[0];

    private static int numRows(ResultSet rset) throws SQLException {
        int i = 0;
        while (rset.next()) {
            ++i;
        }
        return i;
    }

    public static void main(String[] args) {
    }

    class ORMHashMap extends HashMap {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        ORMField getField(Object key) {
            return (ORMField) get(key);
        }
    }

    class Executer extends SQLFunction {

        ORMObject orm;

        int i;

        public Executer(ORMObject orm) {
            this.orm = orm;
        }

        public void apply(PreparedStatement pstmt) throws SQLException {
            ORMField[] fields = orm.getORMFields();
            for (int i = 0; i != fields.length; ++i) {
                fields[i].set(orm, pstmt);
            }
            i = pstmt.executeUpdate();
            pstmt.getConnection().commit();
        }

        public int getNumAffected() {
            return i;
        }
    }

    class DeleteExecuter extends SQLFunction {

        ORMObject orm;

        int i;

        public DeleteExecuter(ORMObject orm) {
            this.orm = orm;
        }

        public void apply(Connection con) throws SQLException {
            Statement stmt = con.createStatement();
            i = stmt.executeUpdate(orm.getDeleteStatement());
        }

        public int getNumAffected() {
            return i;
        }
    }
}
