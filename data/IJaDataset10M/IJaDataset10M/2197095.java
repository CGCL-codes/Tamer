package com.phloc.commons.utils;

import java.io.PrintStream;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import com.phloc.commons.CGlobal;
import com.phloc.commons.SystemProperties;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.regex.RegExHelper;

/**
 * Utility class for dealing with the Java class path.
 * 
 * @author philip
 */
@Immutable
public final class ClassPathHelper {

    @PresentForCodeCoverage
    @SuppressWarnings("unused")
    private static final ClassPathHelper s_aInstance = new ClassPathHelper();

    private ClassPathHelper() {
    }

    /**
   * @return A non-<code>null</code> list of all directories and files currently
   *         in the class path.
   */
    @Nonnull
    public static List<String> getAllClassPathEntries() {
        return RegExHelper.splitToList(SystemProperties.getJavaClassPath(), SystemProperties.getPathSeparator());
    }

    /**
   * Print all class path entries on the passed print stream, using the system
   * line separator
   * 
   * @param aPS
   *        The print stream to print to. May not be <code>null</code>.
   */
    public static void printClassPathEntries(@Nonnull final PrintStream aPS) {
        printClassPathEntries(aPS, CGlobal.LINE_SEPARATOR);
    }

    /**
   * Print all class path entries on the passed print stream, using the passed
   * separator
   * 
   * @param aPS
   *        The print stream to print to. May not be <code>null</code>.
   * @param sItemSeparator
   *        The separator to be printed between each item.
   */
    public static void printClassPathEntries(@Nonnull final PrintStream aPS, @Nonnull final String sItemSeparator) {
        for (final String sClassPathEntry : getAllClassPathEntries()) {
            aPS.print(sClassPathEntry);
            aPS.print(sItemSeparator);
        }
    }
}
