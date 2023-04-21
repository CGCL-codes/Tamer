package org.xbill.DNS;

import junit.framework.TestCase;

public class TypeBitmapTest extends TestCase {

    public void test_empty() {
        TypeBitmap typeBitmap = new TypeBitmap(new int[] {});
        assertEquals(typeBitmap.toString(), "");
    }

    public void test_typeA() {
        TypeBitmap typeBitmap = new TypeBitmap(new int[] { 1 });
        assertEquals(typeBitmap.toString(), "A");
    }

    public void test_typeNSandSOA() {
        TypeBitmap typeBitmap = new TypeBitmap(new int[] { 2, 6 });
        assertEquals(typeBitmap.toString(), "NS SOA");
    }
}
