package android.os;

/** @hide */
public class MailboxNotAvailableException extends Throwable {

    /**
   * This exception represents the case when a request for a
   * named, published mailbox fails because the requested name has not been published
   */
    public MailboxNotAvailableException() {
    }

    public MailboxNotAvailableException(String s) {
        super(s);
    }
}
