package org.mmtk.plan.refcount;

import org.mmtk.utility.Constants;
import org.mmtk.utility.deque.*;
import org.vmmagic.pragma.*;
import org.vmmagic.unboxed.*;

/**
 * This class implements a dec-buffer for a reference counting collector
 *
 * @see org.mmtk.plan.TransitiveClosure
 */
@Uninterruptible
public final class RCDecBuffer extends ObjectReferenceBuffer implements Constants {

    /**
   * Constructor
   *
   * @param queue The shared deque that is used.
   */
    public RCDecBuffer(SharedDeque queue) {
        super("dec", queue);
    }

    /**
   * This is the method that ensures
   *
   * @param object The object to process.
   */
    @Inline
    protected void process(ObjectReference object) {
        if (RCBase.isRCObject(object)) {
            push(object);
        }
    }
}
