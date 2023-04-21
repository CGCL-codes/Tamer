package de.spotnik.nestedset.persistence;

import de.spotnik.nestedset.AbstractNode;
import de.spotnik.persistence.DatabaseException;
import java.util.List;

/**
 * INestedSetDAO.
 * 
 * @author Jens Rehp�hler
 * @since 29.04.2006
 * @param <T>
 */
public interface INestedSet<T extends AbstractNode> {

    /**
     * adds a new child to the parent node.
     * 
     * @param parent parent node
     * @param child child node
     */
    void addChild(T parent, T child);

    /**
     * adds a new node without any children and without a parent node.
     * 
     * @param node the new node
     */
    void addNode(T node);

    /**
     * Check if a node exists in the nested set.
     * 
     * @param node to check
     * @return true if the node exists
     * @throws DatabaseException
     */
    boolean contains(T node) throws DatabaseException;

    /**
     * get all nodes of a thread (subtree), starting with the root node of the
     * given node.
     * 
     * @param node a node of a thread
     * @return the nodes of the thread
     */
    List<AbstractNode> getCompleteSubtree(T node);

    /**
     * get all nodes of a thread (subtree), starting with the given node.
     * 
     * @param node the node
     * @return the nodes of the thread
     */
    List<AbstractNode> getSubtree(T node);

    /**
     * removes the node from the nested set.
     * 
     * @param node the node
     */
    void removeNode(T node);

    /**
     * removes a complete thread, starting with the Node, from the set.
     * 
     * @param node the root node of the threat (subtree) to remove
     */
    void removeSubtree(T node);
}
