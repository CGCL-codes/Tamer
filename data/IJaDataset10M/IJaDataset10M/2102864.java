package ti.io;

import java.io.Writer;
import java.io.IOException;

/**
 * This is a writer that can split what is written to it to two
 * different output Writers.
 * 
 * @author Rob Clark
 * @version 0.1
 */
public class TeeWriter extends Writer {

    private Writer w1;

    private Writer w2;

    /**
   * Class Constructor.
   * 
   * @param w1          the first of the two output writers
   * @param w2          the second of the two output writers
   */
    public TeeWriter(Writer w1, Writer w2) {
        this.w1 = w1;
        this.w2 = w2;
    }

    /**
   * Write a portion of an array of characters.
   *
   * @param cbuf        Array of characters 
   * @param off         Offset from which to start writing characters 
   * @param len         Number of characters to write 
   * @exception IOException If an I/O error occurs
   */
    public void write(char[] cbuf, int off, int len) throws IOException {
        if (w1 != null) {
            w1.write(cbuf, off, len);
        }
        if (w2 != null) {
            w2.write(cbuf, off, len);
        }
    }

    /**
   * Flush both the output writers.
   *
   * @exception IOException - If an I/O error occurs
   */
    public void flush() throws IOException {
        if (w1 != null) {
            w1.flush();
        }
        if (w2 != null) {
            w2.flush();
        }
    }

    /**
   * Close both the output writers.
   *
   * @exception IOException - If an I/O error occurs
   */
    public void close() throws IOException {
        if (w1 != null) {
            w1.close();
        }
        if (w2 != null) {
            w2.close();
        }
    }

    /**
   * Set the primary output writer.
   * @param w1          the first of the two output writers
   */
    public void setPrimaryWriter(Writer w1) {
        this.w1 = w1;
    }

    /**
   * Set the secondary output writer.
   * @param w2          the second of the two output writers
   */
    public void setSecondaryWriter(Writer w2) {
        this.w2 = w2;
    }
}
