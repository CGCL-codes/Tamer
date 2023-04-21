package org.codecover.instrumentation.java15.location;

import org.codecover.instrumentation.java15.visitor.TreeDumperWithException;

/**
 * A listener for {@link TreeDumperWithException#addOffsetListener(OffsetListener)}.
 * 
 * @author Christoph Müller
 *
 * @version 1.0 ($Id: OffsetListener.java 1 2007-12-12 17:37:26Z t-scheller $)
 */
public interface OffsetListener {

    /**
     * A start offset was found, here it is.
     * 
     * @param startOffset The found start offset.
     */
    public void startOffset(int startOffset);
}
