package net.sf.xwav.soundrenderer;

import net.sf.xwav.utils.XwavException;

/**
 * 
 * Thrown if a bad parameter is passed into a Generator.
 * 
 */
@SuppressWarnings("serial")
public class BadParameterException extends XwavException {

    public BadParameterException() {
        super();
    }

    public BadParameterException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public BadParameterException(String arg0) {
        super(arg0);
    }

    public BadParameterException(Throwable arg0) {
        super(arg0);
    }
}
