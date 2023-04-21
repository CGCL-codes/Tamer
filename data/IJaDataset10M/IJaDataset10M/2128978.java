package com.armatiek.infofuze.stream.filesystem.webcrawl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.IOUtils;
import org.apache.tika.mime.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.armatiek.infofuze.config.Definitions;
import com.armatiek.infofuze.error.InfofuzeException;
import com.armatiek.infofuze.stream.filesystem.AbstractFile;
import com.armatiek.infofuze.stream.filesystem.FileIf;
import com.armatiek.infofuze.stream.filesystem.webdav.AutoCloseResponseInputStream;
import com.armatiek.infofuze.utils.StringUtils;

/**
 * Class representing a file that can be retrieved using the HTTP.
 * 
 * @author Maarten Kroon
 */
public class HTTPFile extends AbstractFile {

    private static final Logger logger = LoggerFactory.getLogger(HTTPFile.class);

    protected HttpClient httpClient;

    protected HeadMethod headMethod;

    protected CrawlState crawlState;

    protected long lastModified = -1;

    protected String charset;

    protected String mimeType;

    protected boolean isHtml;

    protected byte[] html;

    protected int depth;

    protected String uri;

    public HTTPFile(HttpClient httpClient, String uri, int depth, CrawlState crawlState) {
        this.httpClient = httpClient;
        this.uri = uri;
        this.depth = depth;
        this.crawlState = crawlState;
        try {
            headMethod = new HeadMethod(uri);
            try {
                headMethod.setDoAuthentication(true);
                headMethod.setFollowRedirects(true);
                int status = httpClient.executeMethod(headMethod);
                if (status != HttpStatus.SC_OK) {
                    throw new InfofuzeException("HEAD request to \"" + uri + "\" failed with status " + status);
                }
            } finally {
                headMethod.releaseConnection();
            }
        } catch (IOException ioe) {
            throw new InfofuzeException("Could not get HTTPFile for uri \"" + uri + "\"", ioe);
        }
    }

    public CrawlState getCrawlState() {
        return crawlState;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (html == null) {
            GetMethod getMethod = new GetMethod(uri);
            getMethod.setDoAuthentication(true);
            getMethod.setFollowRedirects(true);
            int status = httpClient.executeMethod(getMethod);
            if (status != HttpStatus.SC_OK) {
                getMethod.releaseConnection();
                throw new IOException("GET request to \"" + uri + "\" failed with status " + status);
            }
            if (isFile()) {
                return new AutoCloseResponseInputStream(getMethod.getResponseBodyAsStream(), getMethod);
            }
            html = IOUtils.toByteArray(getMethod.getResponseBodyAsStream());
        }
        return new ByteArrayInputStream(html);
    }

    @Override
    public String getCharacterEncoding() {
        if (charset == null) {
            Header contentTypeHeader = headMethod.getResponseHeader("Content-Type");
            if (contentTypeHeader == null) {
                return null;
            }
            String contentType = contentTypeHeader.getValue();
            if (contentType == null) {
                return null;
            }
            MediaType mt = MediaType.parse(contentType);
            if (mt == null) {
                return null;
            }
            String param = mt.getParameters().get("charset");
            if ((param != null) && Charset.isSupported(param)) {
                charset = param;
            }
        }
        return charset;
    }

    @Override
    public String getMimeType() {
        if (mimeType == null) {
            Header contentTypeHeader = headMethod.getResponseHeader("Content-Type");
            if (contentTypeHeader == null) {
                return null;
            }
            String contentType = contentTypeHeader.getValue();
            if (contentType == null) {
                return null;
            }
            MediaType mt = MediaType.parse(contentType);
            if (mt != null) {
                mimeType = mt.toString();
            }
        }
        return mimeType;
    }

    @Override
    public String getName() {
        return URIUtil.getName(uri);
    }

    @Override
    public String getParent() {
        throw new UnsupportedOperationException("Method HTTPFile.getParent() not supported");
    }

    @Override
    public FileIf getParentFile() {
        throw new UnsupportedOperationException("Method HTTPFile.getParentFile() not supported");
    }

    @Override
    public String getPath() {
        return uri.toString();
    }

    @Override
    public boolean hasContent() {
        return true;
    }

    @Override
    public boolean isDirectory() {
        String mimeType = getMimeType();
        if (mimeType == null) {
            return false;
        }
        return StringUtils.equalsAny(getMimeType(), Definitions.MIMETYPES_HTML);
    }

    @Override
    public boolean isFile() {
        return !isDirectory();
    }

    @Override
    public long lastModified() {
        if (lastModified == -1) {
            try {
                Header header = headMethod.getResponseHeader("Last-Modified");
                if (header != null) {
                    lastModified = DateUtil.parseDate(header.getValue()).getTime();
                } else {
                    lastModified = 0;
                }
            } catch (Exception e) {
                throw new InfofuzeException("Error getting Last-Modified header", e);
            }
        }
        return lastModified;
    }

    @Override
    public long length() {
        return headMethod.getResponseContentLength();
    }

    protected void addFiles(List<FileIf> files, List<String> linkList, String base) throws URIException, UnsupportedEncodingException, URISyntaxException {
        Iterator<String> links = linkList.iterator();
        while (links.hasNext()) {
            String linkText = links.next();
            try {
                linkText = linkText.replaceAll("\\s", "%20");
                URI link = new URI(linkText, true);
                if (link.isRelativeURI()) {
                    link = new URI(new URI(base, true), link);
                }
                String uri = URLNormalizer.normalize(link.toString());
                if (uri == null) {
                    continue;
                }
                if (!uri.startsWith(crawlState.getSeedURI())) {
                    continue;
                }
                if (crawlState.getVisitedURLSet().contains(uri)) {
                    continue;
                }
                crawlState.getVisitedURLSet().add(uri);
                HTTPFile file = new HTTPFile(httpClient, uri, depth++, crawlState);
                files.add(file);
            } catch (Exception e) {
                logger.error("Error getting HTTPFile \"" + linkText + "\"", e);
            }
        }
    }

    @Override
    public Iterator<FileIf> listFiles() {
        if (isFile()) {
            return null;
        }
        ArrayList<FileIf> files = new ArrayList<FileIf>();
        if (depth >= crawlState.getMaxDepth()) {
            return files.iterator();
        }
        try {
            HTMLParser parser = new HTMLParser();
            HTMLLinkInfo linkInfo = parser.getHTMLLinkInfo(new BufferedInputStream(getInputStream()), getCharacterEncoding());
            if (!linkInfo.getFollow()) {
                return files.iterator();
            }
            String base = (linkInfo.getBase() != null) ? linkInfo.getBase() : this.uri;
            addFiles(files, linkInfo.getHrefList(), base);
            addFiles(files, linkInfo.getFrameList(), base);
            if (crawlState.getFollowImages()) {
                addFiles(files, linkInfo.getImageList(), base);
            }
            if (crawlState.getFollowLinks()) {
                addFiles(files, linkInfo.getLinkList(), base);
            }
            if (crawlState.getFollowScripts()) {
                addFiles(files, linkInfo.getScriptList(), base);
            }
            return files.iterator();
        } catch (Exception e) {
            throw new InfofuzeException(e);
        }
    }
}
