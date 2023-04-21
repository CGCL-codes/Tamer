package org.axed.user.client;

import java.util.Stack;

/**
 * Stores unneeded buffers in a pool. 
 * This should relieve the garbage collector to
 * have to constantly collect things that otherwise
 * would in mass be created and become unreferenced
 * short after.
 */
public class UndoBufferPool {

    Stack pool = new Stack();

    /**
	 * Pulls an buffer from the pool if available or
	 * creates a new one if pool empty.
	 */
    public UndoBuffer newBuffer(AxedArea axed) {
        if (pool.size() > 0) {
            return (UndoBuffer) pool.pop();
        }
        return new UndoBuffer(axed);
    }

    /**
	 * Puts the Buffer into the Pool to be not
	 * used again, until its pulled by newBuffer(),
	 */
    public void releaseBuffer(UndoBuffer b) {
        pool.push(b);
    }
}
