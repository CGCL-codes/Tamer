package com.duroty.lucene.mail.search;

import com.duroty.lucene.analysis.KeywordAnalyzer;
import com.duroty.lucene.mail.LuceneMessageConstants;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.CharStream;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParserTokenManager;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

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
    public static final String[] messageFields = new String[] { Field_from, Field_to, Field_cc, Field_subject, Field_body, Field_attachments, Field_filetype, Field_has_attachments };

    /**
     * DOCUMENT ME!
     */
    public static final int[] messageFlags = new int[] { NORMAL_FIELD, NORMAL_FIELD, NORMAL_FIELD, NORMAL_FIELD, NORMAL_FIELD, NORMAL_FIELD, NORMAL_FIELD, NORMAL_FIELD };

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
            query = MultiFieldQueryParser.parse(QueryParser.escape(queryString), messageFields, messageFlags, analyzer);
        } else {
            if (queryString.indexOf("hasattachments:true") > -1) {
                queryString = queryString.replaceAll("hasattachments:true", "").trim();
                BooleanQuery booleanQuery = new BooleanQuery();
                if ((queryString != null) && (queryString.length() > 0)) {
                    QueryParser parser = new QueryParser("", analyzer);
                    parser.setDefaultOperator(Operator.AND);
                    Query aux1 = parser.parse(queryString);
                    booleanQuery.add(aux1, BooleanClause.Occur.MUST);
                    parser = new QueryParser(Field_has_attachments, new KeywordAnalyzer());
                    parser.setDefaultOperator(Operator.AND);
                    Query aux2 = parser.parse("hasattachments:true");
                    booleanQuery.add(aux2, BooleanClause.Occur.MUST);
                    query = booleanQuery;
                } else {
                    QueryParser parser = new QueryParser(Field_has_attachments, new KeywordAnalyzer());
                    parser.setDefaultOperator(Operator.AND);
                    query = parser.parse("hasattachments:true");
                }
            } else {
                QueryParser parser = new QueryParser("", analyzer);
                parser.setDefaultOperator(Operator.AND);
                query = parser.parse(queryString);
            }
        }
        return query;
    }
}
