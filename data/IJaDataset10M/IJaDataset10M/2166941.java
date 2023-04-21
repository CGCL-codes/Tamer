package org.hibernate.search.test.analyzer;

import java.util.HashSet;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.queryParser.QueryParser;
import org.slf4j.Logger;
import org.hibernate.Transaction;
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.SearchException;
import org.hibernate.search.impl.ConfigContext;
import org.hibernate.search.cfg.SearchConfigurationFromHibernateCore;
import org.hibernate.search.engine.DocumentBuilderContainedEntity;
import org.hibernate.search.test.SearchTestCase;
import org.hibernate.search.test.util.AnalyzerUtils;
import org.hibernate.search.util.LoggerFactory;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
public class AnalyzerTest extends SearchTestCase {

    public static final Logger log = LoggerFactory.make();

    public void testAnalyzerDiscriminator() throws Exception {
        Article germanArticle = new Article();
        germanArticle.setLanguage("de");
        germanArticle.setText("aufeinanderschlügen");
        Set<Article> references = new HashSet<Article>();
        references.add(germanArticle);
        Article englishArticle = new Article();
        englishArticle.setLanguage("en");
        englishArticle.setText("acknowledgment");
        englishArticle.setReferences(references);
        FullTextSession s = Search.getFullTextSession(openSession());
        Transaction tx = s.beginTransaction();
        s.persist(englishArticle);
        tx.commit();
        tx = s.beginTransaction();
        QueryParser parser = new QueryParser(getTargetLuceneVersion(), "references.text", SearchTestCase.standardAnalyzer);
        org.apache.lucene.search.Query luceneQuery = parser.parse("aufeinanderschlug");
        FullTextQuery query = s.createFullTextQuery(luceneQuery);
        assertEquals(1, query.getResultSize());
        parser = new QueryParser(getTargetLuceneVersion(), "text", SearchTestCase.standardAnalyzer);
        luceneQuery = parser.parse("acknowledg");
        query = s.createFullTextQuery(luceneQuery);
        assertEquals(1, query.getResultSize());
        tx.commit();
        s.close();
    }

    public void testMultipleAnalyzerDiscriminatorDefinitions() {
        SearchConfigurationFromHibernateCore searchConfig = new SearchConfigurationFromHibernateCore(cfg);
        ReflectionManager reflectionManager = searchConfig.getReflectionManager();
        XClass xclass = reflectionManager.toXClass(BlogEntry.class);
        Set<XClass> optimizationBlackList = new HashSet<XClass>();
        ConfigContext context = new ConfigContext(searchConfig);
        try {
            new DocumentBuilderContainedEntity(xclass, context, reflectionManager, optimizationBlackList);
            fail();
        } catch (SearchException e) {
            assertTrue("Wrong error message", e.getMessage().startsWith("Multiple AnalyzerDiscriminator defined in the same class hierarchy"));
        }
    }

    public void testScopedAnalyzers() throws Exception {
        MyEntity en = new MyEntity();
        en.setEntity("Entity");
        en.setField("Field");
        en.setProperty("Property");
        en.setComponent(new MyComponent());
        en.getComponent().setComponentProperty("component property");
        FullTextSession s = Search.getFullTextSession(openSession());
        Transaction tx = s.beginTransaction();
        s.persist(en);
        tx.commit();
        tx = s.beginTransaction();
        QueryParser parser = new QueryParser(getTargetLuceneVersion(), "id", SearchTestCase.standardAnalyzer);
        org.apache.lucene.search.Query luceneQuery = parser.parse("entity:alarm");
        FullTextQuery query = s.createFullTextQuery(luceneQuery, MyEntity.class);
        assertEquals(1, query.getResultSize());
        luceneQuery = parser.parse("property:cat");
        query = s.createFullTextQuery(luceneQuery, MyEntity.class);
        assertEquals(1, query.getResultSize());
        luceneQuery = parser.parse("field:energy");
        query = s.createFullTextQuery(luceneQuery, MyEntity.class);
        assertEquals(1, query.getResultSize());
        luceneQuery = parser.parse("component.componentProperty:noise");
        query = s.createFullTextQuery(luceneQuery, MyEntity.class);
        assertEquals(1, query.getResultSize());
        s.delete(query.uniqueResult());
        tx.commit();
        s.close();
    }

    public void testScopedAnalyzersFromSearchFactory() throws Exception {
        FullTextSession session = Search.getFullTextSession(openSession());
        SearchFactory searchFactory = session.getSearchFactory();
        Analyzer analyzer = searchFactory.getAnalyzer(MyEntity.class);
        Token[] tokens = AnalyzerUtils.tokensFromAnalysis(analyzer, "entity", "");
        AnalyzerUtils.assertTokensEqual(tokens, new String[] { "alarm", "dog", "performance" });
        tokens = AnalyzerUtils.tokensFromAnalysis(analyzer, "property", "");
        AnalyzerUtils.assertTokensEqual(tokens, new String[] { "sound", "cat", "speed" });
        tokens = AnalyzerUtils.tokensFromAnalysis(analyzer, "field", "");
        AnalyzerUtils.assertTokensEqual(tokens, new String[] { "music", "elephant", "energy" });
        tokens = AnalyzerUtils.tokensFromAnalysis(analyzer, "component.componentProperty", "");
        AnalyzerUtils.assertTokensEqual(tokens, new String[] { "noise", "mouse", "light" });
        try {
            searchFactory.getAnalyzer((Class) null);
        } catch (IllegalArgumentException iae) {
            log.debug("success");
        }
        try {
            searchFactory.getAnalyzer(String.class);
        } catch (IllegalArgumentException iae) {
            log.debug("success");
        }
        session.close();
    }

    public void testNotAnalyzedFieldAndScopedAnalyzer() throws Exception {
        FullTextSession session = Search.getFullTextSession(openSession());
        SearchFactory searchFactory = session.getSearchFactory();
        Analyzer analyzer = searchFactory.getAnalyzer(MyEntity.class);
        Token[] tokens = AnalyzerUtils.tokensFromAnalysis(analyzer, "notAnalyzed", "pass through");
        AnalyzerUtils.assertTokensEqual(tokens, new String[] { "pass through" });
        session.close();
    }

    protected Class<?>[] getAnnotatedClasses() {
        return new Class[] { MyEntity.class, Article.class };
    }
}
