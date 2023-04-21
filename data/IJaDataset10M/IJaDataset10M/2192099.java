package net.sf.xpontus.plugins.codecompletion.xml;

import net.sf.xpontus.utils.URIUtil;
import java.io.File;
import java.net.URL;

/**
 *
 * @author mrcheeks
 */
public class CachingUtils {

    private CachingUtils() {
    }

    public static String getCachedURL(String uri) throws Exception {
        URL url = null;
        if (new File(uri).exists()) {
            url = new File(uri).toURL();
        } else {
            url = new URL(uri);
        }
        int pos = url.getProtocol().length() + 3;
        String str = URIUtil.decodePath(url.toExternalForm());
        String texte = str.substring(pos, str.length());
        System.out.println("texte:" + texte);
        return texte;
    }
}
