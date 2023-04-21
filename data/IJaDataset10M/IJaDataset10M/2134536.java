package net.omnivention.wulumuqi;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.PropertyResourceBundle;

public class URLResourceBundle extends PropertyResourceBundle {

    public URLResourceBundle(InputStream stream) throws IOException {
        super(stream);
    }

    public URLResourceBundle(String url) throws MalformedURLException, IOException {
        super(new URL(url).openStream());
    }
}
