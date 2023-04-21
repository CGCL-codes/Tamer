package cz.cuni.mff.ufal.volk.services;

import cz.cuni.mff.ufal.volk.Expression;
import cz.cuni.mff.ufal.volk.UnsupportedInputTypeException;

/**
 * <p>A simple language recognizer. It provides the method {@link #recognize(Expression)}.</p>
 * 
 * @author Bartłomiej Etenkowski
 *
 * @param <T> the input type
 */
public interface LanguageRecognizer<T extends Expression> {

    /**
   * Tries to recognize the language of the given item. If the recognition fails, the method
   * returns {@code null}. Otherwise, it returns the ID of the language which suits best
   * to the item.
   * 
   * @param item the item to be recognized
   * @return the id of the recognized language or {@code null} if the language of the item
   *         cannot be recognized
   * 
   * @throws UnsupportedInputTypeException if the particular input type is not supported
   */
    String recognize(T item);
}
