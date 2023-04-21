package org.openfast.template;

import org.openfast.error.FastConstants;
import org.openfast.error.FastException;
import org.openfast.template.type.Type;
import org.openfast.test.OpenFastTestCase;

public class TypeTest extends OpenFastTestCase {

    public void testGetType() {
        assertEquals(Type.U32, Type.getType("uInt32"));
        try {
            Type.getType("u32");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The type named u32 does not exist.  Existing types are {uInt8,uInt16,uInt32,uInt64,int8,int16,int32,int64,string,ascii,unicode,byteVector,decimal}", e.getMessage());
        }
    }

    public void testIncompatibleDefaultValue() {
        try {
            template("<template>" + "  <decimal><copy value=\"10a\"/></decimal>" + "</template>");
            fail();
        } catch (FastException e) {
            assertEquals(FastConstants.S3_INITIAL_VALUE_INCOMP, e.getCode());
            assertEquals("The value \"10a\" is not compatible with type decimal", e.getMessage());
        }
    }
}
