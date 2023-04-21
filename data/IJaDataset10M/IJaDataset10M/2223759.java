package com.volantis.xml.pipeline.sax.drivers.picasa;

import java.text.MessageFormat;
import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import com.volantis.xml.pipeline.sax.drivers.uri.Fetcher;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.Locator;

/**
 * A FetchAdapterProcess is an AdapterProcess that includes another
 * Document that is specified via a URL.
 *
 * <p>This is used by DSB.</p>
 *
 * @volantis-api-include-in InternalAPI
 */
public class ListPhotosRule extends DynamicElementRuleImpl {

    /**
     * The default instance.
     */
    private static final DynamicElementRule DEFAULT_INSTANCE = new ListPhotosRule();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static DynamicElementRule getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER = LocalizationFactory.createLogger(ListPhotosRule.class);

    /**
     * Identifier for the picasa feed api url
     */
    private static final String PICASA_FEED_API_URL = "http://picasaweb.google.com/data/feed/api";

    /**
     * Identifier for the picasa user name url fragment
     */
    private static final String PICASA_USER_FRAGMENT = "/user/{0}";

    /**
     * Identifier for the picasa album id url fragment
     */
    private static final String PICASA_ALBUM_ID_FRAGMENT = "/albumid/{0}";

    /**
     * Identifier for the picasa album name url fragment
     */
    private static final String PICASA_ALBUM_NAME_FRAGMENT = "/album/{0}";

    /**
     * Identifier for the picasa photo id url fragment
     */
    private static final String PICASA_PHOTO_ID_FRAGMENT = "/photoid/{0}";

    /**
     * Identifier for the picasa kind url fragment with photo value
     */
    private static final String PICASA_KIND_PHOTO_FRAGMENT = "kind=photo";

    /**
     * Identifier for the picasa imgmax url fragment with size value
     */
    private static final String PICASA_IMGMAX_FRAGMENT = "imgmax=800";

    /**
     * Identifier for the picasa tag url fragment
     */
    private static final String PICASA_TAG_FRAGMENT = "tag={0}";

    /**
     * Identifier for the picasa query url fragment
     */
    private static final String PICASA_QUERY_FRAGMENT = "q={0}";

    /**
     * Identifier for the picasa max-results url fragment
     */
    private static final String PICASA_MAX_RESULTS_FRAGMENT = "max-results={0}";

    /**
     * Identifier for the picasa start-index url fragment
     */
    private static final String PICASA_START_INDEX_FRAGMENT = "start-index={0}";

    /**
     * Identifier for the user id attribute 
     */
    private static final String USER_ID_ATTRIBUTE = "user-id";

    /**
     * Identifier for the album id attribute 
     */
    private static final String ALBUM_ID_ATTRIBUTE = "album-id";

    /**
     * Identifier for the album name attribute
     */
    private static final String ALBUM_NAME_ATTRIBUTE = "album";

    /**
     * Identifier for the photo id attribute
     */
    private static final String PHOTO_ID_ATTRIBUTE = "photo-id";

    /**
     * Identifier for the tags attribute
     */
    private static final String TAGS_ATTRIBUTE = "tags";

    /**
     * Identifier for the query attribute
     */
    private static final String QUERY_ATTRIBUTE = "query";

    /**
     * Identifier for the page-size attribute
     */
    private static final String PAGE_SIZE_ATTRIBUTE = "page-size";

    /**
     * Identifier for the page-index attribute
     */
    private static final String PAGE_INDEX_ATTRIBUTE = "page-index";

    /**
     * Identifier for the page-size attribute default value
     */
    private static final String DEFAULT_PAGE_SIZE = "10";

    /**
     * Identifier for the page-index attribute default value
     */
    private static final String DEFAULT_PAGE_INDEX = "1";

    /**
     * Identifier for the parse attribute default value
     */
    private static final String PARSE_ATTRIBUTE_VALUE = "xml";

    /**
     * Identifier for the encoding attribute default value
     */
    private static final String ENCODING_ATTRIBUTE_VALUE = "UTF-8";

    /**
     * Identifier for the timeout attribute default value
     */
    private static final String TIMEOUT_ATTRIBUTE_VALUE = "-1";

    /**
     * Creates a new ListPhotosRule
     */
    public ListPhotosRule() {
    }

