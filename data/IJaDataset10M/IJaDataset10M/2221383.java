package de.byteholder.geoclipse.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ResponseCache;
import java.net.URI;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class LocalResponseCache extends ResponseCache {

    public static final boolean IS_CACHE_DISABLED = true;

    static {
        if (IS_CACHE_DISABLED) {
            System.out.println("[AERITH] Cache disabled");
        }
    }

    public static final File CHACHE_DIR = new File(System.getProperty("user.home") + File.separator + ".aerith");

    static {
        if (!CHACHE_DIR.exists()) {
            CHACHE_DIR.mkdir();
        }
    }

    /**
	 * Private constructor to prevent instantiation.
	 */
    private LocalResponseCache() {
    }

    public static void installResponseCache() {
        if (!IS_CACHE_DISABLED) {
            ResponseCache.setDefault(new LocalResponseCache());
        }
    }

    /**
	 * Returns the local File corresponding to the given remote URI.
	 */
    public static File getLocalFile(final URI remoteUri) {
        int code = remoteUri.hashCode();
        final String fileName = Integer.toString(code >= 0 ? code : -code);
        return new File(CHACHE_DIR, fileName);
    }

    /**
	 * Returns true if the resource at the given remote URI is newer than the resource cached
	 * locally.
	 */
    private static boolean isUpdateAvailable(final URI remoteUri, final File localFile) {
        URLConnection conn;
        try {
            conn = remoteUri.toURL().openConnection();
        } catch (final MalformedURLException ex) {
            ex.printStackTrace();
            return false;
        } catch (final IOException ex) {
            ex.printStackTrace();
            return false;
        }
        if (!(conn instanceof HttpURLConnection)) {
            return false;
        }
        final long localLastMod = localFile.lastModified();
        long remoteLastMod = 0L;
        final HttpURLConnection httpconn = (HttpURLConnection) conn;
        httpconn.setUseCaches(false);
        try {
            httpconn.connect();
            remoteLastMod = httpconn.getLastModified();
        } catch (final IOException ex) {
            return false;
        } finally {
            httpconn.disconnect();
        }
        return (remoteLastMod > localLastMod);
    }

    @Override
    public CacheResponse get(final URI uri, final String rqstMethod, final Map<String, List<String>> rqstHeaders) throws IOException {
        final File localFile = getLocalFile(uri);
        if (!localFile.exists()) {
            return null;
        }
        if (isUpdateAvailable(uri, localFile)) {
            return null;
        }
        if (!localFile.exists()) {
            return null;
        }
        return new LocalCacheResponse(localFile, rqstHeaders);
    }

    @Override
    public CacheRequest put(final URI uri, final URLConnection conn) throws IOException {
        if (!(conn instanceof HttpURLConnection) || !(((HttpURLConnection) conn).getRequestMethod().equals("GET"))) {
            return null;
        }
        final File localFile = getLocalFile(uri);
        return new LocalCacheRequest(localFile);
    }

    private class LocalCacheResponse extends CacheResponse {

        private FileInputStream fis;

        private final Map<String, List<String>> headers;

        private LocalCacheResponse(final File localFile, final Map<String, List<String>> rqstHeaders) {
            try {
                fis = new FileInputStream(localFile);
            } catch (final FileNotFoundException ex) {
                ex.printStackTrace();
            }
            headers = rqstHeaders;
        }

        @Override
        public Map<String, List<String>> getHeaders() throws IOException {
            return headers;
        }

        @Override
        public InputStream getBody() throws IOException {
            return fis;
        }
    }

    private class LocalCacheRequest extends CacheRequest {

        private final File localFile;

        private FileOutputStream fos;

        private LocalCacheRequest(final File localFile) {
            this.localFile = localFile;
            try {
                fos = new FileOutputStream(localFile);
            } catch (final FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public OutputStream getBody() throws IOException {
            return fos;
        }

        @Override
        public void abort() {
            try {
                fos.close();
                localFile.delete();
            } catch (final IOException e) {
            }
        }
    }
}
