package avrora.sim.platform;

import avrora.sim.mcu.USART;

/**
 * Debug class. Connect this for TestUart test case. "Formats" LCD display.
 *
 * @author Daniel Lee
 * @author Ben L. Titzer
 */
public class LCDScreen implements USART.USARTDevice {

    static final boolean MODE_DATA = false;

    static final boolean MODE_INSTRUCTION = true;

    static final int CLEAR_SCREEN = 1;

    static final int SCROLL_LEFT = 24;

    static final int SCROLL_RIGHT = 28;

    static final int HOME = 2;

    static final int CURSOR_UNDERLINE = 14;

    static final int CURSOR_BLOCK = 13;

    static final int CURSOR_INVIS = 12;

    static final int BLANK_DISPLAY = 8;

    static final int RESTORE_DISPLAY = 12;

    int cursor;

    boolean mode;

    final char[] line1;

    final char[] line2;

    /**
     * The constructor for the <code>LCDScreen</code> class initializes a 40x2 character array that
     * represents the character area of the LCD screen.
     */
    public LCDScreen() {
        line1 = new char[40];
        line2 = new char[40];
        clearScreen();
    }

    /**
     * The <code>memory()</code> method returns the character in memory at the given location.
     * @param cursor the index into the memory for which to retrieve the character
     * @return the character that is in the specified memory location
     */
    public char memory(byte cursor) {
        if (cursor < 40) {
            return line1[cursor];
        } else if (cursor < 80) {
            return line2[cursor - 40];
        } else {
            return 'c';
        }
    }

    public USART.Frame transmitFrame() {
        return new USART.Frame((byte) 0, false, 8);
    }

    private void scrollRight() {
        char c1 = line1[39];
        System.arraycopy(line1, 0, line1, 1, 39);
        line1[0] = c1;
        char c2 = line2[39];
        System.arraycopy(line2, 0, line2, 1, 39);
        line2[0] = c2;
    }

    private void scrollLeft() {
        char c1 = line1[0];
        System.arraycopy(line1, 1, line1, 0, 39);
        line1[39] = c1;
        char c2 = line2[0];
        System.arraycopy(line2, 1, line2, 0, 39);
        line2[39] = c2;
    }

    private void clearScreen() {
        for (int i = 0; i < 40; i++) {
            line1[i] = ' ';
            line2[i] = ' ';
        }
    }

    /**
     * The <code>exchange()</code> method receives a frame from the USART that this device is
     * connected to. It then decodes the command, performs the specified action, and updates the
     * character memory accordingly.
     * @param frame the USART frame to receive
     */
    public void receiveFrame(USART.Frame frame) {
        int data = frame.value;
        if (mode) {
            switch(data) {
                case CLEAR_SCREEN:
                    clearScreen();
                    break;
                case SCROLL_LEFT:
                    scrollLeft();
                    break;
                case SCROLL_RIGHT:
                    scrollRight();
                    break;
                case HOME:
                    cursor = 0;
                    break;
                default:
                    if (data >= 192) {
                        cursor = data + 40 - 192;
                    } else if (data >= 128) {
                        cursor = data - 128;
                    }
                    break;
            }
            mode = MODE_DATA;
        } else if (data == (byte) 254) {
            mode = MODE_INSTRUCTION;
        } else {
            setCursor(frame.value);
            cursor = (cursor + 1) % 80;
        }
    }

    private void setCursor(int b) {
        if (cursor < 40) {
            line1[cursor] = (char) b;
        } else if (cursor < 80) {
            line2[cursor - 40] = (char) b;
        }
    }

    /**
     * The <code>toString()</code> method converts this LCD screen into a String representation.
     * @return a string representation of this LCD screen.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer(90);
        buf.append("\n|");
        buf.append(line1);
        buf.append("|\n");
        buf.append(line2);
        buf.append("|\n");
        return buf.toString();
    }
}
