package name.angoca.zemucan.ui.impl.jline;

import name.angoca.zemucan.tools.messages.jline.Messages;
import name.angoca.zemucan.ui.api.AbstractUIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This exception represents a console problem.
 * <p>
 * Note: This exception has not been tested because it is thrown depending on a
 * behavior of jLine.
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>1.0.0 Class creation.</li>
 * <li>1.0.1 Logger and final.</li>
 * <li>1.1.0 Message.</li>
 * <li>1.1.1 New id.</li>
 * </ul>
 *
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m">(AngocA)</a>
 * @version 1.1.1 2009-11-02
 */
public final class ConsoleProblemException extends AbstractUIException {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleProblemException.class);

    /**
     * ID of the exception.
     */
    private static final long serialVersionUID = -3671109185507418059L;

    /**
     * Default constructor.
     */
    public ConsoleProblemException() {
        super();
        ConsoleProblemException.LOGGER.trace(ConsoleProblemException.class.getName() + " created.");
    }

    @Override
    public String getMessage() {
        return Messages.getString("ConsoleProblemException." + "JLINE3-ConsoleProblem");
    }
}
