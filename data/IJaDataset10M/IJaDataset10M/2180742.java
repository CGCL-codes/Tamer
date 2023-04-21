package org.systemsbiology.apps.corragui.server.executor.superhirn;

import java.io.File;
import java.io.IOException;
import org.systemsbiology.apps.corragui.client.constants.PipelineStep;
import org.systemsbiology.apps.corragui.server.executor.superhirn.SuperHirnConstants.Output;

public class BtModuleExecutorTest extends SuperHirnExecutorTestCase {

    PipelineStep step = PipelineStep.ALIGNMENT;

    protected void setUp() {
        executor = new BtModuleExecutor(user, projSetup, fpSetup, projLocation, step, binDir);
        createAnalysisDirectory();
        assertTrue(getAnalysisDir().exists());
    }

    protected void tearDown() {
        deleteAnalysisDirectory();
    }

    public void testMatchInputMzxml() {
        assertTrue(executor.prepare());
        deleteSomeRequiredXmlFiles();
        assertFalse(executor.prepare());
        createRequiredXmlFiles();
        assertTrue(executor.prepare());
        addExtraXmlFiles();
        assertFalse(executor.prepare());
        createRequiredXmlFiles();
        assertTrue(executor.prepare());
    }

    public void testDeleteOldResults() {
        File outFile = getOutputFile();
        createOutputFile();
        assertTrue(outFile.exists());
        executor.deleteOldResults();
        assertFalse(outFile.exists());
    }

    public void testCheckOutput() {
        File outFile = getOutputFile();
        outFile.delete();
        assertFalse(outFile.exists());
        assertTrue(executor.checkOutput());
        createOutputFile();
        assertTrue(outFile.exists());
        assertTrue(executor.checkOutput());
        assertFalse(outFile.exists());
    }

    public void testGetJobName() {
        String jobName = user.getLoginName() + "_" + projName + "_" + "SHBT";
        assertEquals(jobName, executor.getExecutorName());
    }

    public void testGetPipelineStep() {
        assertEquals(PipelineStep.ALIGNMENT, executor.getPipelineStep());
    }

    private void createOutputFile() {
        File outFile = getOutputFile();
        try {
            outFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getOutputFile() {
        File outFile = new File(executor.getAnalysisDirWebServer() + File.separatorChar + Output.BTOUT);
        return outFile;
    }
}
