package ontorama.webkbtools.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: DSTC</p>
 * @author nataliya
 * @version 1.0
 */
public class CancelledQueryException extends Exception {

    public CancelledQueryException() {
    }

    public String getMessage() {
        return "Query was cancelled by user";
    }
}
