package nl.multimedian.eculture.annocultor.xconverter.api;

import java.net.URI;

public class Resource {

    URI uri;

    public Resource(URI uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return uri.toString();
    }
}
