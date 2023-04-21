package com.io_software.catools.search;

/** Exception is thrown in case a {@link Query} is applied to
    a {@link Searchable} object that does not support this type
    of query.

    @author Axel Uhl
    @version $Id: IllegalQueryTypeException.java,v 1.5 2001/01/15 09:45:07 aul Exp $
*/
public class IllegalQueryTypeException extends Exception {

    public IllegalQueryTypeException(String message) {
        super(message);
    }
}
