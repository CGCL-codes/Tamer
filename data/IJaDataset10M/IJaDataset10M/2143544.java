package com.jme.input.dummy;

import org.lwjgl.input.Keyboard;
import com.jme.input.KeyInput;

/**
 * <code>DummyKeyInput</code> simulates a Keyinput system, usable for Applications that can be headless or not.
 * @author Kai Börnert
 * @version $Id: DummyKeyInput.java 4310 2009-05-01 13:53:42Z blaine.dev $
 */
public class DummyKeyInput extends KeyInput {

    /**
     * Constructor instantiates a new <code>DummyKeyInput</code> object, it does nothing at all.
     */
    protected DummyKeyInput() {
    }

    /**
     * This always returns false
     * @see com.jme.input.KeyInput#isKeyDown(int)
     */
    public boolean isKeyDown(int key) {
        return false;
    }

    /**
     * <code>getKeyName</code> returns the string representation of the key
     * code.
     * @see com.jme.input.KeyInput#getKeyName(int)
     */
    public String getKeyName(int key) {
        return Keyboard.getKeyName(key);
    }

    /**
     * <code>getKeyIndex</code> returns the value of the key name
     * @param name the name of the key
     * @return the value of the key
     */
    public int getKeyIndex(String name) {
        return Keyboard.getKeyIndex(name);
    }

    /**
     * <code>update</code> The Dummy does nothing at all.
     * @see com.jme.input.KeyInput#update()
     */
    public void update() {
    }

    /**
     * <code>destroy</code> nothing to destroy.
     * @see com.jme.input.KeyInput#destroy()
     */
    public void destroy() {
    }

    @Override
    public void clear() {
    }

    @Override
    public void clearKey(int keycode) {
    }
}
