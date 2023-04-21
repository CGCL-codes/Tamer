package org.epoline.print.shared;

/**
 * Exception during BatchPrinting
 * @author eb50109
 */
public class BatchPrintException extends Exception {

    /**
     * 
     */
    public BatchPrintException() {
        super();
    }

    /**
     * @param s
     */
    public BatchPrintException(String s) {
        super(s);
    }
}
