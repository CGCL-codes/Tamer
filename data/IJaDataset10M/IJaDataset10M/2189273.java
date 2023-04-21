package net.sf.doolin.gui.swing;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestMnemonicInfo {

    @Test
    public void testBlank() {
        MnemonicInfo i = new MnemonicInfo(" ");
        assertEquals("", i.getText());
        assertEquals('\0', i.getMnemonic());
        assertEquals(-1, i.getMnemonicIndex());
    }

    @Test
    public void testFirst() {
        MnemonicInfo i = new MnemonicInfo("&First");
        assertEquals("First", i.getText());
        assertEquals('F', i.getMnemonic());
        assertEquals(0, i.getMnemonicIndex());
    }

    @Test
    public void testNone() {
        MnemonicInfo i = new MnemonicInfo("None");
        assertEquals("None", i.getText());
        assertEquals('\0', i.getMnemonic());
        assertEquals(-1, i.getMnemonicIndex());
    }

    @Test
    public void testNull() {
        MnemonicInfo i = new MnemonicInfo(null);
        assertEquals("", i.getText());
        assertEquals('\0', i.getMnemonic());
        assertEquals(-1, i.getMnemonicIndex());
    }

    @Test
    public void testSecond() {
        MnemonicInfo i = new MnemonicInfo("This &string");
        assertEquals("This string", i.getText());
        assertEquals('s', i.getMnemonic());
        assertEquals(5, i.getMnemonicIndex());
    }
}
