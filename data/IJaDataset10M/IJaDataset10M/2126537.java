package org.equanda.util;

import junit.framework.TestCase;
import java.util.Map;
import java.util.Hashtable;
import org.equanda.util.barcode.*;

public class BarcodeTest extends TestCase {

    public void testBuilder() throws Exception {
        Map map = new Hashtable();
        map.put("01", "00041771662486");
        map.put("10", "54997630941079ABCD12");
        map.put("251", "BE12345678");
        EAN128Builder builder = new EAN128Sato();
        assertEquals(">I>F01000417716624861054997630941079>DABCD>C12>F25>D1BE>C12345678", builder.build(map));
        assertEquals("(01)00041771662486(10)54997630941079ABCD12(251)BE12345678", builder.buildLabel(map));
        map.clear();
        map.put("91", "XXY0120021234567");
        assertEquals(">I>F91>DXXY0>C120021234567", builder.build(map));
        assertEquals("(91)XXY0120021234567", builder.buildLabel(map));
    }

    public void testIterator() throws Exception {
        EAN128Iterator it;
        EAN128Part part;
        it = new EAN128Iterator("1012345678901234ABCDEF91U0120021234567");
        part = it.next();
        assertEquals("10", part.getAI());
        assertEquals("12345678901234ABCDEF", part.getValue());
        part = it.next();
        assertEquals("91", part.getAI());
        assertEquals("U0120021234567", part.getValue());
        assertFalse(it.hasNext());
        it = new EAN128Iterator("91U0120021234567");
        part = it.next();
        assertEquals("91", part.getAI());
        assertEquals("U0120021234567", part.getValue());
        assertFalse(it.hasNext());
        it = new EAN128Iterator("011234567890123491U0120021234567");
        part = it.next();
        assertEquals("01", part.getAI());
        assertEquals("12345678901234", part.getValue());
        part = it.next();
        assertEquals("91", part.getAI());
        assertEquals("U0120021234567", part.getValue());
        assertFalse(it.hasNext());
        it = new EAN128Iterator("011234567890123491U0120021234567");
        part = it.next();
        assertEquals("01", part.getAI());
        assertEquals("12345678901234", part.getValue());
        part = it.next();
        assertEquals("91", part.getAI());
        assertEquals("U0120021234567", part.getValue());
        assertFalse(it.hasNext());
        it = new EAN128Iterator("(01)12345678901234(91)U0120021234567");
        part = it.next();
        assertEquals("01", part.getAI());
        assertEquals("12345678901234", part.getValue());
        part = it.next();
        assertEquals("91", part.getAI());
        assertEquals("U0120021234567", part.getValue());
        assertFalse(it.hasNext());
    }

    public void testWeightIteratorTyped() throws Exception {
        EAN128Iterator it = new EAN128Iterator("(01)92652700000005(3103)026795(10)00400070632");
        EAN128Part part = it.next();
        assertEquals("01", part.getAI());
        assertEquals("92652700000005", part.getValue());
        part = it.next();
        assertEquals("3103", part.getAI());
        assertEquals("026795", part.getValue());
        part = it.next();
        assertEquals("10", part.getAI());
        assertEquals("00400070632", part.getValue());
    }

    public void testWeightIteratorScanned() throws Exception {
        EAN128Iterator it = new EAN128Iterator("]C1019265270000000531030267951000400070632");
        EAN128Part part = it.next();
        assertEquals("01", part.getAI());
        assertEquals("92652700000005", part.getValue());
        part = it.next();
        assertEquals("3103", part.getAI());
        assertEquals("026795", part.getValue());
        part = it.next();
        assertEquals("10", part.getAI());
        assertEquals("00400070632", part.getValue());
    }
}
