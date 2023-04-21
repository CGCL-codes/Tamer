package org.jcvi.vics.compute.service.common.file;

import org.apache.log4j.Logger;
import org.jcvi.vics.compute.engine.data.IProcessData;
import org.jcvi.vics.compute.engine.data.MissingDataException;
import org.jcvi.vics.compute.engine.service.IService;
import org.jcvi.vics.compute.service.common.ProcessDataHelper;
import org.jcvi.vics.compute.service.common.grid.submit.SubmitJobException;
import org.jcvi.vics.model.tasks.Task;
import org.jcvi.vics.model.user_data.FileNode;
import org.jcvi.vics.shared.utils.FileUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: smurphy
 * Date: Nov 3, 2008
 * Time: 3:59:29 PM
 */
public class CreateOutputDirsService implements IService {

    protected Logger logger;

    protected List<File> outputDirs = new ArrayList<File>();

    protected List<File> queryFiles = new ArrayList<File>();

    protected FileNode resultFileNode;

    protected IProcessData processData;

    protected Task task;

    protected HashMap<File, File> inputOutputDirMap = new HashMap<File, File>();

    public void execute(IProcessData processData) throws SubmitJobException {
        try {
            logger = ProcessDataHelper.getLoggerForTask(processData, this.getClass());
            init(processData);
            createOutputDirs();
            processData.putItem(FileServiceConstants.OUTPUT_DIR_LIST, outputDirs);
            processData.putItem(FileServiceConstants.INPUT_OUTPUT_DIR_MAP, inputOutputDirMap);
        } catch (Exception e) {
            throw new SubmitJobException(e);
        }
    }

    protected void createOutputDirs() throws Exception {
        queryFiles = getQueryFiles();
        if (queryFiles == null || queryFiles.size() == 0) logger.error("CreateOutputDirsService createOutputDirs queryFile is null or empty");
        for (File queryFile : queryFiles) {
            File outputDir = createOutputDir(queryFile);
            outputDirs.add(outputDir);
            inputOutputDirMap.put(queryFile, outputDir);
            doAdditionalIntegrationPerInputOutput(queryFile, outputDir);
        }
    }

    protected void doAdditionalIntegrationPerInputOutput(File queryFile, File outputDir) throws Exception {
    }

    protected void init(IProcessData processData) throws MissingDataException {
        this.processData = processData;
        this.task = ProcessDataHelper.getTask(processData);
        this.resultFileNode = ProcessDataHelper.getResultFileNode(processData);
        if (resultFileNode == null) {
            throw new MissingDataException("ResultFileNode for createtask " + task.getObjectId() + " must exist before a grid job is submitted");
        }
    }

    private List<File> getQueryFiles() throws MissingDataException {
        List<File> queryFiles = new ArrayList<File>();
        Object obj = processData.getMandatoryItem(FileServiceConstants.POST_SPLIT_INPUT_FILE_LIST);
        if (obj instanceof List) {
            queryFiles.addAll((List) obj);
        } else {
            queryFiles.add((File) obj);
        }
        return queryFiles;
    }

    private File createOutputDir(File queryFile) throws MissingDataException, IOException {
        String outputDirPath = FileUtil.checkFilePath(resultFileNode.getDirectoryPath()) + File.separator + "r_" + queryFile.getName();
        logger.debug("createOutputDir creating directory=" + outputDirPath);
        return FileUtil.ensureDirExists(outputDirPath);
    }
}
