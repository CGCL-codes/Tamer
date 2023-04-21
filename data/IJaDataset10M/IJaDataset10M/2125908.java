package com.loribel.commons.module.states;

import java.io.File;

/**
 * Tools.
 *
 * @author Gregory Borelli
 */
public final class GB_StateMapFileTools {

    public static int getState(File a_file) {
        if (a_file == null) {
            return -1;
        }
        if (!a_file.exists()) {
            return GB_StateMapFile.STATE_NOT_EXIST;
        }
        if (a_file.isFile()) {
            if (a_file.canWrite()) {
                return GB_StateMapFile.STATE_WRITE;
            } else {
                return GB_StateMapFile.STATE_READ_ONLY;
            }
        } else {
            if (a_file.list().length == 0) {
                return GB_StateMapFile.STATE_DIR_EMPTY;
            } else {
                return GB_StateMapFile.STATE_DIR;
            }
        }
    }
}
