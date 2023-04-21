package wayic.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import wayic.commons.WayicResourceEx;
import wayic.utils.CommonUtils;

/**
 * THIS CLASS IS NOT THREAD SAFE. It should always be used in a single-threaded
 * context.
 * 
 * @author Ashesh Nishant
 * 
 */
public class HttpFetcher extends AbstractHttpFetcher {

    private static final Logger LOGGER = LogManager.getLogger(HttpFetcher.class);

    protected GetMethod get;

    /**
     * Full constructor.
     * 
     * @param client
     * @param url
     */
    public HttpFetcher(HttpLoader client, URL url) throws WayicResourceEx {
        super(client, url);
    }

    /**
     * Constructor. Creates with LoaderFactory.getDefaultHttpClient().
     * 
     * @param url
     * @throws WayicResourceEx
     */
    public HttpFetcher(URL url) throws WayicResourceEx {
        super(HttpFactory.getDefaultHttpClient(), url);
    }

    @Override
    public ContentHeader load(ContentHeader header) throws IOException {
        ContentHeader outHeader = null;
        if ("file".equals(url.getProtocol())) {
            UrlLoader loader = HttpFactory.urlLoader(url);
            if (loader == null) {
                LOGGER.warn("Can't deal with delegate UrlLoader");
                throw new NullPointerException("Cannot read from delegate URLLoader");
            }
            outHeader = loader.load(header);
            in = loader.getAsInputStream();
            encoding = loader.getEncoding();
            if (encoding == null) {
                encoding = DEFAULT_ENCODING;
            }
            LOGGER.debug("Loaded (via delegate) " + url);
        } else {
            get = newGetMethod(url.toExternalForm(), header);
            int status = client.execute(get);
            outHeader = getContentHeader(get);
            outHeader.setStatusCode(status);
            if (outHeader.getEtag() == null && header != null) {
                outHeader.setEtag(header.getEtag());
            }
            if (outHeader.getLastModified() == ContentHeader.UNINITIALIZED && header != null) {
                outHeader.setLastModified(header.getLastModified());
            }
            in = get.getResponseBodyAsStream();
            if (in == null || status == HttpStatus.SC_NOT_MODIFIED) {
                conditionalGetFailed = true;
            }
            encoding = get.getRequestCharSet();
            if (encoding == null) {
                encoding = DEFAULT_ENCODING;
            }
            LOGGER.debug("Loaded " + url);
        }
        return outHeader;
    }

    public void close() {
        super.close();
        if (get != null) {
            get.releaseConnection();
            get = null;
        }
    }

    /**
     * In case conditional-get has failed(not modified), this will return null.
     * If this is being called after close() or before load(), this will throw
     * an IOException. In case conditional-get has failed and close() has been
     * called, this will still return null.
     * 
     * @see wayic.http.wayic.utils.AbstractUrlLoader#getAsInputStream()
     */
    public InputStream getAsInputStream() throws IOException {
        if ((in == null && conditionalGetFailed == false)) {
            throw new IOException("InputStream null: Source not modified OR called prior to load() OR after close()");
        }
        return in;
    }

    /**
     * Override this if a different implementation of ContentHeaderI is to be
     * used.
     * 
     * @return. wayic.feed.impl.basic.ContentHeader implementation of
     *          ContentHeaderI interface.
     */
    public ContentHeader newContentHeader() {
        HttpContentHeader header = new HttpContentHeader();
        header.setUrl(getUrl());
        return header;
    }

    /**
     * Retrieves the content header from an executed GetMethod. Change this if
     * the new properties are added to ContentHeaderI.
     * 
     * @param get
     * @return
     */
    protected ContentHeader getContentHeader(GetMethod get) {
        Header modifiedHeader = get.getResponseHeader("Last-Modified");
        Header etagHeader = get.getResponseHeader("ETag");
        long modified = ContentHeader.UNINITIALIZED;
        String etag = null;
        String value = null;
        if (modifiedHeader != null) {
            value = modifiedHeader.getValue();
            if (value != null && !"".equals(value.trim())) {
                Date date = CommonUtils.getDate(value);
                modified = date.getTime();
            }
        }
        if (etagHeader != null) {
            value = etagHeader.getValue();
            if (value != null) {
                etag = value;
            }
        }
        ContentHeader outHeader = newContentHeader();
        outHeader.setEtag(etag);
        outHeader.setLastModified(modified);
        return outHeader;
    }
}
