package eduburner.crawler.model;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import eduburner.crawler.enumerations.FetchStatusCodes;
import eduburner.crawler.frontier.WorkQueue;
import eduburner.crawler.util.UrlUtils;

public class CrawlURI implements Serializable {

    private static final long serialVersionUID = 968032112154104528L;

    private String url = StringUtils.EMPTY;

    private URI uri;

    private String classKey = StringUtils.EMPTY;

    private int fetchStatus = 0;

    private int fetchAttempts = 0;

    private String userAgent = StringUtils.EMPTY;

    private WorkQueue holder;

    public CrawlURI(String url) {
        this.url = url;
        try {
            this.uri = new URI(url);
        } catch (URISyntaxException e) {
            this.uri = null;
        }
        this.classKey = DigestUtils.md5Hex(UrlUtils.getHostFromUrl(this.url));
    }

    /**
	 * 最短抓取时间间隔
	 * @return
	 */
    public long getMinCrawlInterval() {
        return 1000 * 10L;
    }

    public void clearUp() {
        fetchStatus = FetchStatusCodes.S_UNATTEMPTED;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getClassKey() {
        return classKey;
    }

    public void setClassKey(String classKey) {
        this.classKey = classKey;
    }

    public int getFetchStatus() {
        return fetchStatus;
    }

    public void setFetchStatus(int fetchStatus) {
        this.fetchStatus = fetchStatus;
    }

    public int getFetchAttempts() {
        return fetchAttempts;
    }

    public void setFetchAttempts(int fetchAttempts) {
        this.fetchAttempts = fetchAttempts;
    }

    public WorkQueue getHolder() {
        return holder;
    }

    public void setHolder(WorkQueue holder) {
        this.holder = holder;
    }

    @Override
    public boolean equals(Object o) {
        CrawlURI c = (CrawlURI) o;
        if (this == c) {
            return true;
        }
        if (this.classKey.equals(c.classKey) && this.url.equals(c.url)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return this.url;
    }
}
