package simplespider.simplespider.bot.extractor.html.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import simplespider.simplespider.bot.extractor.LinkExtractor;
import simplespider.simplespider.util.SimpleUrl;
import simplespider.simplespider.util.StringUtils;
import simplespider.simplespider.util.ValidityHelper;

public class StreamExtractor implements LinkExtractor {

    private static final Log LOG = LogFactory.getLog(StreamExtractor.class);

    private static final String EXTRACTOR_HTML_STREAM_MAX_URL_LENGTH = "extractor.html-stream.max-url-length";

    private static final int EXTRACTOR_HTML_STREAM_MAX_URL_LENGTH_DEFAULT = 1024;

    private static final String EXTRACTOR_HTML_STREAM_BUFFER_SIZE = "extractor.html-stream.buffer-size-bytes";

    private static final int EXTRACTOR_HTML_STREAM_BUFFER_SIZE_BYTES_DEFAULT = 4096;

    private final Configuration configuration;

    public StreamExtractor(final Configuration configuration) {
        this.configuration = configuration;
    }

    public List<String> getUrls(final InputStream body, final String baseUrl) throws IOException {
        ValidityHelper.checkNotNull("body", body);
        int bufferSize = this.configuration.getInt(EXTRACTOR_HTML_STREAM_BUFFER_SIZE, EXTRACTOR_HTML_STREAM_BUFFER_SIZE_BYTES_DEFAULT);
        if (bufferSize <= 0) {
            LOG.warn("Configuration " + EXTRACTOR_HTML_STREAM_BUFFER_SIZE + " is invalid. Using default value: " + EXTRACTOR_HTML_STREAM_BUFFER_SIZE_BYTES_DEFAULT);
            bufferSize = EXTRACTOR_HTML_STREAM_BUFFER_SIZE_BYTES_DEFAULT;
        }
        final TagListenerImpl listener = new TagListenerImpl();
        final HtmlWriter htmlWriter = new HtmlWriter(true, listener, bufferSize);
        parse(body, htmlWriter, baseUrl, bufferSize);
        final List<String> links = getLinks(baseUrl, listener.getLinks());
        return links;
    }

    private List<String> getLinks(final String baseUrl, final List<String> extractedLinks) throws MalformedURLException {
        int maxUrlLength = this.configuration.getInt(EXTRACTOR_HTML_STREAM_MAX_URL_LENGTH, EXTRACTOR_HTML_STREAM_MAX_URL_LENGTH_DEFAULT);
        if (maxUrlLength <= 0) {
            LOG.warn("Configuration " + EXTRACTOR_HTML_STREAM_MAX_URL_LENGTH + " is invalid. Using default value: " + EXTRACTOR_HTML_STREAM_MAX_URL_LENGTH_DEFAULT);
            maxUrlLength = EXTRACTOR_HTML_STREAM_MAX_URL_LENGTH_DEFAULT;
        }
        final SimpleUrl url = new SimpleUrl(baseUrl);
        final List<String> links = new ArrayList<String>(extractedLinks.size());
        for (final String reference : extractedLinks) {
            if (reference.contains("<") || reference.contains(">")) {
                LOG.warn("Ignoring possible invalid reference based on URL \"" + baseUrl + "\":\n" + StringUtils.clipping(reference, 128));
                continue;
            }
            try {
                final SimpleUrl newUrl = SimpleUrl.newURL(url, reference);
                if (newUrl == null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Ignoring reference \"" + reference + "\" based on URL \"" + baseUrl + "\", because it contains nothing");
                    }
                    continue;
                }
                final String normalformedUrl = newUrl.toNormalform(false, true);
                if (normalformedUrl.length() > maxUrlLength) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Ignoring reference \"" + reference + "\" based on URL \"" + baseUrl + "\", because its size is greater than " + maxUrlLength);
                    }
                    continue;
                }
                links.add(normalformedUrl);
            } catch (final Exception e) {
                if (e instanceof RuntimeException) {
                    LOG.error("Ignoring reference \"" + reference + "\" based on URL \"" + baseUrl + "\":" + e, e);
                } else if (LOG.isDebugEnabled()) {
                    LOG.debug("Ignoring reference \"" + reference + "\" based on URL \"" + baseUrl + "\":" + e, e);
                } else {
                    LOG.warn("Ignoring reference \"" + reference + "\" based on URL \"" + baseUrl + "\":" + e);
                }
            }
        }
        return links;
    }

    private void parse(final InputStream sourceStream, final HtmlWriter target, final String baseUrl, final int bufferSize) throws IOException {
        final Reader source = new InputStreamReader(sourceStream);
        final char[] buffer = new char[bufferSize];
        long count = 0;
        for (int n = 0; -1 != (n = source.read(buffer)); ) {
            target.write(buffer, 0, n);
            count += n;
            if (target.binarySuspect()) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Skip binary content: \"" + baseUrl + "\"");
                }
                break;
            }
        }
        target.flush();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Loaded url \"" + baseUrl + "\": " + count + " bytes");
        }
        target.close();
    }
}
