package net.sourceforge.osm2postgis.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;
import net.sourceforge.osm2postgis.model.OSMPrimitive;

public abstract class PrimitiveDAO<T extends OSMPrimitive> extends AbstractDAO<T> {

    private static Logger logger = Logger.getLogger("osm2postgis");

    static final String SQL_PRIMITIVE_FIELDS = "id,changeset,timestamp,version";

    static final String SQL_PRIMITIVE_PARAMS = "?,?,? AT TIME ZONE 'UTC',?";

    static final String SQL_PRIMITIVE_UPDATE = "changeset=?," + "timestamp=? AT TIME ZONE 'UTC',version=?";

    static final String SQL_TAG_VALUES = " (id,k,v) VALUES (?,?,?)";

    static final String SQL_PRIMITIVE_COLS = "id bigint PRIMARY KEY," + "changeset bigint NOT NULL," + "\"timestamp\" timestamp(0) without time zone NOT NULL," + "version integer NOT NULL";

    static final String SQL_TAG_COLS = "k varchar(255) DEFAULT '' NOT NULL," + "v varchar(255) DEFAULT '' NOT NULL";

    private final String sqlInsertTag;

    private final String sqlDeleteTags;

    protected PrimitiveDAO(String schema, String relation) {
        super(schema, relation);
        sqlInsertTag = "INSERT INTO " + getSchemaName() + "." + getTagsName() + SQL_TAG_VALUES;
        sqlDeleteTags = "DELETE FROM " + getSchemaName() + "." + getTagsName() + SQL_WHERE_ID;
    }

    /**
     * Sets the basic parameters for an SQL insert statement. Subclasses
     * p * should
     * create the pstmt and call super.prepare(pstmt). The parameters are: 1
     * =
     * id, 2 = changeset, 3 = timestamp, 4 = version
     * 
     * @param primitive
     * @param pstmt
     * @throws SQLException
     */
    void preparePrimitiveInsert(T primitive, PreparedStatement pstmt) throws SQLException {
        pstmt.setLong(1, primitive.getId());
        pstmt.setLong(2, primitive.getChangeSet());
        pstmt.setTimestamp(3, new java.sql.Timestamp(primitive.getTime()));
        pstmt.setInt(4, primitive.getVersion());
    }

    /**
     * Sets the basic parameters for an SQL update statement. Subclasses
     * should
     * create the pstmt and call super.prepareUpdate(pstmt). The parameters
     * are:
     * 1 = changeset, 2 = timestamp, 3 = version
     * 
     * @param primitive
     * @param pstmt
     * @throws SQLException
     */
    void preparePrimitiveUpdate(T primitive, PreparedStatement pstmt) throws SQLException {
        pstmt.setLong(1, primitive.getChangeSet());
        pstmt.setTimestamp(2, new java.sql.Timestamp(primitive.getTime()));
        pstmt.setInt(3, primitive.getVersion());
    }

    @Override
    public int delete(Long pk) throws SQLException {
        int result = deleteTags(pk);
        result += super.delete(pk);
        return result;
    }

    protected final int deleteTags(long id) throws SQLException {
        return deleteById(id, target.cacheableStatement(sqlDeleteTags));
    }

    int insertTags(T primitive) throws SQLException {
        PreparedStatement pstmt = target.cacheableStatement(sqlInsertTag);
        int result = 0;
        if (null == primitive.getTagMap()) {
        } else {
            for (Map.Entry<String, String> tag : primitive.getTagMap().entrySet()) {
                String key = tag.getKey();
                String value = tag.getValue();
                pstmt.setLong(1, primitive.getId());
                pstmt.setString(2, key);
                pstmt.setString(3, value);
                result += pstmt.executeUpdate();
            }
        }
        return result;
    }

    /**
     * Returns false if the given primitive is newer than the one with same
     * id possibly existing in the database, true if p is unchanged or
     * older, or null if no primitive with the same id exists.
     * 
     * @param p
     * @return
     */
    protected final Boolean isOld(T p) throws SQLException {
        T current = read(p.getId());
        if (null == current) return null;
        if (p.hasChanged(current, this)) {
            logger.finest("Data changed to " + p + " from " + current + ".");
            return false;
        }
        return true;
    }

    /**
     * Override to return the name of the tags table.
     * 
     * @return name of the tags table without schema name.
     */
    abstract String getTagsName();

    /**
     * Replaces the current tags of this element with those loaded from the
     * database. Does not clear any tags (except those that have empty
     * values in the database.) If this element contains any other tags,
     * they will remain as is.
     * 
     * @param primitive
     * @throws SQLException
     */
    protected final void selectTags(T primitive) throws SQLException {
        ResultSet rs = executeSelect("SELECT k,v FROM " + getSchemaName() + "." + getTagsName() + SQL_WHERE_ID, primitive.getId());
        Map<String, String> tags = primitive.getTagMap();
        while (rs.next()) {
            String key = rs.getString(1);
            String value = rs.getString(2);
            tags.put(key, value);
        }
        rs.close();
    }
}
