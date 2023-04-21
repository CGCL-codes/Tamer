package name.angoca.zemucan.core.graph.model;

import name.angoca.zemucan.tools.messages.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This exception is thrown when a node does not have EndingNode as descendancy.
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>1.0.0 Class creation.</li>
 * <li>1.0.1 New id.</li>
 * <li>1.1.0 GrammarReader separated from Graph.</li>
 * <li>1.2.0 final.</li>
 * </ul>
 *
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m">(AngocA)</a>
 * @version 1.2.0 2011-11-08
 */
public final class NodeNotGoesToEndingNodeException extends AbstractGraphException {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeNotGoesToEndingNodeException.class);

    /**
     * ID of the exception.
     */
    private static final long serialVersionUID = 3785496546324863860L;

    /**
     * Node's name that does not go to EndingNode.
     */
    private final String nodename;

    /**
     * Constructor that associates the node that does not go to EndingNode.
     *
     * @param excepNodename
     *            Node's name without EndingNode as descendancy.
     */
    public NodeNotGoesToEndingNodeException(final String excepNodename) {
        super();
        assert excepNodename != null;
        this.nodename = excepNodename;
        NodeNotGoesToEndingNodeException.LOGGER.debug(NodeNotGoesToEndingNodeException.class.getName() + " created.");
    }

    @Override
    public final String getMessage() {
        return Messages.getString("NodeNotGoesToEndingNodeException." + "GRPH16-NoDescendencyToEndingNode") + this.nodename;
    }

    /**
     * Retrieves the name of the node.
     *
     * @return The node name.
     */
    public final String getNodename() {
        return this.nodename;
    }
}
