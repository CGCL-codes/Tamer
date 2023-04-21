package sywico.core.change;

import org.apache.log4j.Logger;
import junit.framework.TestCase;
import sywico.core.MockFiles;
import sywico.core.Util;
import sywico.core.change.Applier;
import sywico.core.change.PatchesChangeListBuilder;
import sywico.core.change.model.ChangeList;
import sywico.core.checksumreport.FileAndDirReport;
import sywico.core.checksumreport.FileAndDirReportBuilder;
import sywico.core.checksumreport.Filter;

/**
 * 
 * Test that the merging on mockfiles performs as expected
 *
 */
public class MergeTest extends TestCase {

    public static Logger logger = Logger.getLogger(ChangeListBuilderTest.class.getName());

    public void testScenario1() {
        Util.delete(MockFiles.TMP_DIR + "/", null);
        MockFiles.createScenario1_OriginalAndCompany(MockFiles.TMP_DIR + "/base");
        MockFiles.createScenario1_OriginalAndCompany(MockFiles.TMP_DIR + "/company");
        MockFiles.createScenario1_Developer(MockFiles.TMP_DIR + "/developer");
        MockFiles.createScenario1_Developer(MockFiles.TMP_DIR + "/expected");
        mergeHelper(MockFiles.TMP_DIR + "/base", MockFiles.TMP_DIR + "/company", MockFiles.TMP_DIR + "/developer", MockFiles.TMP_DIR + "/expected", MockFiles.TMP_DIR + "/merge");
    }

    public void testScenario1Reversed() {
        Util.delete(MockFiles.TMP_DIR + "/", null);
        MockFiles.createScenario1_OriginalAndCompany(MockFiles.TMP_DIR + "/base");
        MockFiles.createScenario1_OriginalAndCompany(MockFiles.TMP_DIR + "/developer");
        MockFiles.createScenario1_Developer(MockFiles.TMP_DIR + "/company");
        MockFiles.createScenario1_Developer(MockFiles.TMP_DIR + "/expected");
        mergeHelper(MockFiles.TMP_DIR + "/base", MockFiles.TMP_DIR + "/company", MockFiles.TMP_DIR + "/developer", MockFiles.TMP_DIR + "/expected", MockFiles.TMP_DIR + "/merge");
    }

    public void testScenario2() {
        Util.delete(MockFiles.TMP_DIR + "/", null);
        MockFiles.createScenario2_Original(MockFiles.TMP_DIR + "/base");
        MockFiles.createScenario2_Company(MockFiles.TMP_DIR + "/company");
        MockFiles.createScenario2_Developer(MockFiles.TMP_DIR + "/developer");
        MockFiles.createScenario2_ExpectedMerged(MockFiles.TMP_DIR + "/expected", true);
        mergeHelper(MockFiles.TMP_DIR + "/base", MockFiles.TMP_DIR + "/company", MockFiles.TMP_DIR + "/developer", MockFiles.TMP_DIR + "/expected", MockFiles.TMP_DIR + "/merge");
    }

    public void testScenario2Reversed() {
        Util.delete(MockFiles.TMP_DIR + "/", null);
        MockFiles.createScenario2_Original(MockFiles.TMP_DIR + "/base");
        MockFiles.createScenario2_Company(MockFiles.TMP_DIR + "/developer");
        MockFiles.createScenario2_Developer(MockFiles.TMP_DIR + "/company");
        MockFiles.createScenario2_ExpectedMerged(MockFiles.TMP_DIR + "/expected", false);
        mergeHelper(MockFiles.TMP_DIR + "/base", MockFiles.TMP_DIR + "/company", MockFiles.TMP_DIR + "/developer", MockFiles.TMP_DIR + "/expected", MockFiles.TMP_DIR + "/merge");
    }

    public void testScenario6() {
        Util.delete(MockFiles.TMP_DIR + "/", null);
        MockFiles.createScenario6_Original(MockFiles.TMP_DIR + "/base");
        MockFiles.createScenario6_Company(MockFiles.TMP_DIR + "/company");
        MockFiles.createScenario6_Developer(MockFiles.TMP_DIR + "/developer");
        MockFiles.createScenario6_ExpectedMerged(MockFiles.TMP_DIR + "/expected");
        mergeHelper(MockFiles.TMP_DIR + "/base", MockFiles.TMP_DIR + "/company", MockFiles.TMP_DIR + "/developer", MockFiles.TMP_DIR + "/expected", MockFiles.TMP_DIR + "/merge");
    }

    public void testScenario6Reversed() {
        Util.delete(MockFiles.TMP_DIR + "/", null);
        MockFiles.createScenario6_Original(MockFiles.TMP_DIR + "/base");
        MockFiles.createScenario6_Company(MockFiles.TMP_DIR + "/developer");
        MockFiles.createScenario6_Developer(MockFiles.TMP_DIR + "/company");
        MockFiles.createScenario6_ExpectedMerged(MockFiles.TMP_DIR + "/expected");
        mergeHelper(MockFiles.TMP_DIR + "/base", MockFiles.TMP_DIR + "/company", MockFiles.TMP_DIR + "/developer", MockFiles.TMP_DIR + "/expected", MockFiles.TMP_DIR + "/merge");
    }

