package net.mikaboshi.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class CodePointUtilsTest {

    @Test
    public void test_toCodePointArray_半角() {
        String str = "abc";
        int[] result = CodePointUtils.toCodePointArray(str);
        String actual = new String(result, 0, result.length);
        assertEquals(str, actual);
    }

    @Test
    public void test_toCodePointArray_全角() {
        String str = "あいう";
        int[] result = CodePointUtils.toCodePointArray(str);
        String actual = new String(result, 0, result.length);
        assertEquals(str, actual);
    }

    @Test
    public void test_toCodePointArray_JIS2004() {
        int codePonit = 0x0002000B;
        String str = new String(new int[] { codePonit }, 0, 1);
        int[] result = CodePointUtils.toCodePointArray(str);
        String actual = new String(result, 0, result.length);
        assertEquals(str, actual);
    }
}
