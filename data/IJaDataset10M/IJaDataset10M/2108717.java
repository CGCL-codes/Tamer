package org.dcm4chex.archive.exceptions;

/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Revision$ $Date$
 * @since Sep 26, 2008
 */
public class NoSuchStudyException extends Exception {

    private static final long serialVersionUID = -2820727883355660328L;

    public NoSuchStudyException(Throwable cause) {
        super(cause);
    }
}
