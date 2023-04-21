package name.angoca.zemucan.ui.api;

import java.io.IOException;
import name.angoca.zemucan.tools.messages.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This exception represents any input problem.
 * <p>
 * Note: This exception has not been tested because it wraps an IOException.
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>0.0.1 Class creation.</li>
 * <li>0.1.0 Exception instead of IOException.</li>
 * <li>1.0.0 Moved to version 1.</li>
 * <li>1.1.0 New constructor.</li>
 * <li>1.2.0 Exception hierarchy changed.</li>
 * <li>1.2.1 Logger and final.</li>
 * <li>1.3.0 Message.</li>
 * <li>1.3.1 New id.</li>
 * </ul>
 *
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m">(AngocA)</a>
 * @version 1.3.1 2009-11-02
 * @since 1.0
 */
public final class InputReaderException extends AbstractUIException {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InputReaderException.class);

    /**
     * ID of the exception.
     */
    private static final long serialVersionUID = 7455573768452283261L;

    /**
     * Wraps a generated exception.
     *
     * @param exception
     *            Generated exception.
     */
    public InputReaderException(final IOException exception) {
        super(exception);
        assert exception != null;
        InputReaderException.LOGGER.trace(InputReaderException.class.getName() + " created.");
    }

    @Override
    public String getMessage() {
        return Messages.getString("InputReaderException.UI1-InputReaderProblem");
    }
}
