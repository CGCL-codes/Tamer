package com.yubarta.docman.impl;

import junit.framework.TestCase;
import com.yubarta.docman.impl.DocPermissionImpl;
import com.yubarta.docman.DataTypeException;
import com.yubarta.docman.DocPermission;
import com.yubarta.docman.LiteralType;

/**
 * @author César
 */
public class DocPermissionImplTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(DocPermissionImplTest.class);
    }

    public void testConstructor() {
        DocPermission dp1 = new DocPermissionImpl("rwrwrw");
        assertTrue(dp1.userCanRead());
        assertTrue(dp1.userCanWrite());
        assertTrue(dp1.groupCanRead());
        assertTrue(dp1.groupCanWrite());
        assertTrue(dp1.othersCanRead());
        assertTrue(dp1.othersCanWrite());
        DocPermission dp2 = new DocPermissionImpl("------");
        assertTrue(!dp2.userCanRead());
        assertTrue(!dp2.userCanWrite());
        assertTrue(!dp2.groupCanRead());
        assertTrue(!dp2.groupCanWrite());
        assertTrue(!dp2.othersCanRead());
        assertTrue(!dp2.othersCanWrite());
        DocPermission dp3 = new DocPermissionImpl("r-r-r");
        assertTrue(dp3.userCanRead());
        assertTrue(!dp3.userCanWrite());
        assertTrue(dp3.groupCanRead());
        assertTrue(!dp3.groupCanWrite());
        assertTrue(dp3.othersCanRead());
        assertTrue(!dp3.othersCanWrite());
        DocPermission dp4 = new DocPermissionImpl("rwrw--");
        assertTrue(dp4.userCanRead());
        assertTrue(dp4.userCanWrite());
        assertTrue(dp4.groupCanRead());
        assertTrue(dp4.groupCanWrite());
        assertTrue(!dp4.othersCanRead());
        assertTrue(!dp4.othersCanWrite());
        DocPermission dp5 = new DocPermissionImpl("rwr---");
        assertTrue(dp5.userCanRead());
        assertTrue(dp5.userCanWrite());
        assertTrue(dp5.groupCanRead());
        assertTrue(!dp5.groupCanWrite());
        assertTrue(!dp5.othersCanRead());
        assertTrue(!dp5.othersCanWrite());
    }

    public final void testValidateValueString() throws DataTypeException {
        LiteralType lt1 = new DocPermissionImpl();
        lt1.validateValue("rwrwrw");
        lt1.validateValue("------");
        lt1.validateValue("r-r-r-");
        lt1.validateValue("-w-w-w");
        try {
            lt1.validateValue("wrwrwr");
            fail("Error sintactico no detectado");
        } catch (DataTypeException dte) {
        }
        try {
            lt1.validateValue("rwrw");
            fail("Error sintactico no detectado");
        } catch (DataTypeException dte) {
        }
        try {
            lt1.validateValue("");
            fail("Error sintactico no detectado");
        } catch (DataTypeException dte) {
        }
    }
}
