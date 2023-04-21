package org.apache.solr.common.params;

/**
 * Defines the request parameters used by all analysis request handlers.
 *
 * @version $Id: AnalysisParams.java 767412 2009-04-22 08:53:28Z shalin $
 * @since solr 1.4
 */
public interface AnalysisParams {

    /**
   * The prefix for all parameters.
   */
    static final String PREFIX = "analysis";

    /**
   * Holds the query to be analyzed.
   */
    static final String QUERY = PREFIX + ".query";

    /**
   * Set to {@code true} to indicate that the index tokens that match query tokens should be marked as "mateched".
   */
    static final String SHOW_MATCH = PREFIX + ".showmatch";

    /**
   * Holds the value of the field which should be analyzed.
   */
    static final String FIELD_NAME = PREFIX + ".fieldname";

    /**
   * Holds a comma-separated list of field types that the analysis should be peformed for.
   */
    static final String FIELD_TYPE = PREFIX + ".fieldtype";

    /**
   * Hodls a comma-separated list of field named that the analysis should be performed for.
   */
    static final String FIELD_VALUE = PREFIX + ".fieldvalue";
}
