package net.sourceforge.retriever.fetcher;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import sun.net.www.protocol.file.FileURLConnection;

/**
 * This class is a representation of resources that live on local hard disks.
 */
public class FileFetcher extends Fetcher {

    /**
	 * TODO Write javadoc.
	 */
    public FileFetcher(final URL url) {
        super(url);
    }

    /**
	 * Returns all inner URLS, which in this case are the URLs of local folder resources.
	 * 
	 * @param charset Not used.
	 * @return All inner URLS, which in this case are the URLs of local folder resources.
	 */
    @Override
    public List<String> getInnerURLs(final String charset) {
        final List<String> resources = new ArrayList<String>();
        try {
            final File resourceAsFileObject = new File(this.getURLAuthority() + super.getURL().getPath());
            if (resourceAsFileObject.isDirectory()) {
                final File[] innerFiles = resourceAsFileObject.listFiles();
                for (File innerFile : innerFiles) {
                    resources.add("file:/" + innerFile.getAbsolutePath().replaceAll("\\\\", "/"));
                }
            }
        } catch (final Exception e) {
        }
        return resources;
    }

    private String getURLAuthority() {
        String authority = super.getURL().getAuthority();
        authority = authority == null ? "" : authority;
        return authority;
    }

    /**
	 * Returns true if the resource isn't a folder and false if it is.
	 * 
	 * @return True if the resource isn't a folder and false if it is.
	 */
    @Override
    public boolean canCrawl() {
        try {
            final File resource = new File(super.getURL().getPath());
            if (resource.exists() && resource.isFile()) {
                return true;
            } else {
                return false;
            }
        } catch (final Exception e) {
            return false;
        }
    }

    @Override
    protected void close(final URLConnection urlConnection) {
        if (urlConnection != null && urlConnection instanceof FileURLConnection) {
            ((FileURLConnection) urlConnection).close();
        }
    }

    @Override
    protected void treatFetchException(final URLConnection urlConnection, final Exception e) {
    }

    @Override
    protected void lastFetch(final URLConnection urlConnection) {
    }
}
