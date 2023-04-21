package edu.ucdavis.genomics.metabolomics.binbase.converter.exception;

import edu.ucdavis.genomics.metabolomics.exception.BinBaseException;

/**
 * User: wohlgemuth
 * Date: Sep 3, 2009
 * Time: 3:57:55 PM
 */
public class ConverterException extends BinBaseException {

    public ConverterException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ConverterException(String s) {
        super(s);
    }

    public ConverterException(Throwable throwable) {
        super(throwable);
    }

    public ConverterException() {
    }
}
