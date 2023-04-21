package net.deytan.wofee.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import net.deytan.wofee.entity.FetchInfos;
import net.deytan.wofee.entity.FetchInfos.FETCHING_RESULT;
import net.deytan.wofee.exception.HttpException;
import org.apache.commons.digester.Digester;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.ContentEncodingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HTTP;

@Deprecated
public class ApacheHttpService {

    private DefaultHttpClient httpClient;

    public ApacheHttpService() {
        final SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        final SingleClientConnManager connMgr = new SingleClientConnManager(schemeRegistry);
        final HttpParams params = new SyncBasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        this.httpClient = new ContentEncodingHttpClient(connMgr, params);
    }

    public InputStream fetchFeed(final String uriStr, final FetchInfos fetchInfos) throws HttpException {
        InputStream responseStream = null;
        HttpEntity entity = null;
        URI uri = null;
        try {
            uri = new URI(uriStr);
        } catch (URISyntaxException exception) {
            throw new HttpException("get '" + uri + "' failed", exception);
        }
        try {
            entity = this.getResponse(uri, fetchInfos);
            responseStream = entity.getContent();
        } catch (IOException exception) {
            this.abort(entity);
            throw new HttpException("get '" + uri + "' failed", exception);
        }
        return responseStream;
    }

    public String getFavIconURI() {
        final FavIconHtmlParser htmlParser = new FavIconHtmlParser();
        final Digester digester = new Digester();
        digester.push(htmlParser);
        digester.addCallMethod("head/link", "setPath", 2);
        digester.addCallParam("head/link", 0, "rel");
        digester.addCallParam("head/link", 1, "href");
        return htmlParser.getPath();
    }

    public void close() {
        this.httpClient.getConnectionManager().shutdown();
    }

    private HttpEntity getResponse(final URI uri, final FetchInfos fetchInfos) throws HttpException {
        final HttpGet method = new HttpGet(uri);
        method.setHeader("Accept-Encoding", "gzip");
        if (fetchInfos.getETag() != null) {
            method.setHeader("If-None-Match", fetchInfos.getETag());
        }
        if (fetchInfos.getLastUpdate() != null) {
            method.setHeader("If-Modified-Since", DateUtils.formatDate(fetchInfos.getLastUpdate()));
        }
        if (fetchInfos.isDeltaEncoding()) {
            method.setHeader("A-IM", "feed");
        }
        HttpResponse response = null;
        try {
            response = this.httpClient.execute(method);
        } catch (IOException exception) {
            fetchInfos.setResult(FETCHING_RESULT.IO_ERROR);
            this.abort(method);
            throw new HttpException("getResponse '" + uri + "' failed", exception);
        }
        if (response.getFirstHeader("ETag") != null) {
            fetchInfos.setETag(response.getFirstHeader("ETag").getValue());
        }
        try {
            if (response.getFirstHeader("Last-Modified") != null) {
                fetchInfos.setLastUpdate(DateUtils.parseDate(response.getFirstHeader("Last-Modified").getValue()));
            }
        } catch (DateParseException exception) {
        }
        fetchInfos.setLastSiteAnswer(response.getStatusLine().toString());
        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            this.abort(method);
            fetchInfos.setResult(FETCHING_RESULT.PATH_ERROR);
            if (statusCode == HttpStatus.SC_SERVICE_UNAVAILABLE) {
                throw new HttpException("getResponse '" + uri + "' failed");
            } else {
                throw new HttpException("getResponse '" + uri + "' failed: " + statusCode);
            }
        }
        final HttpEntity responseEntity = response.getEntity();
        if (responseEntity == null) {
            this.abort(method);
            fetchInfos.setResult(FETCHING_RESULT.PATH_ERROR);
            throw new HttpException("getResponse '" + uri + "' failed: empty response");
        }
        final String contentType = fetchInfos.getContentType();
        if ((contentType != null) && (!responseEntity.getContentType().getValue().startsWith(contentType))) {
            this.abort(method);
            fetchInfos.setResult(FETCHING_RESULT.CONTENT_ERROR);
            throw new HttpException("getResponse '" + uri + "' failed: bad content type '" + contentType + "'!='" + responseEntity.getContentType().getValue() + "'");
        }
        return responseEntity;
    }

    private void abort(final HttpEntity entity) throws HttpException {
        try {
            entity.consumeContent();
        } catch (IOException exception) {
            throw new HttpException("abort failed", exception);
        }
    }

    private void abort(final HttpUriRequest request) {
        request.abort();
    }

    private class FavIconHtmlParser {

        private String path = null;

        public String getPath() {
            return this.path;
        }

        public void setPath(final String rel, final String href) {
            if ("icon".equals(rel) || "shortcut icon".equals(rel)) {
                this.path = href;
            }
        }
    }
}
