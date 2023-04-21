package com.gm.core.test.lang.encrypt;

import junit.framework.TestCase;
import com.gm.core.lang.encrypt.Unicode;

public class UnicodeTestCase extends TestCase {

    public void testEncode() {
        TestCase.assertEquals("abc", Unicode.encode("abc"));
        TestCase.assertEquals("\\u5C0F\\u5783\\u573E", Unicode.encode("小垃圾"));
        TestCase.assertEquals("\\ab\\c", Unicode.encode("\\ab\\c"));
    }

    public static void testDecode() {
        TestCase.assertEquals("abc", Unicode.decode(Unicode.encode("abc")));
        TestCase.assertEquals("彭宙硕", Unicode.decode(Unicode.encode("彭宙硕")));
    }
}
