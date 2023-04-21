package console.ssh;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.io.OutputStream;
import org.gjt.sp.util.Log;
import com.jcraft.jcterm.Connection;
import com.jcraft.jcterm.Emulator;
import com.jcraft.jcterm.EmulatorVT100;
import com.jcraft.jcterm.Term;
import console.Console;
import console.ConsolePane;

/**
 * A wrapper around the ConsolePane that provides a Term interface for
 * com.jcraft.jcterm.EmulatorVT100. Based on the JCTermSwing.java.
 * 
 * @author ezust
 * 
 * @deprecated - not working yet - stay tuned.
 * 
 */
public class JCTerm implements Term, KeyListener {

    Console c;

    ConsolePane cp;

    OutputStream out;

    InputStream in;

    Emulator emulator = null;

    Connection connection = null;

    private Graphics2D graphics;

    private java.awt.Color defaultbground = Color.black;

    private java.awt.Color defaultfground = Color.white;

    private java.awt.Color bground = Color.black;

    private java.awt.Color fground = Color.white;

    private boolean bold = false;

    private boolean underline = false;

    private boolean reverse = false;

    private int term_width = 80;

    private int term_height = 24;

    private int descent = 0;

    private int x = 0;

    private int y = 0;

    private int char_width;

    private int char_height;

    private int line_space = -2;

    private int compression = 0;

    private final Object[] colors = { Color.black, Color.red, Color.green, Color.yellow, Color.blue, Color.magenta, Color.cyan, Color.white };

    public JCTerm(Console console, ConsolePane consolePane) {
        cp = consolePane;
        cp.addKeyListener(this);
        graphics = (Graphics2D) cp.getGraphics();
        setSize(getTermWidth(), getTermHeight());
        cp.setPreferredSize(new Dimension(getTermWidth(), getTermHeight()));
        cp.setSize(getTermWidth(), getTermHeight());
        cp.enableInputMethods(true);
        cp.setFocusTraversalKeysEnabled(false);
    }

    public void setSize(int w, int h) {
        cp.setSize(w, h);
        if (graphics != null) graphics.dispose();
        int column = w / getCharWidth();
        int row = h / getCharHeight();
        term_width = column;
        term_height = row;
        if (emulator != null) emulator.reset();
        clear_area(0, 0, w, h);
        if (connection != null) {
            connection.requestResize(this);
        }
    }

    public void start(Connection connection) {
        this.connection = connection;
        in = connection.getInputStream();
        out = connection.getOutputStream();
        emulator = new EmulatorVT100(this, in);
        emulator.reset();
        emulator.start();
        clear();
        redraw(0, 0, getTermWidth(), getTermHeight());
    }

    public void paint(Graphics g) {
        cp.paint(g);
    }

    public void processKeyEvent(KeyEvent e) {
        int id = e.getID();
        if (id == KeyEvent.KEY_PRESSED) {
            keyPressed(e);
        } else if (id == KeyEvent.KEY_RELEASED) {
        } else if (id == KeyEvent.KEY_TYPED) {
            keyTyped(e);
        }
        e.consume();
    }

