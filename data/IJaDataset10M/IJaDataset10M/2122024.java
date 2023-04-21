package console;

import java.util.Arrays;

/**
 * User: sachinh
 * Date: 25-Dec-2008
 * Time: 23:31:39
 */
public class ConsoleBuffer {

    protected StringBuffer buffer = new StringBuffer(1024);

    private char[] chars = new char[1024];

    protected Coord startCL;

    protected Rect screenSize;

    private boolean echo;

    protected int loc = 0;

    private boolean isRunning = false;

    public ConsoleBuffer(boolean echo) {
        this.echo = echo;
    }

    public void start() {
        loc = 0;
        screenSize = Console.getScreenSize();
        startCL = Console.getCursorLocation();
        setCursorLocation();
        isRunning = true;
        while (isRunning) {
            Key key;
            key = echo ? Console.echoReadChar() : Console.readChar();
            process(key);
        }
    }

    public void stop() {
        isRunning = false;
    }

    public boolean isEcho() {
        return echo;
    }

    protected void process(Key key) {
        int oldLen = buffer.length();
        switch(key.code) {
            case Key.VK_LEFT:
                processLeft(key);
                break;
            case Key.VK_RIGHT:
                processRight(key);
                break;
            case Key.VK_UP:
                processUp(key);
                break;
            case Key.VK_DOWN:
                processDown(key);
                break;
            case Key.VK_BKSPC:
                processBackspace(key);
                break;
            case Key.VK_DEL:
                processDel(key);
                break;
            case Key.VK_HOME:
                processHome(key);
                break;
            case Key.VK_END:
                processEnd(key);
                break;
            case Key.VK_ESC:
                processEscape(key);
                break;
            case Key.VK_INSERT:
                break;
            default:
                processDefault(key);
        }
        printRestOfBuffer(oldLen);
        setCursorLocation();
    }

    protected void processEscape(Key key) {
        replace(0, buffer.length(), "");
    }

    protected void processDefault(Key key) {
        if (key.isCharacter()) {
            processCharacter(key.getCharacter());
        }
    }

    protected void processCharacter(char c) {
        if (loc >= buffer.length()) buffer.append(c); else buffer.setCharAt(loc, c);
        loc++;
    }

    protected void processEnd(Key key) {
        loc = buffer.length();
    }

    protected void processHome(Key key) {
        loc = 0;
    }

    protected void processDel(Key key) {
        int oldLen = buffer.length();
        if (loc < buffer.length()) {
            buffer.deleteCharAt(loc);
            printRestOfBuffer(oldLen);
        }
    }

    protected void processBackspace(Key key) {
        int oldLen = buffer.length();
        if (loc > 0) {
            loc--;
            buffer.deleteCharAt(loc);
            printRestOfBuffer(oldLen);
        }
    }

    protected void processDown(Key key) {
    }

    protected void processUp(Key key) {
    }

    protected void processRight(Key key) {
        if (!key.isControl()) {
            if (loc < buffer.length()) {
                loc++;
                setCursorLocation();
            }
        } else {
            loc = findWordEnd(loc);
        }
    }

    protected void processLeft(Key key) {
        if (!key.isControl()) {
            if (loc > 0) {
                loc--;
                setCursorLocation();
            }
        } else {
            loc = findWordStart(loc);
        }
    }

    protected void replace(int start, int end, String str) {
        int oldLen = buffer.length();
        buffer.replace(start, end, str);
        loc = start;
        printRestOfBuffer(oldLen);
        if (loc > buffer.length()) {
            loc = buffer.length();
        }
        setCursorLocation();
    }

    protected void replace(int start, int end, char[] chars, int off, int len) {
        replace(start, end, new String(chars, off, len));
    }

    protected int findWordEnd(int loc) {
        for (int i = loc, len = buffer.length(); i < len; i++) {
            if (buffer.charAt(i) == ' ') return i;
        }
        return buffer.length();
    }

    protected int findWordStart(int loc) {
        for (int i = loc - 1; i >= 0; i--) {
            if (buffer.charAt(i) == ' ') return i;
        }
        return 0;
    }

    private void printRestOfBuffer(int oldLen) {
        setCursorLocation();
        int len = buffer.length() - loc;
        char[] chars = getCharArray(len);
        buffer.getChars(loc, buffer.length(), chars, 0);
        Console.outputChars(chars, 0, len);
        if (oldLen <= buffer.length()) return;
        len = oldLen - buffer.length();
        chars = getCharArray(len);
        Arrays.fill(chars, ' ');
        Console.outputChars(chars, 0, len);
        setCursorLocation();
    }

    private char[] getCharArray(int len) {
        if (chars.length > len) return chars; else return chars = new char[2 * len];
    }

    protected void setCursorLocation() {
        Console.setCursorLocation(getCoord(loc));
    }

    protected Coord getCoord(int loc) {
        return new Coord(startCL.x + (loc % screenSize.getWidth()), startCL.y + (loc / screenSize.getWidth()));
    }

    public void reset() {
        loc = 0;
        buffer.setLength(0);
        resetIfRequired();
        setCursorLocation();
    }

    protected void resetIfRequired() {
        Coord cCL = Console.getCursorLocation();
        Coord locCL = getCoord(loc);
        if (cCL.equals(locCL)) return;
        Console.setCursorLocation(cCL);
        System.out.println();
        startCL = Console.getCursorLocation();
        replace(0, loc, buffer.toString());
        setCursorLocation();
    }
}
