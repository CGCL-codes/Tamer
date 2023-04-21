package net.sf.plexian.indexator.lucene;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import net.sf.plexian.IndexType;
import net.sf.plexian.PlexianConstants;
import net.sf.plexian.indexator.AbstractFieldIndexator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;

public class LuceneFieldIndexator extends AbstractFieldIndexator {

    private final Log log = LogFactory.getLog(getClass());

    private static final String ALFA = "0123456789-=+_.,:;";

    private Map<String, Set<String>> luceneDocs;

    private LuceneIndexProvider luceneIndexProvider;

    private int batchSize;

    private boolean useAlfaFilter;

    LuceneFieldIndexator(Map<String, Set<String>> docs, LuceneIndexProvider luceneIndexProvider, int batchSize, boolean useAlfaFilter) {
        this.luceneDocs = docs;
        this.luceneIndexProvider = luceneIndexProvider;
        this.batchSize = batchSize;
        this.useAlfaFilter = useAlfaFilter;
    }

    @Override
    protected void runInternal() {
        plexian.remove(field);
        plexian.setBatchSize(0);
        for (String doc : luceneDocs.keySet()) {
            try {
                indexDoc(doc, luceneDocs.get(doc));
            } catch (Exception e) {
                log.error("indexator fail", e);
            }
        }
        plexian.setBatchSize(PlexianConstants.DEFAULT_BATCH_SIZE);
    }

    @SuppressWarnings("unchecked")
    public TermsResult getTerms(final Term startTerm, final String doc, final Set<String> fields, final int maxcnt) throws IOException {
        return (TermsResult) luceneIndexProvider.doInIndex(doc, new IndexReaderAware() {

            public TermsResult invoke(IndexReader reader) throws IOException {
                TermsResult result = new TermsResult();
                result.freqs = new int[maxcnt];
                result.texts = new String[maxcnt];
                result.fetched = 0;
                TermEnum termEnum = startTerm != null ? reader.terms(startTerm) : reader.terms();
                while (termEnum.next()) {
                    if (termEnum.term() != startTerm && fields.contains(termEnum.term().field())) {
                        result.texts[result.fetched] = termEnum.term().text();
                        result.freqs[result.fetched] = termEnum.docFreq();
                        result.fetched++;
                        result.lastTerm = termEnum.term();
                        if (result.fetched == maxcnt) {
                            break;
                        }
                    }
                }
                return result;
            }
        });
    }

    private void indexDoc(String doc, Set<String> fields) throws IOException {
        Term lastTerm = null;
        while (true) {
            TermsResult result = getTerms(lastTerm, doc, fields, batchSize);
            for (int i = 0; i < result.fetched; ++i) {
                boolean add = true;
                if (useAlfaFilter) {
                    if (isAllAlfa(result.texts[i])) {
                        add = false;
                    }
                }
                if (add) {
                    plexian.index(field, result.texts[i], IndexType.ADD_FREQ, result.freqs[i]);
                }
            }
            plexian.flush(field);
            if (result.fetched < batchSize) {
                break;
            }
            lastTerm = result.lastTerm;
        }
    }

    private boolean isAllAlfa(String str) {
        for (int i = 0; i < str.length(); ++i) {
            if (ALFA.indexOf(str.charAt(i)) == -1) {
                return false;
            }
        }
        return true;
    }

    public static class TermsResult {

        public Term lastTerm;

        public int fetched;

        public String[] texts;

        public int[] freqs;
    }
}
