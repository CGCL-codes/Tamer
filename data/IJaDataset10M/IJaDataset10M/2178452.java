package net.sf.javadc.mockups;

import java.util.List;
import net.sf.javadc.interfaces.IDownloadManager;
import net.sf.javadc.net.DownloadRequest;

public class BaseDownloadManager implements IDownloadManager {

    public void flushDownloadQueue() {
    }

    public List getDownloadQueue() {
        return null;
    }

    public void removeDownload(DownloadRequest dr) {
    }

    public void requestDownload(DownloadRequest dr) {
    }
}
