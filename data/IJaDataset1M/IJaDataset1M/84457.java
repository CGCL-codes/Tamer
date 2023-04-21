package org.gamio.standalone;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Agemo Cui <agemocui@gamio.org>
 * @version $Rev: 19 $ $Date: 2008-09-26 19:00:58 -0400 (Fri, 26 Sep 2008) $
 */
public final class JarFileFilter implements FilenameFilter {

    private static JarFileFilter jarFileFilter = new JarFileFilter();

    private JarFileFilter() {
    }

    public static JarFileFilter getInstance() {
        return jarFileFilter;
    }

    public boolean accept(File dir, String name) {
        return name.endsWith(".jar");
    }
}
