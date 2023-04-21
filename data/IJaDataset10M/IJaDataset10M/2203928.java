package objectivehtml.htmlwidget.exception;

import objectivehtml.exception.ObjectiveHtmlRuntimeException;

/**
 * This is the base exception of all html widget runtime exceptions.
 * @author Keith Wong
 */
public class HtmlWidgetRuntimeException extends ObjectiveHtmlRuntimeException {

    public HtmlWidgetRuntimeException() {
        super();
    }

    public HtmlWidgetRuntimeException(String sExceptionMessage) {
        super(sExceptionMessage);
    }
}
