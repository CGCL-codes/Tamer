package org.zm;

import java.io.File;

public interface Sizer extends SystemComponent {

    /**
     * Calculate free space at target location
     * 
     * @param target
     * @return free space in KB
     */
    public long freeSpace(File target);

    /**
     * Calculate size of target directory/file
     * 
     * @param target
     * @return file size in KB
     */
    public long usedSpace(File target);
}
