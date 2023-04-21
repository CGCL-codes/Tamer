package net.sf.adatagenerator.ex.healthreg.bean.resources;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import net.sf.adatagenerator.api.CreationException;
import net.sf.adatagenerator.ex.healthreg.util.Util;
import net.sf.adatagenerator.util.FrequencyBasedList;
import net.sf.adatagenerator.util.FrequencyBasedListFactory;
import net.sf.adatagenerator.util.FrequencyBasedListStatistics;
import net.sf.adatagenerator.util.StringFrequencyFile;

public class FrequencyBasedList_Statistics_Test extends TestCase {

    @SuppressWarnings("serial")
    Map<String, Class<? extends FrequencyBasedListStatistics<String>>> tests = new HashMap<String, Class<? extends FrequencyBasedListStatistics<String>>>() {

        {
            put("ABORIGIN_FREQUENCIES.csv", ABORIGIN_FREQUENCIES.class);
            put("ABTSI_FREQUENCIES.csv", ABTSI_FREQUENCIES.class);
            put("AGE_FREQUENCIES.csv", AGE_FREQUENCIES.class);
            put("ALGNAME_FREQUENCIES.csv", ALGNAME_FREQUENCIES.class);
            put("ALTSUR_FREQUENCIES.csv", ALTSUR_FREQUENCIES.class);
            put("COB_FREQUENCIES.csv", COB_FREQUENCIES.class);
            put("HOSCODE_FREQUENCIES.csv", HOSCODE_FREQUENCIES.class);
            put("LOCALITY_FREQUENCIES.csv", LOCALITY_FREQUENCIES.class);
            put("PCODE_FREQUENCIES.csv", PCODE_FREQUENCIES.class);
            put("PLURAL_FREQUENCIES.csv", PLURAL_FREQUENCIES.class);
            put("PLURNUM_FREQUENCIES.csv", PLURNUM_FREQUENCIES.class);
            put("SEX_FREQUENCIES.csv", SEX_FREQUENCIES.class);
            put("SOURCE_FREQUENCIES.csv", SOURCE_FREQUENCIES.class);
            put("TFRHOSP_FREQUENCIES.csv", TFRHOSP_FREQUENCIES.class);
            put("TRANSFROM_FREQUENCIES.csv", TRANSFROM_FREQUENCIES.class);
        }
    };

    public void testStatistics() {
        List<String> failedTests = new ArrayList<String>();
        List<Exception> exceptions = new ArrayList<Exception>();
        for (String resourceBaseName : tests.keySet()) {
            try {
                Class<? extends FrequencyBasedListStatistics<String>> clz = tests.get(resourceBaseName);
                testStatistics(resourceBaseName, clz);
            } catch (Exception x) {
                failedTests.add(resourceBaseName);
                exceptions.add(x);
            }
        }
        if (!exceptions.isEmpty()) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println("Failed tests: " + failedTests.toString());
            for (Exception e : exceptions) {
                pw.println(e.toString());
                e.printStackTrace(pw);
                pw.println();
            }
            fail(sw.toString());
        }
    }

    public void testStatistics(String resourceBaseName, Class<? extends FrequencyBasedListStatistics<String>> clz) {
        FrequencyBasedListStatistics<String> fbls = null;
        try {
            fbls = clz.newInstance();
        } catch (InstantiationException e1) {
            fail(e1.toString());
        } catch (IllegalAccessException e1) {
            fail(e1.toString());
        }
        assertTrue(fbls != null);
        FrequencyBasedList<String> fbl = null;
        try {
            ClassLoader cl = Util.class.getClassLoader();
            String fqrn = Util.getFullyQualifiedDataName(resourceBaseName);
            FrequencyBasedListFactory<String> factory = new StringFrequencyFile(cl, fqrn);
            fbl = factory.createFrequencyBasedList();
        } catch (CreationException e) {
            fail(e.toString());
        }
        assertTrue(fbl != null);
        assertTrue(fbl.getLeastFrequentValues().contains(fbls.getaLeastFrequentValue()));
        assertTrue(fbl.getMostFrequentValues().contains(fbls.getaMostFrequentValue()));
        assertTrue((fbls.getMaxValue() == null && fbl.getMaximumValue() == null) || fbls.getMaxValue().equals(fbl.getMaximumValue()));
        assertTrue((fbls.getMinValue() == null && fbl.getMinimumValue() == null) || fbls.getMinValue().equals(fbl.getMinimumValue()));
        assertTrue(fbls.getCountLeastFrequentValues() == fbl.getLeastFrequentValues().size());
        assertTrue(fbls.getCountMostFrequentValues() == fbl.getMostFrequentValues().size());
        assertTrue(fbls.getSumOfCounts() == fbl.size());
        Map<String, Integer> relativeValues = fbl.getRelativeFrequencies();
        assertTrue(fbls.getLowestCount() == relativeValues.get(fbls.getaLeastFrequentValue()));
        assertTrue(fbls.getHighestCount() == relativeValues.get(fbls.getaMostFrequentValue()));
    }
}
