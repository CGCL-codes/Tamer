package net.sourceforge.jwbf.actions.mw.util;

/**
 * exception indicating that a feature is not
 * supported by the wiki's software version.
 * 
 * @author Thomas Stock
 */
public class VersionException extends ProcessException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -156908450310809588L;

    /**
	 * 
	 * @param s exeption text
	 */
    public VersionException(String s) {
        super(s);
    }
}
