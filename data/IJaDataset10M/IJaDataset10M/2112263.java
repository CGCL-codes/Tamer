package name.angoca.zemucan.core.graph.model;

import name.angoca.zemucan.tools.messages.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The grammar described in the graph does not represents a real grammar. There
 * are just the starting node and the ending node.
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>1.0.0 Class creation.</li>
 * <li>1.0.1 Logger and final.</li>
 * <li>1.1.0 Message.</li>
 * <li>1.1.1 New id.</li>
 * <li>1.2.0 Part of graph.</li>
 * </ul>
 *
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m">(AngocA)</a>
 * @version 1.2.0 2009-11-08
 */
public final class EmptyGrammarException extends AbstractGraphException {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EmptyGrammarException.class);

    /**
     * ID of the exception.
     */
    private static final long serialVersionUID = 3035617747111000771L;

    /**
     * Default constructor.
     */
    public EmptyGrammarException() {
        super();
        EmptyGrammarException.LOGGER.trace(EmptyGrammarException.class.getName() + " created.");
    }

    @Override
    public String getMessage() {
        return Messages.getString("EmptyGrammarException.GRPH5-EmptyGrammar");
    }
}
