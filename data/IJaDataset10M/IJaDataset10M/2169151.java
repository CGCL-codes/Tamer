package org.jcvi.util;

import java.io.File;
import java.io.FileFilter;

/**
 * @author dkatzel
 *
 *
 */
public class FileIteratorTestUtil {

    /**
     * A {@link FileFilter} that only accepts
     * Files whose names end with "2"
     */
    public static final FileFilter FILE_FILTER_ANYTHING_THAT_DOESNT_END_WITH_2 = new FileFilter() {

        @Override
        public boolean accept(File pathname) {
            return pathname.getName().endsWith("2");
        }
    };
}
