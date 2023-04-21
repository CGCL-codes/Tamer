package textures;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ResourceRetriever {

    public static URL getResource(final String filename) throws IOException {
        URL url = ClassLoader.getSystemResource(filename);
        if (url == null) {
            return new URL("file", "localhost", filename);
        } else {
            return url;
        }
    }

    public static InputStream getResourceAsStream(final String filename) throws IOException {
        InputStream stream = ClassLoader.getSystemResourceAsStream(filename);
        if (stream == null) {
            return new FileInputStream(filename);
        } else {
            return stream;
        }
    }
}
