package com.alphacsp.common.io;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Yoav Hakman
 */
public class DefaultResource implements Resource {

    private Resource resource;

    public DefaultResource(String location) {
        if (location == null) {
            throw new NullPointerException("the given location can not be null!");
        }
        if (location.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)) {
            this.resource = new ClassPathResource(location.substring(ResourceUtils.CLASSPATH_URL_PREFIX.length()));
        } else if (location.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
            this.resource = new FileSystemResource(location.substring(ResourceUtils.FILE_URL_PREFIX.length()));
        } else {
            this.resource = new FileSystemResource(location);
        }
    }

    public boolean exists() {
        return resource.exists();
    }

    public boolean isOpen() {
        return resource.isOpen();
    }

    public URL getURL() throws IOException {
        return resource.getURL();
    }

    public File getFile() throws IOException {
        return resource.getFile();
    }

    public Resource createRelative(String relativePath) throws IOException {
        return resource.createRelative(relativePath);
    }

    public String getFilename() {
        return resource.getFilename();
    }

    public String getDescription() {
        return resource.getDescription();
    }

    public InputStream getInputStream() throws IOException {
        return resource.getInputStream();
    }
}
