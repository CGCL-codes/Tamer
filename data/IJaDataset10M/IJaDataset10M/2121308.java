package org.jcvi.common.examples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.SortedSet;
import java.util.TreeSet;
import org.jcvi.common.core.datastore.DataStoreException;
import org.jcvi.common.core.io.IOUtil;
import org.jcvi.common.core.seq.fastx.fasta.nt.DefaultNucleotideSequenceFastaRecord;
import org.jcvi.common.core.seq.fastx.fasta.nt.IndexedNucleotideFastaFileDataStore;
import org.jcvi.common.core.seq.fastx.fasta.nt.LargeNucleotideSequenceFastaFileDataStore;
import org.jcvi.common.core.seq.fastx.fasta.nt.NucleotideSequenceFastaDataStore;
import org.jcvi.common.core.seq.fastx.fasta.nt.NucleotideSequenceFastaRecord;
import org.jcvi.common.core.symbol.residue.nt.NucleotideSequence;
import org.jcvi.common.core.symbol.residue.nt.NucleotideSequenceBuilder;
import org.jcvi.common.core.util.iter.CloseableIterator;

public class ReverseComplementFasta {

    public static void main(String[] args) throws FileNotFoundException, DataStoreException {
        File inputFasta = new File("path/to/input/fasta");
        File reverseComplimentOutputFasta = new File("/path/to/output.sorted.fasta");
        NucleotideSequenceFastaDataStore dataStore = LargeNucleotideSequenceFastaFileDataStore.create(inputFasta);
        PrintWriter out = new PrintWriter(reverseComplimentOutputFasta);
        CloseableIterator<NucleotideSequenceFastaRecord> iter = null;
        try {
            iter = dataStore.iterator();
            while (iter.hasNext()) {
                NucleotideSequenceFastaRecord record = iter.next();
                NucleotideSequence reverseSequence = new NucleotideSequenceBuilder(record.getSequence()).reverseComplement().build();
                NucleotideSequenceFastaRecord reverseRecord = new DefaultNucleotideSequenceFastaRecord(record.getId(), record.getComment(), reverseSequence);
                out.print(reverseRecord.toFormattedString());
            }
        } finally {
            IOUtil.closeAndIgnoreErrors(iter, out);
        }
    }
}
