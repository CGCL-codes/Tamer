package org.rubypeople.rdt.internal.launching;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TS_InternalLaunching {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(TC_RubyInterpreter.class);
        suite.addTestSuite(TC_RubyRuntime.class);
        suite.addTestSuite(TC_RunnerLaunching.class);
        suite.addTestSuite(TC_ArgumentSplitter.class);
        return suite;
    }
}
