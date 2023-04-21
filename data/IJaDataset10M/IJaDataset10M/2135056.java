package net.sourceforge.javautil.developer.enterprise.jboss;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import net.sourceforge.javautil.common.io.VirtualArtifactIO;
import net.sourceforge.javautil.common.io.VirtualDirectory;
import net.sourceforge.javautil.common.io.VirtualFile;
import net.sourceforge.javautil.common.io.VirtualFileAbstract;
import org.jboss.shrinkwrap.api.Asset;

/**
 * This will wrap an {@link Asset} as a {@link VirtualFile}.
 *
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class JBossFileAsset extends VirtualFileAbstract {

    protected final Asset original;

    public JBossFileAsset(String name, VirtualDirectory owner, Asset original) {
        super(name, owner);
        this.original = original;
    }

    @Override
    protected InputStream getRawInputStream() throws IOException {
        return original.getStream();
    }

    @Override
    protected OutputStream getRawOutputStream() throws IOException {
        throw new UnsupportedOperationException("Cannot get output stream for JBoss Assets");
    }

    public long getLastModified() {
        return owner.getLastModified();
    }

    public URL getURL() {
        return this.createURL();
    }

    public boolean isExists() {
        return true;
    }

    public long getSize() {
        return -1;
    }

    public boolean isReadOnly() {
        return true;
    }

    public boolean remove() {
        return owner.remove(getName());
    }

    public void rename(String newName) {
        this.name = newName;
    }
}
