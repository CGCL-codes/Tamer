package org.fxplayer.service.cover;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * The Class DiscogsFetcher.
 */
public class DiscogsFetcher extends HttpFetcher {

    private static final String API_KEY = "536a66e97a";

    /**
	 * The main method.
	 * @param args
	 *          the arguments
	 */
    public static void main(final String[] args) {
        new DiscogsFetcher().httpFetch("Hijack", "The Original Horns of Jericho");
    }

    private String executeRequest(final String url) throws IOException {
        final HttpMethod method = new GetMethod(url);
        final InputStream in = null;
        try {
            if (new HttpClient().executeMethod(method) != HttpStatus.SC_OK) throw new IOException("Method failed: " + method.getStatusLine());
            try {
                return IOUtils.toString(new GZIPInputStream(new ByteArrayInputStream(method.getResponseBody())), "UTF-8");
            } catch (final Exception e) {
            }
            return method.getResponseBodyAsString();
        } finally {
            IOUtils.closeQuietly(in);
            method.releaseConnection();
        }
    }

    private List<String> getImagesForRelease(final int releaseId) throws IOException, JDOMException {
        String url = null;
        try {
            url = "http://www.discogs.com/release/" + releaseId + "?f=xml&api_key" + API_KEY;
            if (LOG.isDebugEnabled()) LOG.debug("Fetching cover From release:" + releaseId);
            final String searchResult = executeRequest(url);
            final SAXBuilder builder = new SAXBuilder();
            final Document document = builder.build(new StringReader(searchResult));
            final Element root = document.getRootElement();
            final Element release = root.getChild("release");
            final Element images = release.getChild("images");
            final List<String> imageUrls = new ArrayList<String>();
            if (images != null) {
                final List<?> imageList = images.getChildren("image");
                for (final Object obj : imageList) {
                    final String imageUrl = ((Element) obj).getAttributeValue("uri");
                    if (imageUrl != null) imageUrls.add(imageUrl);
                }
                return imageUrls;
            }
        } catch (final Exception e) {
            LOG.debug("Error fetching url " + url, e);
        }
        return null;
    }

    @Override
    public byte[] httpFetch(final String artist, final String album) {
        String url = null;
        try {
            if (LOG.isDebugEnabled()) LOG.debug("Fetching cover From Discogs:album=" + album + ", artist=" + artist);
            final String query = URLEncoder.encode(artist + " " + album, "UTF-8");
            url = "http://www.discogs.com/search?type=all&q=" + query + "&f=xml&api_key=" + API_KEY;
            final String searchResult = executeRequest(url);
            final List<Integer> releaseIds = parseReleaseIds(searchResult);
            for (final Integer releaseId : releaseIds) {
                final List<String> imagesForRelease = getImagesForRelease(releaseId);
                for (final String imageUrl : imagesForRelease) {
                    final byte[] imageBytes = wget(imageUrl);
                    if (checkImage(imageBytes)) return imageBytes;
                }
            }
        } catch (final Exception e) {
            LOG.debug("Error fetching url " + url, e);
        }
        return null;
    }

    private List<Integer> parseReleaseIds(final String xml) throws Exception {
        final SAXBuilder builder = new SAXBuilder();
        final Document document = builder.build(new StringReader(xml));
        final Element root = document.getRootElement();
        final Element searchResults = root.getChild("searchresults");
        final List<?> results = searchResults.getChildren("result");
        final List<Integer> releaseIds = new ArrayList<Integer>();
        for (final Object obj : results) {
            final String uri = ((Element) obj).getChildText("uri");
            if (uri != null && uri.contains("/release/")) {
                final String relaseId = uri.substring(uri.indexOf("/release/") + 9);
                releaseIds.add(new Integer(relaseId));
            }
        }
        return releaseIds;
    }
}
