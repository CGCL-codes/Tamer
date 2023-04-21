package net.sourceforge.freejava.vfs.impl.url;

import net.sourceforge.freejava.vfs.IVolume;
import net.sourceforge.freejava.vfs.path.AbstractPath;

public class URLPath extends AbstractPath {

    private static final long serialVersionUID = 1L;

    public URLPath(String url) {
        super(url);
    }

    public String getURL() {
        return localPath;
    }

    @Override
    public IVolume getVolume() {
        return URLVolume.getInstance();
    }
}
