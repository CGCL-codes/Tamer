package gnu.java.net.loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.security.cert.Certificate;

/**
 * A <code>Resource</code> represents a resource in some
 * <code>URLLoader</code>. It also contains all information (e.g.,
 * <code>URL</code>, <code>CodeSource</code>, <code>Manifest</code> and
 * <code>InputStream</code>) that is necessary for loading resources
 * and creating classes from a <code>URL</code>.
 */
public abstract class Resource {

    final URLLoader loader;

    public Resource(URLLoader loader) {
        this.loader = loader;
    }

    /**
   * Returns the non-null <code>CodeSource</code> associated with
   * this resource.
   */
    public CodeSource getCodeSource() {
        Certificate[] certs = getCertificates();
        if (certs == null) return loader.noCertCodeSource; else return new CodeSource(loader.baseURL, certs);
    }

    /**
   * Returns <code>Certificates</code> associated with this
   * resource, or null when there are none.
   */
    public Certificate[] getCertificates() {
        return null;
    }

    /**
   * Return the URLLoader for this resource.
   */
    public final URLLoader getLoader() {
        return loader;
    }

    /**
   * Return a <code>URL</code> that can be used to access this resource.
   */
    public abstract URL getURL();

    /**
   * Returns the size of this <code>Resource</code> in bytes or
   * <code>-1</code> when unknown.
   */
    public abstract int getLength();

    /**
   * Returns the non-null <code>InputStream</code> through which
   * this resource can be loaded.
   */
    public abstract InputStream getInputStream() throws IOException;
}
