package com.volantis.styling.impl.engine;

import com.volantis.styling.impl.engine.matchers.MatcherContext;
import com.volantis.styling.impl.engine.listeners.MutableListeners;

public class AbstractStylerContext implements StylerContext {

    /**
     * The matcher context to use.
     */
    private final MatcherContext matcherContext;

    private final MutableListeners depthChangeListeners;

    public AbstractStylerContext(MatcherContext matcherContext, MutableListeners depthChangeListeners) {
        this.matcherContext = matcherContext;
        this.depthChangeListeners = depthChangeListeners;
    }

    public MatcherContext getMatcherContext() {
        return matcherContext;
    }

    public MutableListeners getDepthChangeListeners() {
        return depthChangeListeners;
    }
}
