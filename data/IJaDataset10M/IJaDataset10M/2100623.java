package org.dbwiki.data.query;

import java.util.Vector;
import org.dbwiki.data.database.DatabaseElementNode;
import org.dbwiki.data.database.DatabaseNode;
import org.dbwiki.data.query.handler.QueryNodeHandler;
import org.dbwiki.data.schema.SchemaNode;
import org.dbwiki.data.time.TimeSequence;

/** Implements the set of results of a query as a list/vector of database
 * nodes which are all assumed to be of the same type.
 * 
 * @author jcheney, hmueller
 *
 */
public class QueryResultSet implements QueryNodeHandler {

    private Vector<DatabaseNode> _nodes;

    private TimeSequence _timestamp;

    /** Creates an empty query result set
	 * 
	 */
    public QueryResultSet(TimeSequence timestamp) {
        _timestamp = timestamp;
        _nodes = new Vector<DatabaseNode>();
    }

    public QueryResultSet() {
        this((TimeSequence) null);
    }

    /** Creates a query result set that contains the given node
	 * 
	 * @param node: Initial DatabaseNode contained in the new query result set.
	 */
    public QueryResultSet(TimeSequence timestamp, DatabaseNode node) {
        this(timestamp);
        this.add(node);
    }

    public QueryResultSet(DatabaseNode node) {
        this((TimeSequence) null, node);
    }

    /** Adds the given node to the query result set.
	 * 
	 * @param node: Node to be added to the result set
	 */
    public void add(DatabaseNode node) {
        _nodes.add(node);
    }

    /** Retrieves the node with the given index from the result set
	 * 
	 * @param index: Index of the node in  the result set.
	 * @return DatabaseNode at the given index position.
	 */
    public DatabaseNode get(int index) {
        return _nodes.get(index);
    }

    /** The timestamp specified in the VERSION clause of the query
	 * 
	 * @return timestamp or null if no version clause was given (always null
	 * for results of queries other than XAQL queries).
	 */
    public TimeSequence getTimestamp() {
        return _timestamp;
    }

    /** Implements QueryNodeHandler.handle(). Adds the given query result node
	 * to the result set.
	 * 
	 */
    public void handle(DatabaseElementNode node) {
        this.add(node);
    }

    /** Check whether the result set is restricted to a certain timestamp
	 * 
	 * @return true if a version clause was specified for the query
	 */
    public boolean hasTimestamp() {
        return (_timestamp != null);
    }

    /** Test whether the query returned element nodes or text nodes
	 * 
	 * @return true if the query result are of type element node,
	 * false if the result nodes are text nodes. 
	 */
    public boolean isElement() {
        if (_nodes.size() > 0) {
            return _nodes.get(0).isElement();
        } else {
            return false;
        }
    }

    /** Tests if the query result is empty.
	 * 
	 * @return true if the query result set is empty
	 */
    public boolean isEmpty() {
        return _nodes.isEmpty();
    }

    /** The size of the query result set
	 * 
	 * @return Number of nodes in the result set.
	 */
    public int size() {
        return _nodes.size();
    }

    /** Returns the schema node for the query result set.
	 * 
	 * @return Schema node of the first element in the result set (assuming that
	 * all nodes in the result set have the same schema). Returns null if the result
	 * set is empty or if the result node(s) are TextNodes.
	 * 
	 */
    public SchemaNode schema() {
        if (_nodes.size() > 0) {
            DatabaseNode node = _nodes.firstElement();
            if (node.isElement()) {
                return ((DatabaseElementNode) node).schema();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String toString() {
        int n = _nodes.size();
        if (n == 0) {
            return "{}";
        } else {
            StringBuffer buf = new StringBuffer();
            buf.append("{");
            for (int i = 0; i < n - 1; i++) {
                buf.append(_nodes.get(i).toString());
                buf.append(",");
            }
            buf.append(_nodes.get(n - 1).toString());
            buf.append("}");
            return buf.toString();
        }
    }
}
