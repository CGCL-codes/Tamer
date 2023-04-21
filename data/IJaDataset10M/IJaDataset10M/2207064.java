package org.apache.commons.vfs.impl;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.AbstractFileName;

/**
 * A simple Filename to hold the scheme for to be created virtual filesytsem.
 *
 * @author imario@apache.org
 * @version $Revision: 480428 $ $Date: 2006-11-28 22:15:24 -0800 (Tue, 28 Nov 2006) $
 */
public class VirtualFileName extends AbstractFileName {

    public VirtualFileName(final String scheme, final String absPath, final FileType type) {
        super(scheme, absPath, type);
    }

    public FileName createName(String absPath, FileType type) {
        return new VirtualFileName(getScheme(), absPath, type);
    }

    protected void appendRootUri(StringBuffer buffer, boolean addPassword) {
        buffer.append(getScheme());
    }
}
