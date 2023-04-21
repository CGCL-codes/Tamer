package objectivehtml.htmlwidget.exception;

/**
 * This is exception should be thrown when an invalid quote type is specified.
 * @author Keith Wong
 */
public class InvalidQuoteTypeException extends HtmlWidgetRuntimeException {

    public InvalidQuoteTypeException() {
        super();
    }

    public InvalidQuoteTypeException(String sExceptionMessage) {
        super(sExceptionMessage);
    }
}
