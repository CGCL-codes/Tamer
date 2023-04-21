package il.ac.biu.cs.grossmm.impl.activeData;

import il.ac.biu.cs.grossmm.api.OperationFailedException;
import il.ac.biu.cs.grossmm.api.data.NodeType;
import il.ac.biu.cs.grossmm.api.data.NodeTypeByInterface;
import il.ac.biu.cs.grossmm.api.flow.ActiveDataManager;
import il.ac.biu.cs.grossmm.api.flow.PersistentPointManager;
import il.ac.biu.cs.grossmm.api.flow.PointTypeMissmatchException;
import il.ac.biu.cs.grossmm.api.keys.ArrayKeyPattern;
import il.ac.biu.cs.grossmm.api.keys.Attribute;
import il.ac.biu.cs.grossmm.api.keys.KeyPattern;
import il.ac.biu.cs.grossmm.api.keys.PatternEntry;
import il.ac.biu.cs.grossmm.api.server.Component;
import il.ac.biu.cs.grossmm.api.server.ComponentManager;
import il.ac.biu.cs.grossmm.api.sql.SqlDataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class SqlPointManager implements PersistentPointManager, Component {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(SqlPointManager.class);

    ActiveDataManagerImpl adm;

    Connection conn;

    PreparedStatement insertDefinition;

    PreparedStatement deleteDefinition;

    PreparedStatement selectDefinitionByKey;

    PreparedStatement selectDefinitionIdByKey;

    PreparedStatement selectDefinitionIdByName;

    PreparedStatement insertName;

    PreparedStatement deleteName;

    PreparedStatement isReachable;

    PreparedStatement callIdentity;

    Cache<SqlNodeSet, Set<SqlInnerNode>> subnodesCache;

    Cache<SqlNodeBase, Object[]> valuesCache;

    Map<KeyPattern, SqlPublicationPoint> publicationPoints;

    SqlDataSource dataSource;

    private SqlInspector curInspector;

    static final int ROOT_CACHE_SIZE = 1000;

    public SqlPointManager() {
        this.publicationPoints = new HashMap<KeyPattern, SqlPublicationPoint>();
        this.subnodesCache = new Cache<SqlNodeSet, Set<SqlInnerNode>>(new SqlSubnodesRetriever(), ROOT_CACHE_SIZE);
        this.valuesCache = new Cache<SqlNodeBase, Object[]>(new SqlValuesRetriever(), ROOT_CACHE_SIZE);
    }

    void init() throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        conn = dataSource.getConnection();
        try {
            update("CREATE TABLE definitions (id INTEGER IDENTITY, key_pattern VARBINARY, node_type VARBINARY)");
            update("CREATE TABLE names (name VARCHAR(256) PRIMARY KEY, pp_id INTEGER)");
        } catch (SQLException e) {
            logger.debug(e);
        }
        curInspector = new SqlInspector();
        insertDefinition = conn.prepareStatement("INSERT INTO definitions (key_pattern, node_type) VALUES(?, ?)");
        deleteDefinition = conn.prepareStatement("DELETE FROM definitions WHERE id=?");
        selectDefinitionByKey = conn.prepareStatement("SELECT key_pattern, node_type FROM definitions WHERE id = ?");
        selectDefinitionIdByKey = conn.prepareStatement("SELECT id FROM definitions WHERE key_pattern = ?");
        selectDefinitionIdByName = conn.prepareStatement("SELECT pp_id FROM names WHERE name = ?");
        insertName = conn.prepareStatement("INSERT INTO names (name, pp_id) VALUES (?, ?)");
        deleteName = conn.prepareStatement("DELETE FROM names where name=?");
        isReachable = conn.prepareStatement("SELECT count(*) FROM names where pp_id=?");
        callIdentity = conn.prepareStatement("CALL IDENTITY()");
    }

    int getLastId() throws SQLException {
        ResultSet rs = callIdentity.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    /**
	 * Executes sql update command
	 * 
	 * @param command
	 *            sql update command to execute
	 * @throws SQLException
	 */
    synchronized void update(String command) throws SQLException {
        if (logger.isDebugEnabled()) {
            logger.debug("update() - start");
            logger.debug("SQL = [" + command + "]");
        }
        Statement st = null;
        st = conn.createStatement();
        int i = st.executeUpdate(command);
        if (i == -1) {
            throw new SQLException("Unknown Error (executeUpadte returned -1)");
        }
        st.close();
        if (logger.isDebugEnabled()) {
            logger.debug("update() - end");
        }
    }

    public synchronized <N> SqlPublicationPoint<N> create(KeyPattern pattern, NodeType<N> nodeType, String name) throws OperationFailedException {
        if (logger.isDebugEnabled()) {
            logger.debug("create(" + pattern + ", " + nodeType + ", " + name + ") - start");
        }
        SqlRootMapping<N> mapping;
        try {
            byte[] patternBytes = getPatternBytes(pattern);
            byte[] nodeTypeBytes = getNodeTypeBytes(nodeType);
            int id = definitionIdByPattern(patternBytes);
            int id1 = definitionIdByName(name);
            if (id == -1) {
                if (id1 != -1) {
                    throw new Exception("PP Identifier " + name + " is alredy registered to a different PP type");
                }
            } else {
                if (id1 == -1) {
                    PatternNodeTypePair pnp = getPatternDefinition(id);
                    if (!pnp.pattern.equals(pattern)) throw new PointTypeMissmatchException(name);
                    registerIdentifier(name, id);
                    return get(name);
                } else if (id == id1) {
                    return get(name);
                } else {
                    throw new Exception("PP Identifier " + name + " is alredy registered to a different PP type");
                }
            }
            id = insertPointDefinition(patternBytes, nodeTypeBytes);
            String tableBase = getTableNamePrefix(id);
            mapping = new SqlRootMapping<N>(conn, tableBase, nodeType, pattern);
            if (logger.isDebugEnabled()) {
                logger.debug("PP Prefix=" + tableBase);
            }
            mapping.createTables();
            registerIdentifier(name, id);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ignored) {
            }
            throw new OperationFailedException(e);
        }
        SqlPublicationPoint<N> sqlPublicationPoint;
        try {
            conn.commit();
            update("CHECKPOINT");
            conn.commit();
            DataLayer<N> dataLayer = adm.getLayer(nodeType);
            sqlPublicationPoint = new SqlPublicationPoint<N>(mapping, dataLayer, pattern, name, valuesCache, subnodesCache, ROOT_CACHE_SIZE);
        } catch (SQLException e) {
            throw new OperationFailedException(e);
        }
        publicationPoints.put(pattern, sqlPublicationPoint);
        if (logger.isDebugEnabled()) {
            logger.debug("create() - end");
        }
        return sqlPublicationPoint;
    }

    /**
	 * @param id
	 * @return
	 */
    private String getTableNamePrefix(int id) {
        String tableBase = "nodes_" + id + "_";
        return tableBase;
    }

    @SuppressWarnings("unchecked")
    public synchronized <N> SqlPublicationPoint<N> get(String name) throws OperationFailedException {
        try {
            int id = definitionIdByName(name);
            if (id == -1) return null;
            PatternNodeTypePair pnp = getPatternDefinition(id);
            SqlPublicationPoint<N> pp = publicationPoints.get(pnp.getPattern());
            if (pp != null) return pp;
            String tableBase = getTableNamePrefix(id);
            KeyPattern pattern = pnp.getPattern();
            NodeTypeByInterface nodeType = pnp.getNodeType();
            SqlRootMapping<N> mapping = new SqlRootMapping<N>(conn, tableBase, nodeType, pattern);
            DataLayer<N> dataLayer = adm.getLayer(nodeType);
            pp = new SqlPublicationPoint<N>(mapping, dataLayer, pattern, name, valuesCache, subnodesCache, ROOT_CACHE_SIZE);
            publicationPoints.put(pattern, pp);
            return pp;
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    /**
	 * @param <N>
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    private <N> PatternNodeTypePair getPatternDefinition(int id) throws SQLException, IOException, ClassNotFoundException {
        selectDefinitionByKey.setInt(1, id);
        ResultSet rs = selectDefinitionByKey.executeQuery();
        if (!rs.next()) throw new SQLException("PP definition with id=" + id + " does not exist");
        byte[] patternBytes = rs.getBytes(1);
        byte[] nodeBytes = rs.getBytes(2);
        KeyPattern pattern = getPattern(patternBytes);
        NodeTypeByInterface nodeType = getNodeType(nodeBytes);
        PatternNodeTypePair pnp = new PatternNodeTypePair(pattern, nodeType);
        return pnp;
    }

    /**
	 * @param name
	 * @param id
	 * @throws SQLException
	 * @throws Exception
	 */
    private void registerIdentifier(String name, int id) throws SQLException {
        insertName.setString(1, name);
        insertName.setInt(2, id);
        if (insertName.executeUpdate() != 1) {
            throw new SQLException("Could not insert publication point definition");
        }
        conn.commit();
    }

    /**
	 * @param name
	 * @throws SQLException
	 * @throws Exception
	 */
    private void deregisterIdentifier(String name) throws SQLException {
        deleteName.setString(1, name);
        if (deleteName.executeUpdate() != 1) {
            throw new SQLException("Could not insert publication point definition");
        }
        conn.commit();
    }

    private boolean isReachable(int id) throws SQLException {
        isReachable.setInt(1, id);
        ResultSet rs = isReachable.executeQuery();
        rs.next();
        return rs.getInt(1) != 0;
    }

    /**
	 * @param name
	 * @return
	 * @throws SQLException
	 */
    private int definitionIdByName(String name) throws SQLException {
        int id1;
        selectDefinitionIdByName.setString(1, name);
        ResultSet rs = null;
        try {
            rs = selectDefinitionIdByName.executeQuery();
            if (rs.next()) id1 = rs.getInt(1); else id1 = -1;
        } finally {
            if (rs != null) rs.close();
        }
        return id1;
    }

    private int definitionIdByPattern(byte[] patternBytes) throws SQLException {
        selectDefinitionIdByKey.setBytes(1, patternBytes);
        ResultSet rs = null;
        try {
            rs = selectDefinitionIdByKey.executeQuery();
            if (!rs.next()) {
                return -1;
            }
            return rs.getInt(1);
        } finally {
            if (rs != null) rs.close();
        }
    }

    /**
	 * @param pattern
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    protected ArrayKeyPattern getPattern(byte[] patternBytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream patternStream = new ByteArrayInputStream(patternBytes);
        return readPattern(new ObjectInputStream(patternStream));
    }

    /**
	 * @param <N>
	 * @param nodeType
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    protected NodeTypeByInterface getNodeType(byte[] nodeBytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream nodeTypeStream = new ByteArrayInputStream(nodeBytes);
        return readNodeType(new ObjectInputStream(nodeTypeStream));
    }

    /**
	 * @param pattern
	 * @return
	 * @throws IOException
	 */
    protected byte[] getPatternBytes(KeyPattern pattern) throws IOException {
        ByteArrayOutputStream patternStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(patternStream);
        writePattern(pattern, oos);
        oos.close();
        byte[] patternBytes = patternStream.toByteArray();
        return patternBytes;
    }

    /**
	 * @param <N>
	 * @param nodeType
	 * @return
	 * @throws IOException
	 */
    protected byte[] getNodeTypeBytes(NodeType nodeType) throws IOException {
        ByteArrayOutputStream nodeTypeStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(nodeTypeStream);
        writeNodeType((NodeTypeByInterface) nodeType, oos);
        oos.close();
        byte[] nodeTypeBytes = nodeTypeStream.toByteArray();
        return nodeTypeBytes;
    }

    void writePattern(KeyPattern pattern, ObjectOutput out) throws IOException {
        int size = pattern.size();
        Class clazz = pattern.valueClass();
        boolean asValue = clazz == null;
        out.writeBoolean(pattern.isMask());
        out.writeBoolean(asValue);
        out.writeObject(asValue ? pattern.value() : clazz);
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            PatternEntry e = pattern.entry(i);
            out.writeObject(e.getAttribute());
            out.writeBoolean(e.isMandatory());
            writePattern(e.getPattern(), out);
        }
    }

    ArrayKeyPattern readPattern(ObjectInput in) throws IOException, ClassNotFoundException {
        boolean isMask = in.readBoolean();
        boolean asValue = in.readBoolean();
        Serializable value = (Serializable) in.readObject();
        int size = in.readInt();
        ArrayKeyPattern pattern = new ArrayKeyPattern(isMask, value, asValue);
        for (int i = 0; i < size; i++) {
            Attribute attribute = (Attribute) in.readObject();
            boolean isMandatory = in.readBoolean();
            ArrayKeyPattern nested = readPattern(in);
            pattern.add(attribute, nested, isMandatory);
        }
        return pattern;
    }

    void writeNodeType(NodeTypeByInterface nodeType, ObjectOutput out) throws IOException {
        out.writeUTF(nodeType.getNodeInterface().getName());
    }

    @SuppressWarnings("unchecked")
    NodeTypeByInterface readNodeType(ObjectInput in) throws IOException, ClassNotFoundException {
        String className = in.readUTF();
        Class nodeInterface = Class.forName(className);
        return NodeTypeByInterface.nodeType(nodeInterface);
    }

    public void aborted(Object hint, Exception e) {
        throw new RuntimeException("Not implemented");
    }

    public void setComponentManager(ComponentManager container) throws Exception {
        dataSource = (SqlDataSource) container.getComponent(SqlDataSource.class);
        adm = (ActiveDataManagerImpl) container.getComponent(ActiveDataManager.class);
        init();
    }

    @Override
    protected void finalize() throws Throwable {
        if (dataSource != null) {
            Connection c = dataSource.getConnection();
            Statement s = c.createStatement();
            s.execute("SHUTDOWN");
            s.close();
            c.close();
            System.out.println("DB Shut Down.");
        }
    }

    public synchronized boolean remove(String name) throws OperationFailedException {
        try {
            int id = definitionIdByName(name);
            if (id == -1) {
                return false;
            }
            deregisterIdentifier(name);
            if (isReachable(id)) return true;
            PatternNodeTypePair pnp = getPatternDefinition(id);
            SqlPublicationPoint pp = publicationPoints.get(pnp.getPattern());
            SqlRootMapping mapping;
            if (pp != null) {
                mapping = pp.rootMapping;
            } else {
                mapping = new SqlRootMapping(conn, getTableNamePrefix(id), pnp.getNodeType(), pnp.getPattern());
            }
            assert mapping != null;
            try {
                deletePointDefinition(id);
                mapping.dropTables();
                conn.commit();
                update("CHECKPOINT");
                conn.commit();
                publicationPoints.remove(pnp.getPattern());
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationFailedException(e);
        }
    }

    /**
	 * @param patternBytes
	 * @param nodeTypeBytes
	 * @return
	 * @throws SQLException
	 */
    private int insertPointDefinition(byte[] patternBytes, byte[] nodeTypeBytes) throws SQLException {
        int id;
        insertDefinition.setBytes(1, patternBytes);
        insertDefinition.setBytes(2, nodeTypeBytes);
        if (insertDefinition.executeUpdate() != 1) {
            throw new SQLException("Could not insert publication point definition");
        }
        id = getLastId();
        return id;
    }

    /**
	 * @param id
	 * @throws SQLException
	 */
    private void deletePointDefinition(int id) throws SQLException {
        deleteDefinition.setInt(1, id);
        deleteDefinition.executeUpdate();
    }

    public boolean pointExists(String identifier) throws OperationFailedException {
        try {
            return definitionIdByName(identifier) != -1;
        } catch (SQLException e) {
            throw new OperationFailedException(e);
        }
    }

    public boolean pointExists(KeyPattern pattern) throws OperationFailedException {
        try {
            byte[] patternBytes;
            patternBytes = getPatternBytes(pattern);
            int id = definitionIdByPattern(patternBytes);
            return id != -1;
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }
}
