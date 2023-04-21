package org.jcvi.common.core.seq.fastx.fasta.pos;

import java.io.File;
import org.jcvi.common.core.datastore.DataStore;
import org.jcvi.common.core.seq.fastx.fasta.pos.LargePositionFastaFileDataStore;
import org.jcvi.common.core.seq.fastx.fasta.pos.PositionSequenceFastaRecord;
import org.jcvi.common.core.symbol.Sequence;
import org.jcvi.common.core.symbol.ShortSymbol;

public class TestLargePositionsFastaDataStore extends AbstractTestPositionsFastaDataStore {

    @Override
    protected DataStore<PositionSequenceFastaRecord<Sequence<ShortSymbol>>> createPositionFastaMap(File fastaFile) throws Exception {
        return LargePositionFastaFileDataStore.create(fastaFile);
    }
}
