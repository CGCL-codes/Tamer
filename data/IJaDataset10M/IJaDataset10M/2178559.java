package com.google.template.soy.internal.i18n;

import junit.framework.TestCase;

/**
 * Unit tests for BidiGlobalDir.
 *
 */
public class BidiGlobalDirTest extends TestCase {

    public void testBidiGlobalDir() {
        BidiGlobalDir bidiGlobalDir;
        bidiGlobalDir = BidiGlobalDir.forStaticIsRtl(false);
        assertTrue(bidiGlobalDir.isStaticValue());
        assertEquals(1, bidiGlobalDir.getStaticValue());
        assertEquals("1", bidiGlobalDir.getCodeSnippet());
        bidiGlobalDir = BidiGlobalDir.forStaticIsRtl(true);
        assertTrue(bidiGlobalDir.isStaticValue());
        assertEquals(-1, bidiGlobalDir.getStaticValue());
        assertEquals("-1", bidiGlobalDir.getCodeSnippet());
        bidiGlobalDir = BidiGlobalDir.forStaticLocale("en");
        assertTrue(bidiGlobalDir.isStaticValue());
        assertEquals(1, bidiGlobalDir.getStaticValue());
        assertEquals("1", bidiGlobalDir.getCodeSnippet());
        bidiGlobalDir = BidiGlobalDir.forStaticLocale("ar");
        assertTrue(bidiGlobalDir.isStaticValue());
        assertEquals(-1, bidiGlobalDir.getStaticValue());
        assertEquals("-1", bidiGlobalDir.getCodeSnippet());
        bidiGlobalDir = BidiGlobalDir.forStaticLocale(null);
        assertTrue(bidiGlobalDir.isStaticValue());
        assertEquals(1, bidiGlobalDir.getStaticValue());
        assertEquals("1", bidiGlobalDir.getCodeSnippet());
        bidiGlobalDir = BidiGlobalDir.forIsRtlCodeSnippet("IS_RTL");
        assertFalse(bidiGlobalDir.isStaticValue());
        assertEquals("IS_RTL?-1:1", bidiGlobalDir.getCodeSnippet());
        try {
            bidiGlobalDir.getStaticValue();
            fail();
        } catch (RuntimeException e) {
        }
    }
}
