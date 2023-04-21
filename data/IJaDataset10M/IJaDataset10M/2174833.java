package org.simpleframework.http;

import java.io.OutputStream;

/**
 * It is important to note that once one of these methods are 
 * invoked by an <code>OutputStream</code> or an object monitoring 
 * an <code>OutputStream</code> then the <code>OutputStream</code>
 * must not be used from that point on, that is, no more writing.
 *
 * @author Niall Gallagher
 */
interface OutputMonitor {

    /**
    * This will simply mark this stream as finished. This means that
    * the <code>OutputStream</code> or object that was monitoring 
    * this <code>OutputStream</code> is now finished using it. The
    * <code>OutputStream</code> 'out' must not be used by the object
    * that made the invocation from this point on.
    *
    * @param out the <code>OutputStream</code> being monitored
    */
    public void notifyFinished(OutputStream out);

    /**
    * This will notify that this <code>OutputStream</code> should be
    * closed. It is the task of the <code>OutputMonitor</code> and 
    * not the object that is making the invocation to close the
    * <code>OutputStream</code>, it may be close later.
    *
    * @param out the <code>OutputStream</code> being monitored    
    */
    public void notifyClose(OutputStream out);

    /**
    * This will notify the monitor that an error occured when writing
    * to the <code>OutputStream</code>. The <code>OutputMonitor</code> 
    * will in its own time, mabye asynchronously, close the 
    * <code>OutputStream</code>, this is similar to close.
    *
    * @param out the <code>OutputStream</code> being monitored    
    */
    public void notifyError(OutputStream out);
}
