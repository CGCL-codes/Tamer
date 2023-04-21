package org.apache.commons.vfs.provider;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileType;

/**
 * A file name for layered files.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision: 480428 $ $Date: 2006-11-28 22:15:24 -0800 (Tue, 28 Nov 2006) $
 */
public class LayeredFileName extends AbstractFileName {

    private final FileName outerUri;

    public LayeredFileName(final String scheme, final FileName outerUri, final String path, final FileType type) {
        super(scheme, path, type);
        this.outerUri = outerUri;
    }

    /**
     * Returns the URI of the outer file.
     */
    public FileName getOuterName() {
        return outerUri;
    }

    public FileName createName(String path, FileType type) {
        return new LayeredFileName(getScheme(), getOuterName(), path, type);
    }

    protected void appendRootUri(StringBuffer buffer, boolean addPassword) {
        buffer.append(getScheme());
        buffer.append(":");
        buffer.append(getOuterName().getURI());
        buffer.append("!");
    }
}
