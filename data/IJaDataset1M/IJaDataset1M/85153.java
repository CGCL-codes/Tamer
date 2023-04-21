package jconch.pipeline.impl;

import jconch.pipeline.PipeLink;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A pipe link with a specified upper bound on the queue length.
 */
public class BoundedPipeLink<T> extends PipeLink<T> {

    /**
     * Constructor.
     *
     * @param maxElements The maximum number of elements.
     * @throws IllegalArgumentException If the argument is not greater than zero.
     */
    public BoundedPipeLink(final int maxElements) {
        super(new LinkedBlockingQueue<T>(maxElements));
    }
}
