package com.raelity.jvi;

/**
 * The vim code uses character constants and key constants.
 * Characters are unicode characters. Keys are keystrokes that
 * do not have a character value, for example the down key is
 * not a character.
 * In this file define the keys used by vi. All keys must be representable
 * in 16 bits, so that they fit into unicode strings. Further,
 * we don't want the keys (as opposed to characters) to have
 * a valid unicode value. The unicode spec reserves
 * \\uE000 - \\uF8FF for private use according to the doc.
 * So keys (not chars) are put into this range.
 * <p>
 * So set up this magic range as follows:
 * <br>
 * \\uEXYY the second hex digit "X" encodes the four modifiers,
 * shft, ctrl, meta and alt.
 * <br>
 * The bottom byte "YY" is arbitrarily assigned below to refer to the keys.
 * </p><p>
 * There is a further complication for vi, some of the special keys have
 * separate values for the shifted variety, e.g. K_S_UP is a shifted up key.
 * The function keys also have separate defines for the shifted varieties,
 * but we will get rid of these defines and use generic techniques when
 * dealing with shifted items like in the map command.
 * Note: the special shifted varieties do not have to be kept within
 * 16 bits. These values are used primarily in switch statements and
 * for equality tests. They could be constructed by GetChar.gotc()
 * and GetChar.pumpChar() when a special shifted character is encountered.
 * <br>
 * Since there are less than 16 keys that have separate shifted defines,
 * offset them from their unshifted values.
 * <br>So the range \\uEX10 - \\uEX1F is for these shifted special keys,
 * and they are identified by being in the first 16.
 * </p>
 */
public interface KeyDefs {

    /**
   * Any keys as opposed to characters, must be put out of range.
   * This is used to flag keys (non-ascii characters).
   */
    public static final char VIRT = 0xE000;

    /** modifier tags for the keys. */
    public static final char SHFT = 0x01;

    public static final char CTRL = 0x02;

    public static final char META = 0x04;

    public static final char ALT = 0x08;

    public static final int MODIFIER_POSITION_SHIFT = 8;

    /** the special keys that have shifted versions */
    public static final char SHIFTED_VIRT_OFFSET = 0x10;

    public static final char K_CCIRCM = 0x1e;

    public static final char K_ZERO = 0xf8ff;

    public static final char MAP_K_UP = 0;

    public static final char MAP_K_DOWN = 1;

    public static final char MAP_K_LEFT = 2;

    public static final char MAP_K_RIGHT = 3;

    public static final char MAP_K_TAB = 4;

    public static final char MAP_K_HOME = 5;

    public static final char MAP_K_END = 6;

    public static final char MAP_K_F1 = 0x20;

    public static final char MAP_K_F2 = 0x21;

    public static final char MAP_K_F3 = 0x22;

    public static final char MAP_K_F4 = 0x23;

    public static final char MAP_K_F5 = 0x24;

    public static final char MAP_K_F6 = 0x25;

    public static final char MAP_K_F7 = 0x26;

    public static final char MAP_K_F8 = 0x27;

    public static final char MAP_K_F9 = 0x28;

    public static final char MAP_K_F10 = 0x29;

    public static final char MAP_K_F11 = 0x2a;

    public static final char MAP_K_F12 = 0x2b;

    public static final char MAP_K_F13 = 0x2c;

    public static final char MAP_K_F14 = 0x2d;

    public static final char MAP_K_F15 = 0x2e;

    public static final char MAP_K_F16 = 0x2f;

    public static final char MAP_K_F17 = 0x30;

    public static final char MAP_K_F18 = 0x31;

    public static final char MAP_K_F19 = 0x32;

    public static final char MAP_K_F20 = 0x33;

    public static final char MAP_K_F21 = 0x34;

    public static final char MAP_K_F22 = 0x35;

    public static final char MAP_K_F23 = 0x36;

    public static final char MAP_K_F24 = 0x37;

    public static final char MAP_K_HELP = 0x38;

    public static final char MAP_K_UNDO = 0x39;

    public static final char MAP_K_BS = 0x3a;

    public static final char MAP_K_INS = 0x3b;

    public static final char MAP_K_DEL = 0x3c;

    public static final char MAP_K_PAGEUP = 0x3d;

    public static final char MAP_K_PAGEDOWN = 0x3e;

    public static final char MAP_K_KPLUS = 0x3f;

    public static final char MAP_K_KMINUS = 0x40;

    public static final char MAP_K_KDIVIDE = 0x41;

    public static final char MAP_K_KMULTIPLY = 0x42;

    public static final char MAP_K_KENTER = 0x43;

    public static final char MAP_K_X_PERIOD = 0x44;

    public static final char MAP_K_X_COMMA = 0x45;

    public static final char MAP_K_X_SEARCH_FINISH = 0x46;

    public static final char MAP_K_X_INCR_SEARCH_DONE = 0x47;

    public static final char MAP_K_X_SEARCH_CANCEL = 0x48;

