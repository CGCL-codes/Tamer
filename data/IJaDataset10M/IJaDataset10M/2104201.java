package org.kablink.teaming.search.local;

import java.util.Collections;
import java.util.Map;
import org.kablink.teaming.lucene.LuceneProviderManager;
import org.kablink.teaming.search.AbstractLuceneSessionFactory;
import org.kablink.teaming.search.LuceneReadSession;
import org.kablink.teaming.search.LuceneWriteSession;

public class LocalLuceneSessionFactory extends AbstractLuceneSessionFactory implements LocalLuceneSessionFactoryMBean {

    private LuceneProviderManager luceneProviderManager;

    public LuceneProviderManager getLuceneProviderManager() {
        return luceneProviderManager;
    }

    public void setLuceneProviderManager(LuceneProviderManager luceneProviderManager) {
        this.luceneProviderManager = luceneProviderManager;
    }

    public LuceneReadSession openReadSession(String indexName) {
        return new LocalLuceneReadSession(luceneProviderManager.getProvider(indexName));
    }

    public LuceneWriteSession openWriteSession(String indexName) {
        return new LocalLuceneWriteSession(luceneProviderManager.getProvider(indexName));
    }

    public Map<String, String> getDisplayProperties() {
        return Collections.EMPTY_MAP;
    }
}
