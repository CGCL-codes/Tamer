package com.phloc.commons.format.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.phloc.commons.format.IFormatter;

/**
 * A formatter that adds a prefix to a string.
 * 
 * @author philip
 */
public class StringPrefixFormatter extends StringPrefixAndSuffixFormatter {

    public StringPrefixFormatter(@Nonnull final String sPrefix) {
        super(sPrefix, "");
    }

    public StringPrefixFormatter(@Nonnull final String sPrefix, @Nullable final IFormatter aNestedFormatter) {
        super(sPrefix, "", aNestedFormatter);
    }
}
