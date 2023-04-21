package net.sf.ngrease.core.metalanguage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import net.sf.ngrease.core.ast.NgreaseException;

public class UrlDefaultImpl implements Url {

    private final String urlString;

    private final boolean isRelative;

    public UrlDefaultImpl(String urlString, boolean isRelative) {
        this.urlString = urlString;
        this.isRelative = isRelative;
    }

    public String getUrlString() {
        return urlString;
    }

    public InputStream getInputStream() {
        if (isRelative()) {
            throw new NgreaseException("Cannot read a relative url!");
        }
        URL javaUrl = toJavaUrl();
        try {
            InputStream stream = javaUrl.openStream();
            return stream;
        } catch (IOException e) {
            throw new NgreaseException(e);
        }
    }

    public boolean isRelative() {
        return isRelative;
    }

    public Url append(Url url) {
        if (isRelative()) {
            throw new NgreaseException("Cannot append to a relative url!");
        }
        String base = getUrlString();
        String relative = url.getUrlString();
        return new UrlDefaultImpl(base + "/" + relative, false);
    }

    public String toString() {
        return urlString;
    }

    public boolean isLocalFile() {
        URL url = toJavaUrl();
        return "file".equals(url.getProtocol());
    }

    private URL toJavaUrl() {
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            throw new NgreaseException(e);
        }
    }

    public String getPath() {
        URL url = toJavaUrl();
        return url.getPath();
    }
}
