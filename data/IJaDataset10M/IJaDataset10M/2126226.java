package org.apache.batik.parser;

/**
 *
 * @author  tonny@kiyut.com
 */
public class DefaultNumberListHandler implements NumberListHandler {

    /**
     * The only instance of this class.
     */
    public static final NumberListHandler INSTANCE = new DefaultNumberListHandler();

    /**
     * This class does not need to be instantiated.
     */
    protected DefaultNumberListHandler() {
    }

    /**
     * Implements {@link NumberListHandler#startNumberList()}.
     */
    public void startNumberList() throws ParseException {
    }

    /**
     * Implements {@link NumberListHandler#endNumberList()}.
     */
    public void endNumberList() throws ParseException {
    }

    /**
     * Implements {@link NumberListHandler#startNumber()}.
     */
    public void startNumber() throws ParseException {
    }

    /**
     * Implements {@link NumberListHandler#numberValue(float)}.
     */
    public void numberValue(float v) throws ParseException {
    }

    /**
     * Implements {@link NumberListHandler#endNumber()}.
     */
    public void endNumber() throws ParseException {
    }
}
