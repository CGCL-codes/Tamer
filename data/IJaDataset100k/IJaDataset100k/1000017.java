package com.compomics.acromics.mapreduce;

import com.compomics.acromics.mapreduce.io.picard.HeadlessSAMFileReaderFactory;
import com.compomics.acromics.mapreduce.io.writables.CoordinateWritable;
import com.compomics.acromics.mapreduce.io.writables.CoordinateWritableArray;
import com.compomics.acromics.mapreduce.io.writables.PeptideWritable;
import com.compomics.acromics.mapreduce.io.writables.SAMWritable;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import org.apache.hadoop.io.Writable;
import java.io.*;
import java.util.ArrayList;

/**
 * Date: 11/02/11
 * Time: 14:11
 */
public class TestWritable extends TestCase {

    private String iTestFileName;

    private String iTestPath;

    private File iWritableTestFile;

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TestWritable.class);
    }

    /**
     * Create TestWritable
     *
     * @param testName name of the test case
     */
    public TestWritable(String testName) {
        super(testName);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        iTestPath = MyTestSuite.getTestResourceURI().getPath();
        iTestFileName = "acromics-test-serialization.writable";
        iWritableTestFile = new File(iTestPath, iTestFileName);
        if (iWritableTestFile.exists()) {
            clean();
        }
        iWritableTestFile.createNewFile();
    }

    private void clean() {
        iWritableTestFile.delete();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        clean();
    }

    /**
     * Main test.
     */
    public void testCoordinateWritable() {
        try {
            int lFileNameHash = iWritableTestFile.getPath().hashCode();
            String lSequence = "TESTPEPTIDE";
            String lChromosome = "1";
            int lStart = 100000;
            int lEnd = lStart + 33;
            boolean lStrand = false;
            CoordinateWritable lPeptideWritable = new CoordinateWritable(lFileNameHash, lSequence);
            lPeptideWritable.setChromosome(lChromosome);
            lPeptideWritable.setStart(lStart);
            lPeptideWritable.setEnd(lEnd);
            lPeptideWritable.setStrand(false);
            DataOutputStream lDataOutputStream = new DataOutputStream(new FileOutputStream(iWritableTestFile.getPath()));
            lPeptideWritable.write(lDataOutputStream);
            lDataOutputStream.flush();
            lDataOutputStream.close();
            lPeptideWritable.setResourceHash(-1);
            lPeptideWritable.setChromosome("-1");
            DataInputStream lDataInputStream = new DataInputStream(new FileInputStream(iWritableTestFile.getPath()));
            lPeptideWritable.readFields(lDataInputStream);
            lDataInputStream.close();
            Assert.assertEquals(lPeptideWritable.getResourceHash(), lFileNameHash);
            Assert.assertEquals(lPeptideWritable.getChromosome().toString(), lChromosome);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main test.
     */
    public void testPeptideWritable() {
        try {
            int lFileNameHash = iWritableTestFile.getPath().hashCode();
            String lSequence = "TESTPEPTIDE";
            String lChromosome = "1";
            int lStart = 100000;
            int lEnd = lStart + 33;
            boolean lStrand = false;
            CoordinateWritable lCoordinateWritable = new CoordinateWritable(lFileNameHash, lSequence);
            lCoordinateWritable.setChromosome(lChromosome);
            lCoordinateWritable.setStart(lStart);
            lCoordinateWritable.setEnd(lEnd);
            lCoordinateWritable.setStrand(false);
            DataOutputStream lDataOutputStream = new DataOutputStream(new FileOutputStream(iWritableTestFile.getPath()));
            lCoordinateWritable.write(lDataOutputStream);
            lDataOutputStream.flush();
            lDataOutputStream.close();
            DataInputStream lDataInputStream = new DataInputStream(new FileInputStream(iWritableTestFile.getPath()));
            CoordinateWritable lCoordinateWritableTwo = new CoordinateWritable();
            lCoordinateWritableTwo.readFields(lDataInputStream);
            lDataInputStream.close();
            Assert.assertEquals(lCoordinateWritableTwo.getFileType(), CoordinateWritable.PEPTIDE_FILE);
            PeptideWritable lPeptideWritable = (PeptideWritable) lCoordinateWritable.getCoordinateExtension();
            Assert.assertEquals(lPeptideWritable.getSequence(), lSequence);
            Assert.assertEquals(lCoordinateWritableTwo.getResourceHash(), lFileNameHash);
            Assert.assertEquals(lCoordinateWritableTwo.getChromosome().toString(), lChromosome);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main test.
     */
    public void testSAMHeadlessWritable() {
        try {
            File lSAMInputFile = new File(Resources.getResource("picard_test_input.sam").getPath());
            int lSAMInputFileNameHash = lSAMInputFile.getPath().hashCode();
            File lSAMHeadlessInputFile = new File(Resources.getResource("picard_test_input_headless.sam").getPath());
            StringBuffer lHeaderbuffer = new StringBuffer();
            BufferedReader br = Files.newReader(lSAMInputFile, Charsets.UTF_8);
            String line = "";
            while ((line = br.readLine()).charAt(0) == '@') {
                lHeaderbuffer.append(line);
                lHeaderbuffer.append("\n");
            }
            SAMFileReader lSAMFileReader;
            lSAMFileReader = HeadlessSAMFileReaderFactory.createHeadlessSAMFileReader(lSAMHeadlessInputFile, lHeaderbuffer.toString());
            ArrayList<CoordinateWritable> lWritableList = new ArrayList<CoordinateWritable>();
            for (Object o : lSAMFileReader) {
                SAMRecord lSAMRecord = (SAMRecord) o;
                CoordinateWritable lWritable = new CoordinateWritable(lSAMInputFileNameHash, lSAMRecord);
                lWritableList.add(lWritable);
            }
            CoordinateWritable[] lWritableArray = new CoordinateWritable[lWritableList.size()];
            lWritableList.toArray(lWritableArray);
            CoordinateWritableArray lCoordinateWritableArray = new CoordinateWritableArray(lWritableArray);
            lWritableArray = null;
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(iWritableTestFile.getPath()));
            lCoordinateWritableArray.write(dos);
            dos.flush();
            dos.close();
            DataInputStream dis = new DataInputStream(new FileInputStream(iWritableTestFile.getPath()));
            lCoordinateWritableArray.readFields(dis);
            dis.close();
            Writable[] lNewWritableArray = lCoordinateWritableArray.getCoordinateWritableArray();
            Assert.assertEquals(lNewWritableArray.length, 8);
            CoordinateWritable lCoordinateWritable = (CoordinateWritable) lNewWritableArray[0];
            Assert.assertEquals(lCoordinateWritable.getFileType(), CoordinateWritable.SAM_FILE);
            SAMWritable lSAMWritable = (SAMWritable) lCoordinateWritable.getCoordinateExtension();
            Assert.assertEquals(lCoordinateWritable.getResourceHash(), lSAMInputFileNameHash);
            Assert.assertEquals(lSAMWritable.getCigar(), "101M");
            lCoordinateWritable = (CoordinateWritable) lNewWritableArray[6];
            lSAMWritable = (SAMWritable) lCoordinateWritable.getCoordinateExtension();
            Assert.assertEquals(lSAMWritable.getCigar(), "10M1D10M5I76M");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main test.
     */
    public void testSAMWritable() {
        try {
            File lSAMInputFile = new File(Resources.getResource("picard_test_input.sam").getPath());
            int lSAMInputFileNameHash = lSAMInputFile.getPath().hashCode();
            SAMFileReader lSAMFileReader = new SAMFileReader(lSAMInputFile);
            ArrayList<CoordinateWritable> lWritableList = new ArrayList<CoordinateWritable>();
            for (Object o : lSAMFileReader) {
                SAMRecord lSAMRecord = (SAMRecord) o;
                CoordinateWritable lWritable = new CoordinateWritable(lSAMInputFileNameHash, lSAMRecord);
                lWritableList.add(lWritable);
            }
            CoordinateWritable[] lWritableArray = new CoordinateWritable[lWritableList.size()];
            lWritableList.toArray(lWritableArray);
            CoordinateWritableArray lCoordinateWritableArray = new CoordinateWritableArray(lWritableArray);
            lWritableArray = null;
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(iWritableTestFile.getPath()));
            lCoordinateWritableArray.write(dos);
            dos.flush();
            dos.close();
            DataInputStream dis = new DataInputStream(new FileInputStream(iWritableTestFile.getPath()));
            lCoordinateWritableArray.readFields(dis);
            dis.close();
            Writable[] lNewWritableArray = lCoordinateWritableArray.getCoordinateWritableArray();
            Assert.assertEquals(lNewWritableArray.length, 10);
            CoordinateWritable lCoordinateWritable = (CoordinateWritable) lNewWritableArray[0];
            Assert.assertEquals(lCoordinateWritable.getFileType(), CoordinateWritable.SAM_FILE);
            SAMWritable lSAMWritable = (SAMWritable) lCoordinateWritable.getCoordinateExtension();
            Assert.assertEquals(lCoordinateWritable.getResourceHash(), lSAMInputFileNameHash);
            Assert.assertEquals(lCoordinateWritable.getStart(), 1);
            Assert.assertEquals(lSAMWritable.getCigar(), "101M");
            lCoordinateWritable = (CoordinateWritable) lNewWritableArray[6];
            lSAMWritable = (SAMWritable) lCoordinateWritable.getCoordinateExtension();
            Assert.assertEquals(lSAMWritable.getCigar(), "10M1D10M5I76M");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
