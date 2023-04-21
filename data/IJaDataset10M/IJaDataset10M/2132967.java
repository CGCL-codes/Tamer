package it.freax.fpm.core.download;

import java.net.URL;

public class MockAbstractDownload extends AbstractDownload {

    public MockAbstractDownload(URL url, String path) {
        super(url, path);
    }

    @Override
    public void run() {
        return;
    }
}
