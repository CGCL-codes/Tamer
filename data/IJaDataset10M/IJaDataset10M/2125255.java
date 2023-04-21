package net.sourceforge.seqware.pipeline.util.conversion;

import static org.testng.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import net.sf.samtools.SAMRecord;
import org.testng.annotations.Test;

/**
 * Unit tests for {@code SamReadPairReader}
 * 
 * @author lmose
 */
public class SamReadPairReaderTest {

    private static final String TEST1_SAM_FILE = "java/test/net/sourceforge/seqware/pipeline/util/conversion/testdata/test1.sam";

    @Test(groups = "unit")
    public void testReadBasicSam() {
        SamReadPairReader reader = new SamReadPairReader(TEST1_SAM_FILE);
        List<ReadPair> readPairs = new ArrayList<ReadPair>();
        for (ReadPair readPair : reader) {
            readPairs.add(readPair);
        }
        assertEquals(3, readPairs.size());
        verifyRead(readPairs.get(0).getRead1(), "UNC11-SN627_70:1:1101:1075:187685/1", "chrM", 8610, "50M", "chrM", 8671);
        verifyRead(readPairs.get(0).getRead2(), "UNC11-SN627_70:1:1101:1075:187685/2", "chrM", 8671, "50M", "chrM", 8610);
        verifyRead(readPairs.get(1).getRead1(), "UNC11-SN627_70:1:1101:4231:57374/1", "chr1", 144829336, "50M", "chr1", 144829414);
        verifyRead(readPairs.get(1).getRead2(), "UNC11-SN627_70:1:1101:4231:57374/2", "chr1", 144829414, "50M", "chr1", 144829336);
        verifyRead(readPairs.get(2).getRead1(), "UNC11-SN627_70:1:1101:4231:57374/2", "chr1", 146033486, "50M", "chr1", 146033564);
        verifyRead(readPairs.get(2).getRead2(), "UNC11-SN627_70:1:1101:4231:57374/1", "chr1", 146033564, "50M", "chr1", 146033486);
    }

    private void verifyRead(SAMRecord read, String name, String ref, int position, String cigar, String mateRef, int matePosition) {
        assertEquals(name, read.getReadName());
        assertEquals(ref, read.getReferenceName());
        assertEquals(position, read.getAlignmentStart());
        assertEquals(cigar, read.getCigarString());
        assertEquals(mateRef, read.getMateReferenceName());
        assertEquals(matePosition, read.getMateAlignmentStart());
    }
}
