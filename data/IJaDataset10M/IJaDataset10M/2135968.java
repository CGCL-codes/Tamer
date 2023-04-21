package com.googlecode.pinthura.traverser.collection;

import com.googlecode.pinthura.bean.PathEvaluator;

public final class PathResolverImpl implements PathResolver {

    private final PathEvaluator pathEvaluator;

    public PathResolverImpl(final PathEvaluator pathEvaluator) {
        this.pathEvaluator = pathEvaluator;
    }

    @SuppressWarnings({ "unchecked" })
    public <Input, Output> Output resolvePath(final String path, final Input input) {
        if (NO_PATH.equals(path)) {
            return (Output) input;
        }
        return (Output) pathEvaluator.evaluate(path, input);
    }
}
