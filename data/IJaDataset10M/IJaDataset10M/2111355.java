package ru.adv.io;

/**
 * User: roma
 * Date: 16.03.2004
 * Time: 12:32:06
 * $Id: CopyException.java 1106 2009-06-03 07:32:17Z vic $
 * $Name:  $
 */
public class CopyException extends InputOutputException {

    public CopyException(Exception e, String from, String to) {
        super(IO_CANNOT_COPY, e);
        setAttr("source", from);
        setAttr("destination", to);
    }
}
