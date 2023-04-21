package org.axsl.hyphenR;

/**
 * Specialized information about a specific hyphenation break opportunity in a
 * word.
 * This handles "hard" hyphenation cases, such as those where the word changes
 * spelling when it is hyphenated.
 * Most hyphenation break opportunities do not require a HyphenBreak.
 */
public interface HyphenBreak {

    /**
     * Assuming that a hyphen is placed at this point, provides the text
     * immediately before that hyphen that should be removed.
     * Note that this deletion should occur chronologically before the text
     * returned by {@link #preInsert()} is inserted.
     * @return The text that should be deleted before the hyphen, or null if
     * none should be deleted.
     */
    String preDelete();

    /**
     * Assuming that a hyphen is placed at this point, provides the text that
     * should be inserted immediately before that hyphen.
     * Note that this insertion should occur chronologically after the text
     * returned by {@link #preDelete()} is deleted.
     * @return The text that should be inserted before the hyphen, or null if
     * none should be inserted.
     */
    String preInsert();

    /**
     * Assuming that a hyphen is placed at this point, provides the text
     * immediately after that hyphen that should be removed.
     * Note that this deletion should occur chronologically before the text
     * returned by {@link #postInsert()} is inserted.
     * @return The text that should be deleted after the hyphen, or null if
     * none should be deleted.
     */
    String postDelete();

    /**
     * Assuming that a hyphen is placed at this point, provides the text that
     * should be inserted immediately after that hyphen.
     * Note that this insertion should occur chronologically after the text
     * returned by {@link #postDelete()} is deleted.
     * @return The text that should be inserted after the hyphen, or null if
     * none should be inserted.
     */
    String postInsert();
}
