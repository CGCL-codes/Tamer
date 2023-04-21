package gnu.inet.imap;

import java.io.IOException;

/**
 * Exception corresponding to an IMAP4 BAD or NO server response.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class IMAPException extends IOException {

    /**
   * ID of the exception (BAD or NO).
   */
    protected String id;

    public IMAPException(String id, String message) {
        super(message);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
