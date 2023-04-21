package com.google.template.soy.shared;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * An interface for a one-to-one string mapping function used to rename CSS selectors.
 * CSS renaming can be used for minimization, obfuscation, normalization, etc.
 *
 */
@ParametersAreNonnullByDefault
public interface SoyCssRenamingMap {

    /**
   * Gets the string that should be substituted for {@code key}. The same
   * value must be consistently returned for any particular {@code key}, and
   * the returned value must not be returned for any other {@code key} value.
   *
   * @param key The text to be replaced, never null.
   * @return The value to substitute for {@code key}.
   */
    String get(String key);

    /** A renaming map that maps every name to itself. */
    public static final SoyCssRenamingMap IDENTITY = new SoyCssRenamingMap() {

        @Override
        public String get(String key) {
            return key;
        }
    };
}
