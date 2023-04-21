package ru.goldenforests.forum.util.uri;

import java.util.Collections;
import java.util.Map;

public class URIPatternMatcher {

    private final String actionName;

    private final Map parameters;

    public URIPatternMatcher(String actionName, Map parameters) {
        this.actionName = actionName;
        this.parameters = parameters;
    }

    public String getActionName() {
        if (!matches()) throw new UnsupportedOperationException();
        return actionName;
    }

    public Map getParameters() {
        if (!matches()) throw new UnsupportedOperationException();
        return Collections.unmodifiableMap(parameters);
    }

    public boolean matches() {
        return actionName != null;
    }
}
