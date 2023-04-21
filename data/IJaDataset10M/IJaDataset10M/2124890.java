package edu.uga.galileo.voci.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import edu.uga.galileo.voci.bo.MetadataElement;
import edu.uga.galileo.voci.db.ConnectionPoolFactory;
import edu.uga.galileo.voci.db.QueryParser;
import edu.uga.galileo.voci.db.QueryParserElement;
import edu.uga.galileo.voci.exception.DataTypeMismatchException;
import edu.uga.galileo.voci.exception.NoAvailablePoolException;
import edu.uga.galileo.voci.exception.NoSuchMetadataException;
import edu.uga.galileo.voci.logging.Logger;
import edu.uga.galileo.voci.model.Configuration;
import edu.uga.galileo.voci.model.ContentType;

/**
 * {@link edu.uga.galileo.voci.db.dao.MetadataDAO} implementation for
 * PostgreSQL.
 * 
 * @author <a href="mailto:mdurant@uga.edu">Mark Durant</a>
 * @version 1.0
 */
public class PSQLMetadataDAO extends MetadataDAO {

    /**
	 * @see edu.uga.galileo.voci.db.dao.MetadataDAO#getProjectMetadataList(int,
	 *      int)
	 */
    @Override
    public ArrayList<MetadataElement> getProjectMetadataList(int projectId, int contentType) throws NoSuchMetadataException {
        StringBuffer sql = new StringBuffer();
        sql.append("select * ");
        sql.append("from metadata_registry ");
        sql.append("where project_id=? ");
        sql.append("and content_type=? ");
        sql.append("and community_id is null ");
        sql.append("and active=true ");
        sql.append("order by display_order asc ");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(projectId);
        qp.addPreparedStmtElementDefinition(contentType);
        try {
            Configuration.getConnectionPool().executeQuery(qp);
            return createObjectsFromQueryParser(MetadataElement.class, qp);
        } catch (SQLException e) {
            Logger.warn("Metadata listing couldn't be retrieved", e);
            throw new NoSuchMetadataException("SQLException prevented metadata listing retrieval: " + e.getMessage());
        }
    }

    /**
	 * @see edu.uga.galileo.voci.db.dao.MetadataDAO#getMetadataElement(int)
	 */
    @Override
    public MetadataElement getMetadataElement(int id) throws NoSuchMetadataException {
        StringBuffer sql = new StringBuffer();
        sql.append("select * ");
        sql.append("from metadata_registry ");
        sql.append("where metadata_id=? ");
        sql.append("and active=true ");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(id);
        try {
            Configuration.getConnectionPool().executeQuery(qp);
            if (qp.getResultCount() > 0) {
                return createObjectFromQueryParser(MetadataElement.class, qp);
            } else {
                throw new NoSuchMetadataException("The requested community pID (" + id + ") doesn't exist.");
            }
        } catch (SQLException e) {
            Logger.warn("Metadata record couldn't be retrieved", e);
            throw new NoSuchMetadataException("SQLException prevented metadata record retrieval: " + e.getMessage());
        }
    }

