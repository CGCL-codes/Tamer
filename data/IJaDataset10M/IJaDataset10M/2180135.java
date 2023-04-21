package com.google.appengine.testing.cloudcover.harness.junitx;

/**
 * @author Max Ross <max.ross@gmail.com>
 */
public final class JUnitStackTraceRewriter {

    private JUnitStackTraceRewriter() {
    }

    public static String rewrite(String trace) {
        return trace.replaceAll("\n", "<br>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    }
}
