package com.meterware.httpunit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests the base64 converter.
 *
 * @author <a href="mailto:russgold@acm.org">Russell Gold</a>
 * @author <a href="mailto:mtarruella@silacom.com">Marcos Tarruella</a> 
 **/
public class Base64Test extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(Base64Test.class);
    }

    public Base64Test(String name) {
        super(name);
    }

    public void testEncode() {
        assertEquals("Result of encoding", "QWxhZGRpbjpvcGVuIHNlc2FtZQ==", Base64.encode("Aladdin:open sesame"));
        assertEquals("Result of encoding", "QWRtaW46Zm9vYmFy", Base64.encode("Admin:foobar"));
    }

    public void testDecode() {
        assertEquals("Result of decoding", "Aladdin:open sesame", Base64.decode("QWxhZGRpbjpvcGVuIHNlc2FtZQ=="));
        assertEquals("Result of decoding", "Admin:foobar", Base64.decode("QWRtaW46Zm9vYmFy"));
    }

    public void testExceptionDecoding() {
        try {
            Base64.decode("123");
            fail("valid Base64 codes have a multiple of 4 characters");
        } catch (Exception e) {
            assertEquals("valid Base64 codes have a multiple of 4 characters", e.getMessage());
        }
    }
}
