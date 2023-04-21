package net.sf.plexian.indexator.lucene;

import java.util.Map;
import java.util.Set;
import net.sf.plexian.indexator.AbstractIndexator;
import net.sf.plexian.utils.PlexianUtils;

public class LuceneIndexator extends AbstractIndexator {

    public static final int BATCH_SIZE = 10000;

    private LuceneIndexProvider luceneIndexProvider;

    private int batchSize = BATCH_SIZE;

    private boolean useAlfaFilter = true;

    public void setLuceneIndexProvider(LuceneIndexProvider luceneIndexProvider) {
        this.luceneIndexProvider = luceneIndexProvider;
    }

    public void setFields(Map<String, String> paramFields) {
        for (String plexianField : paramFields.keySet()) {
            Map<String, Set<String>> docs = PlexianUtils.parseObjectsFieldsExpression(paramFields.get(plexianField));
            LuceneFieldIndexator indexator = new LuceneFieldIndexator(docs, luceneIndexProvider, batchSize, useAlfaFilter);
            indexator.setField(plexianField);
            indexator.setPlexian(plexian);
            indexators.add(indexator);
        }
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void setUseAlfaFilter(boolean useAlfaFilter) {
        this.useAlfaFilter = useAlfaFilter;
    }
}
