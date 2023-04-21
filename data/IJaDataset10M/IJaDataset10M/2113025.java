package com.bluemarsh.jswat.core.watch;

import com.sun.jdi.ObjectReference;

/**
 *
 * @author Nathan Fiedler
 */
public class DefaultWatchFactory implements WatchFactory {

    /**
     * Creates a new instance of DefaultWatchFactory.
     */
    public DefaultWatchFactory() {
    }

    @Override
    public ExpressionWatch createExpressionWatch(String expr) {
        ExpressionWatch w = new DefaultExpressionWatch();
        w.setExpression(expr);
        return w;
    }

    @Override
    public FixedWatch createFixedWatch(ObjectReference obj) {
        FixedWatch w = new DefaultFixedWatch();
        w.setObjectReference(obj);
        return w;
    }
}
