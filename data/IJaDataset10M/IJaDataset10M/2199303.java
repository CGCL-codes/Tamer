package org.sventon.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.Validate;
import org.sventon.model.DirEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * File extension filter.
 * Filters out given extension and removes entries with non-matching extension.
 *
 * @author jesper@sventon.org
 */
public final class FileExtensionFilter {

    /**
   * The file extension to filter.
   */
    private final String filterExtension;

    /**
   * Constructor.
   *
   * @param filterExtension File extension to filter (i.e. keep).
   */
    public FileExtensionFilter(final String filterExtension) {
        Validate.notEmpty(filterExtension, "File extension was null or empty");
        this.filterExtension = filterExtension;
    }

    /**
   * Filter a given list of entries.
   *
   * @param entries List of entries to filter.
   * @return List of entries matching given extension.
   */
    public List<DirEntry> filter(final List<DirEntry> entries) {
        final List<DirEntry> dir = Collections.checkedList(new ArrayList<DirEntry>(), DirEntry.class);
        for (final DirEntry entry : entries) {
            final String fileExtension = FilenameUtils.getExtension(entry.getName()).toLowerCase();
            if (filterExtension.equals(fileExtension)) {
                dir.add(entry);
            }
        }
        return dir;
    }
}
