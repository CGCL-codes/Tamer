package velosurf.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class WriterOutputStream extends OutputStream {

    protected Writer writer = null;

    /** 
	 * Construct a new WriterOutputStream, bound to the specified writer.
	 * 
	 * @param w the writer
	 */
    public WriterOutputStream(Writer w) {
        writer = w;
    }

    /** 
	 * Write a byte to this output stream.
	 * 
	 * @exception IOException may be thrown
	 */
    public void write(int c) throws IOException {
        writer.write(c);
    }
}
