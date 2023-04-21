package name.angoca.db2sa.core.graph.model;

import name.angoca.db2sa.tools.messages.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The node has a child that does not exist.
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>1.0.0 Class creation.</li>
 * <li>1.0.1 Error code.</li>
 * <li>1.0.2 Logger and final.</li>
 * <li>1.0.3 Assert.</li>
 * <li>1.0.4 New id.</li>
 * <li>1.1.0 GrammarReader separated from Graph.</li>
 * <li>1.2.0 get id.</li>
 * </ul>
 * 
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m">(AngocA)</a>
 * @version 1.2.0 2009-11-08
 */
public final class NotExistingChildNodeException extends AbstractGraphException {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NotExistingChildNodeException.class);

    /**
     * ID of the exception.
     */
    private static final long serialVersionUID = -3547193297634519255L;

    /**
     * Not existing node.
     */
    private final String childNodeId;

    /**
     * Wrongly defined node.
     */
    private final String parentNodeId;

    /**
     * Creates the exception with the node id where there is child that does not
     * exists.
     * 
     * @param excepParentNodeId
     *            Wrongly defined node id.
     * @param excepChildNodeId
     *            Not existing node id.
     */
    public NotExistingChildNodeException(final String excepParentNodeId, final String excepChildNodeId) {
        assert excepParentNodeId != null;
        assert excepChildNodeId != null;
        this.parentNodeId = excepParentNodeId;
        this.childNodeId = excepChildNodeId;
        NotExistingChildNodeException.LOGGER.debug(NotExistingChildNodeException.class.getName() + " created.");
    }

    /**
     * Retrieves the name of the inexistent node.
     * 
     * @return child node id.
     */
    public String getChildNodeId() {
        return this.childNodeId;
    }

    @Override
    public String getMessage() {
        return Messages.getString("NotExistingChildNodeException." + "GRPH13-InvalidNode") + this.parentNodeId + Messages.getString("NotExistingChildNodeException." + "GRPH13-InexistingChildNode") + this.childNodeId;
    }

    /**
     * Retrieves the id of the node that makes reference to an inexistent node.
     * 
     * @return parent node id.
     */
    public String getParentNodeId() {
        return this.parentNodeId;
    }
}
