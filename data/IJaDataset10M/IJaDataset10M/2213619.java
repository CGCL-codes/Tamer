package org.rococoa.internal;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.rococoa.ObjCObject;
import org.rococoa.cocoa.foundation.NSNumber;
import org.rococoa.test.RococoaTestCase;
import com.sun.jna.TypeMapper;

@SuppressWarnings({ "unchecked", "cast" })
public class RococoaTypeMapperTest extends RococoaTestCase {

    private TypeMapper typeMapper = new RococoaTypeMapper();

    @Test
    public void testString() {
        assertTrue(typeMapper.getToNativeConverter(String.class) instanceof StringTypeConverter);
        assertTrue(typeMapper.getFromNativeConverter(String.class) instanceof StringTypeConverter);
    }

    @Test
    public void testObjCObject() {
        ObjCObjectTypeConverter toNativeConverter = (ObjCObjectTypeConverter) typeMapper.getToNativeConverter(NSNumber.class);
        assertTrue(toNativeConverter.convertsJavaType(ObjCObject.class));
        ObjCObjectTypeConverter fromNativeConverter = (ObjCObjectTypeConverter) typeMapper.getFromNativeConverter(NSNumber.class);
        assertTrue(fromNativeConverter.convertsJavaType(NSNumber.class));
    }
}
