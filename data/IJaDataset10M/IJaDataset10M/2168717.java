package name.angoca.zemucan.core.graph.model;

import name.angoca.zemucan.tools.messages.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StartingNode not defined.
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>0.0.1 Class creation.</li>
 * <li>1.0.0 Moved to version 1.</li>
 * <li>1.1.0 Exception hierarchy changed.</li>
 * <li>1.1.1 Logger and final.</li>
 * <li>1.2.0 Message.</li>
 * <li>1.2.1 Message changed.</li>
 * <li>1.2.2 New id.</li>
 * <li>1.2.3 GrammarReader separated from Graph.</li>
 * <li>1.3.0 Part of graph.</li>
 * </ul>
 *
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m">(AngocA)</a>
 * @version 1.3.0 2009-11-08
 */
public final class StartingNodeNotDefinedException extends AbstractGraphException {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StartingNodeNotDefinedException.class);

    /**
     * ID of the exception.
     */
    private static final long serialVersionUID = -3562605417635581483L;

    /**
     * Default constructor.
     */
    public StartingNodeNotDefinedException() {
        super();
        StartingNodeNotDefinedException.LOGGER.debug(StartingNodeNotDefinedException.class.getName() + " created.");
    }

    @Override
    public String getMessage() {
        return Messages.getString("StartingNodeNotDefinedException.GRPH14-NoStartingNode");
    }
}