    /**
	 * @see edu.uga.galileo.voci.db.dao.MetadataDAO#addMetadataElement(edu.uga.galileo.voci.bo.MetadataElement)
	 */
    @Override
    public synchronized void addMetadataElement(MetadataElement elem) throws NoSuchMetadataException {
        int id = getNextMetadataId();
        elem.setMetadataId(id);
        StringBuffer sql = new StringBuffer();
        sql.append("insert into metadata_registry ");
        sql.append("(metadata_id, project_id, community_id, element, qualifier, ");
        sql.append("display_name, description, is_dublin_core, ");
        sql.append("repeatable, validate_regex, field_type, ");
        sql.append("index_multiplier, is_required, content_type, ");
        sql.append("display_order, is_key_field, is_unique, visible_in_face) values ");
        sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(id);
        qp.addPreparedStmtElementDefinition(elem.getProjectId());
        qp.addPreparedStmtElementDefinition(QueryParserElement.INT, elem.getCommunityId() == -1 ? null : elem.getCommunityId());
        qp.addPreparedStmtElementDefinition(elem.getElement());
        qp.addPreparedStmtElementDefinition(elem.getQualifier());
        qp.addPreparedStmtElementDefinition(elem.getDisplayName());
        qp.addPreparedStmtElementDefinition(elem.getDescription());
        qp.addPreparedStmtElementDefinition(elem.isDublinCore());
        qp.addPreparedStmtElementDefinition(elem.isRepeatable());
        qp.addPreparedStmtElementDefinition(elem.getValidateRegex());
        qp.addPreparedStmtElementDefinition(elem.getFieldType());
        qp.addPreparedStmtElementDefinition(elem.getIndexMultiplier());
        qp.addPreparedStmtElementDefinition(elem.isRequired());
        qp.addPreparedStmtElementDefinition(elem.getContentType());
        qp.addPreparedStmtElementDefinition(getNextDisplayOrderId(elem.getProjectId(), elem.getContentType()));
        qp.addPreparedStmtElementDefinition(elem.isKeyField());
        qp.addPreparedStmtElementDefinition(elem.isUnique());
        qp.addPreparedStmtElementDefinition(elem.isVisibleInFace());
        try {
            Configuration.getConnectionPool().executeInsertOrUpdate(qp);
        } catch (SQLException e) {
            Logger.error("Metadata record couldn't be added", e);
            throw new NoSuchMetadataException("SQLException prevented metadata record addition: " + e.getMessage());
        }
    }

    /**
	 * @see edu.uga.galileo.voci.db.dao.MetadataDAO#updateMetadataElement(edu.uga.galileo.voci.bo.MetadataElement)
	 */
    @Override
    public void updateMetadataElement(MetadataElement elem) throws NoSuchMetadataException {
        StringBuffer sql = new StringBuffer();
        sql.append("update metadata_registry ");
        sql.append("set element=?, qualifier=?, display_name=?, ");
        sql.append("description=?, is_dublin_core=?, is_unique=?, ");
        sql.append("repeatable=?, validate_regex=?, field_type=?, ");
        sql.append("index_multiplier=?, is_required=?, content_type=?, ");
        sql.append("visible_in_face=?, include_in_blurb=? ");
        sql.append("where metadata_id=? ");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(elem.getElement());
        qp.addPreparedStmtElementDefinition(elem.getQualifier());
        qp.addPreparedStmtElementDefinition(elem.getDisplayName());
        qp.addPreparedStmtElementDefinition(elem.getDescription());
        qp.addPreparedStmtElementDefinition(elem.isDublinCore());
        qp.addPreparedStmtElementDefinition(elem.isUnique());
        qp.addPreparedStmtElementDefinition(elem.isRepeatable());
        qp.addPreparedStmtElementDefinition(elem.getValidateRegex());
        qp.addPreparedStmtElementDefinition(elem.getFieldType());
        qp.addPreparedStmtElementDefinition(elem.getIndexMultiplier());
        qp.addPreparedStmtElementDefinition(elem.isRequired());
        qp.addPreparedStmtElementDefinition(elem.getContentType());
        qp.addPreparedStmtElementDefinition(elem.isVisibleInFace());
        qp.addPreparedStmtElementDefinition(elem.isIncludeInBlurb());
        qp.addPreparedStmtElementDefinition(elem.getMetadataId());
        try {
            Configuration.getConnectionPool().executeInsertOrUpdate(qp);
        } catch (SQLException e) {
            Logger.error("Metadata record couldn't be updated", e);
            throw new NoSuchMetadataException("SQLException prevented metadata record update: " + e.getMessage());
        }
    }

