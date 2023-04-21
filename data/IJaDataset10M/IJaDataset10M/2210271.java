package gnu.mail.handler;

/**
 * A JAF data content handler for the application/octet-stream MIME content
 * type.
 */
public final class ApplicationOctetStream extends Application {

    /**
   * Constructor for application/octet-stream.
   */
    public ApplicationOctetStream() {
        super("application/octet-stream", "binary");
    }
}
