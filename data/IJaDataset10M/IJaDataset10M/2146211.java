package org.expasy.jpl.io.hadoop.ms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.VIntWritable;
import org.expasy.jpl.commons.collection.stat.HistogramDataSet;
import org.expasy.jpl.core.ms.spectrum.stat.PeakHistKey;
import org.expasy.jpl.io.hadoop.ms.hist.PeakHistKeyExtWritable;
import org.expasy.jpl.io.hadoop.ms.hist.reader.PeakHistBinMapFileReader;
import org.expasy.jpl.io.hadoop.ms.hist.writer.PeakHistBinMapFileWriter;
import org.junit.Before;
import org.junit.Test;

public class PeakHistBinMapFileTest {

    Map<PeakHistKey, HistogramDataSet> hists;

    PeakHistBinMapFileWriter writer;

    PeakHistBinMapFileReader reader;

    String uri;

    @Before
    public void setup() {
        uri = "/tmp/hist-bins.hmap";
        hists = new HashMap<PeakHistKey, HistogramDataSet>();
        for (int i = 100; i < 2000; i += 50) {
            hists.put(new PeakHistKey(2, i, i + 50, 0, 0), PeakHistSequenceFileTest.createHistogram(i, i + 50));
        }
        writer = new PeakHistBinMapFileWriter();
        reader = new PeakHistBinMapFileReader();
    }

    @Test
    public void testPrintHisto() {
        List<PeakHistKey> keys = new ArrayList<PeakHistKey>(hists.keySet());
        Collections.sort(keys);
        for (PeakHistKey key : keys) {
            HistogramDataSet histogram = hists.get(key);
            System.out.println(key + " => " + Arrays.toString(histogram.getBins(null)));
        }
    }

    @Test
    public void testWriteHistMapFile() throws IOException {
        writer.createFile(new File(uri));
        List<PeakHistKey> keys = new ArrayList<PeakHistKey>(hists.keySet());
        Collections.sort(keys);
        for (PeakHistKey key : keys) {
            writer.write(key, hists.get(key));
        }
        writer.close();
    }

    @Test
    public void testParseAll() throws IOException {
        reader.parse(new File(uri));
        while (reader.next()) {
            System.out.println(reader.getKey() + "->" + reader.getValue());
        }
        reader.close();
    }

    @Test
    public void testGetFinalKey() throws IOException {
        reader.parse(new File(uri));
        Assert.assertTrue(reader.getFinalKey());
        Assert.assertEquals(1950.0, reader.getKey().getPrecMzFrom().get());
        reader.get(reader.getKey());
        Assert.assertTrue(reader.getValue().get() > 0);
        reader.close();
    }

    @Test
    public void testFoundKey() throws IOException {
        reader.parse(new File(uri));
        PeakHistKeyExtWritable key = new PeakHistKeyExtWritable(new VIntWritable(2), new DoubleWritable(1950), new DoubleWritable(2000), new DoubleWritable(1990), new DoubleWritable(2000), new VIntWritable(0), new VIntWritable(0));
        boolean found = reader.get(key);
        Assert.assertTrue(found);
        reader.close();
    }
}
