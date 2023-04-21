package es.eucm.eadventure.engine.resourcehandler.zipurl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Ca�izal, G., Del Blanco, A., Torrente, F.J. (alphabetical order) *
 * @author L�pez Ma�as, E., P�rez Padilla, F., Sollet, E., Torijano, B. (former
 *         developers by alphabetical order)
 * 
 */
public class ZipURL {

    public ZipURL() {
    }

    public static URL createAssetURL(String zipFile, String assetPath) throws MalformedURLException {
        URL url = null;
        File parentFile = new File(zipFile);
        File file = new File(parentFile, assetPath);
        url = file.toURI().toURL();
        url = new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile(), new ZipURLStreamHandler(zipFile, assetPath));
        return url;
    }

    public static URL createAssetURL(File file) throws MalformedURLException {
        URL url = null;
        url = file.toURI().toURL();
        url = new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile(), new ZipURLStreamHandler(file.getAbsolutePath()));
        return url;
    }
}
