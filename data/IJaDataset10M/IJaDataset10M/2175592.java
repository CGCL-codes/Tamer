package com.phloc.types.datatype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link MapDataType}.
 * 
 * @author philip
 */
public final class MapDataTypeTest {

    @Test
    public void testBasic() {
        final MapDataType aLDT = new MapDataType(SimpleDataTypeRegistry.DT_INT, SimpleDataTypeRegistry.DT_STRING);
        assertFalse(aLDT.isSimple());
        assertTrue(aLDT.isComplex());
        assertEquals(SimpleDataTypeRegistry.DT_INT, aLDT.getKeyDataType());
        assertEquals(SimpleDataTypeRegistry.DT_STRING, aLDT.getValueDataType());
        PhlocTestUtils.testDefaultImplementationWithEqualContentObject(aLDT, new MapDataType(SimpleDataTypeRegistry.DT_INT, SimpleDataTypeRegistry.DT_STRING));
        PhlocTestUtils.testDefaultImplementationWithDifferentContentObject(aLDT, new MapDataType(SimpleDataTypeRegistry.DT_INT, SimpleDataTypeRegistry.DT_LONG));
        PhlocTestUtils.testDefaultImplementationWithDifferentContentObject(aLDT, new MapDataType(SimpleDataTypeRegistry.DT_BYTE, SimpleDataTypeRegistry.DT_STRING));
        PhlocTestUtils.testDefaultImplementationWithDifferentContentObject(aLDT, new ListDataType(SimpleDataTypeRegistry.DT_INT));
    }
}