    public static final char MAP_K_X_IM_SHIFT_RIGHT = 0x49;

    public static final char MAP_K_X_IM_SHIFT_LEFT = 0x4a;

    public static final char MAP_K_X_IM_INS_REP = 0x4b;

    public static final char MAX_JAVA_KEY_MAP = 0x4b;

    public static final char K_UP = MAP_K_UP + VIRT;

    public static final char K_DOWN = MAP_K_DOWN + VIRT;

    public static final char K_LEFT = MAP_K_LEFT + VIRT;

    public static final char K_RIGHT = MAP_K_RIGHT + VIRT;

    public static final char K_TAB = MAP_K_TAB + VIRT;

    public static final char K_HOME = MAP_K_HOME + VIRT;

    public static final char K_END = MAP_K_END + VIRT;

    public static final char K_S_UP = K_UP + SHIFTED_VIRT_OFFSET;

    public static final char K_S_DOWN = K_DOWN + SHIFTED_VIRT_OFFSET;

    public static final char K_S_LEFT = K_LEFT + SHIFTED_VIRT_OFFSET;

    public static final char K_S_RIGHT = K_RIGHT + SHIFTED_VIRT_OFFSET;

    public static final char K_S_TAB = K_TAB + SHIFTED_VIRT_OFFSET;

    public static final char K_S_HOME = K_HOME + SHIFTED_VIRT_OFFSET;

    public static final char K_S_END = K_END + SHIFTED_VIRT_OFFSET;

    public static final char K_F1 = MAP_K_F1 + VIRT;

    public static final char K_F2 = MAP_K_F2 + VIRT;

    public static final char K_F3 = MAP_K_F3 + VIRT;

    public static final char K_F4 = MAP_K_F4 + VIRT;

    public static final char K_F5 = MAP_K_F5 + VIRT;

    public static final char K_F6 = MAP_K_F6 + VIRT;

    public static final char K_F7 = MAP_K_F7 + VIRT;

    public static final char K_F8 = MAP_K_F8 + VIRT;

    public static final char K_F9 = MAP_K_F9 + VIRT;

    public static final char K_F10 = MAP_K_F10 + VIRT;

    public static final char K_F11 = MAP_K_F11 + VIRT;

    public static final char K_F12 = MAP_K_F12 + VIRT;

    public static final char K_F13 = MAP_K_F13 + VIRT;

    public static final char K_F14 = MAP_K_F14 + VIRT;

    public static final char K_F15 = MAP_K_F15 + VIRT;

    public static final char K_F16 = MAP_K_F16 + VIRT;

    public static final char K_F17 = MAP_K_F17 + VIRT;

    public static final char K_F18 = MAP_K_F18 + VIRT;

    public static final char K_F19 = MAP_K_F19 + VIRT;

    public static final char K_F20 = MAP_K_F20 + VIRT;

    public static final char K_F21 = MAP_K_F21 + VIRT;

    public static final char K_F22 = MAP_K_F22 + VIRT;

    public static final char K_F23 = MAP_K_F23 + VIRT;

    public static final char K_F24 = MAP_K_F24 + VIRT;

    public static final char K_HELP = MAP_K_HELP + VIRT;

    public static final char K_UNDO = MAP_K_UNDO + VIRT;

    public static final char K_BS = MAP_K_BS + VIRT;

    public static final char K_INS = MAP_K_INS + VIRT;

    public static final char K_DEL = MAP_K_DEL + VIRT;

    public static final char K_PAGEUP = MAP_K_PAGEUP + VIRT;

    public static final char K_PAGEDOWN = MAP_K_PAGEDOWN + VIRT;

    public static final char K_KPLUS = MAP_K_KPLUS + VIRT;

    public static final char K_KMINUS = MAP_K_KMINUS + VIRT;

    public static final char K_KDIVIDE = MAP_K_KDIVIDE + VIRT;

    public static final char K_KMULTIPLY = MAP_K_KMULTIPLY + VIRT;

    public static final char K_KENTER = MAP_K_KENTER + VIRT;

    public static final char K_X_PERIOD = MAP_K_X_PERIOD + VIRT;

    public static final char K_X_COMMA = MAP_K_X_COMMA + VIRT;

    public static final char K_X_SEARCH_FINISH = MAP_K_X_SEARCH_FINISH + VIRT;

    public static final char K_X_INCR_SEARCH_DONE = MAP_K_X_INCR_SEARCH_DONE + VIRT;

    public static final char K_X_SEARCH_CANCEL = MAP_K_X_SEARCH_CANCEL + VIRT;

    public static final char K_X_IM_SHIFT_RIGHT = MAP_K_X_IM_SHIFT_RIGHT + VIRT;

    public static final char K_X_IM_SHIFT_LEFT = MAP_K_X_IM_SHIFT_LEFT + VIRT;

    public static final char K_X_IM_INS_REP = MAP_K_X_IM_INS_REP + VIRT;
}
