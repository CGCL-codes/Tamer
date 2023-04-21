package jasci.terminal;

import java.lang.Integer;
import java.io.*;
import java.util.HashMap;
import jasci.util.Color;

public class VT100 implements Terminal {

    Reader in;

    Writer out;

    boolean bright;

    boolean processGfx;

    boolean gfxMode;

    int state;

    int fcolor, bcolor;

    int x, y;

    int nbuf;

    private static final char ESC = '';

    private static final HashMap<Character, Character> gfxMap = new HashMap<Character, Character>(40);

    static {
        gfxMap.put('─', 'q');
        gfxMap.put('━', 'q');
        gfxMap.put('│', 'x');
        gfxMap.put('┃', 'x');
        gfxMap.put('┼', 'n');
        gfxMap.put('╋', 'n');
        gfxMap.put('┌', 'l');
        gfxMap.put('┏', 'l');
        gfxMap.put('┐', 'k');
        gfxMap.put('┓', 'k');
        gfxMap.put('└', 'm');
        gfxMap.put('┗', 'm');
        gfxMap.put('┘', 'j');
        gfxMap.put('┛', 'j');
        gfxMap.put('┴', 'v');
        gfxMap.put('┻', 'v');
        gfxMap.put('┬', 'w');
        gfxMap.put('┳', 'w');
        gfxMap.put('├', 't');
        gfxMap.put('┣', 't');
        gfxMap.put('┤', 'u');
        gfxMap.put('┫', 'u');
    }

    public VT100(Reader in, Writer out, boolean processGfx) {
        this.in = in;
        this.out = out;
        this.state = 0;
        this.bright = false;
        this.processGfx = processGfx;
        this.gfxMode = false;
        this.fcolor = -1;
        this.bcolor = -1;
        this.x = 0;
        this.y = 0;
    }

    public void setColor(Color c) throws IOException {
        int fcolor = c.getForegroundColor() + (c.isBright() ? 8 : 0);
        int bcolor = c.getBackgroundColor() + (c.isBright() ? 8 : 0);
        if (fcolor != this.fcolor && bcolor != this.bcolor) color(fcolor, bcolor); else if (fcolor != this.fcolor) foregroundColor(fcolor); else if (bcolor != this.bcolor) backgroundColor(bcolor); else return;
        this.fcolor = fcolor;
        this.bcolor = bcolor;
    }

    public void setPosition(int x, int y) throws IOException {
        if (x == this.x && y == this.y) return;
        cursorSet(x, y);
        this.x = x;
        this.y = y;
    }

    public void put(char c) throws IOException {
        Character g = processGfx ? VT100.gfxMap.get(c) : null;
        if (g == null) {
            asciiMode();
            out.write(c);
        } else {
            gfxMode();
            out.write(g);
        }
        this.x++;
    }

    public void flush() throws IOException {
        out.flush();
    }

    public void processInput(TerminalInputHandler handler) throws IOException {
        char c;
        while (in.ready()) {
            c = (char) in.read();
            if (state == 0) {
                switch(c) {
                    case 8:
                    case 127:
                        handler.onBackspace();
                        break;
                    case '\t':
                        handler.onTab();
                        break;
                    case 0x1b:
                        state = 1;
                        break;
                    case 0x0d:
                        handler.onEnter();
                        break;
                    case 0x00:
                        handler.onControlChar(' ');
                        break;
                    case 0x01:
                        handler.onControlChar('A');
                        break;
                    case 0x02:
                        handler.onControlChar('B');
                        break;
                    case 0x03:
                        handler.onControlChar('C');
                        break;
                    case 0x04:
                        handler.onControlChar('D');
                        break;
                    case 0x05:
                        handler.onControlChar('E');
                        break;
                    case 0x06:
                        handler.onControlChar('F');
                        break;
                    case 0x07:
                        handler.onControlChar('G');
                        break;
                    case 0x0a:
                        break;
                    case 0x0b:
                        handler.onControlChar('K');
                        break;
                    case 0x0c:
                        handler.onControlChar('L');
                        break;
                    case 0x0e:
                        handler.onControlChar('N');
                        break;
                    case 0x0f:
                        handler.onControlChar('O');
                        break;
                    case 0x10:
                        handler.onControlChar('P');
                        break;
                    case 0x11:
                        handler.onControlChar('Q');
                        break;
                    case 0x12:
                        handler.onControlChar('R');
                        break;
                    case 0x13:
                        handler.onControlChar('S');
                        break;
                    case 0x14:
                        handler.onControlChar('T');
                        break;
                    case 0x15:
                        handler.onControlChar('U');
                        break;
                    case 0x16:
                        handler.onControlChar('V');
                        break;
                    case 0x17:
                        handler.onControlChar('W');
                        break;
                    case 0x18:
                        handler.onControlChar('X');
                        break;
                    case 0x19:
                        handler.onControlChar('Y');
                        break;
                    case 0x1a:
                        handler.onControlChar('Z');
                        break;
                    case 0x1c:
                        handler.onControlChar('\\');
                        break;
                    case 0x1d:
                        handler.onControlChar(']');
                        break;
                    case 0x1e:
                        handler.onControlChar('~');
                        break;
                    case 0x1f:
                        handler.onControlChar('?');
                        break;
                    default:
                        if (Character.isIdentifierIgnorable(c) == false && c != 0x0a) handler.onChar(c);
                }
            } else if (state == 1) {
                nbuf = 0;
                if ((char) c == '[' || c == 'O') state = 2; else {
                    if ((char) c == 'A') handler.onUpArrow(); else if ((char) c == 'B') handler.onDownArrow(); else if ((char) c == 'C') handler.onRightArrow(); else if ((char) c == 'D') handler.onLeftArrow();
                    state = 0;
                }
            } else if (state == 2) {
                switch(c) {
                    case 'A':
                        handler.onUpArrow();
                        state = 0;
                        break;
                    case 'B':
                        handler.onDownArrow();
                        state = 0;
                        break;
                    case 'C':
                        handler.onRightArrow();
                        state = 0;
                        break;
                    case 'D':
                        handler.onLeftArrow();
                        state = 0;
                        break;
                    case '0':
                        nbuf = nbuf * 10;
                        break;
                    case '1':
                        nbuf = nbuf * 10 + 1;
                        break;
                    case '2':
                        nbuf = nbuf * 10 + 2;
                        break;
                    case '3':
                        nbuf = nbuf * 10 + 3;
                        break;
                    case '4':
                        nbuf = nbuf * 10 + 4;
                        break;
                    case '5':
                        nbuf = nbuf * 10 + 5;
                        break;
                    case '6':
                        nbuf = nbuf * 10 + 6;
                        break;
                    case '7':
                        nbuf = nbuf * 10 + 7;
                        break;
                    case '8':
                        nbuf = nbuf * 10 + 8;
                        break;
                    case '9':
                        nbuf = nbuf * 10 + 9;
                        break;
                    case '~':
                        handler.onPFKey(nbuf - 10);
                        state = 0;
                        break;
                    default:
                        state = 0;
                        break;
                }
            }
        }
    }

