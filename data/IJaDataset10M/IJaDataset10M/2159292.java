package org.im4java.core;

/**
   This class is the base class of all im4java -exceptions.

   @version $Revision: 1.4.1 $
   @author  $Author: bablokb (main author), Bruno Mascret (warnings) $
*/
public class IM4JavaException extends Exception {

    /** For Serialization (not used)*/
    private static final long serialVersionUID = 1L;

    /**
   * Constructor.
   */
    public IM4JavaException() {
        super();
    }

    /**
   * Constructor.
   * @param pMessage the Exception message
   */
    public IM4JavaException(String pMessage) {
        super(pMessage);
    }

    /**
   * Constructor.
   * @param pMessage the Exception message
   * @param pCause the Exception cause
   */
    public IM4JavaException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }

    /**
   * Constructor.
   * @param pCause the Exception cause
   */
    public IM4JavaException(Throwable pCause) {
        super(pCause);
    }
}