    /**
	 * @see edu.uga.galileo.voci.db.dao.MetadataDAO#deleteMetadataElement(edu.uga.galileo.voci.bo.MetadataElement,
	 *      java.sql.Connection)
	 */
    @Override
    public synchronized void deleteMetadataElement(MetadataElement meta, Connection connection) throws NoSuchMetadataException {
        try {
            int currentDisplayOrder = getDisplayOrder(meta.getMetadataId());
            StringBuffer sql = new StringBuffer();
            sql.append("delete from metadata_registry ");
            sql.append("where metadata_id=? ");
            QueryParser qp = new QueryParser(sql.toString());
            if (connection != null) {
                qp.setConnection(connection);
            }
            qp.addPreparedStmtElementDefinition(meta.getMetadataId());
            try {
                Configuration.getConnectionPool().executeInsertOrUpdate(qp);
            } catch (SQLException e) {
                Logger.error("Metadata record couldn't be disabled", e);
                throw new NoSuchMetadataException("SQLException prevented metadata record disabled: " + e.getMessage());
            }
            sql = new StringBuffer();
            sql.append("update metadata_registry ");
            sql.append("set display_order=display_order-1 ");
            sql.append("where project_id=? ");
            sql.append("and content_type=? ");
            sql.append("and display_order>? ");
            qp.clearForNewSQL();
            qp.setSql(sql.toString());
            qp.addPreparedStmtElementDefinition(meta.getProjectId());
            qp.addPreparedStmtElementDefinition(meta.getContentType());
            qp.addPreparedStmtElementDefinition(currentDisplayOrder);
            Configuration.getConnectionPool().executeInsertOrUpdate(qp);
        } catch (SQLException e) {
            Logger.error("Metadata record couldn't be disabled", e);
            throw new NoSuchMetadataException("SQLException prevented metadata record disabled: " + e.getMessage());
        }
    }

    /**
	 * @see edu.uga.galileo.voci.db.dao.MetadataDAO#moveMetadataElement(edu.uga.galileo.voci.bo.MetadataElement,
	 *      boolean, java.sql.Connection)
	 */
    @Override
    public synchronized void moveMetadataElement(MetadataElement meta, boolean up, Connection connection) throws NoSuchMetadataException {
        try {
            int currentDisplayOrder = getDisplayOrder(meta.getMetadataId());
            int maxValue = getNextDisplayOrderId(meta.getProjectId(), meta.getContentType());
            int moveTo;
            if (up) {
                if (currentDisplayOrder >= (maxValue - 1)) {
                    return;
                }
                moveTo = currentDisplayOrder + 1;
            } else {
                if (currentDisplayOrder == 0) {
                    return;
                }
                moveTo = currentDisplayOrder - 1;
            }
            StringBuffer sql = new StringBuffer();
            sql.append("update metadata_registry ");
            sql.append("set display_order=? ");
            sql.append("where project_id=? ");
            sql.append("and content_type=? ");
            sql.append("and display_order=? ");
            QueryParser qp = new QueryParser(sql.toString());
            if (connection != null) {
                qp.setConnection(connection);
            }
            qp.addPreparedStmtElementDefinition(currentDisplayOrder);
            qp.addPreparedStmtElementDefinition(meta.getProjectId());
            qp.addPreparedStmtElementDefinition(meta.getContentType());
            qp.addPreparedStmtElementDefinition(moveTo);
            Configuration.getConnectionPool().executeInsertOrUpdate(qp);
            sql = new StringBuffer();
            sql.append("update metadata_registry ");
            sql.append("set display_order=? ");
            sql.append("where metadata_id=? ");
            qp.clearForNewSQL();
            qp.setSql(sql.toString());
            qp.addPreparedStmtElementDefinition(moveTo);
            qp.addPreparedStmtElementDefinition(meta.getMetadataId());
            Configuration.getConnectionPool().executeInsertOrUpdate(qp);
        } catch (SQLException e) {
            throw new NoSuchMetadataException("SQLException prevented getting current display order for " + meta.getMetadataId());
        }
    }

    /**
	 * Get the next metadata ID from the <code>metadata_id_seq</code> database
	 * sequence.
	 * 
	 * @return The next available metadata ID from the sequence.
	 * @throws NoSuchMetadataException
	 *             If an error occurs trying to get the ID.
	 */
    private int getNextMetadataId() throws NoSuchMetadataException {
        StringBuffer sql = new StringBuffer();
        sql.append("select nextval('metadata_id_seq') as nextVal ");
        QueryParser qp = new QueryParser(sql.toString());
        try {
            Configuration.getConnectionPool().executeQuery(qp);
            return qp.getResult(Integer.class, "nextVal");
        } catch (SQLException e) {
            Logger.error("Metadata ID couldn't be retrieved b/c of SQLException", e);
            throw new NoSuchMetadataException("Couldn't get a metadata ID from the metadata_id_seq b/c of a SQLException: " + e.getMessage());
        } catch (DataTypeMismatchException e) {
            Logger.error("Metadata ID couldn't be retrieved", e);
            throw new NoSuchMetadataException("Couldn't get a metadata ID from the metadata_id_seq: " + e.getMessage());
        }
    }

