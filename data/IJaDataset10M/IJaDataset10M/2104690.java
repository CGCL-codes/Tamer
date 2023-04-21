package proguard.util;

import java.util.*;

/**
 * This StringMatcher tests whether strings end in a given extension.
 *
 * @author Eric Lafortune
 */
public class ExtensionMatcher implements StringMatcher {

    private String extension;

    /**
     * Creates a new StringMatcher.
     * @param extension the extension against which strings will be matched.
     */
    public ExtensionMatcher(String extension) {
        this.extension = extension;
    }

    public boolean matches(String string) {
        return string.endsWith(extension);
    }
}
