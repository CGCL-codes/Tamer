package com.msli.graphic.locale;

import com.msli.core.util.JavaUtils;
import com.msli.core.util.Singleton;

/**
 * The singleton immutable Locator representing the "universal locale". The
 * universal locator is the default locator used or assumed if no locale is
 * specified.
 * <P>
 * Derived from gumbo.graphic.locale.UniversalLocator.
 * @author jonb
 */
public final class UniversalLocator implements Locator, Singleton.Normal {

    @Override
    public boolean equalsValue(Object obj, double tolerance) {
        return obj == this;
    }

    public boolean equals(Object obj) {
        return obj == this;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return JavaUtils.toShortString(this);
    }

    private UniversalLocator() {
    }

    public static final UniversalLocator getInstance() {
        return _instance;
    }

    private static UniversalLocator _instance = new UniversalLocator();
}
