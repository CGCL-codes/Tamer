package com.loribel.commons.exception;

/**
 * Exception Class.
 *
 * @author Gregory Borelli
 */
public class GB_MetaDataException extends GB_Exception {

    public GB_MetaDataException(String a_message) {
        super(a_message);
    }

    public GB_MetaDataException(String a_msg, Throwable a_source) {
        super(a_msg, a_source);
    }

    public GB_MetaDataException(Throwable a_source) {
        super(a_source);
    }
}
