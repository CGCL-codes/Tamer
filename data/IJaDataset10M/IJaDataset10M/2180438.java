package org.nodal.util;

import org.nodal.model.Getter;

/**
 * Interface to record value modifications.
 */
public interface Monitor {

    /**
   * Record a the start of a change of value from prev.
   */
    void beginChange(Getter prev);

    /**
   * Record a the completion of a change of value to curr.
   */
    void endChange(Getter curr);
}
