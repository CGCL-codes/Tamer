package org.apache.axis2.classloader;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @version $Rev: 704201 $ $Date: 2008-10-13 14:52:25 -0400 (Mon, 13 Oct 2008) $
 */
public abstract class AbstractResourceHandle implements ResourceHandle {

    public byte[] getBytes() throws IOException {
        InputStream in = getInputStream();
        try {
            byte[] bytes = IoUtil.getBytes(in);
            return bytes;
        } finally {
            IoUtil.close(in);
        }
    }

    public Manifest getManifest() throws IOException {
        return null;
    }

    public Certificate[] getCertificates() {
        return null;
    }

    public Attributes getAttributes() throws IOException {
        Manifest m = getManifest();
        if (m == null) {
            return null;
        }
        String entry = getUrl().getFile();
        return m.getAttributes(entry);
    }

    public void close() {
    }

    public String toString() {
        return "[" + getName() + ": " + getUrl() + "; code source: " + getCodeSourceUrl() + "]";
    }
}
