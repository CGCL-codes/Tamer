package com.volantis.styling.impl.engine.matchers;

import com.volantis.styling.debug.DebugStylingWriter;

/**
 * Matches the local name of the current element.
 */
public class LocalNameMatcher extends AbstractSimpleMatcher {

    private final String localName;

    /**
     * Initialise.
     *
     * @param localName The local name against which the current element must
     * match, may not be null.
     */
    public LocalNameMatcher(String localName) {
        if (localName == null) {
            throw new IllegalArgumentException("localName cannot be null");
        }
        this.localName = localName;
    }

    public MatcherResult matchesWithinContext(MatcherContext context) {
        String contextElementLocalName = context.getLocalName();
        boolean matched = localName.equals(contextElementLocalName);
        return matched ? MatcherResult.MATCHED : MatcherResult.FAILED;
    }

    public void debug(DebugStylingWriter writer) {
        writer.print(localName);
    }

    public String getMatchableElement() {
        return localName;
    }

    public boolean isMatchAny() {
        return false;
    }

    public int hashCode() {
        return 85 + localName.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            return localName.equals(((LocalNameMatcher) obj).localName);
        } else {
            return false;
        }
    }

    public String toString() {
        return localName;
    }
}
