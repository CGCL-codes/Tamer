package org.jcvi.fasta.fastq;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jcvi.datastore.DataStore;
import org.jcvi.datastore.DataStoreException;

public class DefaultFastQDataStore<T extends FastQRecord> implements DataStore<T> {

    private final Map<String, T> map;

    private boolean closed = false;

    private void checkNotClosed() throws DataStoreException {
        if (closed) {
            throw new DataStoreException("can not access closed dataStore");
        }
    }

    /**
     * @param map
     */
    private DefaultFastQDataStore(Map<String, T> map) {
        this.map = map;
    }

    @Override
    public boolean contains(String id) throws DataStoreException {
        checkNotClosed();
        return map.containsKey(id);
    }

    @Override
    public T get(String id) throws DataStoreException {
        checkNotClosed();
        return map.get(id);
    }

    @Override
    public int size() throws DataStoreException {
        checkNotClosed();
        return map.size();
    }

    @Override
    public Iterator<String> getIds() throws DataStoreException {
        checkNotClosed();
        return map.keySet().iterator();
    }

    @Override
    public void close() throws IOException {
        closed = true;
        map.clear();
    }

    @Override
    public Iterator<T> iterator() {
        try {
            checkNotClosed();
        } catch (DataStoreException e) {
            throw new IllegalStateException("could not create iterator", e);
        }
        return map.values().iterator();
    }

    public static class Builder<T extends FastQRecord> implements org.jcvi.Builder<DefaultFastQDataStore> {

        private final Map<String, T> map = new HashMap<String, T>();

        public Builder put(T fastQRecord) {
            map.put(fastQRecord.getId(), fastQRecord);
            return this;
        }

        public Builder remove(T fastQRecord) {
            map.remove(fastQRecord.getId());
            return this;
        }

        public Builder putAll(Collection<T> fastQRecords) {
            for (T fastQRecord : fastQRecords) {
                put(fastQRecord);
            }
            return this;
        }

        public Builder removeAll(Collection<T> fastQRecords) {
            for (T fastQRecord : fastQRecords) {
                remove(fastQRecord);
            }
            return this;
        }

        @Override
        public DefaultFastQDataStore build() {
            return new DefaultFastQDataStore<T>(map);
        }
    }
}
