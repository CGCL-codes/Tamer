package sun.tools.tree;

import sun.tools.java.*;

/**
 * WARNING: The contents of this source file are not part of any
 * supported API.  Code that depends on them does so at its own risk:
 * they are subject to change or removal without notice.
 */
public class CheckContext extends Context {

    public Vset vsBreak = Vset.DEAD_END;

    public Vset vsContinue = Vset.DEAD_END;

    public Vset vsTryExit = Vset.DEAD_END;

    /**
     * Create a new nested context, for a block statement
     */
    CheckContext(Context ctx, Statement stat) {
        super(ctx, stat);
    }
}
