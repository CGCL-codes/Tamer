package org.fest.swing.keystroke;

import javax.swing.KeyStroke;

/**
 * Understands a mapping between a character and a <code>{@link KeyStroke}</code>.
 *
 * @author Yvonne Wang
 */
public class KeyStrokeMapping {

    private final char character;

    private final KeyStroke keyStroke;

    /**
   * Creates a new </code>{@link KeyStrokeMapping}</code>.
   * @param character the character corresponding to the intended <code>KeyStroke</code>.
   * @param keyCode the numeric key code for the intended <code>KeyStroke</code>.
   * @param modifiers the set of modifiers for the intended <code>KeyStroke</code>.
   * @return the created <code>KeyStrokeMapping</code>.
   */
    public static KeyStrokeMapping mapping(char character, int keyCode, int modifiers) {
        return new KeyStrokeMapping(character, keyCode, modifiers);
    }

    /**
   * Creates a new </code>{@link KeyStrokeMapping}</code>.
   * @param character the character corresponding to the intended <code>KeyStroke</code>.
   * @param keyCode the numeric key code for the intended <code>KeyStroke</code>.
   * @param modifiers the set of modifiers for the intended <code>KeyStroke</code>.
   */
    public KeyStrokeMapping(char character, int keyCode, int modifiers) {
        this(character, KeyStroke.getKeyStroke(keyCode, modifiers));
    }

    /**
   * Creates a new </code>{@link KeyStrokeMapping}</code>.
   * @param character the character corresponding to the given <code>KeyStroke</code>.
   * @param keyStroke the <code>KeyStroke</code> corresponding to the given character.
   */
    public KeyStrokeMapping(char character, KeyStroke keyStroke) {
        this.character = character;
        this.keyStroke = keyStroke;
    }

    /**
   * Returns the character corresponding to this mapping's <code>{@link #keyStroke()}</code>.
   * @return the character corresponding to this mapping's <code>KeyStroke</code>.
   */
    public char character() {
        return character;
    }

    /**
   * Returns the <code>{@link KeyStroke}</code> corresponding to this mapping's <code>{@link #character()}</code>.
   * @return the <code>KeyStroke</code> corresponding to this mapping's character.
   */
    public KeyStroke keyStroke() {
        return keyStroke;
    }

    /**
   * Returns the <code>String</code> representation of this class.
   * @return the <code>String</code> representation of this class.
   */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(getClass().getSimpleName()).append("[");
        b.append("character='").append(character).append("',");
        b.append("keyStroke=").append(keyStroke).append("]");
        return b.toString();
    }
}
