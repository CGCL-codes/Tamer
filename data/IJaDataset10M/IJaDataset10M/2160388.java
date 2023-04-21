package org.ourgrid.common.executor;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.ourgrid.common.util.StringUtil;
import org.ourgrid.common.util.TempFileManager;
import org.ourgrid.worker.WorkerConstants;

public class FolderBasedSandboxedUnixEnvironmentUtil {

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(FolderBasedSandboxedUnixEnvironmentUtil.class);

    /**
	 * @param envVars
	 * @param vmStorageDir
	 * @throws IOException
	 */
    @SuppressWarnings("unchecked")
    public void copyStorageFiles(Map<String, String> envVars, File vmStorageDir) throws IOException {
        String storagePath = envVars.get(WorkerConstants.ENV_STORAGE);
        if (storagePath == null) {
            return;
        }
        File storageDir = new File(storagePath);
        if (!storageDir.isDirectory()) {
            return;
        }
        Collection<File> storageFiles = FileUtils.listFiles(storageDir, null, true);
        LOG.debug("Storage path: " + storagePath);
        StringBuffer sb = new StringBuffer("Storage files:\n");
        for (File file : storageFiles) {
            sb.append(file.getAbsolutePath() + "\n");
        }
        LOG.debug(sb.toString());
        for (File file : storageFiles) {
            String destination = file.getAbsolutePath().replace(storagePath, "");
            File vmStoredFile = new File(vmStorageDir + File.separator + destination);
            FileUtils.copyFile(file, vmStoredFile);
            LOG.debug("Held stored file copied to: " + vmStoredFile + " vm storage directory ");
        }
    }

    /**
	 * A script created to run the command passed as a param. This is
	 * the script that will be executed in an Unix secure environment.
	 * 
	 * @param command
	 * @param dirName
	 * @param envVars
	 * @return
	 * @throws ExecutorException
	 */
    public File createScript(String command, String dirName, Map<String, String> envVars) throws ExecutorException {
        File dir = new File(dirName);
        File commandFile = null;
        boolean isScript = false;
        if (dirName == null) {
            dirName = ".";
        } else if (!dirName.equals(".") && !dir.isDirectory()) {
            throw new ExecutorException(command, new FileNotFoundException(dir.getAbsolutePath()));
        }
        try {
            commandFile = new File(dir, command);
            if (commandFile.exists() && commandFile.canRead()) {
                FileInputStream commandFIS = new FileInputStream(commandFile);
                DataInputStream commandDIS = new DataInputStream(commandFIS);
                if (commandDIS.readChar() == '#' && commandDIS.readChar() == '!') {
                    isScript = true;
                }
                commandFIS.close();
                commandDIS.close();
            }
        } catch (FileNotFoundException e) {
            throw new ExecutorException(command, new FileNotFoundException(commandFile.getAbsolutePath()));
        } catch (IOException e) {
            throw new ExecutorException(command, e);
        }
        File temp;
        BufferedWriter writerTemp;
        String exportCommand = "export ";
        try {
            temp = TempFileManager.createTempFile("broker", ".tmp", dir);
            writerTemp = new BufferedWriter(new FileWriter(temp));
            if (envVars != null) {
                if (!envVars.isEmpty()) {
                    for (String key : envVars.keySet()) {
                        writerTemp.write(key + "=\'" + envVars.get(key) + "\'");
                        writerTemp.newLine();
                        exportCommand = exportCommand + " " + key;
                    }
                }
            }
            writerTemp.write("PATH=$PATH:$PLAYPEN:$STORAGE:.");
            writerTemp.newLine();
            exportCommand = exportCommand + " PATH";
            writerTemp.write(exportCommand);
            writerTemp.newLine();
            if (isScript) {
                writerTemp.write("sh ");
            }
            writerTemp.write(command);
            writerTemp.newLine();
            writerTemp.flush();
            writerTemp.close();
            return temp;
        } catch (IOException ioe) {
            throw new ExecutorException(ioe);
        }
    }

    /**
	 * @param result
	 * @param fileStdOutPut
	 * @param fileStdError
	 * @param fileExitValue
	 * @throws IOException
	 */
    public void catchOutputFromFile(ExecutorResult result, File fileStdOutPut, File fileStdError, File fileExitValue) throws IOException {
        StringBuffer outputResult = new StringBuffer();
        StringBuffer outputErrorResult = new StringBuffer();
        StringBuffer exitValueResult = new StringBuffer();
        LOG.debug("Catching output file: " + fileStdOutPut.getAbsolutePath());
        outputResult = StringUtil.readFile(fileStdOutPut);
        result.setStdout(outputResult.toString());
        LOG.debug("Output catched - file: " + fileStdOutPut.getAbsolutePath());
        LOG.debug("Catching error output file: " + fileStdError.getAbsolutePath());
        outputErrorResult = StringUtil.readFile(fileStdError);
        result.setStderr(outputErrorResult.toString());
        LOG.debug("Error Output catched - file: " + fileStdError.getAbsolutePath());
        LOG.debug("Catching Exit value file: " + fileExitValue.getAbsolutePath());
        exitValueResult = StringUtil.readFile(fileExitValue);
        String ecode = exitValueResult.toString().trim();
        int exit = Integer.parseInt(ecode);
        result.setExitValue(exit);
        LOG.debug("Exit value catched - file: " + fileExitValue.getAbsolutePath());
    }
}
