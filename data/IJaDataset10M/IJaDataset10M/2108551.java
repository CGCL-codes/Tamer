package org.jcvi.common.core.assembly.tasm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jcvi.common.core.datastore.DataStoreException;
import org.jcvi.common.core.util.iter.CloseableIterator;
import org.jcvi.common.core.util.iter.CloseableIteratorAdapter;

/**
 * {@code DefaultTigrAssemblerFileContigDataStore} is an implemenation
 * of {@link AbstractTigrAssemblerFileContigDataStore} that stores
 * all TIGR Assembler contigs in a HashMap, This may take up a lot 
 * of memory if the contigs are large or if there are many contigs.
 * @author dkatzel
 *
 *
 */
public class DefaultTigrAssemblerFileContigDataStore extends AbstractTigrAssemblerFileContigDataStore {

    private Map<String, TigrAssemblerContig> contigs;

    public DefaultTigrAssemblerFileContigDataStore(File tasmFile) throws FileNotFoundException {
        super(tasmFile);
    }

    @Override
    protected void handleClose() throws IOException {
        contigs.clear();
    }

    @Override
    public synchronized TigrAssemblerContig get(String id) throws DataStoreException {
        super.get(id);
        return contigs.get(id);
    }

    @Override
    public synchronized CloseableIterator<String> idIterator() throws DataStoreException {
        super.idIterator();
        return CloseableIteratorAdapter.adapt(contigs.keySet().iterator());
    }

    @Override
    public synchronized CloseableIterator<TigrAssemblerContig> iterator() {
        super.iterator();
        return CloseableIteratorAdapter.adapt(contigs.values().iterator());
    }

    @Override
    public synchronized long getNumberOfRecords() throws DataStoreException {
        super.getNumberOfRecords();
        return contigs.size();
    }

    /**
    * {@inheritDoc}
    */
    @Override
    protected void visitContig(TigrAssemblerContig contig) {
        contigs.put(contig.getId(), contig);
    }

    /**
    * Initialize contig Map.
    */
    @Override
    protected void initialize(File tasmFile) {
        contigs = new LinkedHashMap<String, TigrAssemblerContig>();
    }
}
