package org.jcvi.common.core.seq.trim.lucy;

import static org.junit.Assert.assertEquals;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.jcvi.common.core.Range;
import org.jcvi.common.core.Range.CoordinateSystem;
import org.jcvi.common.core.datastore.DataStoreException;
import org.jcvi.common.core.seq.fastx.fasta.qual.DefaultQualityFastaFileDataStore;
import org.jcvi.common.core.seq.fastx.fasta.qual.QualityFastaRecordDataStoreAdapter;
import org.jcvi.common.core.seq.trim.lucy.LucyLikeQualityTrimmer;
import org.jcvi.common.core.symbol.Sequence;
import org.jcvi.common.core.symbol.qual.PhredQuality;
import org.jcvi.common.core.symbol.qual.QualityDataStore;
import org.jcvi.common.io.fileServer.ResourceFileServer;
import org.junit.Before;
import org.junit.Test;

/**
 * @author dkatzel
 *
 *
 */
public class TestLucyQualityTrimmer {

    private static final ResourceFileServer RESOURCES = new ResourceFileServer(TestLucyQualityTrimmer.class);

    private QualityDataStore qualities;

    LucyLikeQualityTrimmer sut = new LucyLikeQualityTrimmer.Builder(30).addTrimWindow(30, 0.1F).addTrimWindow(10, 0.35F).build();

    @Before
    public void setup() throws IOException {
        qualities = QualityFastaRecordDataStoreAdapter.adapt(DefaultQualityFastaFileDataStore.create(RESOURCES.getFile("files/fullLength.qual")));
    }

    @Test
    public void SAJJA07T27G07MP1F() throws DataStoreException {
        final Sequence<PhredQuality> fullQualities = qualities.get("SAJJA07T27G07MP1F");
        Range actualTrimRange = sut.trim(fullQualities);
        Range expectedRange = Range.create(CoordinateSystem.RESIDUE_BASED, 12, 679);
        assertEquals(expectedRange, actualTrimRange);
    }

    @Test
    public void SAJJA07T27G07MP675R() throws DataStoreException {
        final Sequence<PhredQuality> fullQualities = qualities.get("SAJJA07T27G07MP675R");
        Range actualTrimRange = sut.trim(fullQualities);
        Range expectedRange = Range.create(CoordinateSystem.RESIDUE_BASED, 16, 680);
        assertEquals(expectedRange, actualTrimRange);
    }

    @Test
    public void noGoodQualityDataShouldReturnEmptyRange() throws FileNotFoundException, IOException, DataStoreException {
        QualityDataStore badQualDataStore = QualityFastaRecordDataStoreAdapter.adapt(DefaultQualityFastaFileDataStore.create(RESOURCES.getFile("files/bad.qual")));
        final Sequence<PhredQuality> badQualities = badQualDataStore.get("SCJIA01T48H08PB26F");
        assertEquals(Range.createEmptyRange(), sut.trim(badQualities));
    }

    @Test
    public void bTrashShouldReturnEmptyRange() throws FileNotFoundException, IOException, DataStoreException {
        QualityDataStore trashQualDataStore = QualityFastaRecordDataStoreAdapter.adapt(DefaultQualityFastaFileDataStore.create(RESOURCES.getFile("files/trash.qual")));
        final Sequence<PhredQuality> trashQualities = trashQualDataStore.get("JBYHA01T19A06PB2A628FB");
        assertEquals(Range.createEmptyRange(), sut.trim(trashQualities));
    }

    @Test
    public void wTrashShouldReturnEmptyRange() throws FileNotFoundException, IOException, DataStoreException {
        QualityDataStore trashQualDataStore = QualityFastaRecordDataStoreAdapter.adapt(DefaultQualityFastaFileDataStore.create(RESOURCES.getFile("files/trash.qual")));
        final Sequence<PhredQuality> trashQualities = trashQualDataStore.get("JBZTB06T19E09NA1F");
        assertEquals(Range.createEmptyRange(), sut.trim(trashQualities));
    }
}
