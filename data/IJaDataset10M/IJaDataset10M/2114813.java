package net.sourceforge.javautil.classloader.source;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.sourceforge.javautil.common.FileUtil;
import net.sourceforge.javautil.common.exception.ThrowableManagerRegistry;

/**
 * This is used by {@link ZipClassSource} to load another jar that is contained in it.
 * 
 * @author ponder
 * @author $Author: ponderator $
 * @version $Revision: 1070 $
 */
public class InternalZipClassSource extends ZipClassSource {

    /**
	 * @param is The input stream of the entry that is really an internal zip/jar
	 * @return A URL pointing to a temporary file that contains the unzipped entry.
	 */
    public static URL getURL(String name, InputStream is) {
        try {
            return FileUtil.write(is, FileUtil.createTemporaryFile("internal-zip-" + name + "-", ".jar", true)).toURI().toURL();
        } catch (MalformedURLException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }

    /**
	 * This will pass the URL to the decompressed zip entry by calling {@link #getURL()}.
	 */
    public InternalZipClassSource(String name, InputStream is) {
        super(getURL(name, is));
    }

    @Override
    public void cleanup() {
        super.cleanup();
        FileUtil.delete(this.file);
    }
}
