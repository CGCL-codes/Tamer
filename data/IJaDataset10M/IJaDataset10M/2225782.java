package com.velocityme.entity;

import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;
import javax.rmi.*;
import java.util.*;
import com.velocityme.interfaces.*;
import com.velocityme.valueobjects.NodeValue;

/**
 * This is the base class for all nodes in the tree.
 *
 * @author  Robert Crida Work
 * @ejb.bean
 *           type="CMP"
 *           cmp-version="2.x"
 *           name="Node"
 *           jndi-name="ejb/Node"
 *           view-type="local"
 * @ejb.transaction type="Required"
 *
 * @ejb.value-object match="*"
 *
 * @ejb.persistence table-name="node"
 *
 * @ejb.util generate="physical"
 *
 * @ejb.finder signature="com.velocityme.interfaces.Node findRoot()"
 *              query="SELECT OBJECT(o) FROM Node o WHERE o.nodeId = 2"
 *
 * @ejb.finder signature="com.velocityme.interfaces.Node findSystemNodeByName(java.lang.String p_name)"
 *              query="SELECT OBJECT(o) FROM Node o WHERE o.name = ?1 AND o.finalType = 'SystemNode'"
 *
 * @ejb.finder signature="com.velocityme.interfaces.Node findDirectoryNode()"
 *              query="SELECT OBJECT(o) FROM Node o WHERE o.type = 'Node' AND o.finalType = 'Directory'"
 *
 * @ejb.finder signature="java.util.Collection findSubLevel(com.velocityme.interfaces.SearchLevelLocal p_level)"
 *              query="SELECT DISTINCT OBJECT(c) FROM SearchLevel s, IN (s.nodesLocal) n, IN (n.childrenNodesLocal) c WHERE s = ?1 AND c.dBObjectLocal.isDeleted = FALSE" 
 *
 * @ejb.finder signature="java.util.Collection findSearchNodes(com.velocityme.interfaces.SearchLocal p_search)"
 *              query="SELECT DISTINCT OBJECT(n) FROM Search s, IN (s.searchLevelsLocal) l, IN (l.nodesLocal) n WHERE s = ?1 AND n.dBObjectLocal.isDeleted = FALSE" 
 *
 * @jboss.persistence create-table="true"
 *                    remove-table="false"
 **/
public abstract class NodeBean implements EntityBean {

    /**
    * Context set by container
    */
    private javax.ejb.EntityContext m_entityContext;

    public void setEntityContext(javax.ejb.EntityContext entityContext) {
        m_entityContext = entityContext;
    }

    public void unsetEntityContext() {
        m_entityContext = null;
    }

    /** @ejb.create-method */
    public com.velocityme.interfaces.NodePK ejbCreate(com.velocityme.interfaces.DBObjectLocal p_dBObjectLocal, com.velocityme.valueobjects.NodeValue p_value) throws CreateException {
        setNodeId(p_dBObjectLocal.getDBObjectId());
        return null;
    }

    public void ejbPostCreate(com.velocityme.interfaces.DBObjectLocal p_dBObjectLocal, com.velocityme.valueobjects.NodeValue p_value) throws CreateException {
        setNodeValue(p_value);
        setDBObjectLocal(p_dBObjectLocal);
    }

    /** @ejb.create-method 
     *  @ejb.interface-method view-type="local"
     */
    public com.velocityme.interfaces.NodePK ejbCreate(com.velocityme.interfaces.DBObjectLocal p_dBObjectLocal, com.velocityme.valueobjects.NodeValue p_value, com.velocityme.interfaces.NodeLocal p_parentNodeLocal) throws CreateException {
        setNodeId(p_dBObjectLocal.getDBObjectId());
        return null;
    }

    public void ejbPostCreate(com.velocityme.interfaces.DBObjectLocal p_dBObjectLocal, com.velocityme.valueobjects.NodeValue p_value, com.velocityme.interfaces.NodeLocal p_parentNodeLocal) throws CreateException {
        setNodeValue(p_value);
        setDBObjectLocal(p_dBObjectLocal);
        setParentNodeLocal(p_parentNodeLocal);
    }

    /**
     *  This gets a local object from a remote object.
     *  @ejb.interface-method view-type="remote"
     */
    public com.velocityme.interfaces.NodeLocal getLocal() {
        return (NodeLocal) m_entityContext.getEJBLocalObject();
    }

