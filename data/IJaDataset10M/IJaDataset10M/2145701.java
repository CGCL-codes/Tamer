package org.jbuzz.awt.event;

import org.jbuzz.awt.Component;

/**
 * @author Lung-Kai Cheng (lkcheng@users.sourceforge.net)
 */
public class KeyEvent extends ComponentEvent {

    public static final int KEY_TYPED = java.awt.event.KeyEvent.KEY_TYPED;

    public static final int VK_ENTER = java.awt.event.KeyEvent.VK_ENTER;

    public static final int VK_BACK_SPACE = java.awt.event.KeyEvent.VK_BACK_SPACE;

    public static final int VK_TAB = java.awt.event.KeyEvent.VK_TAB;

    public static final int VK_CANCEL = java.awt.event.KeyEvent.VK_CANCEL;

    public static final int VK_CLEAR = java.awt.event.KeyEvent.VK_CLEAR;

    public static final int VK_SHIFT = java.awt.event.KeyEvent.VK_SHIFT;

    public static final int VK_CONTROL = java.awt.event.KeyEvent.VK_CONTROL;

    public static final int VK_ALT = java.awt.event.KeyEvent.VK_ALT;

    public static final int VK_PAUSE = java.awt.event.KeyEvent.VK_PAUSE;

    public static final int VK_CAPS_LOCK = java.awt.event.KeyEvent.VK_CAPS_LOCK;

    public static final int VK_ESCAPE = java.awt.event.KeyEvent.VK_ESCAPE;

    public static final int VK_SPACE = java.awt.event.KeyEvent.VK_SPACE;

    public static final int VK_PAGE_UP = java.awt.event.KeyEvent.VK_PAGE_UP;

    public static final int VK_PAGE_DOWN = java.awt.event.KeyEvent.VK_PAGE_DOWN;

    public static final int VK_END = java.awt.event.KeyEvent.VK_END;

    public static final int VK_HOME = java.awt.event.KeyEvent.VK_HOME;

    public static final int VK_LEFT = java.awt.event.KeyEvent.VK_LEFT;

    public static final int VK_UP = java.awt.event.KeyEvent.VK_UP;

    public static final int VK_RIGHT = java.awt.event.KeyEvent.VK_RIGHT;

    public static final int VK_DOWN = java.awt.event.KeyEvent.VK_DOWN;

    public static final int VK_COMMA = java.awt.event.KeyEvent.VK_COMMA;

    public static final int VK_MINUS = java.awt.event.KeyEvent.VK_MINUS;

    public static final int VK_PERIOD = java.awt.event.KeyEvent.VK_PERIOD;

    public static final int VK_SLASH = java.awt.event.KeyEvent.VK_SLASH;

    public static final int VK_0 = java.awt.event.KeyEvent.VK_0;

    public static final int VK_1 = java.awt.event.KeyEvent.VK_1;

    public static final int VK_2 = java.awt.event.KeyEvent.VK_2;

    public static final int VK_3 = java.awt.event.KeyEvent.VK_3;

    public static final int VK_4 = java.awt.event.KeyEvent.VK_4;

    public static final int VK_5 = java.awt.event.KeyEvent.VK_5;

    public static final int VK_6 = java.awt.event.KeyEvent.VK_6;

    public static final int VK_7 = java.awt.event.KeyEvent.VK_7;

    public static final int VK_8 = java.awt.event.KeyEvent.VK_8;

    public static final int VK_9 = java.awt.event.KeyEvent.VK_9;

    public static final int VK_SEMICOLON = java.awt.event.KeyEvent.VK_SEMICOLON;

    public static final int VK_EQUALS = java.awt.event.KeyEvent.VK_EQUALS;

    public static final int VK_A = java.awt.event.KeyEvent.VK_A;

    public static final int VK_B = java.awt.event.KeyEvent.VK_B;

    public static final int VK_C = java.awt.event.KeyEvent.VK_C;

    public static final int VK_D = java.awt.event.KeyEvent.VK_D;

    public static final int VK_E = java.awt.event.KeyEvent.VK_E;

    public static final int VK_F = java.awt.event.KeyEvent.VK_F;

    public static final int VK_G = java.awt.event.KeyEvent.VK_G;

    public static final int VK_H = java.awt.event.KeyEvent.VK_H;

    public static final int VK_I = java.awt.event.KeyEvent.VK_I;

