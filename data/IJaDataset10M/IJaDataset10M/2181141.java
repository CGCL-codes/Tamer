package exfex.common.displaysystem;

import java.util.LinkedList;
import java.util.List;

/** Helper Class for session tree.
 *
 * Session node keeps session instance (returned with getSession method) and
 * all children of session (returned with getChildren method). If you want to
 * add resp. remove child use {add,remove}Child methods.
 * It implements equals method to compare two SessionNode objects.
 *
 * 
 * <pre>
 * Changes:
 * 30.9.2005	msts -	equals rewritten
 * 			hashCode and toString implemented
 * 20.8.2005	msts - 	created
 * </pre>
 * 
 * @author msts
 */
public class SessionNode {

    /** Session object for this node. */
    private ISession session;

    /** List of all children.
	 *
	 * This field is created when first addChild is called.
	 */
    private LinkedList<SessionNode> children = null;

    /** Forbiden empty constructor.
	 *
	 * We want to create only nodes with session specified.
	 */
    private SessionNode() {
    }

    ;

    /** Constructor.
	 *
	 * Sets session field to given one.
	 * 
	 * @param session Session for node. 
	 */
    public SessionNode(ISession session) {
        this.session = session;
    }

    /** Returns session instance. 
	 * 
	 * @return Session object.
	 */
    public ISession getSession() {
        return session;
    }

    /** Compares for equality.
	 *
	 * Given object equals with this object iff following conditions are 
	 * true:
	 * <ul>
	 * <li>o is instance of SessionNode class
	 * <li>o.getSession equals with this session
	 * <li>all children session nodes are also in o.getChildren
	 * </ul>
	 *
	 * @param o Node to compare.
	 * @return true if node is same as this, otherwise false.
	 */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SessionNode)) return false;
        SessionNode node = (SessionNode) o;
        if (!(getSession().equals(node.getSession()))) return false;
        List<SessionNode> nodechildren = node.getChildren();
        if (children.size() != nodechildren.size()) return false;
        for (SessionNode ch : children) if (!nodechildren.contains(ch)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + ((session == null) ? 0 : session.hashCode());
        for (SessionNode ch : children) result = 37 * result + ((ch == null) ? 0 : ch.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return new String("SessionNode{session=" + session + " children=" + children + "}");
    }

    /** Returns all children nodes as array. 
	 *
	 * Returns all direct descendants (memebers of children list).
	 * <p>
	 * NOTE: Doesn't work recursively for all children.
	 * <p>
	 * NOTE: Makes copy of list, so changes made to the returned list
	 * doesn't affect children in list.
	 *
	 * @return An array of all direct children or null if no child is found.
	 */
    public List<SessionNode> getChildren() {
        if (children == null) return null;
        List<SessionNode> result = new LinkedList<SessionNode>(children);
        return result;
    }

    /** Adds child.
	 *
	 * If there is not any node inserted yet, creates children list. 
	 * Inserts node to the children list. Node must be non null.
	 * <p>
	 * TODO null parameter
	 *
	 * @param node Node to insert as child.
	 * @return true if successfull insert, otherwise false.
	 */
    public boolean addChild(SessionNode node) {
        if (node == null) return false;
        if (children == null) children = new LinkedList<SessionNode>();
        return children.add(node);
    }

    /** Removes given node from children.
	 *
	 * Finds out if node is member of children list and if so, removes it
	 * from the list. If node is not memeber or is null, silently return.
	 *
	 * @param node Node to remove.
	 * @return true if removed or false otherwise.
	 */
    public boolean removeChild(SessionNode node) {
        if (node == null || children == null) return false;
        return children.remove(node);
    }
}
