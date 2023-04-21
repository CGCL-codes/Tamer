package org.apache.commons.compress.archivers.zip;

/**
 * Constants from stat.h on Unix systems.
 */
public interface UnixStat {

    /**
     * Bits used for permissions (and sticky bit)
     */
    int PERM_MASK = 07777;

    /**
     * Indicates symbolic links.
     */
    int LINK_FLAG = 0120000;

    /**
     * Indicates plain files.
     */
    int FILE_FLAG = 0100000;

    /**
     * Indicates directories.
     */
    int DIR_FLAG = 040000;

    /**
     * Default permissions for symbolic links.
     */
    int DEFAULT_LINK_PERM = 0777;

    /**
     * Default permissions for directories.
     */
    int DEFAULT_DIR_PERM = 0755;

    /**
     * Default permissions for plain files.
     */
    int DEFAULT_FILE_PERM = 0644;
}
