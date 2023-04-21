package name.angoca.db2sa.core.graph.model;

import java.util.List;
import name.angoca.db2sa.core.graph.api.InvalidGraphNodeException;
import name.angoca.db2sa.core.graph.api.ParentStartingNodeException;
import name.angoca.db2sa.tools.Constants;

/**
 * This token represents the first token in the grammar (the starting node in
 * the graph.) All the possible ways starts from this token.
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>0.0.1 Class creation.</li>
 * <li>0.0.2 Recommendations from PMD.</li>
 * <li>0.0.3 Change constant value.</li>
 * <li>0.0.4 Starting and ending token as constants.</li>
 * <li>0.0.5 equals from super.</li>
 * <li>0.0.6 equals different super.</li>
 * <li>0.0.7 part equals super.</li>
 * <li>1.0.0 Moved to version 1.</li>
 * <li>1.1.0 Exception hierarchy changed.</li>
 * <li>1.2.0 final.</li>
 * <li>1.2.1 compareTo -> equals.</li>
 * <li>1.3.0 addParent.</li>
 * <li>1.3.1 GraphToken renamed by GraphNode</li>
 * <li>1.3.2 Token constant renamed to Node.</li>
 * <li>1.4.0 GrammarReader separated from Graph.</li>
 * <li>1.5.0 Viibility, getChildren, toString.</li>
 * </ul>
 * 
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m">(AngocA)</a>
 * @version 1.5.0 2009-11-22
 */
public final class StartingNode extends GraphNode {

    /**
     * Constructor that defines the starting node. Its name is STARTING_NODE and
     * this works as a reserved name token.
     * 
     * @throws InvalidGraphNodeException
     *             The name is invalid
     */
    StartingNode() throws InvalidGraphNodeException {
        super(Constants.STARTING_NODE, true);
    }

    /**
     * Always throws an exception because starting node cannot have parents.
     */
    @Override
    void addParent(final GraphNode token) throws AbstractGraphException {
        throw new ParentStartingNodeException();
    }

    @Override
    public boolean equals(final Object object) {
        boolean equals = false;
        if (object instanceof StartingNode) {
            final GraphNode token = (GraphNode) object;
            final List<GraphNode> ways = token.getChildren();
            if (token.getName().equals(this.getName()) && (token.isReservedWord() == this.isReservedWord()) && (ways.size() == this.getChildren().size())) {
                equals = super.equals(token);
            }
        }
        return equals;
    }

    @Override
    public int hashCode() {
        return Constants.STARTING_NODE.hashCode() * -1;
    }

    /**
     * Returns a string representation of an starting token.
     * 
     * @see java.lang.Object#toString()
     * @return String representation of an starting token.
     */
    @Override
    public String toString() {
        String ret = '{' + Constants.STARTING_NODE;
        if (super.getChildren().size() > 0) {
            ret += '<';
            for (final GraphNode node : super.getChildren()) {
                ret += node.getId() + '-';
            }
            ret = ret.substring(0, ret.length() - 1);
            ret += '>';
        }
        ret += '}';
        return ret;
    }
}
