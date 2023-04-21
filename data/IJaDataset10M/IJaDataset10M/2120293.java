package com.realtime.crossfire.jxclient.gui.keybindings;

import com.realtime.crossfire.jxclient.gui.commandlist.CommandList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link KeyBinding} that matches by key character.
 * @author Andreas Kirschbaum
 */
public class KeyCharKeyBinding extends KeyBinding {

    /**
     * The key character to match.
     */
    private final char keyChar;

    /**
     * Creates a {@link KeyBinding} that matches by key character.
     * @param keyChar the key character to match
     * @param commands the commands to associate with this binding
     * @param isDefault whether the key binding is a "default" binding which
     * should not be saved
     */
    public KeyCharKeyBinding(final char keyChar, @NotNull final CommandList commands, final boolean isDefault) {
        super(commands, isDefault);
        this.keyChar = keyChar;
    }

    /**
     * Returns the key character to match.
     * @return the key character to match
     */
    public char getKeyChar() {
        return keyChar;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == null || !(obj instanceof KeyCharKeyBinding)) {
            return false;
        }
        final KeyCharKeyBinding keyBinding = (KeyCharKeyBinding) obj;
        return keyBinding.getKeyChar() == keyChar;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return keyChar;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matchesKeyCode(final int keyCode, final int modifiers) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matchesKeyChar(final char keyChar) {
        return this.keyChar == keyChar;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingDescription() {
        if (keyChar == '\n') {
            return "return";
        }
        if (keyChar == '\t') {
            return "tab";
        }
        if (keyChar == ' ') {
            return "space";
        }
        return new String(new char[] { keyChar });
    }
}
