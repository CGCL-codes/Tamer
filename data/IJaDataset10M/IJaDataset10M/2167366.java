package com.jmonkey.universal.open.jedit;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.KeyStroke;

public class DefaultInputHandler extends InputHandler {

    private Hashtable bindings;

    private Hashtable currentBindings;

    public DefaultInputHandler() {
        bindings = currentBindings = new Hashtable();
    }

    private DefaultInputHandler(DefaultInputHandler copy) {
        bindings = currentBindings = copy.bindings;
    }

    public void addDefaultKeyBindings() {
        addKeyBinding("BACK_SPACE", InputHandler.BACKSPACE);
        addKeyBinding("C+BACK_SPACE", InputHandler.BACKSPACE_WORD);
        addKeyBinding("DELETE", InputHandler.DELETE);
        addKeyBinding("C+DELETE", InputHandler.DELETE_WORD);
        addKeyBinding("ENTER", InputHandler.INSERT_BREAK);
        addKeyBinding("TAB", InputHandler.INSERT_TAB);
        addKeyBinding("INSERT", InputHandler.OVERWRITE);
        addKeyBinding("C+\\", InputHandler.TOGGLE_RECT);
        addKeyBinding("HOME", InputHandler.HOME);
        addKeyBinding("END", InputHandler.END);
        addKeyBinding("S+HOME", InputHandler.SELECT_HOME);
        addKeyBinding("S+END", InputHandler.SELECT_END);
        addKeyBinding("C+HOME", InputHandler.DOCUMENT_HOME);
        addKeyBinding("C+END", InputHandler.DOCUMENT_END);
        addKeyBinding("CS+HOME", InputHandler.SELECT_DOC_HOME);
        addKeyBinding("CS+END", InputHandler.SELECT_DOC_END);
        addKeyBinding("PAGE_UP", InputHandler.PREV_PAGE);
        addKeyBinding("PAGE_DOWN", InputHandler.NEXT_PAGE);
        addKeyBinding("S+PAGE_UP", InputHandler.SELECT_PREV_PAGE);
        addKeyBinding("S+PAGE_DOWN", InputHandler.SELECT_NEXT_PAGE);
        addKeyBinding("LEFT", InputHandler.PREV_CHAR);
        addKeyBinding("S+LEFT", InputHandler.SELECT_PREV_CHAR);
        addKeyBinding("C+LEFT", InputHandler.PREV_WORD);
        addKeyBinding("CS+LEFT", InputHandler.SELECT_PREV_WORD);
        addKeyBinding("RIGHT", InputHandler.NEXT_CHAR);
        addKeyBinding("S+RIGHT", InputHandler.SELECT_NEXT_CHAR);
        addKeyBinding("C+RIGHT", InputHandler.NEXT_WORD);
        addKeyBinding("CS+RIGHT", InputHandler.SELECT_NEXT_WORD);
        addKeyBinding("UP", InputHandler.PREV_LINE);
        addKeyBinding("S+UP", InputHandler.SELECT_PREV_LINE);
        addKeyBinding("DOWN", InputHandler.NEXT_LINE);
        addKeyBinding("S+DOWN", InputHandler.SELECT_NEXT_LINE);
        addKeyBinding("C+ENTER", InputHandler.REPEAT);
    }

    public void addKeyBinding(String keyBinding, ActionListener action) {
        Hashtable current = bindings;
        for (StringTokenizer st = new StringTokenizer(keyBinding); st.hasMoreTokens(); ) {
            KeyStroke keyStroke = parseKeyStroke(st.nextToken());
            if (keyStroke == null) return;
            if (st.hasMoreTokens()) {
                Object o = current.get(keyStroke);
                if (o instanceof Hashtable) {
                    current = (Hashtable) o;
                } else {
                    o = new Hashtable();
                    current.put(keyStroke, o);
                    current = (Hashtable) o;
                }
            } else {
                current.put(keyStroke, action);
            }
        }
    }

