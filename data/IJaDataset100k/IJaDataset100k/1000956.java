package com.nullfish.app.jfd2.comparator;

import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;

/**
 * @author shunji
 *
 */
public class SizeComparator implements FileComparator {

    private int order;

    public SizeComparator(boolean ascend) {
        order = ascend ? 1 : -1;
    }

    public int compare(VFile file1, VFile file2) {
        try {
            long size1 = file1.getAttribute().getLength();
            long size2 = file2.getAttribute().getLength();
            if (size1 == size2) {
                return 0;
            }
            return (size2 > size1 ? -1 : 1) * order;
        } catch (VFSException e) {
            return 0;
        }
    }
}
