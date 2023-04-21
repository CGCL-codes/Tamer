package org.lc.test.utils;

import junit.framework.TestCase;
import org.lc.util.UUIDHexGenerator;

public class UUIDHexGeneratorTest extends TestCase {

    public void testUUIDGenerator() {
        UUIDHexGenerator gen = new UUIDHexGenerator();
        String str = (String) gen.generate(1);
        String str2 = (String) gen.generate(1);
        String str3 = (String) gen.generate(1);
        String str4 = (String) gen.generate(1);
        System.out.println(str);
        System.out.println(str2);
        System.out.println(str3);
        System.out.println(str4);
        assertEquals(1, gen.getPassportID(str));
        System.out.println(gen.getTimeStamp(str));
    }

    public void testIntToShort() {
        int i = 12345678;
        System.out.println((short) i);
    }
}
