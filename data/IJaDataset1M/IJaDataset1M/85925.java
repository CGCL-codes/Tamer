package com.volantis.mcs.unit.css.impl.parser.functions;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.css.impl.parser.functions.FunctionParserFactory;
import com.volantis.mcs.css.impl.parser.functions.FunctionParser;
import com.volantis.mcs.css.impl.parser.functions.MCSContainerInstanceFunctionParser;

/**
 * Test cases for {@link FunctionParserFactory}.
 */
public class FunctionParserFactoryTestCase extends TestCaseAbstract {

    /**
     * Test that a parser has been added for mcs-container-instance.
     */
    public void testMCSContainerInstance() throws Exception {
        FunctionParserFactory factory = new FunctionParserFactory();
        FunctionParser parser = factory.getFunctionParser("mcs-container-instance");
        assertTrue("mcs-container-instance", parser instanceof MCSContainerInstanceFunctionParser);
    }
}