    /**
	 * Get the next highest display order value for a given project and content
	 * type.
	 * 
	 * @param projectId
	 *            The project ID.
	 * @param contentType
	 *            The type of content being ordered.
	 * @return The next highest display order for the requested project and
	 *         content type.
	 */
    private int getNextDisplayOrderId(int projectId, int contentType) {
        StringBuffer sql = new StringBuffer();
        sql.append("select max(display_order)+1 as displayOrder ");
        sql.append("from metadata_registry ");
        sql.append("where project_id=? ");
        sql.append("and content_type=? ");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(projectId);
        qp.addPreparedStmtElementDefinition(contentType);
        try {
            Configuration.getConnectionPool().executeQuery(qp);
            if (qp.getResultCount() == 0) {
                return 0;
            }
            return qp.getResult(Integer.class, "displayOrder");
        } catch (SQLException e) {
            Logger.error("Display order couldn't be retrieved b/c of " + "SQLException ... returning 1000.", e);
        } catch (DataTypeMismatchException e) {
            Logger.error("Display order couldn't be retrieved ... " + "returning 1000", e);
        }
        return 1000;
    }

    /**
	 * Get the display order of a metadata element.
	 * 
	 * @param metadataId
	 *            The ID of the metadata.
	 * @throws SQLException
	 *             If something bad happens with the database.
	 */
    private int getDisplayOrder(int metadataId) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("select display_order ");
        sql.append("from metadata_registry ");
        sql.append("where metadata_id=? ");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(metadataId);
        try {
            Configuration.getConnectionPool().executeQuery(qp);
            return qp.getResult(Integer.class, "display_order");
        } catch (DataTypeMismatchException e) {
            Logger.error("Your code's requesting a bad data type for display_order.", e);
        }
        return -1;
    }

    /**
	 * @see edu.uga.galileo.voci.db.dao.MetadataDAO#getMetadataId(java.lang.String,
	 *      java.lang.String, int, int)
	 */
    @Override
    public int getMetadataId(String element, String qualifier, int projectId, int contentType) throws NoSuchMetadataException {
        StringBuffer sql = new StringBuffer();
        sql.append("select metadata_id ");
        sql.append("from metadata_registry ");
        sql.append("where element=? ");
        sql.append("and qualifier=? ");
        sql.append("and project_id=? ");
        sql.append("and content_type=? ");
        sql.append("and active=true ");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(element);
        qp.addPreparedStmtElementDefinition(qualifier == null ? "" : qualifier);
        qp.addPreparedStmtElementDefinition(projectId);
        qp.addPreparedStmtElementDefinition(contentType);
        try {
            Configuration.getConnectionPool().executeQuery(qp);
            if (qp.getResultCount() > 0) {
                try {
                    return qp.getResult(Integer.class, "metadata_id").intValue();
                } catch (DataTypeMismatchException e) {
                    Logger.error("Your metadata_id column appears to not be an int");
                }
            }
        } catch (SQLException e) {
            Logger.warn("Metadata element ID couldn't be retrieved", e);
            throw new NoSuchMetadataException("SQLException prevented metadata record retrieval: " + e.getMessage());
        }
        throw new NoSuchMetadataException("Metadata ID couldn't be retrieved for " + element + "." + qualifier + " for project " + projectId + " and content type " + contentType);
    }

    /**
	 * @see edu.uga.galileo.voci.db.dao.MetadataDAO#updateKeyField(java.lang.String,
	 *      edu.uga.galileo.voci.model.ContentType, int)
	 */
    public void updateKeyField(String projectHandle, ContentType type, int metadataId) throws NoSuchMetadataException, SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("update metadata_registry ");
        sql.append("set is_key_field=true ");
        sql.append("where project_id=");
        sql.append("  (select project_id ");
        sql.append("   from projects ");
        sql.append("   where handle=?) ");
        sql.append("and content_type=? ");
        sql.append("and metadata_id=? ");
        QueryParser qp = new QueryParser(sql.toString());
        Connection myConn = null;
        try {
            myConn = ConnectionPoolFactory.getConnectionPool().getConnection();
        } catch (NoAvailablePoolException e1) {
            throw new NoSuchMetadataException("Couldn't get metadata b/c of a connection pool problem: " + e1.toString());
        }
        myConn.setAutoCommit(false);
        qp.setConnection(myConn);
        qp.addPreparedStmtElementDefinition(projectHandle);
        qp.addPreparedStmtElementDefinition(type.getValue());
        qp.addPreparedStmtElementDefinition(metadataId);
        try {
            if (!Configuration.getConnectionPool().executeInsertOrUpdate(qp)) {
                throw new NoSuchMetadataException("No matching row was found to update");
            } else {
                qp.clearForNewSQL();
                sql = new StringBuffer();
                sql.append("update metadata_registry ");
                sql.append("set is_key_field=false ");
                sql.append("where project_id=");
                sql.append("  (select project_id ");
                sql.append("   from projects ");
                sql.append("   where handle=?) ");
                sql.append("and content_type=? ");
                sql.append("and metadata_id!=? ");
                qp.setSql(sql.toString());
                qp.addPreparedStmtElementDefinition(projectHandle);
                qp.addPreparedStmtElementDefinition(type.getValue());
                qp.addPreparedStmtElementDefinition(metadataId);
                Configuration.getConnectionPool().executeInsertOrUpdate(qp);
                qp.getConnection().commit();
            }
        } catch (SQLException e) {
            Logger.error("Metadata record couldn't be updated ... rolling back", e);
            qp.getConnection().rollback();
            throw new NoSuchMetadataException("SQLException prevented metadata record update: " + e.getMessage());
        } finally {
            qp.setConnection(null);
            myConn.setAutoCommit(true);
            Configuration.getConnectionPool().returnConnection(myConn);
        }
    }

    /**
	 * @see edu.uga.galileo.voci.db.dao.MetadataDAO#getMetadataValue(int,
	 *      java.lang.String, int)
	 */
    @Override
    public String getMetadataValue(int metadataId, String contentDescriptor, int vboID) {
        StringBuffer sql = new StringBuffer();
        sql.append("select value ");
        sql.append("from metadata2");
        sql.append(contentDescriptor);
        sql.append(" ");
        sql.append("where ");
        sql.append(contentDescriptor);
        sql.append("_id=? ");
        sql.append("and metadata_id=? ");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(vboID);
        qp.addPreparedStmtElementDefinition(metadataId);
        try {
            Configuration.getConnectionPool().executeQuery(qp);
            if (qp.getResultCount() > 0) {
                try {
                    return qp.getResult(String.class, "value");
                } catch (DataTypeMismatchException e) {
                    Logger.error("Your 'value' column in the metadata2" + contentDescriptor + " table appears to not be a String");
                }
            }
        } catch (SQLException e) {
            Logger.warn("Metadata value couldn't be retrieved", e);
            return null;
        }
        return null;
    }

    /**
	 * @see edu.uga.galileo.voci.db.dao.MetadataDAO#getMetadataValuesForVBO(java.lang.String,
	 *      int)
	 */
    @Override
    public HashMap<Integer, String> getMetadataValuesForVBO(String contentDescriptor, int vboID) {
        HashMap<Integer, String> results = new HashMap<Integer, String>();
        StringBuffer sql = new StringBuffer();
        sql.append("select metadata_id, value ");
        sql.append("from metadata2");
        sql.append(contentDescriptor);
        sql.append(" ");
        sql.append("where ");
        sql.append(contentDescriptor);
        sql.append("_id=? ");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(vboID);
        try {
            Configuration.getConnectionPool().executeQuery(qp);
            if (qp.getResultCount() > 0) {
                ArrayList row;
                for (int m = 0; m < qp.getResultCount(); m++) {
                    row = qp.getRowResults(m);
                    results.put((Integer) row.get(0), (String) row.get(1));
                }
            }
        } catch (SQLException e) {
            Logger.error("Metadata values couldn't be retrieved for " + contentDescriptor + " VBO " + vboID, e);
            return null;
        }
        return results;
    }

    /**
	 * @see edu.uga.galileo.voci.db.dao.MetadataDAO#deleteMetadataMappings(int,
	 *      java.lang.String, java.sql.Connection)
	 */
    @Override
    public void deleteMetadataMappings(int vboId, String contentTypeDescriptor, Connection connection) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("delete from metadata2");
        sql.append(contentTypeDescriptor);
        sql.append(" ");
        sql.append("where ");
        sql.append(contentTypeDescriptor);
        sql.append("_id=? ");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(vboId);
        if (connection != null) {
            qp.setConnection(connection);
        }
        try {
            Configuration.getConnectionPool().executeInsertOrUpdate(qp);
        } catch (SQLException e) {
            Logger.warn("Couldn't delete metadata for VBO " + vboId, e);
        }
    }

    /**
	 * @see edu.uga.galileo.voci.db.dao.MetadataDAO#addMetadataMapping(int, int,
	 *      java.lang.String, java.lang.String, java.sql.Connection)
	 */
    @Override
    public void addMetadataMapping(int vboId, int metadataId, String value, String contentTypeDescriptor, Connection connection) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into metadata2");
        sql.append(contentTypeDescriptor);
        sql.append(" (");
        sql.append(contentTypeDescriptor);
        sql.append("_id, metadata_id, value) ");
        sql.append("values (?, ?, ?) ");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(vboId);
        qp.addPreparedStmtElementDefinition(metadataId);
        qp.addPreparedStmtElementDefinition(value);
        if (connection != null) {
            qp.setConnection(connection);
        }
        try {
            Configuration.getConnectionPool().executeInsertOrUpdate(qp);
        } catch (SQLException e) {
            Logger.warn("Couldn't insert metadata mapping for VBO " + vboId + ", metadata ID " + metadataId + ", and value: " + value, e);
            throw e;
        }
    }

    /**
	 * @see edu.uga.galileo.voci.db.dao.MetadataDAO#isUniqueMetadataValue(int,
	 *      int, int, java.lang.String)
	 */
    @Override
    public boolean isUniqueMetadataValue(int metadataId, int elementId, int contentType, String value) {
        String contentDescriptor = ContentType.valueOf(contentType).toString().toLowerCase();
        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) as dupeCount ");
        sql.append("from metadata2");
        sql.append(contentDescriptor);
        sql.append(" ");
        sql.append("where ");
        sql.append(contentDescriptor);
        sql.append("_id!=? ");
        sql.append("and metadata_id=? ");
        sql.append("and value=? ");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(elementId);
        qp.addPreparedStmtElementDefinition(metadataId);
        qp.addPreparedStmtElementDefinition(value);
        try {
            Configuration.getConnectionPool().executeQuery(qp);
            if (qp.getResult(Integer.class, "dupeCount") == 0) {
                return true;
            }
        } catch (SQLException e) {
            Logger.error("Metadata uniqueness couldn't be checked " + "due to SQLException", e);
        } catch (DataTypeMismatchException e) {
            Logger.error("Data type mismatch: " + e.getMessage(), e);
        }
        return false;
    }

    /**
	 * @see edu.uga.galileo.voci.db.dao.MetadataDAO#getMetadataBlurbFields(java.lang.String,
	 *      edu.uga.galileo.voci.model.ContentType)
	 */
    public ArrayList<String> getMetadataBlurbFields(String projectHandle, ContentType contentType) {
        StringBuffer sql = new StringBuffer();
        sql.append("select element, qualifier, display_name ");
        sql.append("from metadata_registry ");
        sql.append("where project_id=");
        sql.append("   (select project_id from projects ");
        sql.append("    where handle=?) ");
        sql.append("and content_type=? ");
        sql.append("and include_in_blurb=true ");
        sql.append("order by display_order ");
        QueryParser qp = new QueryParser(sql.toString());
        qp.addPreparedStmtElementDefinition(projectHandle);
        qp.addPreparedStmtElementDefinition(contentType.getValue());
        try {
            Configuration.getConnectionPool().executeQuery(qp);
            if (qp.getResultCount() > 0) {
                ArrayList<String> results = new ArrayList<String>();
                String result;
                ArrayList row;
                for (int m = 0; m < qp.getResultCount(); m++) {
                    row = qp.getRowResults(m);
                    result = (String) row.get(0);
                    if ((row.get(1) != null) && (((String) row.get(1)).length() > 0)) {
                        result += "." + (String) row.get(1);
                    }
                    result += " (" + ((String) row.get(2)) + ")";
                    results.add(result);
                }
                return results;
            }
        } catch (SQLException e) {
            Logger.error("Metadata blurb fields couldn't be found for " + projectHandle + "/" + contentType, e);
        }
        return null;
    }
}
