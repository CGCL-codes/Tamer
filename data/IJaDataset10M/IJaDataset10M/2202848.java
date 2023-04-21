package net.sf.gridarta.model.errorview;

import org.jetbrains.annotations.NotNull;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * An {@link ErrorHandler} that adds all reported problems to an {@link
 * ErrorViewCollector} instance.
 * @author Andreas Kirschbaum
 */
public class ErrorViewCollectorErrorHandler implements ErrorHandler {

    /**
     * The {@link ErrorViewCollector} instance for adding messages.
     */
    @NotNull
    private final ErrorViewCollector errorViewCollector;

    /**
     * The {@link ErrorViewCategory} to use when adding messages to {@link
     * #errorViewCollector}.
     */
    @NotNull
    private final ErrorViewCategory errorViewCategory;

    /**
     * Creates a new instance.
     * @param errorViewCollector the error view collector instance for adding
     * messages
     * @param errorViewCategory the error view category to use when adding
     * messages to <code>errorViewCollector</code>
     */
    public ErrorViewCollectorErrorHandler(@NotNull final ErrorViewCollector errorViewCollector, @NotNull final ErrorViewCategory errorViewCategory) {
        this.errorViewCollector = errorViewCollector;
        this.errorViewCategory = errorViewCategory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warning(final SAXParseException exception) throws SAXException {
        errorViewCollector.addWarning(errorViewCategory, exception.getMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(final SAXParseException exception) throws SAXException {
        errorViewCollector.addError(errorViewCategory, exception.getMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fatalError(final SAXParseException exception) throws SAXException {
        errorViewCollector.addError(errorViewCategory, exception.getMessage());
    }
}
