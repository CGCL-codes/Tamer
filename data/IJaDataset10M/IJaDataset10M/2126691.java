package com.faunos.util.net;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;

/**
 * An interface provided by a <tt>Handlet</tt> container to a handlet.
 * By now, this is a familiar pattern seen for example in the servlet
 * or applet specs.
 * 
 * @see Handlet#init(HandletContext)
 *
 * @author Babak Farhang
 */
public interface HandletContext {

    /**
     * Returns an executor that the <tt>Handlet</tt> may use for
     * performing asynchronous tasks.
     */
    public ExecutorService executor();

    /**
     * Callback method invoked by a <tt>Handlet</tt> when it has finished
     * an asynchronous task and is now ready to resume I/O.  Prior to invoking
     * this method, the {@linkplain Handlet} was in the {@linkplain
     * IoState#IDLE IDLE} state and is now in some other state--possibly
     * {@linkplain IoState#DONE DONE}.
     * 
     * @see #executor()
     */
    public void resumeIo();

    /**
     * Returns a temporary work buffer which the <tt>Handlet</tt> can use to
     * process I/O (typically reads).  The returned work buffer may only be
     * used while inside one of the {@linkplain Handlet} methods, and then only
     * from the calling thread.  The handlet must not hang on to this work
     * work buffer (or a view of it) as it has undefined state outside a handlet
     * method invocation.
     * <p/>
     * Note that successive invocations return the same buffer, but that each
     * invocation clears the state of the returned buffer. 
     * 
     * @return  a cleared <tt>ByteBuffer</tt>
     * @see ByteBuffer#clear()
     */
    public ByteBuffer workBuffer();

    /**
     * Returns the container hint as to whether the handlet should try to
     * process non-network I/O tasks asynchronously.
     * 
     * @see Handlet#state()
     * @see IoState#IDLE
     */
    public boolean processAsynch();
}
