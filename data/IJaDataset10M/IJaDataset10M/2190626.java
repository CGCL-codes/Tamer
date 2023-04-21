package org.hip.kernel.util.test;

import static org.junit.Assert.*;
import java.util.Iterator;
import org.hip.kernel.util.CheckValueForUmlauts;
import org.junit.Test;

/**
 * @author: Benno Luthiger
 */
public class CheckValueForUmlautsTest {

    @Test
    public void testElements() {
        String lProcessed = "";
        String lOriginal = "";
        String lExpected = "";
        CheckValueForUmlauts lCheckValue = new CheckValueForUmlauts(lOriginal);
        Iterator<String> lSearchStrings = lCheckValue.elements();
        while (lSearchStrings.hasNext()) {
            lProcessed += lSearchStrings.next();
        }
        assertEquals("Test empty ", lExpected, lProcessed);
        lOriginal = "Bärner";
        lExpected = "BÄRNERBAERNER";
        lCheckValue = new CheckValueForUmlauts(lOriginal);
        lProcessed = "";
        lSearchStrings = lCheckValue.elements();
        while (lSearchStrings.hasNext()) {
            lProcessed += lSearchStrings.next();
        }
        assertEquals(lExpected, lProcessed);
        lOriginal = "Baerner";
        lExpected = "BÄRNERBAERNER";
        lCheckValue = new CheckValueForUmlauts(lOriginal);
        lProcessed = "";
        lSearchStrings = lCheckValue.elements();
        while (lSearchStrings.hasNext()) {
            lProcessed += lSearchStrings.next();
        }
        assertEquals("Test Baerner ", lExpected, lProcessed);
        lOriginal = "Bärnerbuecher";
        lExpected = "BÄRNERBÜCHER BAERNERBÜCHER BÄRNERBUECHER BAERNERBUECHER ";
        lCheckValue = new CheckValueForUmlauts(lOriginal);
        lProcessed = "";
        lSearchStrings = lCheckValue.elements();
        while (lSearchStrings.hasNext()) {
            lProcessed += lSearchStrings.next() + " ";
        }
        assertEquals("Test Börnerbuecher ", lExpected, lProcessed);
        lOriginal = "no umlauts at all";
        lExpected = "NO UMLAUTS AT ALL";
        lCheckValue = new CheckValueForUmlauts(lOriginal);
        lProcessed = "";
        lSearchStrings = lCheckValue.elements();
        while (lSearchStrings.hasNext()) {
            lProcessed += lSearchStrings.next();
        }
        assertEquals("Test NO UMLAUTS AT ALL ", lExpected, lProcessed);
    }
}
