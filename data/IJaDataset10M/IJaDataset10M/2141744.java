package org.systemsbiology.apps.corragui.server.executor.superhirn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.systemsbiology.apps.corragui.client.constants.PipelineStep;
import org.systemsbiology.apps.corragui.server.util.FileUtils;

public class FeModuleExecutorTest extends SuperHirnExecutorTestCase {

    PipelineStep step = PipelineStep.FEATURE_PICKING;

    protected void setUp() {
        executor = new FeModuleExecutor(user, projSetup, fpSetup, projLocation, step, binDir, false, false);
    }

    protected void tearDown() {
        deleteAnalysisDirectory();
        File f = getParamFile();
        f.delete();
    }

    public void testDoOverwrite() {
        createAnalysisDirectory();
        assertTrue(getAnalysisDir().exists());
        executor = new FeModuleExecutor(user, projSetup, fpSetup, projLocation, step, binDir, true);
        assertTrue(executor.doOverwrite());
        executor.prepare();
        assertFalse(getAnalysisDir().exists());
        executor.prepare();
        assertFalse(getAnalysisDir().exists());
        executor = new FeModuleExecutor(user, projSetup, fpSetup, projLocation, step, binDir, false, false);
        assertFalse(executor.doOverwrite());
        createAnalysisDirectory();
        assertTrue(getAnalysisDir().exists());
        executor.prepare();
        assertTrue(getAnalysisDir().exists());
    }

    public void testRemoveXmlFilesNotRequired() {
        assertFalse(executor.doOverwrite());
        executor.deleteOldResults();
        assertFalse(getAnalysisDir().exists());
        executor.prepare();
        assertFalse(getAnalysisDir().exists());
        createAnalysisDirectory();
        assertTrue(getAnalysisDir().exists());
        List<String> extractedFiles = requiredXmlFileNames();
        assertTrue(extractedFiles.size() > 0);
        String[] foundFiles = FileUtils.getAllFilesWithExtension(getLcmsRunsDir().getAbsolutePath(), "xml");
        assertEquals(extractedFiles.size(), foundFiles.length);
        executor.prepare();
        foundFiles = FileUtils.getAllFilesWithExtension(getLcmsRunsDir().getAbsolutePath(), "xml");
        assertEquals(extractedFiles.size(), foundFiles.length);
        List<String> addedFiles = new ArrayList<String>(2);
        addedFiles.add("Added_1.xml");
        addedFiles.add("Added_2.xml");
        List<String> filesAdded = addXmlFiles(addedFiles);
        executor.prepare();
        for (String file : filesAdded) {
            assertFalse((new File(file).exists()));
        }
    }

    public void testWriteParamFile() {
        File paramFile = getParamFile();
        paramFile.delete();
        assertFalse(paramFile.exists());
        executor.prepare();
        assertTrue(paramFile.exists());
    }

    public void testDeleteOldResults() {
        createAnalysisDirectory();
        File analysisDir = getAnalysisDir();
        assertTrue(analysisDir.exists());
        executor.deleteOldResults();
        assertFalse(analysisDir.exists());
    }

    public void testCheckOutput() {
        executor.deleteOldResults();
        assertFalse(executor.checkOutput());
        createAnalysisDirectory();
        assertTrue(executor.checkOutput());
        addExtraXmlFiles();
        assertTrue(executor.checkOutput());
        deleteSomeRequiredXmlFiles();
        assertFalse(executor.checkOutput());
    }

    public void testGetJobName() {
        String jobName = user.getLoginName() + "_" + projName + "_" + "SHFE";
        assertEquals(jobName, executor.getExecutorName());
    }

    public void testGetPipelineStep() {
        assertEquals(PipelineStep.FEATURE_PICKING, executor.getPipelineStep());
    }

    private File getParamFile() {
        return new File(projLocation.pathForWebServer() + File.separatorChar + SuperHirnConstants.Input.PARAM);
    }
}
