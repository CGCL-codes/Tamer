package architecture.ee.spring.resources.scanner;

import java.net.URI;
import java.net.URL;
import architecture.common.scanner.DirectoryListener;

public interface DirectoryScanner {

    public abstract void addScanURI(final URI uri);

    public abstract void addScanURL(final URL url);

    public abstract void addScanDir(final String path);

    public abstract void removeScanURL(final URL url);

    public abstract void removeScanURI(final URI uri);

    public abstract void addDirectoryListener(DirectoryListener fileListener);

    public abstract void removeDirectoryListener(DirectoryListener fileListener);

    public abstract DirectoryListener[] getDirectoryListeners();
}
