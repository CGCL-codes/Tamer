package org.tranche.hash;

import org.tranche.util.TrancheTestCase;

/**
 * Tests the base 16 encoding.
 * @author Jayson Falkner - jfalkner@umich.edu
 */
public class Base16Test extends TrancheTestCase {

    public void testBase16Conversion() throws Exception {
        String sig = "0123456789abcdef";
        byte[] bytes = Base16.decode(sig);
        assertEquals("String to Base16 back to String should be identical.", sig, Base16.encode(bytes));
    }
}
