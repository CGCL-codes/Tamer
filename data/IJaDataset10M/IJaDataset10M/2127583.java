package org.expasy.jpl.msident.model.impl;

import java.io.IOException;
import java.text.ParseException;
import junit.framework.Assert;
import org.expasy.jpl.io.hadoop.ms.HMapMSManager;
import org.expasy.jpl.io.hadoop.ms.HMapMSFileConverter.MS2HMSConvertException;
import org.expasy.jpl.io.ms.MSScan;
import org.expasy.jpl.io.ms.reader.MGFReader;
import org.expasy.jpl.io.ms.reader.MGFReader.TitleParser;
import org.junit.Before;
import org.junit.Test;

public class MSSCandidateTest {

    MSSCandidate msCand;

    HMapMSManager msManager;

    @Before
    public void setup() {
        msManager = HMapMSManager.newInstance();
        MGFReader mgfReader = new MGFReader(new TitleParser() {

            public int parseScanNumber(String title) throws ParseException {
                String[] fields = title.split(" ");
                return Integer.parseInt(fields[2]);
            }
        });
        msManager.resetDelegatedReader(mgfReader);
    }

    @Test
    public void testSetResourceAndLookup() throws MS2HMSConvertException, ParseException, IOException {
        msCand = new MSSCandidate("spectrum 4");
        msCand.setStartScanIndex(955);
        msCand.setEndScanIndex(955);
        msCand.setMSResource("/home/def/Projects/JPL/data/in/all/pepxml/albu-co3.mgf");
        msCand.setHmsManager(msManager);
        MSScan sp = msCand.lookupFirstScan();
        Assert.assertEquals(43, sp.getPeakList().size());
        Assert.assertEquals(122.4958, sp.getPeakList().getMzAt(0), 0.0001);
        Assert.assertEquals(1905.7737, sp.getPeakList().getMzAt(42), 0.0001);
    }

    @Test
    public void testLookupFromSpectrumId() throws MS2HMSConvertException, ParseException, IOException {
        msCand = new MSSCandidate("albu-co3.00955.00955.4", MSSCandidate.SPECTRUM_ID_PARSER);
        msCand.setStartScanIndex(955);
        msCand.setEndScanIndex(955);
        msManager.addResourcePath("/home/def/Projects/JPL/data/in/all/pepxml");
        msCand.setHmsManager(msManager);
        MSScan sp = msCand.lookupFirstScan();
        Assert.assertEquals(43, sp.getPeakList().size());
        Assert.assertEquals(122.4958, sp.getPeakList().getMzAt(0), 0.0001);
        Assert.assertEquals(1905.7737, sp.getPeakList().getMzAt(42), 0.0001);
    }
}