    public static final int VK_J = java.awt.event.KeyEvent.VK_J;

    public static final int VK_K = java.awt.event.KeyEvent.VK_K;

    public static final int VK_L = java.awt.event.KeyEvent.VK_L;

    public static final int VK_M = java.awt.event.KeyEvent.VK_M;

    public static final int VK_N = java.awt.event.KeyEvent.VK_N;

    public static final int VK_O = java.awt.event.KeyEvent.VK_O;

    public static final int VK_P = java.awt.event.KeyEvent.VK_P;

    public static final int VK_Q = java.awt.event.KeyEvent.VK_Q;

    public static final int VK_R = java.awt.event.KeyEvent.VK_R;

    public static final int VK_S = java.awt.event.KeyEvent.VK_S;

    public static final int VK_T = java.awt.event.KeyEvent.VK_T;

    public static final int VK_U = java.awt.event.KeyEvent.VK_U;

    public static final int VK_V = java.awt.event.KeyEvent.VK_V;

    public static final int VK_W = java.awt.event.KeyEvent.VK_W;

    public static final int VK_X = java.awt.event.KeyEvent.VK_X;

    public static final int VK_Y = java.awt.event.KeyEvent.VK_Y;

    public static final int VK_Z = java.awt.event.KeyEvent.VK_Z;

    public static final int VK_OPEN_BRACKET = java.awt.event.KeyEvent.VK_OPEN_BRACKET;

    public static final int VK_BACK_SLASH = java.awt.event.KeyEvent.VK_BACK_SLASH;

    public static final int VK_CLOSE_BRACKET = java.awt.event.KeyEvent.VK_CLOSE_BRACKET;

    public static final int VK_MULTIPLY = java.awt.event.KeyEvent.VK_MULTIPLY;

    public static final int VK_ADD = java.awt.event.KeyEvent.VK_ADD;

    public static final int VK_SEPARATER = java.awt.event.KeyEvent.VK_SEPARATER;

    public static final int VK_SEPARATOR = java.awt.event.KeyEvent.VK_SEPARATOR;

    public static final int VK_SUBTRACT = java.awt.event.KeyEvent.VK_SUBTRACT;

    public static final int VK_DECIMAL = java.awt.event.KeyEvent.VK_DECIMAL;

    public static final int VK_DIVIDE = java.awt.event.KeyEvent.VK_DIVIDE;

    public static final int VK_DELETE = java.awt.event.KeyEvent.VK_DELETE;

    public static final int VK_NUM_LOCK = java.awt.event.KeyEvent.VK_NUM_LOCK;

    public static final int VK_SCROLL_LOCK = java.awt.event.KeyEvent.VK_SCROLL_LOCK;

    public static final int VK_F1 = java.awt.event.KeyEvent.VK_F1;

    public static final int VK_F2 = java.awt.event.KeyEvent.VK_F2;

    public static final int VK_F3 = java.awt.event.KeyEvent.VK_F3;

    public static final int VK_F4 = java.awt.event.KeyEvent.VK_F4;

    public static final int VK_F5 = java.awt.event.KeyEvent.VK_F5;

    public static final int VK_F6 = java.awt.event.KeyEvent.VK_F6;

    public static final int VK_F7 = java.awt.event.KeyEvent.VK_F7;

    public static final int VK_F8 = java.awt.event.KeyEvent.VK_F8;

    public static final int VK_F9 = java.awt.event.KeyEvent.VK_F9;

    public static final int VK_F10 = java.awt.event.KeyEvent.VK_F10;

    public static final int VK_F11 = java.awt.event.KeyEvent.VK_F11;

    public static final int VK_F12 = java.awt.event.KeyEvent.VK_F12;

    public static final int VK_F13 = java.awt.event.KeyEvent.VK_F13;

    public static final int VK_F14 = java.awt.event.KeyEvent.VK_F14;

    public static final int VK_F15 = java.awt.event.KeyEvent.VK_F15;

    public static final int VK_F16 = java.awt.event.KeyEvent.VK_F16;

    public static final int VK_F17 = java.awt.event.KeyEvent.VK_F17;

    public static final int VK_F18 = java.awt.event.KeyEvent.VK_F18;

    public static final int VK_F19 = java.awt.event.KeyEvent.VK_F19;

    public static final int VK_F20 = java.awt.event.KeyEvent.VK_F20;

