package name.angoca.db2sa.core.graph.model;

import name.angoca.db2sa.tools.messages.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EndingingNode not defined.
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>0.0.1 Class creation.</li>
 * <li>1.0.0 Moved to version 1.</li>
 * <li>1.1.0 Exception hierarchy changed.</li>
 * <li>1.1.1 Logger and final.</li>
 * <li>1.1.2 Message.</li>
 * <li>1.1.3 New id.</li>
 * <li>1.1.4 GrammarReader separated from Graph.</li>
 * <li>1.2.0 Part of graph.</li>
 * </ul>
 * 
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m">(AngocA)</a>
 * @version 1.2.0 2009-11-08
 */
public final class EndingNodeNotDefinedException extends AbstractGraphException {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EndingNodeNotDefinedException.class);

    /**
     * ID of the exception.
     */
    private static final long serialVersionUID = 2857566573455597207L;

    /**
     * Default constructor.
     */
    public EndingNodeNotDefinedException() {
        super();
        EndingNodeNotDefinedException.LOGGER.debug(EndingNodeNotDefinedException.class.getName() + " created.");
    }

    @Override
    public String getMessage() {
        return Messages.getString("EndingNodeNotDefinedException.GRPH15-NotEndingNode");
    }
}
