package photospace.graphics;

import java.io.*;
import org.apache.commons.io.*;
import org.apache.commons.logging.*;
import junit.framework.*;
import photospace.vfs.FileSystem;
import photospace.vfs.*;

public class ImageCacheTest extends TestCase {

    private static final Log log = LogFactory.getLog(ImageCacheTest.class);

    File cacheRoot;

    File fileRoot;

    public void setUp() throws Exception {
        fileRoot = new File(System.getProperty("project.root"), "build/test/");
        cacheRoot = new File(System.getProperty("java.io.tmpdir"), "ImageCacheTest");
        cacheRoot.mkdirs();
    }

    public void tearDown() throws Exception {
        FileUtils.forceDelete(cacheRoot);
    }

    public void testSync() throws Exception {
        FileSystem filesystem = new FileSystemImpl();
        filesystem.setRoot(fileRoot);
        ImageCache cache = new ImageCache();
        cache.setRoot(cacheRoot);
        cache.setFilesystem(filesystem);
        File photo1 = filesystem.getAsset("/exif-nordf.jpg");
        File photo2 = filesystem.getAsset("/exif-rdf.jpg");
        int count = cache.sync(new File[] { photo1, photo2 });
        assertEquals(2, count);
        count = cache.sync(new File[] { photo1, photo2 });
        assertEquals(0, count);
    }
}
