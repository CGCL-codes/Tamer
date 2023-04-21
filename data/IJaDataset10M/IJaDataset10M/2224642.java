package org.oxyus.crawler;

/**
 * @author Carlos Saltos (csaltos[@]users.sourceforge.net)
 */
public class CrawlingException extends Exception {

    private static final long serialVersionUID = 1L;

    public CrawlingException() {
    }

    public CrawlingException(String msg) {
        super(msg);
    }

    public CrawlingException(Throwable t) {
        super(t);
    }

    public CrawlingException(String msg, Throwable t) {
        super(msg, t);
    }
}
