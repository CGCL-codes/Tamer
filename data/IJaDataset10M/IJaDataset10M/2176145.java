package org.jcvi.common.core.assembly.ace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.jcvi.common.core.assembly.ctg.ContigFileParser;
import org.jcvi.common.core.datastore.DataStore;
import org.jcvi.common.core.datastore.DataStoreException;
import org.jcvi.common.core.datastore.SimpleDataStore;
import org.jcvi.common.core.seq.fastx.FastXRecord;
import org.jcvi.common.core.util.iter.CloseableIterator;

public class DefaultAceAdapterContigFileDataStore extends AbstractAceAdaptedContigFileDataStore implements AceContigDataStore {

    private final Map<String, AceContig> map = new HashMap<String, AceContig>();

    private DataStore<AceContig> dataStore;

    /**
     * @param phdDate
     */
    public DefaultAceAdapterContigFileDataStore(DataStore<? extends FastXRecord> fullLengthFastXDataStore, Date phdDate) {
        super(fullLengthFastXDataStore, phdDate);
    }

    public DefaultAceAdapterContigFileDataStore(DataStore<? extends FastXRecord> fullLengthFastXDataStore, Date phdDate, File contigFile) throws FileNotFoundException {
        this(fullLengthFastXDataStore, phdDate);
        ContigFileParser.parse(contigFile, this);
    }

    @Override
    protected void visitAceContig(AceContig aceContig) {
        map.put(aceContig.getId(), aceContig);
    }

    @Override
    public void visitEndOfFile() {
        super.visitEndOfFile();
        dataStore = new SimpleDataStore<AceContig>(map);
    }

    @Override
    public boolean contains(String id) throws DataStoreException {
        return dataStore.contains(id);
    }

    @Override
    public AceContig get(String id) throws DataStoreException {
        return dataStore.get(id);
    }

    @Override
    public CloseableIterator<String> idIterator() throws DataStoreException {
        return dataStore.idIterator();
    }

    @Override
    public long getNumberOfRecords() throws DataStoreException {
        return dataStore.getNumberOfRecords();
    }

    @Override
    public void close() throws IOException {
        dataStore.close();
    }

    @Override
    public CloseableIterator<AceContig> iterator() throws DataStoreException {
        return dataStore.iterator();
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public boolean isClosed() throws DataStoreException {
        return dataStore.isClosed();
    }
}
