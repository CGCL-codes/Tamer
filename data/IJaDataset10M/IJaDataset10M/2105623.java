package de.spotnik.nestedset.persistence.hibernate;

import de.spotnik.nestedset.AbstractNode;
import de.spotnik.nestedset.persistence.INestedSet;
import de.spotnik.persistence.DatabaseException;
import de.spotnik.persistence.hibernate.AbstractDAO;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;

/**
 * Implementation of a nested set to persist tree like or thread based data.
 * This is optimized for reading and not for adding or updating the set. <br/>
 * <br/> Implementation based on the tutorial <a
 * href="http://www.develnet.org/36.html">"Das 'Nested Sets' Modell - B�ume mit
 * SQL"</a> from Daniel T. Gorski and Holger Morgenstern.
 * 
 * @param <T> The concrete implementation of the {@link AbstractNode}
 * @author Jens Rehp�hler
 * @created 25.02.2006
 */
public abstract class AbstractNestedSetDAO<T extends AbstractNode> extends AbstractDAO implements INestedSet<T> {

    /** the class logger. */
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(AbstractNestedSetDAO.class);

    /** update query for the left branch for adding new nodes. */
    private static final String ADD_LEFT = "UPDATE ${class} n SET n.lft = (n.lft + ?) WHERE n.rootNode = ? AND n.lft > ?";

    /** update query for the right branch for adding new nodes. */
    private static final String ADD_RIGHT = "UPDATE ${class} n SET n.rgt = (n.rgt + ?) WHERE n.rootNode = ? AND n.rgt >= ?";

    /** update query for the left branch for deleting nodes. */
    private static final String DEL_LEFT = "UPDATE ${class} n SET n.lft = n.lft - ? WHERE n.rootNode = ? AND lft > ?";

    /** update query for the right branch for deleting nodes. */
    private static final String DEL_RIGHT = "UPDATE ${class} n SET n.rgt = n.rgt - ? WHERE n.rootNode = ? AND rgt > ?";

    /** read a subtree from the database */
    private static final String GET_SUBTREE = "FROM ${class} n1, ${class} n2 WHERE n2.id = :id AND n1.lft BETWEEN n2.lft AND n2.rft";

    /** deletes a complete subtree */
    private static final String DEL_SUBTREE = "DELETE FROM ${class} n WHERE n.lft BETWEEN :dropLft AND :dropRft";

    public synchronized void addChild(T parent, T child) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("adding child: " + child + " to parent: " + parent);
        }
        updateNodes(parent, 2, ADD_LEFT);
        updateNodes(parent, 2, ADD_RIGHT);
        getCurrentSession().refresh(parent);
        parent.setLeaf(false);
        getCurrentSession().persist(parent);
        child.setRootNode(parent.getRootNode());
        child.setLft(parent.getRgt());
        child.setRgt(parent.getRgt() + 1);
        child.setParentNode(parent);
        getCurrentSession().persist(child);
    }

    public synchronized void addNode(T node) {
        node.setRootNode(node);
        getCurrentSession().persist(node);
    }

    public abstract boolean contains(T node) throws DatabaseException;

    public List<AbstractNode> getCompleteSubtree(T node) {
        return getSubtree((T) node.getRootNode());
    }

    public List<AbstractNode> getSubtree(T node) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("reading thread for node: " + node);
        }
        Query q = createQuery(GET_SUBTREE.replace("${class}", node.getClass().getName()));
        q.setParameter("id", node.getId());
        return q.list();
    }

    public synchronized void removeNode(T node) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("removing node: " + node);
        }
        getCurrentSession().delete(node);
        updateNodes(node, 2, DEL_LEFT);
        updateNodes(node, 2, DEL_RIGHT);
    }

    public synchronized void removeSubtree(T node) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("removing thread of node: " + node);
        }
        String q = DEL_SUBTREE.replace("${class}", node.getClass().getName());
        getHibernateTemplate().bulkUpdate(q, new Object[] { node.getId() });
    }

    /**
     * update the branches of the nested set for adding/deleting nodes.
     * 
     * @param node the parent node
     * @param move move count
     * @param hqlQuery the update query
     */
    private synchronized void updateNodes(T node, int move, String hqlQuery) {
        String q = hqlQuery.replace("${class}", node.getClass().getName());
        getHibernateTemplate().bulkUpdate(q, new Object[] { new Long(move), node.getRootNode(), node.getRgt() });
    }
}
