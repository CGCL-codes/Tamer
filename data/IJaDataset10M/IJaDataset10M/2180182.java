package org.apache.commons.codec;

import junit.framework.TestCase;

/**
 * @version $Id: StringEncoderAbstractTest.java,v 1.9 2004/04/19 01:14:29 ggregory Exp $
 * @author Apache Software Foundation
 */
public abstract class StringEncoderAbstractTest extends TestCase {

    public StringEncoderAbstractTest(String name) {
        super(name);
    }

    protected abstract StringEncoder makeEncoder();

    public void testEncodeEmpty() throws Exception {
        Encoder encoder = makeEncoder();
        encoder.encode("");
        encoder.encode(" ");
        encoder.encode("\t");
    }

    public void testEncodeNull() throws Exception {
        StringEncoder encoder = makeEncoder();
        try {
            encoder.encode(null);
        } catch (EncoderException ee) {
        }
    }

    public void testEncodeWithInvalidObject() throws Exception {
        boolean exceptionThrown = false;
        try {
            StringEncoder encoder = makeEncoder();
            encoder.encode(new Float(3.4));
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertTrue("An exception was not thrown when we tried to encode " + "a Float object", exceptionThrown);
    }
}
