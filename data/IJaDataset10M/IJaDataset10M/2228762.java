package net.sf.maple.resources.protocols;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import net.sf.maple.resources.Fragment;
import net.sf.maple.resources.local.Util;

public class HttpProtocol extends AbstractProtocol {

    private static final String HTTP = "http";

    public String getProtocol() {
        return HTTP;
    }

    public URL toUrl(Fragment path) throws Exception {
        String s = "";
        if (path.words().size() > 1) s = "/" + path.dropFirst();
        String host = path.getFirst().toString();
        URL result = new URL(HTTP + "://" + host + s);
        return result;
    }

    public InputStream openInput(Fragment path) throws IOException {
        int len = path.words().size();
        String p = Util.combine("/", path.words().subList(1, len));
        URL url = new URL("http", path.words().get(0), p);
        InputStream result = url.openStream();
        return result;
    }

    public OutputStream openOutput(Fragment path) throws FileNotFoundException {
        throw new UnsupportedOperationException();
    }

    public Fragment[] list(Fragment path) {
        throw new UnsupportedOperationException();
    }
}
