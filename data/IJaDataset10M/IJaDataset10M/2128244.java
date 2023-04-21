package org.gudy.azureus2.pluginsimpl.local.sharing;

import java.io.File;
import java.util.Map;
import org.gudy.azureus2.plugins.sharing.*;

public class ShareResourceDirImpl extends ShareResourceFileOrDirImpl implements ShareResourceDir {

    protected static ShareResourceDirImpl getResource(ShareManagerImpl _manager, File _file) throws ShareException {
        ShareResourceImpl res = ShareResourceFileOrDirImpl.getResourceSupport(_manager, _file);
        if (res instanceof ShareResourceDirImpl) {
            return ((ShareResourceDirImpl) res);
        }
        return (null);
    }

    protected ShareResourceDirImpl(ShareManagerImpl _manager, ShareResourceDirContentsImpl _parent, File _file) throws ShareException {
        super(_manager, _parent, ST_DIR, _file);
    }

    protected ShareResourceDirImpl(ShareManagerImpl _manager, File _file, Map _map) throws ShareException {
        super(_manager, ST_DIR, _file, _map);
    }

    protected byte[] getFingerPrint() throws ShareException {
        return (getFingerPrint(getFile()));
    }

    public File getDir() {
        return (getFile());
    }
}