    public Object startElement(DynamicProcess dynamicProcess, ExpandedName element, Attributes attributes) throws SAXException {
        XMLPipeline pipeline = dynamicProcess.getPipeline();
        StringBuffer hrefAttr = new StringBuffer(PICASA_FEED_API_URL);
        StringBuffer queryString = new StringBuffer();
        String userId = attributes.getValue(USER_ID_ATTRIBUTE);
        if (userId != null) {
            hrefAttr.append(MessageFormat.format(PICASA_USER_FRAGMENT, userId));
        }
        String albumName = attributes.getValue(ALBUM_NAME_ATTRIBUTE);
        String albumId = attributes.getValue(ALBUM_ID_ATTRIBUTE);
        if (albumName != null && albumId != null) {
            Locator locator = pipeline.getPipelineContext().getCurrentLocator();
            XMLPipelineException e = new XMLPipelineException("not allowed to use both album and album-id attributes", locator);
            getTargetProcess(pipeline).fatalError(e);
            return null;
        }
        if (albumName != null) {
            if (userId == null) {
                Locator locator = pipeline.getPipelineContext().getCurrentLocator();
                XMLPipelineException e = new XMLPipelineException("not allowed to use album attribute without specifying user-id attribute", locator);
                getTargetProcess(pipeline).fatalError(e);
                return null;
            }
            hrefAttr.append(MessageFormat.format(PICASA_ALBUM_NAME_FRAGMENT, albumName));
        } else if (albumId != null) {
            if (userId == null) {
                Locator locator = pipeline.getPipelineContext().getCurrentLocator();
                XMLPipelineException e = new XMLPipelineException("not allowed to use album-id attribute without specifying user-id attribute", locator);
                getTargetProcess(pipeline).fatalError(e);
                return null;
            }
            hrefAttr.append(MessageFormat.format(PICASA_ALBUM_ID_FRAGMENT, albumId));
        }
        String photoId = attributes.getValue(PHOTO_ID_ATTRIBUTE);
        if (photoId != null) {
            if (albumName == null && albumId == null) {
                Locator locator = pipeline.getPipelineContext().getCurrentLocator();
                XMLPipelineException e = new XMLPipelineException("not allowed to use photo-id attribute without specifying album or album-id attribute", locator);
                getTargetProcess(pipeline).fatalError(e);
                return null;
            }
            hrefAttr.append(MessageFormat.format(PICASA_PHOTO_ID_FRAGMENT, photoId));
        } else {
            queryString.append(PICASA_KIND_PHOTO_FRAGMENT);
        }
        String tags = attributes.getValue(TAGS_ATTRIBUTE);
        if (tags != null) {
            String commaSeparatedTags = tags.trim().replaceAll("\\s+", ",");
            if (!"".equals(queryString)) {
                queryString.append("&");
            }
            queryString.append(MessageFormat.format(PICASA_TAG_FRAGMENT, commaSeparatedTags));
        } else if (attributes.getValue(QUERY_ATTRIBUTE) != null) {
            if (!"".equals(queryString)) {
                queryString.append("&");
            }
            queryString.append(MessageFormat.format(PICASA_QUERY_FRAGMENT, attributes.getValue(QUERY_ATTRIBUTE)));
        }
        String pageSize = attributes.getValue(PAGE_SIZE_ATTRIBUTE);
        String pageIndex = attributes.getValue(PAGE_INDEX_ATTRIBUTE);
        if (pageSize != null) {
            Integer pageSizeInt = null;
            try {
                pageSizeInt = Integer.parseInt(pageSize);
            } catch (NumberFormatException nfe) {
                Locator locator = pipeline.getPipelineContext().getCurrentLocator();
                XMLPipelineException e = new XMLPipelineException("page-size attribute should be of integer value", locator);
                getTargetProcess(pipeline).fatalError(e);
                return null;
            }
            if (pageSizeInt < 1 || pageSizeInt > 500) {
                Locator locator = pipeline.getPipelineContext().getCurrentLocator();
                XMLPipelineException e = new XMLPipelineException("page-size attribute value should be in the range of [1,500]", locator);
                getTargetProcess(pipeline).fatalError(e);
                return null;
            }
            pageSize = pageSizeInt.toString();
        } else {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        if (!"".equals(queryString)) {
            queryString.append("&");
        }
        queryString.append(MessageFormat.format(PICASA_MAX_RESULTS_FRAGMENT, pageSize));
        if (pageIndex != null) {
            Integer pageIndexInt = null;
            try {
                pageIndexInt = Integer.parseInt(pageIndex);
            } catch (NumberFormatException nfe) {
                Locator locator = pipeline.getPipelineContext().getCurrentLocator();
                XMLPipelineException e = new XMLPipelineException("page-index attribute should be of integer value", locator);
                getTargetProcess(pipeline).fatalError(e);
                return null;
            }
            if (pageIndexInt < 1) {
                Locator locator = pipeline.getPipelineContext().getCurrentLocator();
                XMLPipelineException e = new XMLPipelineException("page-index attribute value should be in the range of [1,...]", locator);
                getTargetProcess(pipeline).fatalError(e);
                return null;
            }
            pageIndex = pageIndexInt.toString();
        } else {
            pageIndex = DEFAULT_PAGE_INDEX;
        }
        int startIndex = (Integer.valueOf(pageIndex) - 1) * Integer.valueOf(pageSize) + 1;
        queryString.append("&").append(MessageFormat.format(PICASA_START_INDEX_FRAGMENT, startIndex));
        queryString.append("&").append(PICASA_IMGMAX_FRAGMENT);
        if (!"".equals(queryString)) {
            hrefAttr.append("?").append(queryString);
        }
        Fetcher operation = new Fetcher(pipeline);
        operation.setHref(hrefAttr.toString());
        operation.setParse(PARSE_ATTRIBUTE_VALUE);
        operation.setEncoding(ENCODING_ATTRIBUTE_VALUE);
        String timeoutAttr = TIMEOUT_ATTRIBUTE_VALUE;
        Period timeout = null;
        if (timeoutAttr != null) {
            int timeoutInSecs;
            try {
                timeoutInSecs = Integer.parseInt(timeoutAttr);
            } catch (NumberFormatException e) {
                LOGGER.warn("invalid-timeout", timeoutAttr);
                throw e;
            }
            timeout = Period.treatNonPositiveAsIndefinitely(timeoutInSecs * 1000);
        }
        operation.setTimeout(timeout);
        operation.doInclude();
        return null;
    }

    public void endElement(DynamicProcess dynamicProcess, ExpandedName element, Object object) throws SAXException {
    }
}