    public void testScenario5() {
        Util.delete(MockFiles.TMP_DIR + "/", null);
        MockFiles.createScenario5_Original(MockFiles.TMP_DIR + "/base");
        MockFiles.createScenario5_Company(MockFiles.TMP_DIR + "/company");
        MockFiles.createScenario5_Developer(MockFiles.TMP_DIR + "/developer");
        MockFiles.createScenario5_ExpectedMerged(MockFiles.TMP_DIR + "/expected", true);
        mergeHelper(MockFiles.TMP_DIR + "/base", MockFiles.TMP_DIR + "/company", MockFiles.TMP_DIR + "/developer", MockFiles.TMP_DIR + "/expected", MockFiles.TMP_DIR + "/merge");
    }

    public void testScenario5Reversed() {
        Util.delete(MockFiles.TMP_DIR + "/", null);
        MockFiles.createScenario5_Original(MockFiles.TMP_DIR + "/base");
        MockFiles.createScenario5_Developer(MockFiles.TMP_DIR + "/company");
        MockFiles.createScenario5_Company(MockFiles.TMP_DIR + "/developer");
        MockFiles.createScenario5_ExpectedMerged(MockFiles.TMP_DIR + "/expected", false);
        mergeHelper(MockFiles.TMP_DIR + "/base", MockFiles.TMP_DIR + "/company", MockFiles.TMP_DIR + "/developer", MockFiles.TMP_DIR + "/expected", MockFiles.TMP_DIR + "/merge");
    }

    public void testScenario4() {
        Util.delete(MockFiles.TMP_DIR + "/", null);
        MockFiles.createScenario4_Original(MockFiles.TMP_DIR + "/base");
        MockFiles.createScenario4_Company(MockFiles.TMP_DIR + "/company");
        MockFiles.createScenario4_Developer(MockFiles.TMP_DIR + "/developer");
        MockFiles.createScenario4_ExpectedMerged(MockFiles.TMP_DIR + "/expected", true);
        mergeHelper(MockFiles.TMP_DIR + "/base", MockFiles.TMP_DIR + "/company", MockFiles.TMP_DIR + "/developer", MockFiles.TMP_DIR + "/expected", MockFiles.TMP_DIR + "/merge");
    }

    public void testScenario4Reversed() {
        Util.delete(MockFiles.TMP_DIR + "/", null);
        MockFiles.createScenario4_Original(MockFiles.TMP_DIR + "/base");
        MockFiles.createScenario4_Company(MockFiles.TMP_DIR + "/developer");
        MockFiles.createScenario4_Developer(MockFiles.TMP_DIR + "/company");
        MockFiles.createScenario4_ExpectedMerged(MockFiles.TMP_DIR + "/expected", false);
        mergeHelper(MockFiles.TMP_DIR + "/base", MockFiles.TMP_DIR + "/company", MockFiles.TMP_DIR + "/developer", MockFiles.TMP_DIR + "/expected", MockFiles.TMP_DIR + "/merge");
    }

    /**
     * 
     * test helper that will perform a merge and optionally check the result 
     * 
     * @param baseDir
     * @param companyDir
     * @param developerDir
     * @param expectedMergeDir. can be null
     * @param actualMerge
     */
    public void mergeHelper(String baseDir, String companyDir, String developerDir, String expectedMergeDir, String actualMerge) {
        FileAndDirReport reportD = FileAndDirReportBuilder.buildChecksumReport("developer", developerDir, new Filter(), null);
        FileAndDirReport reportC = FileAndDirReportBuilder.buildChecksumReport("company", companyDir, new Filter(), null);
        FileAndDirReport reportO = FileAndDirReportBuilder.buildChecksumReport("original", baseDir, new Filter(), null);
        FileAndDirReport reportE = null;
        if (expectedMergeDir != null) reportE = FileAndDirReportBuilder.buildChecksumReport("expected", expectedMergeDir, new Filter(), null);
        ChangeList companyChangeList = PatchesChangeListBuilder.findChangesIn(baseDir, companyDir, reportO, reportC, null);
        ChangeList developerChangeList = PatchesChangeListBuilder.findChangesIn(baseDir, developerDir, reportO, reportD, null);
        Util.copy(baseDir, actualMerge, null);
        Applier.mergeChanges(actualMerge, companyChangeList, developerChangeList, null);
        FileAndDirReport reportMerged = FileAndDirReportBuilder.buildChecksumReport("merged", actualMerge, new Filter(), null);
        if (expectedMergeDir != null) logger.debug("expected merge:" + reportE);
        logger.debug("actual merge:" + reportMerged);
        if (expectedMergeDir != null) {
            String diffs = MockFiles.reportsAreEqual(reportMerged, reportE);
            if (diffs != null) {
                logger.info("reportMerged:" + reportMerged);
                logger.info("reportE:" + reportE);
                logger.error("diffs:" + diffs);
                fail(diffs);
            }
        }
    }
}