    /** @ejb.interface-method view-type="local" */
    public abstract com.velocityme.valueobjects.NodeValue getNodeValue();

    /** @ejb.interface-method view-type="local" */
    public abstract void setNodeValue(com.velocityme.valueobjects.NodeValue p_value);

    /**
     * @ejb.persistence column-name="nodeId"
     * @ejb.interface-method view-type="local"
     * @ejb.pk-field 
     */
    public abstract java.lang.Integer getNodeId();

    /** @ejb.interface-method view-type="local" */
    public abstract void setNodeId(java.lang.Integer nodeId);

    /**
     * @ejb.persistence column-name="name"
     * @ejb.interface-method view-type="local"
     */
    public abstract java.lang.String getName();

    /** @ejb.interface-method view-type="local" */
    public abstract void setName(java.lang.String name);

    /**
     * @ejb.persistence column-name="description"
     *                  jdbc-type="VARCHAR"
     *                  sql-type="${velocityme.sql.text.type}"
     * @ejb.interface-method view-type="local"
     */
    public abstract java.lang.String getDescription();

    /** @ejb.interface-method view-type="local" */
    public abstract void setDescription(java.lang.String description);

    /**
     * @ejb.persistence column-name="type"
     * @ejb.interface-method view-type="local"
     */
    public abstract java.lang.String getType();

    /** @ejb.interface-method view-type="local" */
    public abstract void setType(java.lang.String type);

    /**
     * @ejb.persistence column-name="finalType"
     * @ejb.interface-method view-type="local"
     */
    public abstract java.lang.String getFinalType();

    /** @ejb.interface-method view-type="local" */
    public abstract void setFinalType(java.lang.String type);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="Node-DBObject"
     *               role-name="Node-has-a-DBObject"
     *               target-ejb="DBObject"
     *               target-role-name="DBObject-has-a-Node"
     *               target-multiple="no"
     * @jboss.relation fk-column="dBObjectIdFk"
     *                 related-pk-field="dBObjectId"
     */
    public abstract com.velocityme.interfaces.DBObjectLocal getDBObjectLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setDBObjectLocal(com.velocityme.interfaces.DBObjectLocal dBObjectLocal);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="Node-ParentNode"
     *               role-name="Node-has-a-parent-Node"
     * @jboss.relation fk-column="parentNodeIdFk"
     *                 related-pk-field="nodeId"
     */
    public abstract com.velocityme.interfaces.NodeLocal getParentNodeLocal();

