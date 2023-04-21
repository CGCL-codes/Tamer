package org.omg.CORBA;

import java.io.Serializable;

/**
 * Means that while the operation being invoked does exists, no
 * implementation for it exists.
 *
 * @author Audrius Meskauskas (AudriusA@Bioinformatics.org)
 */
public class NO_IMPLEMENT extends SystemException implements Serializable {

    /** 
   * Use serialVersionUID for interoperability. 
   */
    private static final long serialVersionUID = 3519190655657192112L;

    /**
   * Creates a NO_IMPLEMENT with the default minor code of 0,
   * completion state COMPLETED_NO and the given explaining message.
   * @param reasom the explaining message.
   */
    public NO_IMPLEMENT(String message) {
        super(message, 0, CompletionStatus.COMPLETED_NO);
    }

    /**
   * Creates NO_IMPLEMENT with the default minor code of 0 and a
   * completion state COMPLETED_NO.
   */
    public NO_IMPLEMENT() {
        super("", 0, CompletionStatus.COMPLETED_NO);
    }

    /** Creates a NO_IMPLEMENT exception with the specified minor
   * code and completion status.
   * @param minor additional error code.
   * @param completed the method completion status.
   */
    public NO_IMPLEMENT(int minor, CompletionStatus completed) {
        super("", minor, completed);
    }

    /**
   * Created NO_IMPLEMENT exception, providing full information.
   * @param reason explaining message.
   * @param minor additional error code (the "minor").
   * @param completed the method completion status.
   */
    public NO_IMPLEMENT(String reason, int minor, CompletionStatus completed) {
        super(reason, minor, completed);
    }
}
