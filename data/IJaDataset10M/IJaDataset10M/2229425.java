package javapoint.spellcheck;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Kyle
 */
public class SpellCheckerManager {

    private SpellDictionaryHashMap dictionary;

    private SpellChecker spellChecker;

    public SpellCheckerManager(final File dictionaryFile) {
        try {
            this.dictionary = new SpellDictionaryHashMap(dictionaryFile);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        this.spellChecker = new SpellChecker(this.dictionary);
    }

    public final List<?> getSuggestions(final String word, final int threshold) {
        try {
            return spellChecker.getSuggestions(word, threshold);
        } catch (final NegativeArraySizeException exception) {
            return Collections.emptyList();
        }
    }

    public final boolean isCorrect(final String word) {
        return this.spellChecker.isCorrect(word);
    }

    public final void addToDictionary(final String newWord) {
        this.spellChecker.addToDictionary(newWord);
        this.spellChecker.reset();
    }
}
