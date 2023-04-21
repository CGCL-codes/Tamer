package org.rubypeople.rdt.internal.ui.symbols;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TS_UiSymbols {

    public static Test suite() {
        TestSuite suite = new TestSuite("org.rubypeople.rdt.internal.ui.symbols");
        suite.addTestSuite(TC_BlockingSymbolFinder.class);
        return suite;
    }
}
