package com.mainatom.af;

import junit.framework.*;

public class AFactoryTest extends TestCase {

    public void test1() throws Exception {
        AFactory f = new AFactory();
        f.setPrefix("Fd,Field");
        f.setSuffix("_field,_f");
        assertEquals(f.makeName("FdString"), "String");
        assertEquals(f.makeName("FxString"), "FxString");
        assertEquals(f.makeName("Fd"), "Fd");
        assertEquals(f.makeName("String_field"), "String");
        assertEquals(f.makeName("FdString_field"), "FdString");
        assertEquals(f.makeName("FieldString"), "String");
        assertEquals(f.makeName("String_f"), "String");
    }
}
