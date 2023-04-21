package org.job.impl;

import org.job.JobObjectArrayHolder;

/**
 * An object array holder class for internal use only.
 * @author Michael Watzek
 */
class JobObjectArray extends JobObjectArrayHolder {

    /**
     * Delegates to {@link JobObjectArrayHolder#JobObjectArrayHolder()}.
     */
    JobObjectArray() {
    }

    /**
     * Delegates to {@link JobObjectArrayHolder#JobObjectArrayHolder(Object[])}.
     * @param array
     */
    JobObjectArray(Object[] array) {
        super(array);
    }
}
