package test.zmpp.vm;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.zmpp.base.MemoryAccess;
import org.zmpp.vm.DefaultStoryFileHeader;
import org.zmpp.vm.StoryFileHeader;
import org.zmpp.vm.StoryFileHeader.Attribute;

/**
 * This class is a test for the StoryFileHeader class.
 * 
 * @author Wei-ju Wu
 * @version 1.0
 */
public class StoryFileHeaderTest extends MockObjectTestCase {

    private Mock mockMemAccess;

    private MemoryAccess memaccess;

    private StoryFileHeader fileHeader;

    protected void setUp() throws Exception {
        mockMemAccess = mock(MemoryAccess.class);
        memaccess = (MemoryAccess) mockMemAccess.proxy();
        fileHeader = new DefaultStoryFileHeader(memaccess);
    }

    public void testGetVersion() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x00)).will(returnValue((short) 3));
        assertEquals(3, fileHeader.getVersion());
    }

    public void testGetRelease() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedShort").with(eq(0x02)).will(returnValue(35));
        assertEquals(35, fileHeader.getRelease());
    }

    public void testGetHighMemAddress() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedShort").with(eq(0x04)).will(returnValue(4711));
        assertEquals(4711, fileHeader.getHighMemAddress());
    }

    public void testGetInitialPC() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedShort").with(eq(0x06)).will(returnValue(4712));
        assertEquals(4712, fileHeader.getProgramStart());
    }

    public void testGetDictionaryAddress() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedShort").with(eq(0x08)).will(returnValue(4713));
        assertEquals(4713, fileHeader.getDictionaryAddress());
    }

    public void testGetObjectTableAddress() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedShort").with(eq(0x0a)).will(returnValue(4714));
        assertEquals(4714, fileHeader.getObjectTableAddress());
    }

    public void testGetGlobalsAddress() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedShort").with(eq(0x0c)).will(returnValue(4715));
        assertEquals(4715, fileHeader.getGlobalsAddress());
    }

    public void testGetStaticMemAddress() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedShort").with(eq(0x0e)).will(returnValue(4716));
        assertEquals(4716, fileHeader.getStaticsAddress());
    }

    public void testGetSerialNumber() {
        mockMemAccess.expects(once()).method("readUnsignedByte").with(eq(0x12)).will(returnValue((short) '0'));
        mockMemAccess.expects(once()).method("readUnsignedByte").with(eq(0x13)).will(returnValue((short) '5'));
        mockMemAccess.expects(once()).method("readUnsignedByte").with(eq(0x14)).will(returnValue((short) '1'));
        mockMemAccess.expects(once()).method("readUnsignedByte").with(eq(0x15)).will(returnValue((short) '2'));
        mockMemAccess.expects(once()).method("readUnsignedByte").with(eq(0x16)).will(returnValue((short) '0'));
        mockMemAccess.expects(once()).method("readUnsignedByte").with(eq(0x17)).will(returnValue((short) '9'));
        assertEquals("051209", fileHeader.getSerialNumber());
    }

    public void testGetAbbreviationsAddress() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedShort").with(eq(0x18)).will(returnValue(4717));
        assertEquals(4717, fileHeader.getAbbreviationsAddress());
    }

    public void testGetFileLengthV3() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x00)).will(returnValue((short) 3));
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedShort").with(eq(0x1a)).will(returnValue(4718));
        assertEquals(4718 * 2, fileHeader.getFileLength());
    }

    public void testGetFileLengthV4() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x00)).will(returnValue((short) 4));
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedShort").with(eq(0x1a)).will(returnValue(4718));
        assertEquals(4718, fileHeader.getFileLength());
    }

    public void testGetChecksum() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedShort").with(eq(0x1c)).will(returnValue(4719));
        assertEquals(4719, fileHeader.getChecksum());
    }

    public void testSetScreenHeightV4() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x00)).will(returnValue((short) 4));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x20), eq((short) 255));
        fileHeader.setScreenHeight(255);
    }

    public void testSetScreenHeightV5() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x00)).will(returnValue((short) 5));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x20), eq((short) 255));
        mockMemAccess.expects(once()).method("writeUnsignedShort").with(eq(0x24), eq(255));
        fileHeader.setScreenHeight(255);
    }

    public void testGetScreenWidth() {
        mockMemAccess.expects(once()).method("readUnsignedByte").with(eq(0x21)).will(returnValue((short) 82));
        assertEquals(82, fileHeader.getScreenWidth());
    }

    public void testSetScreenWidthV4() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x00)).will(returnValue((short) 4));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x21), eq((short) 82));
        fileHeader.setScreenWidth(82);
    }

    public void testSetScreenWidthV5() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x00)).will(returnValue((short) 5));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x21), eq((short) 82));
        mockMemAccess.expects(once()).method("writeUnsignedShort").with(eq(0x22), eq(82));
        fileHeader.setScreenWidth(82);
    }

    public void testSetInterpreterVersionV5() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x00)).will(returnValue((short) 5));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x1f), eq((short) '4'));
        fileHeader.setInterpreterVersion(4);
    }

    public void testSetInterpreterVersionV8() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x00)).will(returnValue((short) 8));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x1f), eq((short) 4));
        fileHeader.setInterpreterVersion(4);
    }

    public void testSetInterpreterNumber() {
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x1e), eq((short) 3));
        fileHeader.setInterpreterNumber(3);
    }

    public void testIsEnabledNull() {
        assertFalse(fileHeader.isEnabled(Attribute.SUPPORTS_STATUSLINE));
    }

    public void testSetTranscripting() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x10)).will(returnValue((short) 0));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x10), eq((short) 1));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x10), eq((short) 0));
        fileHeader.setEnabled(Attribute.TRANSCRIPTING, true);
        fileHeader.setEnabled(Attribute.TRANSCRIPTING, false);
    }

    public void testIsTranscriptingEnabled() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x10)).will(onConsecutiveCalls(returnValue((short) 1), returnValue((short) 0)));
        assertTrue(fileHeader.isEnabled(Attribute.TRANSCRIPTING));
        assertFalse(fileHeader.isEnabled(Attribute.TRANSCRIPTING));
    }

    public void testSetForceFixedFont() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x10)).will(returnValue((short) 1));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x10), eq((short) 3));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x10), eq((short) 1));
        fileHeader.setEnabled(Attribute.FORCE_FIXED_FONT, true);
        fileHeader.setEnabled(Attribute.FORCE_FIXED_FONT, false);
    }

    public void testIsForceFixedFont() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x10)).will(onConsecutiveCalls(returnValue((short) 6), returnValue((short) 5)));
        assertTrue(fileHeader.isEnabled(Attribute.FORCE_FIXED_FONT));
        assertFalse(fileHeader.isEnabled(Attribute.FORCE_FIXED_FONT));
    }

    public void testSetSupportsTimedInput() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x01)).will(onConsecutiveCalls(returnValue((short) 3), returnValue((short) 131)));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x01), eq((short) 131));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x01), eq((short) 3));
        fileHeader.setEnabled(Attribute.SUPPORTS_TIMED_INPUT, true);
        fileHeader.setEnabled(Attribute.SUPPORTS_TIMED_INPUT, false);
    }

    public void testIsScoreGame() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x01)).will(onConsecutiveCalls(returnValue((short) 5), returnValue((short) 7)));
        assertTrue(fileHeader.isEnabled(Attribute.SCORE_GAME));
        assertFalse(fileHeader.isEnabled(Attribute.SCORE_GAME));
    }

    public void testSetSupportsFixed() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x01)).will(onConsecutiveCalls(returnValue((short) 1), returnValue((short) 17)));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x01), eq((short) 17));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x01), eq((short) 1));
        fileHeader.setEnabled(Attribute.SUPPORTS_FIXED_FONT, true);
        fileHeader.setEnabled(Attribute.SUPPORTS_FIXED_FONT, false);
    }

    public void testSetSupportsBold() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x01)).will(onConsecutiveCalls(returnValue((short) 1), returnValue((short) 5)));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x01), eq((short) 5));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x01), eq((short) 1));
        fileHeader.setEnabled(Attribute.SUPPORTS_BOLD, true);
        fileHeader.setEnabled(Attribute.SUPPORTS_BOLD, false);
    }

    public void testSetSupportsItalic() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x01)).will(onConsecutiveCalls(returnValue((short) 1), returnValue((short) 9)));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x01), eq((short) 9));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x01), eq((short) 1));
        fileHeader.setEnabled(Attribute.SUPPORTS_ITALIC, true);
        fileHeader.setEnabled(Attribute.SUPPORTS_ITALIC, false);
    }

    public void testSetSupportsScreenSplitting() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x01)).will(onConsecutiveCalls(returnValue((short) 1), returnValue((short) 33)));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x01), eq((short) 33));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x01), eq((short) 1));
        fileHeader.setEnabled(Attribute.SUPPORTS_SCREEN_SPLITTING, true);
        fileHeader.setEnabled(Attribute.SUPPORTS_SCREEN_SPLITTING, false);
    }

    public void testSetSupportsStatusLine() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x01)).will(onConsecutiveCalls(returnValue((short) 17), returnValue((short) 1)));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x01), eq((short) 1));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x01), eq((short) 17));
        fileHeader.setEnabled(Attribute.SUPPORTS_STATUSLINE, true);
        fileHeader.setEnabled(Attribute.SUPPORTS_STATUSLINE, false);
    }

    public void testSetDefaultFontIsVariable() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x01)).will(onConsecutiveCalls(returnValue((short) 1), returnValue((short) 65)));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x01), eq((short) 65));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x01), eq((short) 1));
        fileHeader.setEnabled(Attribute.DEFAULT_FONT_IS_VARIABLE, true);
        fileHeader.setEnabled(Attribute.DEFAULT_FONT_IS_VARIABLE, false);
    }

    public void testIsDefaultFontVariable() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x01)).will(onConsecutiveCalls(returnValue((short) 69), returnValue((short) 7)));
        assertTrue(fileHeader.isEnabled(Attribute.DEFAULT_FONT_IS_VARIABLE));
        assertFalse(fileHeader.isEnabled(Attribute.DEFAULT_FONT_IS_VARIABLE));
    }

    public void testSetSupportsColors() {
        mockMemAccess.expects(atLeastOnce()).method("readUnsignedByte").with(eq(0x01)).will(onConsecutiveCalls(returnValue((short) 4), returnValue((short) 5)));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x01), eq((short) 5));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x01), eq((short) 4));
        fileHeader.setEnabled(Attribute.SUPPORTS_COLOURS, true);
        fileHeader.setEnabled(Attribute.SUPPORTS_COLOURS, false);
    }

    public void testGetTerminatorsAddress() {
        mockMemAccess.expects(once()).method("readUnsignedShort").with(eq(0x2e)).will(returnValue(1234));
        assertEquals(1234, fileHeader.getTerminatorsAddress());
    }

    public void testSetStandardRevision() {
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x32), eq((short) 1));
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x33), eq((short) 2));
        fileHeader.setStandardRevision(1, 2);
    }

    public void testSetFontWidth() {
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x26), eq((short) 1));
        fileHeader.setFontWidth(1);
    }

    public void testSetFontHeight() {
        mockMemAccess.expects(once()).method("writeUnsignedByte").with(eq(0x27), eq((short) 2));
        fileHeader.setFontHeight(2);
    }

    public void testUseMouseFalse() {
        mockMemAccess.expects(once()).method("readUnsignedByte").with(eq(0x10)).will(returnValue((short) 2));
        assertFalse(fileHeader.isEnabled(Attribute.USE_MOUSE));
    }

    public void testUseMouseTrue() {
        mockMemAccess.expects(once()).method("readUnsignedByte").with(eq(0x10)).will(returnValue((short) 63));
        assertTrue(fileHeader.isEnabled(Attribute.USE_MOUSE));
    }

    public void testGetCustomAlphabetTable() {
        mockMemAccess.expects(once()).method("readUnsignedShort").with(eq(0x34)).will(returnValue(63));
        fileHeader.getCustomAlphabetTable();
    }

    public void testSetMouseCoordinatesNoExtensionTable() {
        mockMemAccess.expects(once()).method("readUnsignedShort").with(eq(0x36)).will(returnValue(0));
        fileHeader.setMouseCoordinates(1, 2);
    }

    public void testSetMouseCoordinatesHasExtensionTable() {
        mockMemAccess.expects(once()).method("readUnsignedShort").with(eq(0x36)).will(returnValue(100));
        mockMemAccess.expects(once()).method("readUnsignedShort").with(eq(100)).will(returnValue(2));
        mockMemAccess.expects(once()).method("writeUnsignedShort").with(eq(101), eq(1));
        mockMemAccess.expects(once()).method("writeUnsignedShort").with(eq(102), eq(2));
        fileHeader.setMouseCoordinates(1, 2);
    }

    public void testGetUnicodeTranslationTableNoExtensionTable() {
        mockMemAccess.expects(once()).method("readUnsignedShort").with(eq(0x36)).will(returnValue(0));
        assertEquals(0, fileHeader.getCustomAccentTable());
    }

    public void testGetCustomUnicodeTranslationTableNoTableInExtTable() {
        mockMemAccess.expects(once()).method("readUnsignedShort").with(eq(0x36)).will(returnValue(100));
        mockMemAccess.expects(once()).method("readUnsignedShort").with(eq(100)).will(returnValue(2));
        assertEquals(0, fileHeader.getCustomAccentTable());
    }

    public void testGetCustomUnicodeTranslationTableHasExtAddress() {
        mockMemAccess.expects(once()).method("readUnsignedShort").with(eq(0x36)).will(returnValue(100));
        mockMemAccess.expects(once()).method("readUnsignedShort").with(eq(100)).will(returnValue(3));
        mockMemAccess.expects(once()).method("readUnsignedShort").with(eq(102)).will(returnValue(1234));
        assertEquals(1234, fileHeader.getCustomAccentTable());
    }
}
