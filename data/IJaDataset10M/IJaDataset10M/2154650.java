package org.donnchadh.gaelbot.urlprocessors;

import java.io.IOException;
import java.net.URL;

public class CompositeUrlProcessor implements UrlProcessor {

    private final UrlProcessor[] urlProcessors;

    public CompositeUrlProcessor(UrlProcessor... urlProcessors) {
        this.urlProcessors = urlProcessors;
    }

    public void processUrl(URL url) throws IOException {
        for (UrlProcessor processor : urlProcessors) {
            processor.processUrl(url);
        }
    }
}
