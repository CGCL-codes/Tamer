package br.ita.autowidget.html.util.exception;

public class HtmlComponentBindingException extends RuntimeException {

    public HtmlComponentBindingException() {
        super();
    }

    public HtmlComponentBindingException(String message, Throwable cause) {
        super(message, cause);
    }

    public HtmlComponentBindingException(String message) {
        super(message);
    }

    public HtmlComponentBindingException(Throwable cause) {
        super(cause);
    }
}
