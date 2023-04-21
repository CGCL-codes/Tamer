package com.faunos.util.net;

import java.io.IOException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

/**
 * A skeletal implementation of an asynchronous stagelet.  An
 * instance starts out in the {@linkplain IoState#IDLE} state.
 * Later, when the task is completed, a stagelet representing the
 * completion of the task is pushed onto the stack.
 *
 * @see #init(HandletContext)
 * @see #nextStage()
 * 
 * @author Babak Farhang
 */
public abstract class AsynchStagelet implements Stagelet {

    protected final StageletStack stack;

    private boolean unwound;

    protected AsynchStagelet(StageletStack stack) {
        this.stack = stack;
    }

    /**
     * Sets the state to <tt>DONE</tt>.
     * 
     * @return <tt>DONE</tt>
     */
    public IoState unwind(IoState preState) {
        unwound = true;
        return IoState.DONE;
    }

    /**
     * Does nothing.
     */
    public void discard() {
    }

    /**
     * Asynchronously invokes the {@linkplain #nextStage()} method, pushes the
     * returned stagelet onto the stack, initializes it, and notifies the
     * container that it is ready to resume I/O.
     * <p/>
     * This method returns immediately.
     * 
     * @return {@linkplain IoState#IDLE}
     */
    public IoState init(final HandletContext context) {
        Runnable task = new Runnable() {

            public void run() {
                Stagelet next = nextStage();
                stack.push(next);
                next.init(context);
                context.resumeIo();
            }
        };
        context.executor().execute(task);
        return IoState.IDLE;
    }

    /**
     * Not implemented.
     * 
     * @see #state()
     * @throws UnsupportedOperationException
     */
    public IoState read(ScatteringByteChannel in) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns <tt>DONE</tt>, if {@linkplain #unwind(IoState) unwound};
     * <tt>IDLE</tt>, otherwise.
     */
    public IoState state() {
        return unwound ? IoState.DONE : IoState.IDLE;
    }

    /**
     * Not implemented.
     * 
     * @see #state()
     * @throws UnsupportedOperationException
     */
    public IoState write(GatheringByteChannel out) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the next stage which will be pushed onto the stack.
     * This method is invoked asynchronously and the returned stagelet
     * is the end result of that execution.  The returned stagelet will be
     * in either of the {@linkplain IoState#READING} or
     * {@linkplain IoState#WRITING} states.
     * 
     * @see #init(HandletContext)
     */
    protected abstract Stagelet nextStage();
}
