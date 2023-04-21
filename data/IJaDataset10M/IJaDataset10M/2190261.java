package org.apache.sanselan.formats.jpeg.xmp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceFile;
import org.apache.sanselan.formats.jpeg.JpegImageParser;
import org.apache.sanselan.util.Debug;

public class JpegXmpRewriteTest extends JpegXmpBaseTest {

    public void testRemoveInsertUpdate() throws IOException, ImageReadException, ImageWriteException {
        List images = getImagesWithXmpData();
        for (int i = 0; i < images.size(); i++) {
            if (i % 10 == 0) Debug.purgeMemory();
            File imageFile = (File) images.get(i);
            Debug.debug("imageFile", imageFile);
            ByteSource byteSource = new ByteSourceFile(imageFile);
            Map params = new HashMap();
            String xmpXml = new JpegImageParser().getXmpXml(byteSource, params);
            assertNotNull(xmpXml);
            File noXmpFile = createTempFile(imageFile.getName() + ".", ".jpg");
            {
                OutputStream os = null;
                try {
                    os = new FileOutputStream(noXmpFile);
                    os = new BufferedOutputStream(os);
                    new JpegXmpRewriter().removeXmpXml(byteSource, os);
                } finally {
                    if (os != null) {
                        os.close();
                    }
                    os = null;
                }
                String outXmp = new JpegImageParser().getXmpXml(new ByteSourceFile(noXmpFile), params);
                assertNull(outXmp);
            }
            {
                String newXmpXml = "test";
                File updated = createTempFile(imageFile.getName() + ".", ".jpg");
                OutputStream os = null;
                try {
                    os = new FileOutputStream(updated);
                    os = new BufferedOutputStream(os);
                    new JpegXmpRewriter().updateXmpXml(byteSource, os, newXmpXml);
                } finally {
                    if (os != null) {
                        os.close();
                    }
                    os = null;
                }
                String outXmp = new JpegImageParser().getXmpXml(new ByteSourceFile(updated), params);
                assertNotNull(outXmp);
                assertEquals(outXmp, newXmpXml);
            }
            {
                String newXmpXml = "test";
                File updated = createTempFile(imageFile.getName() + ".", ".jpg");
                OutputStream os = null;
                try {
                    os = new FileOutputStream(updated);
                    os = new BufferedOutputStream(os);
                    new JpegXmpRewriter().updateXmpXml(new ByteSourceFile(noXmpFile), os, newXmpXml);
                } finally {
                    if (os != null) {
                        os.close();
                    }
                    os = null;
                }
                String outXmp = new JpegImageParser().getXmpXml(new ByteSourceFile(updated), params);
                assertNotNull(outXmp);
                assertEquals(outXmp, newXmpXml);
            }
        }
    }
}