    byte[] obuffer = new byte[3];

    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        byte[] code = null;
        switch(keycode) {
            case KeyEvent.VK_CONTROL:
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_ALT:
            case KeyEvent.VK_CAPS_LOCK:
                return;
            case KeyEvent.VK_ENTER:
                code = emulator.getCodeENTER();
                break;
            case KeyEvent.VK_UP:
                code = emulator.getCodeUP();
                break;
            case KeyEvent.VK_DOWN:
                code = emulator.getCodeDOWN();
                break;
            case KeyEvent.VK_RIGHT:
                code = emulator.getCodeRIGHT();
                break;
            case KeyEvent.VK_LEFT:
                code = emulator.getCodeLEFT();
                break;
            case KeyEvent.VK_F1:
                code = emulator.getCodeF1();
                break;
            case KeyEvent.VK_F2:
                code = emulator.getCodeF2();
                break;
            case KeyEvent.VK_F3:
                code = emulator.getCodeF3();
                break;
            case KeyEvent.VK_F4:
                code = emulator.getCodeF4();
                break;
            case KeyEvent.VK_F5:
                code = emulator.getCodeF5();
                break;
            case KeyEvent.VK_F6:
                code = emulator.getCodeF6();
                break;
            case KeyEvent.VK_F7:
                code = emulator.getCodeF7();
                break;
            case KeyEvent.VK_F8:
                code = emulator.getCodeF8();
                break;
            case KeyEvent.VK_F9:
                code = emulator.getCodeF9();
                break;
            case KeyEvent.VK_F10:
                code = emulator.getCodeF10();
                break;
            case KeyEvent.VK_TAB:
                code = emulator.getCodeTAB();
                break;
        }
        if (code != null) {
            try {
                out.write(code, 0, code.length);
                out.flush();
            } catch (Exception ee) {
            }
            return;
        }
        char keychar = e.getKeyChar();
        if ((keychar & 0xff00) == 0) {
            obuffer[0] = (byte) (e.getKeyChar());
            try {
                out.write(obuffer, 0, 1);
                out.flush();
            } catch (Exception ee) {
            }
        }
    }

    public void keyTyped(KeyEvent e) {
        char keychar = e.getKeyChar();
        if ((keychar & 0xff00) != 0) {
            char[] foo = new char[1];
            foo[0] = keychar;
            try {
                byte[] goo = new String(foo).getBytes("EUC-JP");
                out.write(goo, 0, goo.length);
                out.flush();
            } catch (Exception eee) {
            }
        }
    }

    public int getTermWidth() {
        return char_width * term_width;
    }

    public int getTermHeight() {
        return char_height * term_height;
    }

    public int getCharWidth() {
        return char_width;
    }

    public int getCharHeight() {
        return char_height;
    }

    public int getColumnCount() {
        return term_width;
    }

    public int getRowCount() {
        return term_height;
    }

    public void clear() {
        c.clear();
        graphics.setColor(getBackGround());
        graphics.fillRect(0, 0, char_width * term_width, char_height * term_height);
        graphics.setColor(getForeGround());
    }

    public void setCursor(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw_cursor() {
        cp.getGraphics().fillRect(x, y - char_height, char_width, char_height);
        cp.repaint(x, y - char_height, char_width, char_height);
    }

    public void redraw(int x, int y, int width, int height) {
        cp.repaint(x, y, width, height);
    }

    public void clear_area(int x1, int y1, int x2, int y2) {
        graphics.setColor(getBackGround());
        graphics.fillRect(x1, y1, x2 - x1, y2 - y1);
        graphics.setColor(getForeGround());
    }

    public void scroll_area(int x, int y, int w, int h, int dx, int dy) {
        graphics.copyArea(x, y, w, h, dx, dy);
        cp.repaint(x + dx, y + dy, w, h);
    }

    public void drawBytes(byte[] buf, int s, int len, int x, int y) {
        graphics.drawBytes(buf, s, len, x, y - descent);
        if (bold) graphics.drawBytes(buf, s, len, x + 1, y - descent);
        if (underline) {
            graphics.drawLine(x, y - 1, x + len * char_width, y - 1);
        }
    }

    public void drawString(String str, int x, int y) {
        graphics.drawString(str, x, y - descent);
        if (bold) graphics.drawString(str, x + 1, y - descent);
        if (underline) {
            graphics.drawLine(x, y - 1, x + str.length() * char_width, y - 1);
        }
    }

    public void beep() {
        Toolkit.getDefaultToolkit().beep();
    }

    /** Ignores key released events. */
    public void keyReleased(KeyEvent event) {
    }

    public void setLineSpace(int foo) {
        this.line_space = foo;
    }

    public void setCompression(int compression) {
        if (compression < 0 || 9 < compression) return;
        this.compression = compression;
    }

    private java.awt.Color toColor(Object o) {
        if (o instanceof String) {
            return java.awt.Color.getColor((String) o);
        }
        if (o instanceof java.awt.Color) {
            return (java.awt.Color) o;
        }
        return Color.white;
    }

    public void setDefaultForeGround(Object f) {
        defaultfground = toColor(f);
    }

    public void setDefaultBackGround(Object f) {
        defaultbground = toColor(f);
    }

    public void setForeGround(Object f) {
        fground = toColor(f);
        cp.setForeground(fground);
    }

    public void setBackGround(Object b) {
        bground = toColor(b);
        cp.setBackground(bground);
    }

    private java.awt.Color getForeGround() {
        if (reverse) return bground;
        return fground;
    }

    private java.awt.Color getBackGround() {
        if (reverse) return fground;
        return bground;
    }

    public Object getColor(int index) {
        if (colors == null || index < 0 || colors.length <= index) return null;
        return colors[index];
    }

    public void setBold() {
        bold = true;
    }

    public void setUnderline() {
        underline = true;
    }

    public void setReverse() {
        reverse = true;
        if (graphics != null) graphics.setColor(getForeGround());
    }

    public void resetAllAttributes() {
        bold = false;
        underline = false;
        reverse = false;
        bground = defaultbground;
        fground = defaultfground;
        if (graphics != null) graphics.setColor(getForeGround());
    }
}
