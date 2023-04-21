package org.spbgu.pmpu.athynia.worker.util;

import org.apache.log4j.Logger;
import java.io.File;
import java.net.URI;

/**
 * User: A.Selivanov
 * Date: 28.05.2007
 */
public class FileUtil {

    private static final Logger LOG = Logger.getLogger(FileUtil.class);

    public static void deleteFile(URI uri) {
        File file = new File(uri);
        if (file.exists()) {
            if (!file.delete()) {
                LOG.warn("WARNING:  Cannot delete file: " + file.getAbsolutePath());
            }
        }
    }
}
