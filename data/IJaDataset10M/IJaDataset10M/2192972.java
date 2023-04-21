package org.thymeleaf.exceptions;

/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public class ExpressionParsingException extends TemplateProcessingException {

    private static final long serialVersionUID = -7201684231950647994L;

    public ExpressionParsingException(final String message) {
        super(message);
    }

    public ExpressionParsingException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ExpressionParsingException(final String message, final String templateName, final Integer lineNumber, final Throwable cause) {
        super(message, templateName, lineNumber, cause);
    }

    public ExpressionParsingException(final String message, final String templateName, final Integer lineNumber) {
        super(message, templateName, lineNumber);
    }

    public ExpressionParsingException(final String message, final String templateName, final String expression, final Integer lineNumber, final Throwable cause) {
        super(message, templateName, expression, lineNumber, cause);
    }

    public ExpressionParsingException(final String message, final String templateName, final String expression, final Integer lineNumber) {
        super(message, templateName, expression, lineNumber);
    }

    public ExpressionParsingException(final String message, final String templateName, final String expression, final Throwable cause) {
        super(message, templateName, expression, cause);
    }

    public ExpressionParsingException(final String message, final String templateName, final String expression) {
        super(message, templateName, expression);
    }

    public ExpressionParsingException(final String message, final String templateName, final Throwable cause) {
        super(message, templateName, cause);
    }

    public ExpressionParsingException(final String message, final String templateName) {
        super(message, templateName);
    }
}
