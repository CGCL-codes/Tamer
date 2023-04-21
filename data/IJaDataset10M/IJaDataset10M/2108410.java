package org.mbari.vcr;

/**
 * <p>Thrown by VCR class when a problem occurs with a class in the
 * <i>org.mbari.vcr</i> package. Examples of problems include a bad
 * checksum.</p>
 *
 * @author  : $Author: hohonuuli $
 * @version : $Revision: 3 $
 */
public class VCRException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 3481686778437411662L;

    /**
     * Constructs ...
     *
     */
    public VCRException() {
        super();
    }

    /**
     * Constructs ...
     *
     *
     * @param s
     */
    public VCRException(String s) {
        super(s);
    }
}
