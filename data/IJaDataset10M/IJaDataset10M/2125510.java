package org.tn5250j.cp;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.tn5250j.encoding.CharMappings;
import org.tn5250j.encoding.CodePage;

/**
 * Microbenchmark to test encoding+decoding speed
 * of the 'old' CCSID1141<->Unicode converte versus the new leaner one.
 * 
 * Result: The new implementation is much faster!
 * 
 * @author master_jaf
 */
public class MicroBenchmark_CCSID1141 {

    private static final int RUNS = 50000;

    private final char[] TESTSTRING = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ0987654321abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ0987654321abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ0987654321abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ0987654321abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ0987654321abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ0987654321abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ0987654321abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ0987654321abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ0987654321abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ0987654321".toCharArray();

    private CodePage cp;

    private CCSID1141 cpex;

    @Before
    public void setup() {
        cp = CharMappings.getCodePage("1141");
        cpex = new CCSID1141();
        cpex.init();
        assertNotNull("At least an ASCII Codepage should be available.", cp);
        assertNotNull("At least an ASCII Codepage should be available.", cpex);
    }

    /**
	 * Speed test for new implementation ...
	 */
    @Test(timeout = 10000)
    public void testNewConverter1141() {
        for (int i = 0; i < RUNS; i++) {
            realRunNew();
        }
    }

    /**
	 * Speed test for old implementation ....
	 */
    @Test(timeout = 10000)
    public void testOldConverter1141() {
        for (int i = 0; i < RUNS; i++) {
            realRunOld();
        }
    }

    private void realRunOld() {
        for (int i = 0; i < TESTSTRING.length; i++) {
            final char beginvalue = TESTSTRING[i];
            final byte converted = cp.uni2ebcdic(beginvalue);
            cp.ebcdic2uni(converted);
        }
    }

    private void realRunNew() {
        for (int i = 0; i < TESTSTRING.length; i++) {
            final char beginvalue = TESTSTRING[i];
            final byte converted = cpex.uni2ebcdic(beginvalue);
            cpex.ebcdic2uni(converted & 0xFF);
        }
    }
}
