package org.apache.commons.vfs.provider.irods;

import org.apache.commons.vfs.provider.FileNameParser;
import org.apache.commons.vfs.provider.HostFileNameParser;
import org.apache.commons.vfs.provider.URLFileNameParser;
import org.apache.commons.vfs.provider.VfsComponentContext;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileName;

/**
 * Created by IntelliJ IDEA.
 * User: pmak
 * Date: Dec 4, 2008
 * Time: 1:11:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class IRODSFileNameParser extends HostFileNameParser {

    private static final IRODSFileNameParser INSTANCE = new IRODSFileNameParser();

    public IRODSFileNameParser() {
        super(1247);
    }

    public static FileNameParser getInstance() {
        return INSTANCE;
    }
}
