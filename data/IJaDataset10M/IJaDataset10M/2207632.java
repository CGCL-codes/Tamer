package org.dbcooper.decorator;

/**
 * Generic interface for replacing a given value with a new one.
 */
public interface ValueReplacer {

    /**
   * Replace the given value with a new value.
   * @param value The original value.
   * @return The new value.
   */
    Object replaceValue(Object value);
}
