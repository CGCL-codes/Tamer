package org.sourceforge.jemm.database.components.types;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Test cases for FieldData class.
 * 
 * @author Rory Graves
 */
public class FieldDataTest {

    /** Test toString behaviour */
    @Test
    public void toStringTest() {
        FieldData fieldData = new FieldData();
        Assert.assertEquals("FieldData(", "FieldData(version=0,value=null)", fieldData.toString());
    }

    /** Test behaviour of setting field values */
    @Test
    public void setTests() {
        FieldData fieldData = new FieldData();
        Assert.assertEquals(0, fieldData.getVersion());
        fieldData.setValue(null, 1);
        Assert.assertEquals(0, fieldData.getVersion());
        Integer intValue1 = Integer.valueOf(1);
        Integer intValue1a = Integer.valueOf(1);
        Integer intValue2 = Integer.valueOf(2);
        fieldData.setValue(intValue1, 5);
        Assert.assertEquals(5, fieldData.getVersion());
        fieldData.setValue(intValue1, 6);
        Assert.assertEquals(5, fieldData.getVersion());
        fieldData.setValue(intValue1a, 6);
        Assert.assertEquals(5, fieldData.getVersion());
        fieldData.setValue(intValue2, 6);
        Assert.assertEquals(6, fieldData.getVersion());
    }
}
