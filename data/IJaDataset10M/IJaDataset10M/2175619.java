package org.tn5250j.cp;

import static org.junit.Assert.*;
import org.junit.Test;
import org.tn5250j.encoding.CharMappings;
import org.tn5250j.encoding.CodePage;

/**
 * Testing the correctness of {@link CCSID273} and comparing with existing implementation.
 *
 * @author master_jaf
 */
public class CCSID273Test {

    /**
	 * Correctness test for old implementation ....
	 * Testing byte -> Unicode -> byte
	 */
    @Test
    public void testOldConverter273() {
        CodePage cp = CharMappings.getCodePage("273");
        assertNotNull("At least an ASCII Codepage should be available.", cp);
        for (int i = 0; i < 256; i++) {
            final byte beginvalue = (byte) i;
            final char converted = cp.ebcdic2uni(beginvalue);
            final byte afterall = cp.uni2ebcdic(converted);
            assertEquals("Testing item #" + i, beginvalue, afterall);
        }
    }

    /**
	 * Correctness test for new implementation ...
	 * Testing byte -> Unicode -> byte
	 */
    @Test
    public void testNewConverter273() {
        CCSID273 cp = new CCSID273();
        cp.init();
        assertNotNull("At least an ASCII Codepage should be available.", cp);
        for (int i = 0; i < 256; i++) {
            final byte beginvalue = (byte) i;
            final char converted = cp.ebcdic2uni(beginvalue);
            final byte afterall = cp.uni2ebcdic(converted);
            assertEquals("Testing item #" + i, beginvalue, afterall);
        }
    }

    /**
	 * Testing for Correctness both implementations ...
	 * Testing byte -> Unicode -> byte
	 */
    @Test
    public void testBoth() {
        final CodePage cp = CharMappings.getCodePage("273");
        final CCSID273 cpex = new CCSID273();
        cpex.init();
        assertNotNull("At least an ASCII Codepage should be available.", cpex);
        for (int i = 0; i < 256; i++) {
            final byte beginvalue = (byte) i;
            assertEquals("Testing to EBCDIC item #" + i, cp.ebcdic2uni(beginvalue), cpex.ebcdic2uni(beginvalue));
            final char converted = cp.ebcdic2uni(beginvalue);
            assertEquals("Testing to UNICODE item #" + i, cp.uni2ebcdic(converted), cpex.uni2ebcdic(converted));
            final byte afterall = cp.uni2ebcdic(converted);
            assertEquals("Testing before and after item #" + i, beginvalue, afterall);
        }
    }
}
