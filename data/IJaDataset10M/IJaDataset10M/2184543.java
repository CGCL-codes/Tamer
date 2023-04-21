package com.prime.yui4jsf.jsfplugin.digester;

import junit.framework.TestCase;

/**
 * Latest modification by $Author: cagatay_civici $
 * @version $Revision: 1279 $ $Date: 2008-04-20 08:06:50 -0400 (Sun, 20 Apr 2008) $
 */
public class AttributeTest extends TestCase {

    public void testGetShortTypeNameForPrimitiveType() {
        Attribute attribute = new Attribute();
        attribute.setType("boolean");
        assertEquals("boolean", attribute.getShortTypeName());
    }

    public void testGetShortTypeNameForNonPrimitiveType() {
        Attribute attribute = new Attribute();
        attribute.setType("java.lang.String");
        assertEquals("String", attribute.getShortTypeName());
    }
}
