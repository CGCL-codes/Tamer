package com.volantis.xml.pipeline.sax.operations.value;

import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;

/**
 * Test cases for pipeline:value-of operation.
 */
public class ValueOfTestCase extends PipelineTestAbstract {

    /**
     * Ensure that it can handle string values.
     */
    public void testStringValue() throws Exception {
        doTest(XMLPipelineFactory.getDefaultInstance(), "string-input.xml", "string-expected.xml");
    }

    /**
     * Ensure that it can handle number values.
     */
    public void testNumberValue() throws Exception {
        doTest(XMLPipelineFactory.getDefaultInstance(), "number-input.xml", "number-expected.xml");
    }

    /**
     * Ensure that it can handle template variables.
     */
    public void testTemplateVariables() throws Exception {
        doTest(XMLPipelineFactory.getDefaultInstance(), "template-variable-input.xml", "template-variable-expected.xml");
    }
}
