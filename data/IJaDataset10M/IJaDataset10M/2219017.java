package org.rubypeople.rdt.refactoring.tests.core.inlinemethod;

import junit.framework.TestSuite;
import org.rubypeople.rdt.refactoring.tests.FileTestSuite;
import org.rubypeople.rdt.refactoring.tests.core.inlinemethod.conditions.TS_InlineMethodChecks;

public class TS_InlineMethod extends FileTestSuite {

    public static TestSuite suite() {
        TestSuite suite = createSuite("Inline Method", "inline_method_test_*source.rb", InlineMethodTester.class);
        suite.addTest(createSuite("Inline Method - Parameter Replacer", "parameter_replaces_test_*source.rb", TC_ParameterReplacer.class));
        suite.addTestSuite(TC_SelectedCallFinder.class);
        suite.addTestSuite(TC_TargetClassFinder.class);
        suite.addTestSuite(TC_MethodFinder.class);
        suite.addTestSuite(TC_ReturnStatementReplacer.class);
        suite.addTest(TS_InlineMethodChecks.suite());
        suite.addTestSuite(TC_MethodBodyStatementReplacer.class);
        suite.addTestSuite(TC_RenameDuplicatedVariables.class);
        return suite;
    }
}
