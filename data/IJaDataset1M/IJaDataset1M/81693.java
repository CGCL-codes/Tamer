package org.sempere.commons.base;

import org.junit.*;
import static org.junit.Assert.*;
import static org.sempere.commons.base.BooleanHelper.*;

/**
 * Unit tests class for BooleanHelper.
 *
 * @author bsempere
 */
public class BooleanHelperTest {

    @Test
    public void getBooleanfromNull() {
        assertFalse(BooleanHelper.getBoolean(null));
    }

    @Test
    public void getBooleanfromTrue() {
        assertTrue(BooleanHelper.getBoolean(CHARACTER_TRUE));
    }

    @Test
    public void getBooleanfromFalse() {
        assertFalse(BooleanHelper.getBoolean(CHARACTER_FALSE));
    }

    @Test
    public void getBooleanfromWrongString() {
        assertFalse(BooleanHelper.getBoolean('c'));
    }

    @Test
    public void getCharacterYesNoWhenTrue() {
        assertEquals(CHARACTER_YES, BooleanHelper.getCharacterYesNo(true));
    }

    @Test
    public void getCharacterYesNoWhenFalse() {
        assertEquals(CHARACTER_NO, BooleanHelper.getCharacterYesNo(false));
    }

    @Test
    public void getCharacterTrueFalseWhenTrue() {
        assertEquals(CHARACTER_TRUE, BooleanHelper.getCharacterTrueFalse(true));
    }

    @Test
    public void getCharacterTrueFalseWhenFalse() {
        assertEquals(CHARACTER_FALSE, BooleanHelper.getCharacterTrueFalse(false));
    }

    @Test
    public void getCharacterOneZeroWhenTrue() {
        assertEquals(CHARACTER_ONE, BooleanHelper.getCharacterOneZero(true));
    }

    @Test
    public void getCharacterOneZeroWhenFalse() {
        assertEquals(CHARACTER_ZERO, BooleanHelper.getCharacterOneZero(false));
    }
}
