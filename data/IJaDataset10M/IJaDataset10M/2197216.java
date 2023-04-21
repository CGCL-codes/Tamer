package uk.ac.ebi.rhea.mapper.util.lucene;

import java.util.Collection;
import java.util.Properties;
import uk.ac.ebi.rhea.mapper.util.ChebiSearchResult;
import junit.framework.TestCase;

public class CompoundNameLuceneSearcherTest extends TestCase {

    private CompoundNameLuceneSearcher luceneSearcher1;

    private CompoundNameLuceneSearcher luceneSearcher999;

    protected void setUp() throws Exception {
        Properties chebiIndexProps = new Properties();
        chebiIndexProps.load(this.getClass().getClassLoader().getResourceAsStream("chebi-index-public.properties"));
        final String CHEBI_INDEX = chebiIndexProps.getProperty("chebi.index.core.dir");
        luceneSearcher1 = new CompoundNameLuceneSearcher(CHEBI_INDEX, 1);
        luceneSearcher999 = new CompoundNameLuceneSearcher(CHEBI_INDEX, 999);
    }

    protected void tearDown() throws Exception {
        if (luceneSearcher1 != null) luceneSearcher1.close();
        if (luceneSearcher999 != null) luceneSearcher999.close();
    }

    public void testResultsSize() throws Exception {
        Collection<ChebiSearchResult> results = luceneSearcher1.searchCompoundName("acid");
        assertTrue(results.size() == 1);
        results = luceneSearcher999.searchCompoundName("acid");
        assertTrue(results.size() > 1);
    }
}
