package org.intellij.apiComparator.util;

import com.intellij.CommonBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;
import java.util.ResourceBundle;

/**
 * I18N bundle.
 *
 * @author Alexey Efimov
 */
public final class APIComparatorBundle {

    @NonNls
    private static final String BUNDLE_NAME = "org.intellij.apiComparator.util.APIComparatorBundle";

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private APIComparatorBundle() {
    }

    public static String message(@PropertyKey(resourceBundle = BUNDLE_NAME) String key, Object... params) {
        return CommonBundle.message(BUNDLE, key, params);
    }
}
