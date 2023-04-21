package org.hibernate.search.test.analyzer.inheritance;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.queryParser.QueryParser;
import org.slf4j.Logger;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.test.SearchTestCase;
import org.hibernate.search.test.util.AnalyzerUtils;
import org.hibernate.search.util.LoggerFactory;

/**
 * Test to verify HSEARCH-267.
 *
 * A base class defines a field as indexable without specifying an explicit analyzer. A subclass then defines ab analyzer
 * at class level. This should also be the analyzer used for indexing the field in the base class.
 *
 * @author Hardy Ferentschik
 */
public class AnalyzerInheritanceTest extends SearchTestCase {

    public static final Logger log = LoggerFactory.make();

    /**
	 * Try to verify that the right analyzer is used by indexing and searching.
	 *
	 * @throws Exception in case the test fails.
	 */
    public void testBySearch() throws Exception {
        SubClass testClass = new SubClass();
        testClass.setName("Procaïne");
        FullTextSession s = Search.getFullTextSession(openSession());
        Transaction tx = s.beginTransaction();
        s.persist(testClass);
        tx.commit();
        tx = s.beginTransaction();
        QueryParser parser = new QueryParser(getTargetLuceneVersion(), "name", s.getSearchFactory().getAnalyzer(SubClass.class));
        org.apache.lucene.search.Query luceneQuery = parser.parse("name:Procaïne");
        FullTextQuery query = s.createFullTextQuery(luceneQuery, SubClass.class);
        assertEquals(1, query.getResultSize());
        luceneQuery = parser.parse("name:Procaine");
        query = s.createFullTextQuery(luceneQuery, SubClass.class);
        assertEquals(1, query.getResultSize());
        luceneQuery = parser.parse("name:foo");
        query = s.createFullTextQuery(luceneQuery, SubClass.class);
        assertEquals(0, query.getResultSize());
        tx.commit();
        s.close();
    }

    /**
	 * Try to verify that the right analyzer is used by explicitly retrieving the analyzer form the factory.
	 *
	 * @throws Exception in case the test fails.
	 */
    public void testByAnalyzerRetrieval() throws Exception {
        FullTextSession s = Search.getFullTextSession(openSession());
        Analyzer analyzer = s.getSearchFactory().getAnalyzer(SubClass.class);
        Token[] tokens = AnalyzerUtils.tokensFromAnalysis(analyzer, "name", "Procaïne");
        AnalyzerUtils.assertTokensEqual(tokens, new String[] { "Procaine" });
        s.close();
    }

    protected Class<?>[] getAnnotatedClasses() {
        return new Class[] { SubClass.class };
    }
}
