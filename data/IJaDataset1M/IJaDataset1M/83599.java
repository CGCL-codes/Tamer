package javax.print;

import java.io.OutputStream;

/**
 * @author Michael Koch (konqueror@gmx.de)
 */
public abstract class StreamPrintService implements PrintService {

    private boolean disposed;

    private OutputStream out;

    /**
   * Constructs a <code>StreamPrintService</code> object.
   * 
   * @param out the <code>OutputStream</code> to use
   */
    protected StreamPrintService(OutputStream out) {
        this.out = out;
    }

    /**
   * Dispose this <code>StreamPrintService</code> object.
   */
    public void dispose() {
        disposed = true;
    }

    /**
   * Returns the document format emited by this print service.
   * 
   * @return the document format
   */
    public abstract String getOutputFormat();

    /**
   * Returns the <code>OutputStream</code> of this object.
   * 
   * @return the <code>OutputStream</code>
   */
    public OutputStream getOutputStream() {
        return out;
    }

    /**
   * Determines if this <code>StreamPrintService</code> object is disposed.
   * 
   * @return <code>true</code> if disposed already,
   * otherwise <code>false</code>
   */
    public boolean isDisposed() {
        return disposed;
    }
}
