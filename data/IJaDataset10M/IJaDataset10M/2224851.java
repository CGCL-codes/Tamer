package net.sf.dynxform.container.cocoon.acting;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.File;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: alexanderk
 * Date: 19.05.2004
 * Time: 11:55:25
 * To change this template use File | Settings | File Templates.
 */
public final class ClearCacheAction extends org.apache.cocoon.acting.AbstractAction {

    /**
   * Logger
   */
    static final Log log = LogFactory.getLog(ClearCacheAction.class);

    /**
   * Cahce dir
   */
    private static final String PARAMETER_CHACHE_DIR = "cache-dir";

    public final Map act(final Redirector redirector, final SourceResolver sourceResolver, final Map objectModel, final String string, final Parameters parameters) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Call act() method on ClearCacheAction");
        }
        final String cacheDir = parameters.getParameter(PARAMETER_CHACHE_DIR, null);
        if (log.isDebugEnabled()) {
            log.debug("Cache dir - \"" + cacheDir + '\"');
        }
        final File cacheDirFile = new File(cacheDir);
        if (log.isDebugEnabled()) {
            log.debug("Cache dir exist - " + cacheDirFile.exists());
            log.debug("Cache dir is directory - " + cacheDirFile.isDirectory());
        }
        if (cacheDirFile.exists() && cacheDirFile.isDirectory()) {
            final File[] files = cacheDirFile.listFiles();
            if (log.isDebugEnabled()) {
                log.debug("Cache dir contains files count = " + files.length);
            }
            for (int i = 0; i < files.length; i++) {
                if (log.isDebugEnabled()) {
                    log.debug("Lang dirs - " + files[i].getName());
                }
                final File[] inFiles = files[i].listFiles();
                for (int j = 0; j < inFiles.length; j++) {
                    if (log.isDebugEnabled()) {
                        log.debug("Cached file - " + inFiles[j].getName());
                    }
                    inFiles[j].delete();
                }
                files[i].delete();
            }
            cacheDirFile.delete();
        }
        return null;
    }
}