    void cursorSet(int x, int y) throws IOException {
        out.write(ESC);
        out.write('[');
        out.write(Integer.toString(y));
        out.write(';');
        out.write(Integer.toString(x));
        out.write('H');
    }

    void cursorUp() throws IOException {
        out.write(ESC);
        out.write('[');
        out.write('1');
        out.write('A');
    }

    void cursorUp(int count) throws IOException {
        out.write(ESC);
        out.write('[');
        out.write(Integer.toString(count));
        out.write('A');
    }

    void cursorDown() throws IOException {
        out.write(ESC);
        out.write('[');
        out.write('1');
        out.write('B');
    }

    void cursorDown(int count) throws IOException {
        out.write(ESC);
        out.write('[');
        out.write(Integer.toString(count));
        out.write('B');
    }

    void cursorRight() throws IOException {
        out.write(ESC);
        out.write('[');
        out.write('1');
        out.write('C');
    }

    void cursorRight(int count) throws IOException {
        out.write(ESC);
        out.write('[');
        out.write(Integer.toString(count));
        out.write('C');
    }

    void cursorLeft() throws IOException {
        out.write(ESC);
        out.write('[');
        out.write('1');
        out.write('D');
    }

    void cursorLeft(int count) throws IOException {
        out.write(ESC);
        out.write('[');
        out.write(Integer.toString(count));
        out.write('D');
    }

    void gfxMode() throws IOException {
        if (gfxMode) return;
        out.write(ESC);
        out.write('(');
        out.write('0');
        gfxMode = true;
    }

    void asciiMode() throws IOException {
        if (!gfxMode) return;
        out.write(ESC);
        out.write('(');
        out.write('B');
        gfxMode = false;
    }

    public void foregroundColor(int color) throws IOException {
        int code = color + 30;
        boolean bright = false;
        if (code > 37) {
            code = code - 8;
            bright = true;
        }
        out.write(ESC);
        out.write('[');
        if (bright && !this.bright) {
            out.write('1');
            out.write(';');
            this.bright = true;
        } else if (!bright && this.bright) {
            out.write('0');
            out.write(';');
        }
        out.write(Integer.toString(code));
        out.write('m');
    }

    public void backgroundColor(int color) throws IOException {
        int code = color + 40;
        boolean bright = false;
        if (code > 47) {
            code = code - 8;
            bright = true;
        }
        out.write(ESC);
        out.write('[');
        if (bright && !this.bright) {
            out.write('1');
            out.write(';');
            this.bright = true;
        } else if (!bright && this.bright) {
            out.write('0');
            out.write(';');
        }
        out.write(Integer.toString(code));
        out.write('m');
    }

    public void color(int fcolor, int bcolor) throws IOException {
        int fcode = fcolor + 30;
        int bcode = bcolor + 40;
        boolean bright = false;
        if (fcode > 37) {
            fcode = fcode - 8;
            bright = true;
        }
        if (bcode > 47) {
            bcode = bcode - 8;
            bright = true;
        }
        out.write(ESC);
        out.write('[');
        if (bright && !this.bright) {
            out.write('1');
            out.write(';');
            this.bright = true;
        } else if (!bright && this.bright) {
            out.write('0');
            out.write(';');
        }
        out.write(Integer.toString(fcode));
        out.write(';');
        out.write(Integer.toString(bcode));
        out.write('m');
    }

    public void clear() throws IOException {
        out.write('\f');
    }
}
