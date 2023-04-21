package php.java.bridge.http;

import java.io.IOException;
import java.io.Writer;
import php.java.bridge.Util;

/**
 * A PrintWriter backed by an OutputStream.
 * @author jostb
 *
 */
public class WriterOutputStream extends DefaultCharsetWriterOutputStream {

    protected String charsetName = Util.DEFAULT_ENCODING;

    private boolean written = false;

    /**
     * The encoding used for char[] -&gt; byte[] conversion
     * @param charsetName
     */
    public void setEncoding(String charsetName) {
        if (written) throw new IllegalStateException("setEncoding");
        this.charsetName = charsetName;
    }

    /**
     * Create a new PhpScriptWriter.
     * @param out The OutputStream
     */
    public WriterOutputStream(Writer out) {
        super(out);
    }

    /**{@inheritDoc}*/
    public void write(byte b[], int off, int len) throws IOException {
        written = true;
        String s = new String(b, off, len, charsetName);
        out.write(s);
    }
}
