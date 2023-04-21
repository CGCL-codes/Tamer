package org.obe.xpdl.model.ext;

import org.obe.util.AbstractBean;

/**
 * An abstract base for loop invariants.
 *
 * @author Adrian Price
 */
public abstract class LoopBody extends AbstractBean {

    private static final long serialVersionUID = 3146286115533520041L;

    public static final int NONE = -1;

    public static final int WHILE = 0;

    public static final int UNTIL = 1;

    public static final int FOR_EACH = 2;

    protected LoopBody() {
    }

    public abstract int getType();
}
