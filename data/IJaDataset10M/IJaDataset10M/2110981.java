package org.jcvi.glyph.nuc.datastore;

import java.io.IOException;
import org.jcvi.datastore.DataStore;
import org.jcvi.datastore.DataStoreException;
import org.jcvi.glyph.nuc.NucleotideDataStore;
import org.jcvi.glyph.nuc.NucleotideEncodedGlyphs;
import org.jcvi.util.CloseableIterator;

/**
 * A <code>NucleotideDataStoreAdapter</code> adapts the heavily parameterized
 * <code>{@link DataStore}&lt;{@link NucleotideEncodedGlyphs}&gt;</code>
 * interface to its simplified equivalent {@link NucleotideDataStore}.
 *
 * @author jsitz@jcvi.org
 */
public class NucleotideDataStoreAdapter implements NucleotideDataStore {

    /** The datastore being wrapped and adapted. */
    private final DataStore<NucleotideEncodedGlyphs> datastore;

    /**
     * Constructs a new <code>NucleotideDataStoreAdapter</code>.
     *
     * @param datastore The {@link DataStore} being wrapped and adapted.
     */
    public NucleotideDataStoreAdapter(DataStore<NucleotideEncodedGlyphs> datastore) {
        super();
        this.datastore = datastore;
    }

    @Override
    public boolean contains(String id) throws DataStoreException {
        return this.datastore.contains(id);
    }

    @Override
    public NucleotideEncodedGlyphs get(String id) throws DataStoreException {
        return this.datastore.get(id);
    }

    @Override
    public CloseableIterator<String> getIds() throws DataStoreException {
        return datastore.getIds();
    }

    @Override
    public int size() throws DataStoreException {
        return this.datastore.size();
    }

    @Override
    public void close() throws IOException {
        this.datastore.close();
    }

    @Override
    public CloseableIterator<NucleotideEncodedGlyphs> iterator() {
        return this.datastore.iterator();
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public boolean isClosed() throws DataStoreException {
        return datastore.isClosed();
    }
}
