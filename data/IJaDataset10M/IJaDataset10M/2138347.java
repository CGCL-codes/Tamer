package org.fest.swing.keystroke;

import java.util.HashMap;
import java.util.Map;
import javax.swing.KeyStroke;
import static java.awt.event.InputEvent.SHIFT_MASK;
import static java.awt.event.KeyEvent.CHAR_UNDEFINED;

/**
 * Understands a collection of <code>{@link KeyStrokeMapping}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class KeyStrokeMap {

    private static final Map<Character, KeyStroke> CHAR_TO_KEY_STROKE = new HashMap<Character, KeyStroke>();

    private static final Map<KeyStroke, Character> KEY_STROKE_TO_CHAR = new HashMap<KeyStroke, Character>();

    static {
        initialize();
    }

    private static void initialize() {
        addKeyStrokesFrom(new KeyStrokeMappingProvider_en());
    }

    /**
   * Adds the collection of <code>{@link KeyStrokeMapping}</code>s from the given
   * <code>{@link KeyStrokeMappingProvider}</code> to this map.
   * @param provider the given <code>KeyStrokeMappingProvider</code>.
   */
    public static synchronized void addKeyStrokesFrom(KeyStrokeMappingProvider provider) {
        for (KeyStrokeMapping entry : provider.keyStrokeMappings()) add(entry.character(), entry.keyStroke());
    }

    private static void add(Character character, KeyStroke keyStroke) {
        CHAR_TO_KEY_STROKE.put(character, keyStroke);
        KEY_STROKE_TO_CHAR.put(keyStroke, character);
    }

    /**
   * Removes all the character-<code>{@link KeyStroke}</code> mappings.
   */
    public static synchronized void clearKeyStrokes() {
        CHAR_TO_KEY_STROKE.clear();
        KEY_STROKE_TO_CHAR.clear();
    }

    /**
   * Returns the <code>{@link KeyStroke}</code> corresponding to the given character, as best we can guess it, or
   * <code>null</code> if we don't know how to generate it.
   * @param character the given character.
   * @return the key code-based <code>KeyStroke</code> corresponding to the given character, or <code>null</code> if
   *         we cannot generate it.
   */
    public static KeyStroke keyStrokeFor(char character) {
        return CHAR_TO_KEY_STROKE.get(character);
    }

    /**
   * Given a <code>{@link KeyStroke}</code>, returns the equivalent character. Key strokes are defined properly for
   * US keyboards only. To contribute your own, please add them using the method
   * <code>{@link #addKeyStrokesFrom(KeyStrokeMappingProvider)}</code>.
   * @param keyStroke the given <code>KeyStroke</code>.
   * @return KeyEvent.VK_UNDEFINED if the result is unknown.
   */
    public static char charFor(KeyStroke keyStroke) {
        Character character = KEY_STROKE_TO_CHAR.get(keyStroke);
        if (character == null) {
            int mask = keyStroke.getModifiers() & ~SHIFT_MASK;
            character = KEY_STROKE_TO_CHAR.get(KeyStroke.getKeyStroke(keyStroke.getKeyCode(), mask));
            if (character == null) return CHAR_UNDEFINED;
        }
        return character.charValue();
    }

    private KeyStrokeMap() {
    }
}
