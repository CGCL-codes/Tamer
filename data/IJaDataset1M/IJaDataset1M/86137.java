package saadadb.query.result;

import java.sql.SQLException;
import java.util.Set;
import saadadb.exceptions.QueryException;
import saadadb.meta.AttributeHandler;

/**
 * @author michel
 * * @version $Id: OidResultSet.java 118 2012-01-06 14:33:51Z laurent.mistahl $

 */
public abstract class OidResultSet {

    protected SaadaQLMetaSet col_names;

    protected int limit;

    protected int size;

    protected OidResultSet(int limit) throws SQLException {
        if (limit <= 0) {
            this.limit = Integer.MAX_VALUE;
        } else {
            this.limit = limit;
        }
        this.computeSize();
    }

    public abstract boolean next() throws SQLException;

    public abstract Object getObject(int col_num) throws SQLException;

    public abstract Object getObject(String col_name) throws SQLException;

    public abstract char getChar(int col_num) throws SQLException;

    public abstract char getChar(String col_name) throws SQLException;

    public abstract byte getByte(int col_num) throws SQLException;

    public abstract byte getByte(String col_name) throws SQLException;

    public abstract short getShort(int col_num) throws SQLException;

    public abstract short getShort(String col_name) throws SQLException;

    public abstract int getInt(int col_num) throws SQLException;

    public abstract int getInt(String col_name) throws SQLException;

    public abstract long getLong(int col_num) throws SQLException;

    public abstract long getLong(String col_name) throws SQLException;

    public abstract float getFloat(int col_num) throws SQLException;

    public abstract float getFloat(String col_name) throws SQLException;

    public abstract double getDouble(int col_num) throws SQLException;

    public abstract double getDouble(String col_name) throws SQLException;

    public abstract boolean getBoolean(int col_num) throws SQLException;

    public abstract boolean getBoolean(String col_name) throws SQLException;

    public abstract long getOid() throws SQLException;

    public abstract void close() throws QueryException;

    protected abstract void computeSize() throws SQLException;

    /**
	 * Returns the ResultSet size and rewind it
	 * @return
	 */
    public int getSize() {
        return this.size;
    }

    /**
	 * Set the resultset cursor at row #row
	 * @param row
	 * @return
	 */
    public abstract boolean setCursor(int row);

    /**
	 * Returns a column description formated in AttributeHandlers
	 */
    public abstract Set<AttributeHandler> getMeta();
}
