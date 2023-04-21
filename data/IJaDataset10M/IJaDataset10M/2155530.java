package com.duroty.lucene.didyoumean;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Query;

public interface DidYouMeanParser {

    /**
     * DOCUMENT ME!
     */
    public static final int DEFAULT_OPERATOR_OR = 0;

    /**
     * DOCUMENT ME!
     */
    public static final int DEFAULT_OPERATOR_AND = 1;

    /**
     * DOCUMENT ME!
     *
     * @param queryString DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ParseException DOCUMENT ME!
     */
    public Query parse(String queryString) throws ParseException;

    /**
     * DOCUMENT ME!
     *
     * @param queryString DOCUMENT ME!
     * @param operator DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ParseException DOCUMENT ME!
     */
    public Query suggest(String queryString, Operator operator) throws ParseException;
}
