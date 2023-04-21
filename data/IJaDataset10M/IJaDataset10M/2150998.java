package com.duroty.lucene.files.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.CharStream;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParserTokenManager;
import org.apache.lucene.search.Query;
import com.duroty.lucene.mail.LuceneMessageConstants;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class SimpleQueryParser extends QueryParser implements LuceneMessageConstants {

    /**
    * DOCUMENT ME!
    */
    public static final int NORMAL_FIELD = 0;

    /**
     * DOCUMENT ME!
     */
    public static final int REQUIRED_FIELD = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int PROHIBITED_FIELD = 2;

    /**
        * DOCUMENT ME!
        */
    public static final String[] messageFields = new String[] { Field_attachments, Field_filetype };

    /**
     * DOCUMENT ME!
     */
    public static final int[] messageFlags = new int[] { NORMAL_FIELD, NORMAL_FIELD };

    /**
     * Creates a new SimpleQueryParser object.
     *
     * @param f DOCUMENT ME!
     * @param a DOCUMENT ME!
     */
    public SimpleQueryParser(String f, Analyzer a) {
        super(f, a);
    }

    /**
     * Creates a new SimpleQueryParser object.
     *
     * @param stream DOCUMENT ME!
     */
    public SimpleQueryParser(CharStream stream) {
        super(stream);
    }

    /**
     * Creates a new SimpleQueryParser object.
     *
     * @param tm DOCUMENT ME!
     */
    public SimpleQueryParser(QueryParserTokenManager tm) {
        super(tm);
    }

    /**
     * DOCUMENT ME!
     *
     * @param queryString DOCUMENT ME!
     * @param analyzer DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ParseException DOCUMENT ME!
     */
    public static Query parse(String queryString, Analyzer analyzer) throws ParseException {
        queryString = " " + queryString.replaceAll("\\s*:\\s*", ":");
        Query query = null;
        if (queryString.indexOf(':') == -1) {
        } else {
            queryString = queryString.replaceAll(":", " ");
        }
        query = MultiFieldQueryParser.parse(QueryParser.escape(queryString), messageFields, messageFlags, analyzer);
        return query;
    }
}
