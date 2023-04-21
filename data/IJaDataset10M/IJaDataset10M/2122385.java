package org.freebxml.omar.server.persistence.rdb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.xml.bind.JAXBException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.server.common.RegistryProperties;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.persistence.PersistenceManager;
import org.freebxml.omar.server.persistence.PersistenceManagerFactory;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.rim.ClassificationNode;
import org.oasis.ebxml.registry.bindings.rim.ClassificationNodeType;
import org.oasis.ebxml.registry.bindings.rim.ClassificationSchemeType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRef;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;

/**
 * Concept instances are used to define tree structures where each node in the tree is a Concept. Such
 * classificationNode trees constructed with Concepts are used to define classificationNode schemes or ontologies.
 *
 * Note this interface used to be called ClassificationNode.
 *
 * @see <{ClassificationNode}>
 * @author Farrukh S. Najmi
 * @author Adrian Chong
 */
class ClassificationNodeDAO extends RegistryObjectDAO {

    private static final Log log = LogFactory.getLog(ClassificationNodeDAO.class);

    boolean codeCannotBeNull = Boolean.valueOf(RegistryProperties.getInstance().getProperty("omar.persistence.rdb.ClassificationNodeDAO.CodeCannotBeNull", "true")).booleanValue();

    /**
     * Use this constructor only.
     */
    ClassificationNodeDAO(ServerRequestContext context) {
        super(context);
    }

    public static String getTableNameStatic() {
        return "ClassificationNode";
    }

    public String getTableName() {
        return getTableNameStatic();
    }

    protected void deleteComposedObjects(Object object) throws RegistryException {
        super.deleteComposedObjects(object);
        if (object instanceof RegistryObjectType) {
            RegistryObjectType ro = (RegistryObjectType) object;
            ArrayList parentIds = new ArrayList();
            parentIds.add(ro.getId());
        }
    }

    protected void insertComposedObjects(Object object) throws RegistryException {
        super.insertComposedObjects(object);
        if (object instanceof RegistryObjectType) {
            RegistryObjectType ro = (RegistryObjectType) object;
            ClassificationNodeType node = (ClassificationNodeType) ro;
            ClassificationNodeDAO classificationNodeDAO = new ClassificationNodeDAO(context);
            classificationNodeDAO.setParent(node);
            classificationNodeDAO.insert(node.getClassificationNode());
        }
    }

    /**
     * Returns the SQL fragment string needed by insert or update statements 
     * within insert or update method of sub-classes. This is done to avoid code
     * duplication.
     */
    protected String getSQLStatementFragment(Object ro) throws RegistryException {
        ClassificationNodeType classificationNode = (ClassificationNodeType) ro;
        String stmtFragment = null;
        String code = classificationNode.getCode();
        String parentId = classificationNode.getParent();
        if (parentId == null) {
            if (parent != null) {
                parentId = ((RegistryObjectType) parent).getId();
                classificationNode.setParent(parentId);
            }
        }
        if (parentId != null) {
            parentId = "'" + parentId + "'";
        }
        String path = classificationNode.getPath();
        if (action != DAO_ACTION_DELETE) {
            if (path == null) {
                path = generatePath(classificationNode);
                classificationNode.setPath(path);
            }
        }
        if (action == DAO_ACTION_INSERT) {
            stmtFragment = "INSERT INTO ClassificationNode " + super.getSQLStatementFragment(ro) + ", '" + code + "', " + parentId + ", '" + path + "' ) ";
        } else if (action == DAO_ACTION_UPDATE) {
            stmtFragment = "UPDATE ClassificationNode SET " + super.getSQLStatementFragment(ro) + ", code='" + code + "', parent=" + parentId + ", path='" + path + "' WHERE id = '" + ((RegistryObjectType) ro).getId() + "' ";
        } else if (action == DAO_ACTION_DELETE) {
            stmtFragment = super.getSQLStatementFragment(ro);
        }
        return stmtFragment;
    }

