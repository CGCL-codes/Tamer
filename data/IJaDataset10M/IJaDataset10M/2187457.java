package com.volantis.xml.pipeline.sax.drivers.googledocs;

import java.util.HashMap;
import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import org.xml.sax.Attributes;

/**
 *
 * GDocs list documents rule implementation.
 *
 * @volantis-api-include-in InternalAPI
 */
public class ListDocsRule extends GDocsRule {

    /**
     * The default instance.
     */
    private static final DynamicElementRule DEFAULT_INSTANCE = new ListDocsRule();

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
    private static final LogDispatcher LOGGER = LocalizationFactory.createLogger(ListDocsRule.class);

    private static final ExceptionLocalizer EXCEPTION_LOCALIZER = LocalizationFactory.createExceptionLocalizer(ListDocsRule.class);

    /**
     * Creates a new ListDocsRule
     */
    public ListDocsRule() {
    }

    private static String GOOGLE_DOCS_FEED_URL = "http://docs.google.com/feeds/documents/private/full";

    private static String GOOGLE_DOCS_FEED_URL_CATEGORY_PARAM = "?category=";

    private static String GOOGLE_DOCS_FEED_URL_QUERY_PARAM = "&q=";

    private static String GOOGLE_DOCS_FEED_URL_PAGE_SIZE_PARAM = "&max-results=";

    private static String GOOGLE_DOCS_FEED_URL_START_INDEX_PARAM = "&start-index=";

    /**
     * Identifier for the category attribute
     */
    public static final String ATTRIBUTE_CATEGORY = "category";

    /**
     * Identifier for the query attribute
     */
    public static final String ATTRIBUTE_QUERY = "query";

    /**
     * Identifier for the page-size  attribute
     */
    public static final String ATTRIBUTE_PAGE_SIZE = "page-size";

    public static final String ATTRIBUTE_PAGE_SIZE_DEFAULT_VALUE = "10";

    public static final int ATTRIBUTE_PAGE_SIZE_MAX_VALUE = 500;

    public static final int ATTRIBUTE_PAGE_SIZE_MIN_VALUE = 1;

    /**
     * Identifier for the page-index attribute
     */
    public static final String ATTRIBUTE_PAGE_INDEX = "page-index";

    public static final String ATTRIBUTE_PAGE_INDEX_DEFAULT_VALUE = "1";

    public static final int ATTRIBUTE_PAGE_INDEX_MIN_VALUE = 1;

    public static final String[] VALID_ATTRIBUTE_CATEGORIES = new String[] { "document" };

    protected HashMap<String, String> gatherAndValidateAttributes(Attributes attributes) throws XMLPipelineException {
        HashMap<String, String> atts = new HashMap<String, String>();
        String category = attributes.getValue(ATTRIBUTE_CATEGORY);
        String pageSize = attributes.getValue(ATTRIBUTE_PAGE_SIZE);
        String pageIndex = attributes.getValue(ATTRIBUTE_PAGE_INDEX);
        String query = attributes.getValue(ATTRIBUTE_QUERY);
        validateCategoryAttribute(category);
        if (pageSize == null) {
            pageSize = ATTRIBUTE_PAGE_SIZE_DEFAULT_VALUE;
        } else {
            validatePageSizeAttribute(pageSize);
        }
        if (pageIndex == null) {
            pageIndex = ATTRIBUTE_PAGE_INDEX_DEFAULT_VALUE;
        } else {
            validatePageIndexAttribute(pageIndex);
        }
        atts.put(ATTRIBUTE_CATEGORY, category);
        atts.put(ATTRIBUTE_PAGE_SIZE, pageSize);
        atts.put(ATTRIBUTE_PAGE_INDEX, pageIndex);
        atts.put(ATTRIBUTE_QUERY, query);
        return atts;
    }

    protected String prepareRequestUrl(HashMap<String, String> atts) {
        StringBuffer requestUrl = new StringBuffer(GOOGLE_DOCS_FEED_URL);
        final String category = atts.get(ATTRIBUTE_CATEGORY);
        addAttributeParam(requestUrl, GOOGLE_DOCS_FEED_URL_CATEGORY_PARAM, category);
        addAttributeParam(requestUrl, GOOGLE_DOCS_FEED_URL_QUERY_PARAM, atts.get(ATTRIBUTE_QUERY));
        addAttributeParam(requestUrl, GOOGLE_DOCS_FEED_URL_PAGE_SIZE_PARAM, atts.get(ATTRIBUTE_PAGE_SIZE));
        addAttributeParam(requestUrl, GOOGLE_DOCS_FEED_URL_START_INDEX_PARAM, calculateStartIndex(atts.get(ATTRIBUTE_PAGE_INDEX), atts.get(ATTRIBUTE_PAGE_SIZE)));
        return requestUrl.toString();
    }

    private void addAttributeParam(StringBuffer buffer, String key, String value) {
        if (value != null) {
            buffer.append(key).append(value);
        }
    }

    /**
     * Calculates startIndex (used by Google) on the basis of our given attributes: pageIndex and pageSize
     *
     * @param pageIndex value of pageIndex attribute
     * @param pageSize value of pageSize attribute
     *
     * @return calculated startIndex value
     */
    private String calculateStartIndex(String pageIndex, String pageSize) {
        int pIndex = Integer.valueOf(pageIndex);
        int pSize = Integer.valueOf(pageSize);
        int sIndex = pSize * (pIndex - 1) + 1;
        return sIndex + "";
    }

    private void validateCategoryAttribute(String category) throws XMLPipelineException {
        for (String vc : VALID_ATTRIBUTE_CATEGORIES) {
            if (vc.equals(category)) {
                return;
            }
        }
        throw new XMLPipelineException(EXCEPTION_LOCALIZER.format("attribute-invalid", ATTRIBUTE_CATEGORY), null);
    }

    private void validatePageSizeAttribute(String pageSize) throws XMLPipelineException {
        try {
            int pSize = Integer.valueOf(pageSize);
            if (pSize > ATTRIBUTE_PAGE_SIZE_MAX_VALUE || pSize < ATTRIBUTE_PAGE_SIZE_MIN_VALUE) {
                throw new XMLPipelineException(EXCEPTION_LOCALIZER.format("attribute-size-exceeded", ATTRIBUTE_PAGE_SIZE), null);
            }
        } catch (NumberFormatException e) {
            throw new XMLPipelineException(EXCEPTION_LOCALIZER.format("attribute-numeric-invalid", ATTRIBUTE_PAGE_SIZE), null, e);
        }
    }

    private void validatePageIndexAttribute(String pageIndex) throws XMLPipelineException {
        try {
            int pIndex = Integer.valueOf(pageIndex);
            if (pIndex < ATTRIBUTE_PAGE_SIZE_MIN_VALUE) {
                throw new XMLPipelineException(EXCEPTION_LOCALIZER.format("attribute-size-exceeded", ATTRIBUTE_PAGE_INDEX), null);
            }
        } catch (NumberFormatException e) {
            throw new XMLPipelineException(EXCEPTION_LOCALIZER.format("attribute-numeric-invalid", ATTRIBUTE_PAGE_INDEX), null, e);
        }
    }
}
