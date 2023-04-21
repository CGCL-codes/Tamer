package com.phloc.commons;

import java.util.Collection;
import java.util.Locale;
import javax.annotation.Nonnull;

/**
 * Base interface for objects having zero or more locales.
 * 
 * @author philip
 */
public interface IHasLocales {

    /**
   * @return The locales of this object. May not be <code>null</code>.
   */
    @Nonnull
    Collection<Locale> getAllLocales();
}