    public InputHandler copy() {
        return new DefaultInputHandler(this);
    }

    public void keyPressed(KeyEvent evt) {
        int keyCode = evt.getKeyCode();
        int modifiers = evt.getModifiers();
        if (keyCode == 17 || keyCode == 16 || keyCode == 18 || keyCode == 157) return;
        if ((modifiers & -2) != 0 || evt.isActionKey() || keyCode == 8 || keyCode == 127 || keyCode == 10 || keyCode == 9 || keyCode == 27) {
            if (super.grabAction != null) {
                handleGrabAction(evt);
                return;
            }
            KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
            Object o = currentBindings.get(keyStroke);
            if (o == null) {
                if (currentBindings != bindings) {
                    Toolkit.getDefaultToolkit().beep();
                    super.repeatCount = 0;
                    super.repeat = false;
                    evt.consume();
                }
                currentBindings = bindings;
                return;
            }
            if (o instanceof ActionListener) {
                currentBindings = bindings;
                executeAction((ActionListener) o, evt.getSource(), null);
                evt.consume();
                return;
            }
            if (o instanceof Hashtable) {
                currentBindings = (Hashtable) o;
                evt.consume();
                return;
            }
        }
    }

    public void keyTyped(KeyEvent evt) {
        int modifiers = evt.getModifiers();
        char c = evt.getKeyChar();
        if (c != '￿' && (modifiers & 8) == 0 && c >= ' ' && c != '\177') {
            KeyStroke keyStroke = KeyStroke.getKeyStroke(Character.toUpperCase(c));
            Object o = currentBindings.get(keyStroke);
            if (o instanceof Hashtable) {
                currentBindings = (Hashtable) o;
                return;
            }
            if (o instanceof ActionListener) {
                currentBindings = bindings;
                executeAction((ActionListener) o, evt.getSource(), String.valueOf(c));
                return;
            }
            currentBindings = bindings;
            if (super.grabAction != null) {
                handleGrabAction(evt);
                return;
            }
            if (super.repeat && Character.isDigit(c)) {
                super.repeatCount *= 10;
                super.repeatCount += c - 48;
                return;
            }
            executeAction(InputHandler.INSERT_CHAR, evt.getSource(), String.valueOf(evt.getKeyChar()));
            super.repeatCount = 0;
            super.repeat = false;
        }
    }

    public static KeyStroke parseKeyStroke(String keyStroke) {
        if (keyStroke == null) return null;
        int modifiers = 0;
        int index = keyStroke.indexOf('+');
        if (index != -1) {
            for (int i = 0; i < index; i++) switch(Character.toUpperCase(keyStroke.charAt(i))) {
                case 65:
                    modifiers |= 8;
                    break;
                case 67:
                    modifiers |= 2;
                    break;
                case 77:
                    modifiers |= 4;
                    break;
                case 83:
                    modifiers |= 1;
                    break;
            }
        }
        String key = keyStroke.substring(index + 1);
        int ch;
        if (key.length() == 1) {
            ch = Character.toUpperCase(key.charAt(0));
            if (modifiers == 0) {
                return KeyStroke.getKeyStroke((char) ch);
            }
            return KeyStroke.getKeyStroke(ch, modifiers);
        }
        if (key.length() == 0) {
            System.err.println("Invalid key stroke: " + keyStroke);
            return null;
        }
        try {
            ch = (java.awt.event.KeyEvent.class).getField("VK_".concat(key)).getInt(null);
        } catch (Exception _ex) {
            System.err.println("Invalid key stroke: " + keyStroke);
            return null;
        }
        return KeyStroke.getKeyStroke(ch, modifiers);
    }

    public void removeAllKeyBindings() {
        bindings.clear();
    }

    public void removeKeyBinding(String keyBinding) {
        throw new InternalError("Not yet implemented");
    }
}
