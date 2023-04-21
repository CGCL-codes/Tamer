package org.jcvi.fasta.fastq;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.jcvi.datastore.DataStoreException;
import org.jcvi.glyph.nuc.NucleotideGlyph;
import org.jcvi.glyph.nuc.datastore.H2NucleotideDataStore;
import org.jcvi.io.fileServer.ResourceFileServer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author dkatzel
 *
 *
 */
public class TestH2NucleotideFastQDataStore {

    String filepath = "files/example.fastq";

    ResourceFileServer resources = new ResourceFileServer(TestDefaultFastQFileDataStore.class);

    private H2NucleotideFastQDataStore sut;

    @Before
    public void setup() throws IOException, DataStoreException {
        File fastqFile = resources.getFile(filepath);
        final H2NucleotideDataStore datastore = new H2NucleotideDataStore();
        sut = createSUT(fastqFile, datastore);
    }

    protected H2NucleotideFastQDataStore createSUT(File fastQFile, H2NucleotideDataStore datastore) throws FileNotFoundException {
        return new H2NucleotideFastQDataStore(fastQFile, datastore);
    }

    protected H2NucleotideFastQDataStore getSut() {
        return sut;
    }

    @Test
    public void get() throws DataStoreException {
        assertEquals("TATTTAAAATCTAATANGTCTTGATTTGAAATTGAAAGAGCAAAAATCTGATTGATTTTATTGAAGAATAATTTGATTTAATATATTCTTAAGTCTGTTT", NucleotideGlyph.convertToString(sut.get("SOLEXA1:4:1:12:1489#0/1").decode()));
    }
}
