package org.hibernate.search.test.query.dsl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.NGramFilterFactory;
import org.apache.solr.analysis.SnowballPorterFilterFactory;
import org.apache.solr.analysis.StandardFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.apache.solr.analysis.StopFilterFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.search.Environment;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.cfg.SearchMapping;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.test.SearchTestCase;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
public class DSLTest extends SearchTestCase {

    FullTextSession fullTextSession;

    public void setUp() throws Exception {
        super.setUp();
        Session session = openSession();
        fullTextSession = Search.getFullTextSession(session);
        indexTestData();
    }

    public void tearDown() throws Exception {
        cleanUpTestData();
        super.tearDown();
    }

    public void testUseOfFieldBridge() throws Exception {
        Transaction transaction = fullTextSession.beginTransaction();
        final QueryBuilder monthQb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Month.class).get();
        Query query = monthQb.keyword().onField("monthValue").matching(2).createQuery();
        assertEquals(1, fullTextSession.createFullTextQuery(query, Month.class).getResultSize());
        query = monthQb.keyword().onField("monthValue").ignoreFieldBridge().matching("2").createQuery();
        assertEquals(1, fullTextSession.createFullTextQuery(query, Month.class).getResultSize());
        transaction.commit();
    }

    public void testTermQueryOnAnalyzer() throws Exception {
        Transaction transaction = fullTextSession.beginTransaction();
        final QueryBuilder monthQb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Month.class).get();
        Query query = monthQb.keyword().onField("mythology").matching("cold").createQuery();
        assertEquals(0, fullTextSession.createFullTextQuery(query, Month.class).getResultSize());
        query = monthQb.keyword().onField("mythology").matching("colder darker").createQuery();
        assertEquals(1, fullTextSession.createFullTextQuery(query, Month.class).getResultSize());
        query = monthQb.keyword().onField("mythology_stem").matching("snowboard").createQuery();
        assertEquals(1, fullTextSession.createFullTextQuery(query, Month.class).getResultSize());
        query = monthQb.keyword().onField("mythology_ngram").matching("snobored").createQuery();
        assertEquals(1, fullTextSession.createFullTextQuery(query, Month.class).getResultSize());
        query = monthQb.keyword().onField("mythology").ignoreAnalyzer().matching("Month").createQuery();
        assertEquals(0, fullTextSession.createFullTextQuery(query, Month.class).getResultSize());
        transaction.commit();
    }

    public void testFuzzyAndWildcardQuery() throws Exception {
        Transaction transaction = fullTextSession.beginTransaction();
        final QueryBuilder monthQb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Month.class).get();
        Query query = monthQb.keyword().fuzzy().withThreshold(.8f).withPrefixLength(1).onField("mythology").matching("calder").createQuery();
        assertEquals(1, fullTextSession.createFullTextQuery(query, Month.class).getResultSize());
        query = monthQb.keyword().wildcard().onField("mythology").matching("mon*").createQuery();
        assertEquals(2, fullTextSession.createFullTextQuery(query, Month.class).getResultSize());
        transaction.commit();
    }

    @SuppressWarnings("unchecked")
    public void testQueryCustomization() throws Exception {
        Transaction transaction = fullTextSession.beginTransaction();
        final QueryBuilder monthQb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Month.class).get();
        Query query = monthQb.bool().should(monthQb.keyword().onField("mythology").matching("whitening").createQuery()).should(monthQb.keyword().onField("history").matching("whitening").createQuery()).createQuery();
        List<Month> results = fullTextSession.createFullTextQuery(query, Month.class).list();
        assertEquals(2, results.size());
        assertEquals("January", results.get(0).getName());
        query = monthQb.bool().should(monthQb.keyword().onField("mythology").matching("whitening").createQuery()).should(monthQb.keyword().onField("history").boostedTo(30).matching("whitening").createQuery()).createQuery();
        results = fullTextSession.createFullTextQuery(query, Month.class).list();
        assertEquals(2, results.size());
        assertEquals("February", results.get(0).getName());
        transaction.commit();
    }

    @SuppressWarnings("unchecked")
    public void testMultipleFields() throws Exception {
        Transaction transaction = fullTextSession.beginTransaction();
        final QueryBuilder monthQb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Month.class).get();
        Query query = monthQb.keyword().onField("mythology").andField("history").matching("whitening").createQuery();
        List<Month> results = fullTextSession.createFullTextQuery(query, Month.class).list();
        assertEquals(2, results.size());
        assertEquals("January", results.get(0).getName());
        query = monthQb.keyword().onFields("mythology", "history").boostedTo(30).matching("whitening").createQuery();
        results = fullTextSession.createFullTextQuery(query, Month.class).list();
        assertEquals(2, results.size());
        assertEquals("January", results.get(0).getName());
        query = monthQb.keyword().onField("mythology").andField("history").boostedTo(30).matching("whitening").createQuery();
        results = fullTextSession.createFullTextQuery(query, Month.class).list();
        assertEquals(2, results.size());
        assertEquals("February", results.get(0).getName());
        transaction.commit();
    }

    @SuppressWarnings("unchecked")
    public void testBoolean() throws Exception {
        Transaction transaction = fullTextSession.beginTransaction();
        final QueryBuilder monthQb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Month.class).get();
        Query query = monthQb.bool().must(monthQb.keyword().onField("mythology").matching("colder").createQuery()).createQuery();
        List<Month> results = fullTextSession.createFullTextQuery(query, Month.class).list();
        assertEquals(1, results.size());
        assertEquals("January", results.get(0).getName());
        query = monthQb.bool().should(monthQb.all().createQuery()).must(monthQb.keyword().onField("mythology").matching("colder").createQuery()).not().createQuery();
        results = fullTextSession.createFullTextQuery(query, Month.class).list();
        assertEquals(1, results.size());
        assertEquals("February", results.get(0).getName());
        query = monthQb.bool().must(monthQb.keyword().onField("mythology").matching("colder").createQuery()).not().createQuery();
        results = fullTextSession.createFullTextQuery(query, Month.class).list();
        assertEquals(1, results.size());
        assertEquals("February", results.get(0).getName());
        query = monthQb.all().except(monthQb.keyword().onField("mythology").matching("colder").createQuery()).createQuery();
        results = fullTextSession.createFullTextQuery(query, Month.class).list();
        assertEquals(1, results.size());
        assertEquals("February", results.get(0).getName());
        transaction.commit();
    }

    public void testRangeQuery() throws Exception {
        Transaction transaction = fullTextSession.beginTransaction();
        final QueryBuilder monthQb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Month.class).get();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(0 + 1900, 2, 12, 0, 0, 0);
        Date from = calendar.getTime();
        calendar.set(10 + 1900, 2, 12, 0, 0, 0);
        Date to = calendar.getTime();
        Query query = monthQb.range().onField("estimatedCreation").andField("justfortest").ignoreFieldBridge().ignoreAnalyzer().from(from).to(to).excludeLimit().createQuery();
        assertEquals(1, fullTextSession.createFullTextQuery(query, Month.class).getResultSize());
        query = monthQb.range().onField("estimatedCreation").ignoreFieldBridge().andField("justfortest").ignoreFieldBridge().ignoreAnalyzer().from(DateTools.dateToString(from, DateTools.Resolution.MINUTE)).to(DateTools.dateToString(to, DateTools.Resolution.MINUTE)).excludeLimit().createQuery();
        assertEquals(1, fullTextSession.createFullTextQuery(query, Month.class).getResultSize());
        query = monthQb.range().onField("estimatedCreation").andField("justfortest").ignoreFieldBridge().ignoreAnalyzer().below(to).createQuery();
        FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query, Month.class);
        assertEquals(1, hibQuery.getResultSize());
        assertEquals("January", ((Month) hibQuery.list().get(0)).getName());
        query = monthQb.range().onField("estimatedCreation").ignoreFieldBridge().andField("justfortest").ignoreFieldBridge().ignoreAnalyzer().below(DateTools.dateToString(to, DateTools.Resolution.MINUTE)).createQuery();
        hibQuery = fullTextSession.createFullTextQuery(query, Month.class);
        assertEquals(1, hibQuery.getResultSize());
        assertEquals("January", ((Month) hibQuery.list().get(0)).getName());
        query = monthQb.range().onField("estimatedCreation").andField("justfortest").ignoreFieldBridge().ignoreAnalyzer().above(to).createQuery();
        hibQuery = fullTextSession.createFullTextQuery(query, Month.class);
        assertEquals(1, hibQuery.getResultSize());
        assertEquals("February", ((Month) hibQuery.list().get(0)).getName());
        query = monthQb.range().onField("estimatedCreation").ignoreFieldBridge().andField("justfortest").ignoreFieldBridge().ignoreAnalyzer().above(DateTools.dateToString(to, DateTools.Resolution.MINUTE)).createQuery();
        hibQuery = fullTextSession.createFullTextQuery(query, Month.class);
        assertEquals(1, hibQuery.getResultSize());
        assertEquals("February", ((Month) hibQuery.list().get(0)).getName());
        transaction.commit();
    }

    public void testPhraseQuery() throws Exception {
        Transaction transaction = fullTextSession.beginTransaction();
        final QueryBuilder monthQb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Month.class).get();
        Query query = monthQb.phrase().onField("mythology").sentence("colder and whitening").createQuery();
        assertEquals("test exact phrase", 1, fullTextSession.createFullTextQuery(query, Month.class).getResultSize());
        query = monthQb.phrase().onField("mythology").sentence("Month whitening").createQuery();
        assertEquals("test slop", 0, fullTextSession.createFullTextQuery(query, Month.class).getResultSize());
        query = monthQb.phrase().withSlop(3).onField("mythology").sentence("Month whitening").createQuery();
        assertEquals("test slop", 1, fullTextSession.createFullTextQuery(query, Month.class).getResultSize());
        query = monthQb.phrase().onField("mythology").sentence("whitening").createQuery();
        assertEquals("test one term optimization", 1, fullTextSession.createFullTextQuery(query, Month.class).getResultSize());
        transaction.commit();
    }

    public void testNumericRangeQueries() {
        Transaction transaction = fullTextSession.beginTransaction();
        final QueryBuilder monthQb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Month.class).get();
        Query query = monthQb.range().onField("raindropInMm").from(0.23d).to(0.24d).createQuery();
        assertTrue(query.getClass().isAssignableFrom(NumericRangeQuery.class));
        List results = fullTextSession.createFullTextQuery(query, Month.class).list();
        assertEquals("test range numeric ", 1, results.size());
        assertEquals("test range numeric ", "January", ((Month) results.get(0)).getName());
        transaction.commit();
    }

    public void testNumericFieldsTermQuery() {
        Transaction transaction = fullTextSession.beginTransaction();
        final QueryBuilder monthQb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Month.class).get();
        Query query = monthQb.keyword().onField("raindropInMm").matching(0.231d).createQuery();
        assertTrue(query.getClass().isAssignableFrom(NumericRangeQuery.class));
        assertEquals("test term numeric ", 1, fullTextSession.createFullTextQuery(query, Month.class).getResultSize());
        transaction.commit();
    }

    public void testFieldBridge() {
        Transaction transaction = fullTextSession.beginTransaction();
        final QueryBuilder monthQb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Month.class).get();
        Query query = monthQb.keyword().onField("monthRomanNumber").matching(2).createQuery();
        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(query, Month.class);
        List results = fullTextQuery.list();
        assertEquals(1, results.size());
        Month february = (Month) results.get(0);
        assertEquals(2, february.getMonthValue());
        transaction.commit();
    }

    private void indexTestData() {
        Transaction tx = fullTextSession.beginTransaction();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(0 + 1900, 2, 12, 0, 0, 0);
        fullTextSession.persist(new Month("January", 1, "Month of colder and whitening", "Historically colder than any other month in the northern hemisphere", calendar.getTime(), 0.231d));
        calendar.set(100 + 1900, 2, 12, 0, 0, 0);
        fullTextSession.persist(new Month("February", 2, "Month of snowboarding", "Historically, the month where we make babies while watching the whitening landscape", calendar.getTime(), 0.435d));
        tx.commit();
        fullTextSession.clear();
    }

    private void cleanUpTestData() {
        if (!fullTextSession.isOpen()) {
            return;
        }
        Transaction tx = fullTextSession.beginTransaction();
        final List<Month> results = fullTextSession.createQuery("from " + Month.class.getName()).list();
        assertEquals(2, results.size());
        for (Month entity : results) {
            fullTextSession.delete(entity);
        }
        tx.commit();
        fullTextSession.close();
    }

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[] { Month.class };
    }

    @Override
    protected void configure(Configuration cfg) {
        super.configure(cfg);
        cfg.getProperties().put(Environment.MODEL_MAPPING, MappingFactory.class.getName());
    }

    public static class MappingFactory {

        @Factory
        public SearchMapping build() {
            SearchMapping mapping = new SearchMapping();
            mapping.analyzerDef("stemmer", StandardTokenizerFactory.class).filter(StandardFilterFactory.class).filter(LowerCaseFilterFactory.class).filter(StopFilterFactory.class).filter(SnowballPorterFilterFactory.class).param("language", "English").analyzerDef("ngram", StandardTokenizerFactory.class).filter(StandardFilterFactory.class).filter(LowerCaseFilterFactory.class).filter(StopFilterFactory.class).filter(NGramFilterFactory.class).param("minGramSize", "3").param("maxGramSize", "3");
            return mapping;
        }
    }
}
