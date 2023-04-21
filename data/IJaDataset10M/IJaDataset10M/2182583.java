package org.gbif.ecat.voc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class NomenclaturalCodeTest {

    @Test
    public void testFromString() {
        assertEquals(NomenclaturalCode.Botanical, NomenclaturalCode.fromString("bot"));
        assertEquals(NomenclaturalCode.Botanical, NomenclaturalCode.fromString("Botanical"));
        assertEquals(NomenclaturalCode.Botanical, NomenclaturalCode.fromString("ICBN"));
        assertEquals(NomenclaturalCode.Botanical, NomenclaturalCode.fromString("icbn"));
        assertEquals(NomenclaturalCode.Virus, NomenclaturalCode.fromString("virus"));
        assertEquals(NomenclaturalCode.Virus, NomenclaturalCode.fromString("VIR"));
        assertEquals(NomenclaturalCode.Zoological, NomenclaturalCode.fromString("iczn"));
        assertEquals(NomenclaturalCode.Cultivars, NomenclaturalCode.fromString("culti"));
        assertEquals(NomenclaturalCode.Cultivars, NomenclaturalCode.fromString("ICnCP"));
        assertNull(NomenclaturalCode.fromString("bit"));
        assertNull(NomenclaturalCode.fromString("b"));
        assertNull(NomenclaturalCode.fromString(""));
        assertNull(NomenclaturalCode.fromString(null));
    }
}
