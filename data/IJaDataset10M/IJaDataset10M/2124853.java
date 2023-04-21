package org.hip.kernel.bom.model.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Iterator;
import org.hip.kernel.bom.model.MappingDef;
import org.hip.kernel.bom.model.MappingDefDef;
import org.hip.kernel.bom.model.impl.MappingDefImpl;
import org.hip.kernel.sys.VSys;
import org.junit.Test;

/**
 * @author: Benno Luthiger
 */
public class MappingDefImplTest {

    @SuppressWarnings("deprecation")
    @Test
    public void testCreation() {
        String[] lExpected = { "tableName", "columnName" };
        MappingDef lDef = new MappingDefImpl();
        assertNotNull("testCreation 1", lDef);
        int i = 0;
        for (Iterator<?> lNames = lDef.getPropertyNames(); lNames.hasNext(); ) {
            assertEquals("testCreation " + i, lExpected[i++], lNames.next());
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreationWithInitialValues() {
        String[] lExpected = { "PERSON", "NAME" };
        Object[][] lInitValues = { { "tableName", lExpected[0] }, { "columnName", lExpected[1] } };
        MappingDef lDef = new MappingDefImpl(lInitValues);
        assertNotNull("testCreationWithInitialValues not null", lDef);
        int i = 0;
        for (Iterator<?> lNames = lDef.getPropertyNames(); lNames.hasNext(); ) {
            String lName = (String) lNames.next();
            try {
                assertEquals("testCreationWithInitialValues " + i, lExpected[i++], lDef.get(lName));
            } catch (Exception exc) {
                VSys.err.println(exc);
            }
        }
    }

    @Test
    public void testEquals() {
        String[] lExpected1 = { "PERSON", "NAME" };
        String[] lExpected2 = { "PERSON", "VORNAME" };
        Object[][] lInitValues1 = { { MappingDefDef.tableName, lExpected1[0] }, { MappingDefDef.columnName, lExpected1[1] } };
        Object[][] lInitValues2 = { { MappingDefDef.tableName, lExpected2[0] }, { MappingDefDef.columnName, lExpected2[1] } };
        MappingDef lDef1 = new MappingDefImpl(lInitValues1);
        MappingDef lDef2 = new MappingDefImpl(lInitValues2);
        MappingDef lDef3 = new MappingDefImpl(lInitValues1);
        assertTrue("equals", lDef1.equals(lDef3));
        assertEquals("equal hash code", lDef1.hashCode(), lDef3.hashCode());
        assertTrue("not equals", !lDef1.equals(lDef2));
        assertTrue("not equal hash code", lDef1.hashCode() != lDef2.hashCode());
    }

    @Test
    public void testToString() {
        String[] lExpected = { "PERSON", "NAME" };
        Object[][] lInitValues = { { MappingDefDef.tableName, lExpected[0] }, { MappingDefDef.columnName, lExpected[1] } };
        MappingDef lDef = new MappingDefImpl(lInitValues);
        assertNotNull("toString not null", lDef);
        assertEquals("toString", "< org.hip.kernel.bom.model.impl.MappingDefImpl tableName=\"PERSON\" columnName=\"NAME\" />", lDef.toString());
    }
}
