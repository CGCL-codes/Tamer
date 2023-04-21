package com.volantis.xml.pipeline.sax.operations.diselect;

import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.TestPipelineFactory;

public class SelExprTestCase extends PipelineTestAbstract {

    /**
     * Ensure that if the diselect expr attribute evaluates to true then the
     * element is included after attribute has been removed.
     */
    public void testSelExprTrue() throws Exception {
        doTest(new TestPipelineFactory(), "sel-expr-true-input.xml", "sel-expr-true-expected.xml");
    }

    /**
     * Ensure that if the diselect expr attribute evaluates to true then the
     * element is not included.
     */
    public void testSelExprFalse() throws Exception {
        doTest(new TestPipelineFactory(), "sel-expr-false-input.xml", "sel-expr-false-expected.xml");
    }
}
