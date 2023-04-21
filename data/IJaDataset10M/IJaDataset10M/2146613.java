package name.angoca.zemucan.tools.configurator;

import name.angoca.zemucan.AbstractZemucanException;

/**
 * This exception is the parent for all exceptions generated when configuring
 * the application.
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>0.0.1 Class creation.</li>
 * <li>0.1.0 One constructor.</li>
 * <li>1.0.0 Moved to version 1.</li>
 * <li>1.1.0 Renamed.</li>
 * <li>1.1.1 New id.</li>
 * <li>1.2.0 Deafult constructor.</li>
 * </ul>
 *
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m">(AngocA)</a>
 * @version 1.2.0 2010-05-27
 */
public abstract class AbstractConfiguratorException extends AbstractZemucanException {

    /**
     * ID of the exception.
     */
    private static final long serialVersionUID = -2740475849159766922L;

    /**
     * Default constructor.
     */
    public AbstractConfiguratorException() {
        super();
    }

    /**
     * Constructor with a generated exception.
     *
     * @param exception
     *            Generated exception.
     */
    public AbstractConfiguratorException(final Exception exception) {
        super(exception);
        assert exception != null;
    }
}
