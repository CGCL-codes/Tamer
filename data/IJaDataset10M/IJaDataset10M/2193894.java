package gnu.inet.http;

/**
 * Callback interface for writing request body content.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public interface RequestBodyWriter {

    /**
   * Returns the total number of bytes that will be written in a single pass
   * by this writer.
   */
    int getContentLength();

    /**
   * Initialises the writer.
   * This will be called before each pass.
   */
    void reset();

    /**
   * Writes body content to the supplied buffer.
   * @param buffer the content buffer
   * @return the number of bytes written
   */
    int write(byte[] buffer);
}
