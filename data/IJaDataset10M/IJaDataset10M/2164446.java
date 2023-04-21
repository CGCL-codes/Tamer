package org.jcvi.trace.sanger.phd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.jcvi.datastore.DataStoreException;
import org.jcvi.datastore.DataStoreFilter;
import org.jcvi.glyph.encoder.RunLengthEncodedGlyphCodec;
import org.jcvi.glyph.nuc.DefaultNucleotideEncodedGlyphs;
import org.jcvi.glyph.nuc.NucleotideGlyph;
import org.jcvi.glyph.num.ShortGlyph;
import org.jcvi.glyph.phredQuality.DefaultQualityEncodedGlyphs;
import org.jcvi.glyph.phredQuality.PhredQuality;
import org.jcvi.glyph.phredQuality.QualityGlyphCodec;
import org.jcvi.sequence.Peaks;
import org.jcvi.util.CloseableIterator;
import org.jcvi.util.CloseableIteratorAdapter;

public class DefaultPhdFileDataStore extends AbstractPhdFileDataStore {

    private static final QualityGlyphCodec QUALITY_CODEC = RunLengthEncodedGlyphCodec.DEFAULT_INSTANCE;

    private final Map<String, DefaultPhd> map = new HashMap<String, DefaultPhd>();

    @Override
    protected void visitPhd(String id, List<NucleotideGlyph> bases, List<PhredQuality> qualities, List<ShortGlyph> positions, Properties comments, List<PhdTag> tags) {
        map.put(id, new DefaultPhd(id, new DefaultNucleotideEncodedGlyphs(bases), new DefaultQualityEncodedGlyphs(QUALITY_CODEC, qualities), new Peaks(positions), comments, tags));
    }

    /**
     * 
     */
    public DefaultPhdFileDataStore() {
        super();
    }

    /**
     * @param filter
     */
    public DefaultPhdFileDataStore(DataStoreFilter filter) {
        super(filter);
    }

    public DefaultPhdFileDataStore(File phdFile, DataStoreFilter filter) throws FileNotFoundException {
        super(filter);
        PhdParser.parsePhd(phdFile, this);
    }

    public DefaultPhdFileDataStore(File phdFile) throws FileNotFoundException {
        super();
        PhdParser.parsePhd(phdFile, this);
    }

    @Override
    public synchronized boolean contains(String id) throws DataStoreException {
        checkNotYetClosed();
        return map.containsKey(id);
    }

    @Override
    public synchronized Phd get(String id) throws DataStoreException {
        checkNotYetClosed();
        return map.get(id);
    }

    @Override
    public synchronized CloseableIterator<String> getIds() throws DataStoreException {
        checkNotYetClosed();
        return CloseableIteratorAdapter.adapt(map.keySet().iterator());
    }

    @Override
    public synchronized int size() throws DataStoreException {
        checkNotYetClosed();
        return map.size();
    }

    @Override
    public synchronized void close() throws IOException {
        super.close();
        map.clear();
    }
}
