package org.columba.api.desktop;

import java.io.File;
import java.net.URL;

public interface IDesktop {

    public String getMimeType(File file);

    public String getMimeType(String ext);

    public boolean supportsOpen();

    public boolean open(File file);

    public boolean openAndWait(File file);

    public boolean supportsBrowse();

    public void browse(URL url);
}