    /** @ejb.interface-method view-type="local"  */
    public abstract void setParentNodeLocal(com.velocityme.interfaces.NodeLocal parentNode);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="Node-ParentNode"
     *               role-name="Node-has-many-child-Nodes"
     */
    public abstract java.util.Collection getChildrenNodesLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setChildrenNodesLocal(java.util.Collection childrenNodes);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="Contactable-Node"
     *               role-name="Node-may-have-a-Contactable"
     */
    public abstract com.velocityme.interfaces.ContactableLocal getContactableLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setContactableLocal(com.velocityme.interfaces.ContactableLocal contactableLocal);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="User-Node"
     *               role-name="Node-may-have-a-User"
     */
    public abstract com.velocityme.interfaces.UserLocal getUserLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setUserLocal(com.velocityme.interfaces.UserLocal userLocal);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="Group-Node"
     *               role-name="Node-may-have-a-Group"
     */
    public abstract com.velocityme.interfaces.GroupLocal getGroupLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setGroupLocal(com.velocityme.interfaces.GroupLocal groupLocal);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="Role-Node"
     *               role-name="Node-may-have-a-Role"
     */
    public abstract com.velocityme.interfaces.RoleLocal getRoleLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setRoleLocal(com.velocityme.interfaces.RoleLocal roleLocal);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="Task-Node"
     *               role-name="Node-may-have-a-Task"
     */
    public abstract com.velocityme.interfaces.TaskLocal getTaskLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setTaskLocal(com.velocityme.interfaces.TaskLocal taskLocal);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="TaskStateMachine-Node"
     *               role-name="Node-may-have-a-TaskStateMachine"
     */
    public abstract com.velocityme.interfaces.TaskStateMachineLocal getTaskStateMachineLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setTaskStateMachineLocal(com.velocityme.interfaces.TaskStateMachineLocal taskStateMachineLocal);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="Status-Node"
     *               role-name="Node-may-have-a-Status"
     */
    public abstract com.velocityme.interfaces.StatusLocal getStatusLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setStatusLocal(com.velocityme.interfaces.StatusLocal statusLocal);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="State-Node"
     *               role-name="Node-may-have-a-State"
     */
    public abstract com.velocityme.interfaces.StateLocal getStateLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setStateLocal(com.velocityme.interfaces.StateLocal stateLocal);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="Sprint-Node"
     *               role-name="Node-may-have-a-Sprint"
     */
    public abstract com.velocityme.interfaces.SprintLocal getSprintLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setSprintLocal(com.velocityme.interfaces.SprintLocal sprintLocal);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="UserAccess-Node"
     *               role-name="Node-may-have-many-UserAccesses"
     */
    public abstract java.util.Collection getUserAccessesLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setUserAccessesLocal(java.util.Collection userAccessesLocal);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="GroupAccess-Node"
     *               role-name="Node-may-have-many-GroupAccesses"
     */
    public abstract java.util.Collection getGroupAccessesLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setGroupAccessesLocal(java.util.Collection groupAccessesLocal);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="SearchLevel-Node"
     *               role-name="Node-has-many-SearchLevels"
     * @jboss.relation fk-column="nodeId"
     *                 related-pk-field="searchLevelId"
     */
    public abstract java.util.Collection getSearchLevelsLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setSearchLevelsLocal(java.util.Collection searchLevels);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="NodeLink-FromNode"
     *               role-name="Node-has-many-from-Links"
     */
    public abstract java.util.Collection getFromLinksLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setFromLinksLocal(java.util.Collection fromLinks);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="NodeLink-ToNode"
     *               role-name="Node-has-many-to-Links"
     */
    public abstract java.util.Collection getToLinksLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setToLinksLocal(java.util.Collection toLinks);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.relation name="User-BookmarkNode"
     *               role-name="Node-has-many-BookmarkUsers"
     * @jboss.relation fk-column="nodeId"
     *                 related-pk-field="userId"
     * @jboss.relation-table table-name="userbookmarknode"
     *                       create-table="true"
     *                       remove-table="false"
     */
    public abstract java.util.Collection getBookmarkUsersLocal();

    /** @ejb.interface-method view-type="local" */
    public abstract void setBookmarkUsersLocal(java.util.Collection bookmarkUsers);

    /**
     * @ejb.interface-method view-type="local"
     * @ejb.select query="SELECT OBJECT(o) FROM Node o WHERE o.parentNodeLocal = ?1 AND o.dBObjectLocal.isDeleted = FALSE"
     */
    public abstract java.util.Collection ejbSelectOrderedChildren(com.velocityme.interfaces.NodeLocal p_nodeLocal) throws FinderException;

    /**
     * @ejb.interface-method view-type="local"
     */
    public java.lang.String getPathName() {
        ArrayList nodes = new ArrayList();
        nodes.add(0, getName());
        NodeLocal parentNodeLocal = getParentNodeLocal();
        while (parentNodeLocal != null) {
            nodes.add(0, parentNodeLocal.getName());
            parentNodeLocal = parentNodeLocal.getParentNodeLocal();
        }
        String pathName = new String();
        Iterator i = nodes.iterator();
        pathName = (String) i.next();
        while (i.hasNext()) {
            pathName += "/" + (String) i.next();
        }
        return pathName;
    }

    /**
     * @ejb.interface-method view-type="local"
     */
    public java.util.Collection getAllLinkedNodesLocal() {
        Collection linksLocal = new ArrayList();
        Iterator i = getToLinksLocal().iterator();
        while (i.hasNext()) {
            NodeLinkLocal nodeLinkLocal = (NodeLinkLocal) i.next();
            linksLocal.add(nodeLinkLocal.getFromNodeLocal());
        }
        i = getFromLinksLocal().iterator();
        while (i.hasNext()) {
            NodeLinkLocal nodeLinkLocal = (NodeLinkLocal) i.next();
            linksLocal.add(nodeLinkLocal.getToNodeLocal());
        }
        return linksLocal;
    }
}
