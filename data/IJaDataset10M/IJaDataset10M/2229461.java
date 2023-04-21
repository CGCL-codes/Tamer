package net.sf.practicalxml.builder;

/**
 *  This exception is used both to report errors directly from the {@link
 *  XmlBuilder} code, as well as to wrap exceptions thrown from called code.
 */
public class XmlBuilderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public XmlBuilderException(String msg) {
        super(msg);
    }

    public XmlBuilderException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
