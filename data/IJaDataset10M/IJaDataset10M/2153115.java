package com.frameworkset.orm.sql;

/**
 * An Exception for parsing SQLToAppData.  This class
 * will probably get some extra features in future.
 *
 * @author <a href="mailto:leon@opticode.co.za">Leon Messerschmidt</a>
 * @version $Id: ParseException.java,v 1.3 2004/02/22 06:27:20 jmcnally Exp $
 */
public class ParseException extends Exception {

    /**
     * constructor.
     *
     * @param err error message
     */
    public ParseException(String err) {
        super(err);
    }
}
