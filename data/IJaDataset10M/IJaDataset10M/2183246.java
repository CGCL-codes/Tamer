package org.monet.manager.core.compiler.stages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import org.monet.kernel.agents.AgentFilesystem;
import org.monet.kernel.exceptions.FilesystemException;
import org.monet.kernel.model.BusinessUnit;
import org.monet.kernel.utils.StreamHelper;
import org.monet.manager.core.compiler.GlobalData;
import org.monet.manager.core.compiler.Stage;
import org.monet.manager.core.compiler.errors.CantSwapModelError;

public class SwapModel extends Stage {

    @Override
    public Object getOutData() {
        return null;
    }

    @Override
    public void setInData(Object data) {
    }

    @Override
    public void execute() {
        File newModelDirectory = new File(this.globalData.getData(String.class, GlobalData.WORKING_DIRECTORY));
        File currentModelDirectory = new File(this.globalData.getData(String.class, GlobalData.MODEL_INSTALL_DIRECTORY));
        File hashTableFile = new File(currentModelDirectory, ".hashTable");
        this.loadHashTable(hashTableFile);
        File oldModelDirectory = new File(currentModelDirectory.getAbsolutePath() + ".old");
        if (currentModelDirectory.renameTo(oldModelDirectory)) {
            if (newModelDirectory.renameTo(currentModelDirectory)) {
                AgentFilesystem.removeDir(oldModelDirectory);
            } else {
                oldModelDirectory.renameTo(currentModelDirectory);
                this.problems.add(new CantSwapModelError());
                return;
            }
        } else {
            this.problems.add(new CantSwapModelError());
            return;
        }
        BusinessUnit.getInstance().getBusinessModel().reset();
    }

    private void loadHashTable(File hashTableFile) {
        HashMap<String, String> hashTable = new HashMap<String, String>();
        FileInputStream hashTableInputStream = null;
        BufferedReader bufferedReader = null;
        String sLine;
        try {
            if (hashTableFile.exists()) {
                hashTableInputStream = new FileInputStream(hashTableFile);
                bufferedReader = new BufferedReader(new InputStreamReader(hashTableInputStream, "UTF-8"));
                while ((sLine = bufferedReader.readLine()) != null) {
                    String[] values = sLine.split("#");
                    String sName = values[0];
                    String sHash = values[1];
                    hashTable.put(sName, sHash);
                }
            }
        } catch (IOException e) {
            this.agentLogger.error(e);
            throw new FilesystemException("Could not read file", hashTableFile.getName(), e);
        } finally {
            StreamHelper.close(bufferedReader);
            StreamHelper.close(hashTableInputStream);
        }
        this.globalData.setData(GlobalData.HASH_TABLE, hashTable);
    }
}
