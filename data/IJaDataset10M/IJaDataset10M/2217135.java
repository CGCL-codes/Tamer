package com.googlecode.httl.support.loaders;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import com.googlecode.httl.support.Loader;

/**
 * ClasspathResource. (SPI, Prototype, ThreadSafe)
 * 
 * @see com.googlecode.httl.support.loaders.ClasspathLoader#load(String, String)
 * 
 * @author Liang Fei (liangfei0201 AT gmail DOT com)
 */
public class ClasspathResource extends InputStreamResource {

    private static final long serialVersionUID = 2499229996487593996L;

    private final String path;

    public ClasspathResource(Loader loader, String name, String encoding, String path) {
        super(loader, name, encoding);
        this.path = (path.startsWith("/") ? path.substring(1) : path);
    }

    public long getLastModified() {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(path);
            if (url != null) {
                if ("file".equals(url.getProtocol())) {
                    String path = url.getFile();
                    if (path.startsWith("file:")) {
                        path = path.substring("file:".length());
                    }
                    File file = new File(path);
                    if (file.exists()) {
                        return file.lastModified();
                    }
                } else if ("jar".equals(url.getProtocol())) {
                    String path = url.getFile();
                    if (path.startsWith("jar:")) {
                        path = path.substring("jar:".length());
                    }
                    if (path.startsWith("file:")) {
                        path = path.substring("file:".length());
                    }
                    int i = path.indexOf("!/");
                    if (i > 0) {
                        path = path.substring(0, i);
                    }
                    File file = new File(path);
                    if (file.exists()) {
                        return file.lastModified();
                    }
                }
            }
        } catch (Throwable t) {
        }
        return -1;
    }

    public InputStream getInputStream() throws IOException {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}
