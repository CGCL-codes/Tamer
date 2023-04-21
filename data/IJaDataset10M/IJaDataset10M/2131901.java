package org.mortbay.jetty.handler.rewrite;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.servlet.PathMap;
import org.mortbay.util.URIUtil;

/**
 * Rewrite the URI by replacing the matched {@link PathMap} path with a fixed string. 
 */
public class RewritePatternRule extends PatternRule {

    private String _replacement;

    public RewritePatternRule() {
        _handling = false;
        _terminating = false;
    }

    /**
     * Whenever a match is found, it replaces with this value.
     * 
     * @param value the replacement string.
     */
    public void setReplacement(String value) {
        _replacement = value;
    }

    public String apply(String target, HttpServletRequest request, HttpServletResponse response) throws IOException {
        target = URIUtil.addPaths(_replacement, PathMap.pathInfo(_pattern, target));
        return target;
    }

    /**
     * Returns the replacement string.
     */
    public String toString() {
        return super.toString() + "[" + _replacement + "]";
    }
}
