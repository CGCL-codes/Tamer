package com.loribel.commons.util.impl;

import java.io.File;
import com.loribel.commons.abstraction.GB_FileOwnerSet;

/**
 * Simple implementation of {@link GB_FileOwnerSet}.
 *
 * @author Gregory Borelli
 */
public class GB_FileOwnerImpl implements GB_FileOwnerSet {

    private File file;

    public GB_FileOwnerImpl() {
        super();
    }

    public GB_FileOwnerImpl(File a_file) {
        super();
        file = a_file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File a_file) {
        file = a_file;
    }
}
