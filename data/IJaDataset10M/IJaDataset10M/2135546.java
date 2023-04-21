package com.nullfish.app.jfd2.viewer.jlayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;

public class UrlStreamFactory implements StreamFactory {

    private URL url;

    private String name;

    public UrlStreamFactory(URL url, String name) {
        this.url = url;
        this.name = name;
    }

    public InputStream getInputStream() throws VFSException {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new VFSIOException(e);
        }
    }

    public String getName() {
        return name;
    }
}
