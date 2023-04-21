package org.expasy.jpl.io.ms;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import junit.framework.Assert;
import org.apache.commons.collections15.Transformer;
import org.expasy.jpl.commons.base.cond.Condition;
import org.expasy.jpl.commons.base.cond.ConditionImpl;
import org.expasy.jpl.commons.base.mem.ObjectSizeEstimator;
import org.expasy.jpl.commons.base.task.TerminalProgressBar;
import org.expasy.jpl.commons.collection.ExtraIterable.AbstractExtraIterator;
import org.expasy.jpl.core.ms.spectrum.PeakList;
import org.expasy.jpl.core.ms.spectrum.PeakListRunStats;
import org.expasy.jpl.core.ms.spectrum.peak.Peak;
import org.expasy.jpl.io.AbstractExtraIterableReader;
import org.expasy.jpl.io.ms.reader.MZXMLReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JPLMZXMLReaderTest {

    private static String filename = ClassLoader.getSystemResource("threeSpectra.mzXML").getFile();

    static File file;

    MZXMLReader reader;

    ObjectSizeEstimator memoCalc;

    @Before
    public void init() throws ParseException {
        file = new File(filename);
        reader = MZXMLReader.newInstance();
        memoCalc = ObjectSizeEstimator.newInstance();
        System.out.print(Calendar.getInstance().getTime() + ",  " + filename + "\n");
    }

    @After
    public void exit() {
        System.out.print(Calendar.getInstance().getTime() + "\n");
    }

    private static Condition<MSScan> msLevelFilter(int level) {
        Transformer<MSScan, Integer> sp2level = new Transformer<MSScan, Integer>() {

            public Integer transform(MSScan object) {
                return object.getPeakList().getMSLevel();
            }
        };
        return new ConditionImpl.Builder<MSScan, Integer>(level).accessor(sp2level).build();
    }

    @Test
    public void testParseWithPB() throws ParseException {
        reader.parse(file);
        TerminalProgressBar pb = TerminalProgressBar.newInstance(0, reader.getExperimentInfos().getScanCount());
        pb.setBarLength(10);
        reader.setProgressBar(pb);
        AbstractExtraIterator<MSScan> it = reader.iterator();
        while (it.hasNext()) {
            it.next();
        }
    }

    @Test
    public void testSeek() throws ParseException, IOException {
        reader.parse(file);
        MSScan sp = reader.seek(3);
        Assert.assertEquals(3, sp.getScanNum());
        Assert.assertEquals(2, sp.getParentScanNum());
        PeakList pl = sp.getPeakList();
        Assert.assertEquals(193, pl.size());
    }

    @Test(expected = IOException.class)
    public void testInvalidSeek() throws ParseException, IOException {
        reader.parse(file);
        reader.seek(0);
    }

    @Test(expected = IOException.class)
    public void testInvalidSeek2() throws ParseException, IOException {
        reader.parse(file);
        reader.seek(22);
    }

    @Test
    public void testShortNext() throws ParseException {
        file = new File(filename);
        reader = MZXMLReader.newInstance();
        reader.parse(file);
        AbstractExtraIterator<MSScan> it = reader.iterator();
        MSScan sp = it.next();
        assertEquals(421, sp.getPeakList().size());
        assertEquals(1, sp.getScanNum());
    }

    @Test
    public void testEmptyScan() throws ParseException {
        String name = ClassLoader.getSystemResource("emptyscan.mzXML").getFile();
        file = new File(name);
        MZXMLReader reader = MZXMLReader.newInstance();
        reader.parse(file);
        AbstractExtraIterator<MSScan> it = reader.iterator();
        MSScan sp = it.next();
        assertEquals(1, sp.getScanNum());
        assertEquals(600.5, sp.getPeakList().getPrecursor().getMz(), 0.01);
    }

    @Test
    public void testNext() throws ParseException {
        reader.parse(file);
        AbstractExtraIterator<MSScan> it = reader.iterator();
        it.hasNext();
        MSScan sp = it.next();
        System.out.println(sp);
        System.out.println("base peak mz: " + sp.getBasePeakMz());
        System.out.println("base peak intensity: " + sp.getBasePeakIntensity());
        assertEquals(1, sp.getScanNum());
        assertEquals(120.04, sp.getRetentionTime().getValue(), 0.01);
        sp = it.next();
        it.hasNext();
        it.hasNext();
        assertEquals(2, sp.getScanNum());
        while (it.hasNext()) {
            sp = it.next();
        }
        assertEquals(3, sp.getScanNum());
    }

    @Test
    public void testNextToList() throws ParseException {
        reader.parse(file);
        AbstractExtraIterator<MSScan> it = reader.iterator();
        int blockNumber = 0;
        List<MSScan> spectra = new ArrayList<MSScan>();
        while (it.hasNext()) {
            spectra.addAll(it.nextToList());
            blockNumber++;
        }
        System.out.println(spectra);
        System.out.println(blockNumber + " blocks");
        assertEquals(3, spectra.size());
    }

    @Test
    public void testNextEntriesToList() throws ParseException {
        reader.parse(file);
        AbstractExtraIterableReader<MSScan>.ExtraIterator<MSScan> it = reader.iterator();
        int blockNumber = 0;
        List<MSScan> spectra = new ArrayList<MSScan>();
        while (it.hasNextEntry()) {
            spectra.addAll(it.nextEntriesToList());
            blockNumber++;
        }
        System.out.println(blockNumber + " blocks");
        assertEquals(3, spectra.size());
    }

    @Test
    public void testBuildMS1Next() throws ParseException {
        reader.setCondition(msLevelFilter(1));
        reader.parse(file);
        AbstractExtraIterator<MSScan> it = reader.iterator();
        int count = 0;
        while (it.hasNext()) {
            it.next();
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testBuildMS2Next() throws ParseException {
        reader.setCondition(msLevelFilter(2));
        reader.parse(file);
        AbstractExtraIterator<MSScan> it = reader.iterator();
        int count = 0;
        while (it.hasNext()) {
            it.next();
            count++;
        }
        assertEquals(1, count);
    }

    @Test
    public void testBuildMS1() throws ParseException {
        reader.setCondition(msLevelFilter(1));
        reader.parse(file);
        List<MSScan> spectra = reader.iterator().nextToList();
        assertEquals(2, spectra.size());
    }

    @Test
    public void testBuildMS2() throws ParseException {
        reader.setCondition(msLevelFilter(2));
        reader.parse(file);
        AbstractExtraIterator<MSScan> it = reader.iterator();
        List<MSScan> spectra = it.nextToList();
        assertEquals(1, spectra.size());
    }

    @Test
    public void testSpecStats() throws ParseException {
        reader.parse(file);
        AbstractExtraIterator<MSScan> it = reader.iterator();
        List<MSScan> spectra = it.nextToList(500);
        PeakListRunStats stats = PeakListRunStats.newInstance(spectra.iterator(), new Transformer<MSScan, PeakList>() {

            public PeakList transform(MSScan scan) {
                return scan.getPeakList();
            }
        });
        System.out.println(spectra);
        Assert.assertEquals(3, spectra.size());
        Assert.assertEquals(193, stats.getPeakNumberMin());
        Assert.assertEquals(1733, stats.getPeakNumberMax());
        Assert.assertEquals(782, stats.getPeakNumberAvg());
    }

    @Test
    public void testGetExpInfos() throws ParseException {
        reader.parse(file);
        MassSpectrumInfos infos = reader.getExperimentInfos();
        System.out.println(infos);
        Assert.assertEquals(3, infos.getScanCount());
        System.out.println(infos.getValue(MassSpectrumInfos.FILE_NAME));
        Assert.assertFalse(infos.isCentroided());
        Assert.assertEquals("ThermoFinnigan", infos.getValue(MassSpectrumInfos.MS_MANUFACTURER));
        Object value = infos.getValue(MassSpectrumInfos.DATA_PROC_CENTROIDED);
        Assert.assertEquals(Boolean.class, value.getClass());
    }

    public void testScan() throws ParseException {
        String name = "/home/def/Documents/SIB/jpl/data/mzxml/Ly_05_J02_GPF.mzXML";
        file = new File(name);
        MZXMLReader reader = MZXMLReader.newInstance();
        reader.parse(file);
        AbstractExtraIterator<MSScan> it = reader.iterator();
        int count = 0;
        while (it.hasNext()) {
            count += it.nextToList().size();
        }
        System.out.println(reader.getMemoryCalculator().getMaxObjectNumber());
        Assert.assertEquals(70010, count);
    }

    public void testScan2() throws ParseException {
        String name = "/home/def/Documents/SIB/jpl/data/mzxml/Ly_05_J02_GPF.mzXML";
        file = new File(name);
        MZXMLReader reader = MZXMLReader.newInstance();
        reader.parse(file);
        reader.getMemoryCalculator().setMaxMemoryRatio(.8);
        AbstractExtraIterator<MSScan> it = reader.iterator();
        int count = 0;
        while (it.hasNext()) {
            count += it.nextToList().size();
        }
        System.out.println(reader.getMemoryCalculator().getFreeMemoryRatio());
        System.out.println(reader.getMemoryCalculator().getMaxObjectNumber());
        Assert.assertEquals(70010, count);
    }

    public void testScan3() throws ParseException {
        String name = "/home/def/Documents/SIB/jpl/data/mzxml/Annammp9_2_20071217_2-s1_reindexed.mzXML";
        file = new File(name);
        MZXMLReader reader = MZXMLReader.newInstance();
        reader.parse(file);
        AbstractExtraIterator<MSScan> it = reader.iterator();
        int count = 0;
        while (it.hasNext()) {
            count += it.nextToList().size();
        }
        System.out.println(count);
    }

    public void testParseNegativeScan() throws ParseException {
        String name = "/home/def/Documents/SIB/jpl/data/mzxml/pacific_500_R2.mzXML";
        file = new File(name);
        MZXMLReader reader = MZXMLReader.newInstance();
        reader.parse(file);
        AbstractExtraIterator<MSScan> it = reader.iterator();
        int count = 0;
        Peak prec = null;
        while (it.hasNext()) {
            MSScan spec = it.next();
            if (spec.getScanNum() == 31306) {
                prec = spec.getPeakList().getPrecursor();
                Assert.assertEquals(0.0, prec.getIntensity());
                Assert.assertEquals(500.0, prec.getMz());
            } else if (spec.getScanNum() == 31309) {
                prec = spec.getPeakList().getPrecursor();
                Assert.assertEquals(0.0, prec.getIntensity());
                Assert.assertEquals(506.0, prec.getMz());
                Assert.assertEquals(11, spec.getPeakList().size());
            }
            count++;
        }
        Assert.assertEquals(43331, count);
    }

    @Test
    public void testParseFloScan() throws ParseException {
        String name = "/home/def/Desktop/fred/2012_03_28_CAP_OT_35_FRF_GF_1213_NL_R2.mzXML";
        file = new File(name);
        MZXMLReader reader = MZXMLReader.newInstance();
        reader.parse(file);
        AbstractExtraIterator<MSScan> it = reader.iterator();
        int count = 0;
        Peak prec = null;
        while (it.hasNext()) {
            MSScan spec = it.next();
            if (spec.getScanNum() == 23) {
                prec = spec.getPeakList().getPrecursor();
                Assert.assertEquals(0.0, prec.getIntensity());
                Assert.assertEquals(1812.0527344, prec.getMz());
                break;
            }
            count++;
        }
    }
}
