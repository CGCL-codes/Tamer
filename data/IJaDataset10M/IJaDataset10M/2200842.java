package mathgame.game;

import java.util.*;
import java.awt.event.*;

/**
 * This class will act as the central relay of all input events (mostly key events)
 * and will distribute the events to the proper receiver.
 * The fundamental idea of the class is that there should be no way for two listeners
 * to receive the same key event, thus eliminating possible bugs where one listener
 * is supposed to take priority over another.
 */
public class InputDeviceHub implements KeyListener {

    private Map<Integer, KeyHandler> persistentHandlers;

    private Map<Integer, LinkedList<KeyHandler>> keyMap;

    private LinkedList<KeyHandler> uniReceiverStack;

    /**
     * Creates a new InputDeviceHub and initializes it for operation.
     */
    public InputDeviceHub() {
        persistentHandlers = new HashMap<Integer, KeyHandler>();
        keyMap = new HashMap<Integer, LinkedList<KeyHandler>>();
        uniReceiverStack = new LinkedList<KeyHandler>();
    }

    /** @see java.awt.event.KeyListener.keyPressed */
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        KeyHandler persistentHandler = persistentHandlers.get(keyCode);
        if (persistentHandler != null) persistentHandler.keyDown(e); else {
            LinkedList<KeyHandler> stack = keyMap.get(keyCode);
            if (stack != null) {
                try {
                    stack.getFirst().keyDown(e);
                } catch (NoSuchElementException nsee) {
                }
            } else if (!uniReceiverStack.isEmpty()) {
                try {
                    uniReceiverStack.getFirst().keyDown(e);
                } catch (NoSuchElementException nsee) {
                }
            }
        }
    }

    /** @see java.awt.event.KeyListener.keyReleased */
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        KeyHandler persistentHandler = persistentHandlers.get(keyCode);
        if (persistentHandler != null) persistentHandler.keyUp(e); else {
            LinkedList<KeyHandler> stack = keyMap.get(keyCode);
            if (stack != null) {
                try {
                    stack.getFirst().keyUp(e);
                } catch (NoSuchElementException nsee) {
                }
            } else if (!uniReceiverStack.isEmpty()) {
                try {
                    uniReceiverStack.getFirst().keyUp(e);
                } catch (NoSuchElementException nsee) {
                }
            }
        }
    }

    /** @see java.awt.event.KeyListener.keyTyped */
    public void keyTyped(KeyEvent e) {
        int keyCode = e.getKeyCode();
        KeyHandler persistentHandler = persistentHandlers.get(keyCode);
        if (persistentHandler != null) persistentHandler.keyTyped(e); else {
            LinkedList<KeyHandler> stack = keyMap.get(keyCode);
            if (stack != null) {
                try {
                    stack.getFirst().keyTyped(e);
                } catch (NoSuchElementException nsee) {
                }
            } else if (!uniReceiverStack.isEmpty()) {
                try {
                    uniReceiverStack.getFirst().keyTyped(e);
                } catch (NoSuchElementException nsee) {
                }
            }
        }
    }

    /**
     * Makes the InputDeviceHub map the key with key code <code>keyCode</code>
     * to the specified KeyHandler.
     */
    public synchronized void stealKey(int keyCode, KeyHandler kh) {
        LinkedList<KeyHandler> stack = keyMap.get(keyCode);
        if (stack == null) {
            stack = new LinkedList<KeyHandler>();
            keyMap.put(keyCode, stack);
        }
        stack.addFirst(kh);
    }

    /**
     */
    public synchronized void stealKeyPersistent(int keyCode, KeyHandler kh) {
        persistentHandlers.put(keyCode, kh);
    }

    /**
     * Removes the current mapping of key code <code>keyCode</code> and
     * restores the previous mapping.
     */
    public synchronized void returnKey(int keyCode) {
        LinkedList<KeyHandler> stack = keyMap.get(keyCode);
        if (stack != null && !stack.isEmpty()) {
            try {
                stack.removeFirst();
            } catch (NoSuchElementException nsee) {
            }
        }
    }

    public synchronized void returnPersistentKey(int keyCode) {
        persistentHandlers.remove(keyCode);
    }

    /**
     * Maps all keys to the specified KeyHandler.
     */
    public synchronized void stealAllKeys(KeyHandler kh) {
        Collection<LinkedList<KeyHandler>> allStacks = keyMap.values();
        for (LinkedList<KeyHandler> llkh : allStacks) llkh.addFirst(kh);
        uniReceiverStack.addFirst(kh);
    }

    /**
     * Removes <code>kh</code> from the top of all key stacks. If
     * there is a binding to kh which is below the top of the stack,
     * it is not considered.
     */
    public synchronized void returnAllKeys(KeyHandler kh) {
        Collection<LinkedList<KeyHandler>> allStacks = keyMap.values();
        for (LinkedList<KeyHandler> llkh : allStacks) {
            if (!llkh.isEmpty() && llkh.getFirst() == kh) llkh.removeFirst();
        }
        if (!uniReceiverStack.isEmpty() && uniReceiverStack.getFirst() == kh) uniReceiverStack.removeFirst();
    }
}
