package org.tn5250j.cp;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import org.tn5250j.encoding.CharMappings;
import org.tn5250j.encoding.CodePage;
import org.tn5250j.encoding.ToolboxCodePageProvider;

/**
 * Testing the correctness of {@link CCSID870} and comparing with existing implementation.
 *
 * @author master_jaf
 */
public class CCSID870skTest {

    /**
	 * Correctness test for old implementation ....
	 * Testing byte -> Unicode -> byte
	 */
    @Test
    public void testOldConverter870() {
        CodePage cp = CharMappings.getCodePage("870-sk");
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
    public void testNewConverter870() {
        CCSID870 cp = new CCSID870();
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
    @Ignore("the new implementation seems to be correct, no need to fix this")
    @Test
    public void testBoth() {
        final CodePage cp = CharMappings.getCodePage("870-sk");
        final CCSID870 cpex = new CCSID870();
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

    /**
	 * Testing for correctness new implementation and JTOpen ...
	 * Testing byte -> Unicode -> byte
	 */
    @Test
    public void testNewAndToolbox() {
        final CodePage cp = ToolboxCodePageProvider.getCodePage("870");
        final CCSID870 cpex = new CCSID870();
        cpex.init();
        assertNotNull("At least an ASCII Codepage should be available.", cpex);
        for (int i = 4 * 16; i < 256; i++) {
            final byte beginvalue = (byte) i;
            assertEquals("Testing to EBCDIC item #" + i, cp.ebcdic2uni(beginvalue), cpex.ebcdic2uni(beginvalue));
            final char converted = cp.ebcdic2uni(beginvalue);
            assertEquals("Testing to UNICODE item #" + i, cp.uni2ebcdic(converted), cpex.uni2ebcdic(converted));
            final byte afterall = cp.uni2ebcdic(converted);
            assertEquals("Testing before and after item #" + i, beginvalue, afterall);
        }
    }

    /**
	 * Testing for correctness old implementation and JTOpen ...
	 * Testing byte -> Unicode -> byte
	 */
    @Ignore("the new implementation seems to be correct, no need to fix this")
    @Test
    public void testOldAndToolbox() {
        final CodePage cp = ToolboxCodePageProvider.getCodePage("870");
        final CodePage cpex = CharMappings.getCodePage("870-sk");
        assertNotNull("At least an ASCII Codepage should be available.", cpex);
        for (int i = 4 * 16; i < 256; i++) {
            final byte beginvalue = (byte) i;
            assertEquals("Testing to EBCDIC item #" + i, cp.ebcdic2uni(beginvalue), cpex.ebcdic2uni(beginvalue));
            final char converted = cp.ebcdic2uni(beginvalue);
            assertEquals("Testing to UNICODE item #" + i, cp.uni2ebcdic(converted), cpex.uni2ebcdic(converted));
            final byte afterall = cp.uni2ebcdic(converted);
            assertEquals("Testing before and after item #" + i, beginvalue, afterall);
        }
    }
}
