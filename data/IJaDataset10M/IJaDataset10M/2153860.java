package com.mapbased.sfw.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class WfwEntityResolver implements EntityResolver {

    public static final String BASE_LOCATION = "mapbased-entity-cache";

    public static final EntityResolver INSTANCE = new WfwEntityResolver();

    private WfwEntityResolver() {
    }

    /**
	 * Allow the application to resolve external entities.
	 * 
	 * @param publicId
	 *            The public identifier of the external entity being referenced,
	 *            or null if none was supplied.
	 * @param systemId
	 *            The system identifier of the external entity being referenced.
	 * @return An InputSource object describing the new input source, or null to
	 *         request that the parser open a regular URI connection to the
	 *         system identifier.
	 * @throws SAXException
	 *             Any SAX exception, possibly wrapping another exception.
	 * @throws IOException
	 *             A Java-specific IO exception, possibly the result of creating
	 *             a new InputStream or Reader for the InputSource.
	 * @todo Implement this org.xml.sax.EntityResolver method
	 */
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (!systemId.startsWith("http")) {
            return null;
        }
        java.net.URL u = new java.net.URL(systemId);
        File localfile = new File(BASE_LOCATION, u.getHost() + "/" + u.getPath());
        if (localfile.exists()) {
            InputSource is = new InputSource(new java.io.BufferedInputStream(new java.io.FileInputStream(localfile)));
            is.setPublicId(publicId);
            is.setSystemId(systemId);
            return is;
        } else {
            return new InputSource(new java.io.ByteArrayInputStream(this.download(u, localfile)));
        }
    }

    private synchronized byte[] download(java.net.URL u, File localfile) throws IOException {
        localfile.getParentFile().mkdirs();
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream(1024);
        this.readUrl(u, bos);
        byte[] bts = bos.toByteArray();
        OutputStream os = null;
        try {
            os = new java.io.BufferedOutputStream(new java.io.FileOutputStream(localfile, false));
            os.write(bts);
        } finally {
            if (os != null) {
                os.close();
            }
        }
        return bts;
    }

    private void readUrl(java.net.URL u, java.io.ByteArrayOutputStream bos) throws IOException {
        java.io.InputStream is = null;
        try {
            is = u.openStream();
            int i = is.read();
            while (i >= 0) {
                bos.write(i);
                i = is.read();
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
