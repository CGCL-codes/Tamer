package org.akrogen.core.impl.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import org.akrogen.core.resources.IResourceLocator;

/**
 * Basic File resources locator implementation.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public class FileResourcesLocatorImpl implements IResourceLocator {

    public String resolve(String uri) {
        return uri;
    }

    public InputStream getInputStream(String uri) throws Exception {
        return new FileInputStream(new File(uri));
    }

    public Reader getReader(String uri) throws Exception {
        return new FileReader(new File(uri));
    }
}
