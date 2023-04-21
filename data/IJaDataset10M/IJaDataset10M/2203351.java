package net.sourceforge.javautil.developer.enterprise.jboss;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import net.sourceforge.javautil.common.exception.ThrowableManagerRegistry;
import net.sourceforge.javautil.common.io.VirtualDirectory;
import net.sourceforge.javautil.common.io.VirtualFile;
import net.sourceforge.javautil.common.io.VirtualFileAbstract;
import net.sourceforge.javautil.common.io.VirtualFileWrapped;
import net.sourceforge.javautil.common.io.VirtualMemoryFileAbstract;
import net.sourceforge.javautil.common.io.impl.SimplePath;
import org.jboss.shrinkwrap.api.Asset;

/**
 * This is an {@link Asset} backed by a {@link VirtualFile}.
 *
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class JBossAssetFile extends VirtualFileWrapped implements Asset {

    public JBossAssetFile(VirtualFile original) {
        super(original);
    }

    public InputStream getStream() {
        try {
            return this.getInputStream();
        } catch (IOException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }
}
