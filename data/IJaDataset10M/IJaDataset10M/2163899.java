package marcin.downloader.model.http;

import marcin.downloader.model.utils.Message;
import marcin.downloader.model.utils.Link;
import marcin.downloader.model.exceptions.DownloaderInterruptException;
import marcin.downloader.model.interfaces.IHttpDownloader;
import marcin.downloader.model.interfaces.IHttpData;

/**
 * @version 1.0
 * @created 02-Sep-2009 00:08:02
 */
public class HttpDownloader implements IHttpDownloader {

    public HttpDownloader() {
    }

    @Override
    public void configure(Link l, java.util.Map<String, String> param) {
    }

    @Override
    public Message download(java.util.Map<String, String> params, Link link) throws DownloaderInterruptException {
        return null;
    }

    public Message download(Link link, IHttpData.METHOD m) {
        return null;
    }

    public java.io.InputStream getByteStream() {
        return null;
    }

    public long getContentLen(Link link) {
        return 0;
    }

    /**
	 * 
	 * @param link
	 */
    public void init(Link link) {
    }

    /**
	 * 
	 * @exception DownloaderInterruptException
	 */
    public void start() throws DownloaderInterruptException {
    }
}