    public static final int VK_F21 = java.awt.event.KeyEvent.VK_F21;

    public static final int VK_F22 = java.awt.event.KeyEvent.VK_F22;

    public static final int VK_F23 = java.awt.event.KeyEvent.VK_F23;

    public static final int VK_F24 = java.awt.event.KeyEvent.VK_F24;

    public static final int VK_PRINTSCREEN = java.awt.event.KeyEvent.VK_PRINTSCREEN;

    public static final int VK_INSERT = java.awt.event.KeyEvent.VK_INSERT;

    public static final int VK_HELP = java.awt.event.KeyEvent.VK_HELP;

    public static final int VK_META = java.awt.event.KeyEvent.VK_META;

    public static final int VK_BACK_QUOTE = java.awt.event.KeyEvent.VK_BACK_QUOTE;

    public static final int VK_QUOTE = java.awt.event.KeyEvent.VK_QUOTE;

    public static final int VK_AMPERSAND = java.awt.event.KeyEvent.VK_AMPERSAND;

    public static final int VK_ASTERISK = java.awt.event.KeyEvent.VK_ASTERISK;

    public static final int VK_QUOTEDBL = java.awt.event.KeyEvent.VK_QUOTEDBL;

    public static final int VK_LESS = java.awt.event.KeyEvent.VK_LESS;

    public static final int VK_GREATER = java.awt.event.KeyEvent.VK_GREATER;

    public static final int VK_BRACELEFT = java.awt.event.KeyEvent.VK_BRACELEFT;

    public static final int VK_BRACERIGHT = java.awt.event.KeyEvent.VK_BRACERIGHT;

    public static final int VK_AT = java.awt.event.KeyEvent.VK_AT;

    public static final int VK_COLON = java.awt.event.KeyEvent.VK_COLON;

    public static final int VK_CIRCUMFLEX = java.awt.event.KeyEvent.VK_CIRCUMFLEX;

    public static final int VK_DOLLAR = java.awt.event.KeyEvent.VK_DOLLAR;

    public static final int VK_EURO_SIGN = java.awt.event.KeyEvent.VK_EURO_SIGN;

    public static final int VK_EXCLAMATION_MARK = java.awt.event.KeyEvent.VK_EXCLAMATION_MARK;

    public static final int VK_INVERTED_EXCLAMATION_MARK = java.awt.event.KeyEvent.VK_INVERTED_EXCLAMATION_MARK;

    public static final int VK_LEFT_PARENTHESIS = java.awt.event.KeyEvent.VK_LEFT_PARENTHESIS;

    public static final int VK_NUMBER_SIGN = java.awt.event.KeyEvent.VK_NUMBER_SIGN;

    public static final int VK_PLUS = java.awt.event.KeyEvent.VK_PLUS;

    public static final int VK_RIGHT_PARENTHESIS = java.awt.event.KeyEvent.VK_RIGHT_PARENTHESIS;

    public static final int VK_UNDERSCORE = java.awt.event.KeyEvent.VK_UNDERSCORE;

    public static final int VK_UNDEFINED = java.awt.event.KeyEvent.VK_UNDEFINED;

    public static final char CHAR_UNDEFINED = java.awt.event.KeyEvent.CHAR_UNDEFINED;

    public static String getKeyText(int keyCode) {
        return java.awt.event.KeyEvent.getKeyText(keyCode);
    }

    public static String getKeyModifiersText(int modifiers) {
        return java.awt.event.KeyEvent.getKeyModifiersText(modifiers);
    }

    long when;

    int modifiers;

    int keyCode;

    char keyChar;

    int terminalKey;

    public KeyEvent(Component source, int id, long when, int modifiers, int keyCode, char keyChar) {
        super(source, id);
        this.when = when;
        this.modifiers = modifiers;
        this.keyCode = keyCode;
        this.keyChar = keyChar;
    }

    public long getWhen() {
        return this.when;
    }

    public void setWhen(long when) {
        this.when = when;
    }

    public int getModifiers() {
        return this.modifiers;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    public int getKeyCode() {
        return this.keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public char getKeyChar() {
        return this.keyChar;
    }

    public void setKeyChar(char keyChar) {
        this.keyChar = keyChar;
    }

    public int getTerminalKey() {
        return this.terminalKey;
    }

    public void setTerminalKey(int terminalKey) {
        this.terminalKey = terminalKey;
    }
}
