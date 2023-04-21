package ru.adv.io;

/**
 * User: roma
 * Date: 16.03.2004
 * Time: 12:13:35
 * $Id: UnsupportedEncodingException.java 1106 2009-06-03 07:32:17Z vic $
 * $Name:  $
 */
public class UnsupportedEncodingException extends InputOutputException {

    private static final long serialVersionUID = -6176272523579215819L;

    public UnsupportedEncodingException(String encoding) {
        super(IO_UNSUPPORTED_ENCODING, "Unsupporded encoding");
        setAttr("encoding", encoding);
    }
}