    /**
     * Generate the path for the specified ClassificationNode. The path starts with a slash, followed
     * by the id of its ClassificationScheme, if any, which is followed by codes
     * of the intermidate ClassificationNodes and ends with the code of this
     * ClassificationNode. If any of the ClassificationNode does not have code,
     * empty string is appended for that ClassificationNode.
     * <br>
     */
    public String generatePath(ClassificationNodeType node) throws RegistryException {
        String path = null;
        String parentId = node.getParent();
        RegistryObjectType _parent = null;
        try {
            if (parentId == null) {
                path = "/" + node.getId();
            } else {
                if (_parent == null) {
                    _parent = (RegistryObjectType) context.getSubmittedObjectsMap().get(parentId);
                    if (_parent == null) {
                        PersistenceManager pm = PersistenceManagerFactory.getInstance().getPersistenceManager();
                        ObjectRefType parentRef = bu.rimFac.createObjectRef();
                        parentRef.setId(parentId);
                        _parent = pm.getRegistryObject(context, parentRef);
                        if (_parent == null) {
                            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.couldNotFindParent", new Object[] { parentId, node.getId(), node.getCode() }));
                        }
                    }
                }
                String parentPath = null;
                if (_parent instanceof ClassificationSchemeType) {
                    parentPath = null;
                } else if (_parent instanceof ClassificationNodeType) {
                    parentPath = ((ClassificationNodeType) _parent).getPath();
                } else {
                    throw new RegistryException(ServerResourceBundle.getInstance().getString("message.ClassificationNodeNotMatch", new Object[] { node.getId(), node.getCode(), _parent.getClass().getName() }));
                }
                if (parentPath == null) {
                    if (_parent instanceof ClassificationSchemeType) {
                        parentPath = "/" + _parent.getId();
                    } else if (_parent instanceof ClassificationNodeType) {
                        parentPath = generatePath((ClassificationNodeType) _parent);
                        ((ClassificationNodeType) _parent).setPath(parentPath);
                    }
                }
                String code = node.getCode();
                if (code == null) {
                    String msg = ServerResourceBundle.getInstance().getString("message.ClassificationNodeNotMatch", new Object[] { node.getId() });
                    if (codeCannotBeNull) {
                        throw new RegistryException(msg);
                    } else {
                        log.warn(msg);
                    }
                }
                path = parentPath + "/" + code;
                node.setPath(path);
            }
        } catch (javax.xml.bind.JAXBException e) {
            throw new RegistryException(e);
        }
        return path;
    }

    protected String checkClassificationNodeReferences(java.sql.Connection conn, String nodeId) throws RegistryException {
        String referencingNodeId = null;
        PreparedStatement stmt = null;
        try {
            String sql = "SELECT id FROM ClassificationNode WHERE " + "parent=? AND parent IS NOT NULL";
            stmt = context.getConnection().prepareStatement(sql);
            stmt.setString(1, nodeId);
            ResultSet rs = stmt.executeQuery();
            log.trace("SQL = " + sql);
            if (rs.next()) {
                referencingNodeId = rs.getString(1);
            }
            return referencingNodeId;
        } catch (SQLException e) {
            throw new RegistryException(e);
        } finally {
            closeStatement(stmt);
        }
    }

    protected void loadObject(Object obj, ResultSet rs) throws RegistryException {
        try {
            if (!(obj instanceof org.oasis.ebxml.registry.bindings.rim.ClassificationNode)) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.ClassificationNodeExpected", new Object[] { obj }));
            }
            ClassificationNode node = (ClassificationNode) obj;
            super.loadObject(obj, rs);
            String code = rs.getString("code");
            node.setCode(code);
            String parent = rs.getString("parent");
            if (parent != null) {
                ObjectRef or = bu.rimFac.createObjectRef();
                or.setId(parent);
                context.getObjectRefs().add(or);
                node.setParent(parent);
            }
            String path = rs.getString("path");
            node.setPath(path);
        } catch (SQLException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), e);
            throw new RegistryException(e);
        } catch (JAXBException j) {
            log.error(ServerResourceBundle.getInstance().getString("message.CaughtException1"), j);
            throw new RegistryException(j);
        }
    }

    /**
     * Creates an unitialized binding object for the type supported by this DAO.
     */
    Object createObject() throws JAXBException {
        ClassificationNode obj = bu.rimFac.createClassificationNode();
        return obj;
    }
}
