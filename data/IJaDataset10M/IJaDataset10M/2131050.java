package org.crazydays.junit;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * TestHelper
 * <p>
 * Collection of methods to assist in testing.
 * </p>
 */
public class TestHelper {

    /** class */
    protected Class<?> clazz;

    /** sandbox */
    protected File sandbox;

    /**
     * TestHelper constructor.
     * 
     * @param clazz Test class
     */
    public TestHelper(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * Cleanup sandbox.
     */
    public void tearDown() {
        if (sandbox != null) {
            delete(sandbox);
        }
    }

    /**
     * Delete file or directory.
     * 
     * @param file File
     */
    protected void delete(File file) {
        assertNotNull("file == null", file);
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            for (File child : children) {
                if (child.getName().equals(".") || child.getName().equals("..")) {
                    continue;
                }
                delete(child);
            }
        }
        assertTrue("Unable to delete file: " + file, file.delete());
    }

    /**
     * Get sandbox.
     * 
     * @return Sandbox
     */
    public File getSandbox() {
        if (sandbox == null) {
            sandbox = new File(new File(".", "tmp"), clazz.getName());
            assertTrue("sandbox.mkdirs", sandbox.mkdirs());
        }
        return sandbox;
    }

    /**
     * Copy absolute resource to destination.
     * 
     * @param destination Destination
     * @param resourcePath Absolute resource path
     * @return File
     */
    public File copy(File destination, String resourcePath) {
        assertNotNull("destination == null", destination);
        assertNotNull("resourcePath == null", resourcePath);
        if (destination.isDirectory()) {
            destination = new File(destination, filename(resourcePath));
        }
        assertEquals("destination exists " + destination, false, destination.exists());
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = getResourceAsStream(resourcePath);
            assertNotNull("Unable to load resource " + resourcePath, inputStream);
            outputStream = new FileOutputStream(destination);
            copy(inputStream, outputStream);
            return destination;
        } catch (IOException e) {
            fail(e.toString());
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    fail("Unable to close resourcePath " + resourcePath);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    fail("Unable to close destination " + destination);
                }
            }
        }
    }

    /**
     * Copy inputStream to outputStream.
     * 
     * @param inputStream InputStream
     * @param outputStream OutputStream
     * @return Count
     * @throws IOException
     */
    protected int copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        assertNotNull("inputStream == null", inputStream);
        assertNotNull("outputStream == null", outputStream);
        int total = 0;
        int read = 0;
        byte[] bytes = new byte[8196];
        while ((read = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, read);
            total += read;
        }
        return total;
    }

    /**
     * Determine the filename from the resource path.
     * 
     * @param resourcePath Resource path
     * @return Filename
     */
    protected String filename(String resourcePath) {
        assertNotNull("resourcePath == null", resourcePath);
        int last = resourcePath.lastIndexOf('/');
        if (last >= 0) {
            return resourcePath.substring(last);
        } else {
            return resourcePath;
        }
    }

    /**
     * Get resource as stream.
     * 
     * @param absoluteResourcePath Absolute resource path
     * @return InputStream
     */
    public InputStream getResourceAsStream(String resourcePath) {
        assertNotNull("resourcePath == null", resourcePath);
        InputStream stream = clazz.getResourceAsStream(resourcePath);
        assertNotNull("stream == null", stream);
        return stream;
    }
}
