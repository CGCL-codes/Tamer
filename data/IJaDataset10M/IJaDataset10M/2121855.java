package net.sf.dozer.util.mapping.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import net.sf.dozer.util.mapping.AbstractDozerTest;

/**
 * @author tierney.matt
 */
public class ResourceLoaderTest extends AbstractDozerTest {

    private ResourceLoader loader = new ResourceLoader();

    public void testResourceNotFound() throws Exception {
        assertNull("file URL should not have been found", loader.getResource(String.valueOf(System.currentTimeMillis())));
    }

    public void testGetResourceWithWhitespace() {
        URL url = loader.getResource(" contextMapping.xml ");
        assertNotNull("URL should not be null", url);
    }

    public void testGetResourceWithNewlines() {
        URL url = loader.getResource("\ncontextMapping.xml\n");
        assertNotNull("URL should not be null", url);
    }

    public void testGetResource_FileOutsideOfClasspath() throws Exception {
        File temp = File.createTempFile("dozerfiletest", ".txt");
        temp.deleteOnExit();
        String resourceName = "file:" + temp.getAbsolutePath();
        URL url = loader.getResource(resourceName);
        assertNotNull("URL should not be null", url);
        InputStream is = url.openStream();
        assertNotNull("input stream should not be null", is);
    }

    public void testGetResource_FileOutsideOfClasspath_NotFound() throws Exception {
        URL url = loader.getResource("file:" + System.currentTimeMillis());
        assertNotNull("URL should not be null", url);
        try {
            url.openStream();
            fail("should have thrown a file not found exception");
        } catch (IOException e) {
        }
    }

    public void testGetResource_FileOutsideOfClasspath_InvalidFormat() throws Exception {
        URL url = loader.getResource(String.valueOf(System.currentTimeMillis()));
        assertNull("URL should be null", url);
    }
}
