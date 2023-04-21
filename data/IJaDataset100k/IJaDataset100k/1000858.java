package org.intellij.spellChecker.engine;

/**
 * Spell checker factory.
 *
 * @author Alexey Efimov
 */
public final class SpellCheckerFactory {

    private SpellCheckerFactory() {
    }

    public static SpellChecker create() {
        return new JazzySpellChecker();
    }
}
