package com.dukesoftware.utils.nativeapp;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class NativeUtils {

    public static final void launchBrowser() throws IOException, URISyntaxException {
        Desktop d = Desktop.getDesktop();
        d.browse(new URI("http://www.google.com"));
    }
}
