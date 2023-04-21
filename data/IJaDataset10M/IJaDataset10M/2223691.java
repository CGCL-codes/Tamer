package org.op.service.fileio;

import java.io.File;
import org.op.service.config.ConfigKeys;
import org.op.service.config.ConfigurationService;

public class FileRefactoringServiceImpl implements FileRefactoringService {

    private static final String TRASH_FOLDER_NAME = ".trash";

    private ConfigurationService configService;

    public void addFolder(String parentPath, String folderName) throws Exception {
        String path = parentPath + ConfigurationService.FILE_SEP + folderName;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void trashFile(String originalPath) throws Exception {
        File file = new File(originalPath);
        String workspace = configService.getApplicationProperties().getProperty(ConfigKeys.KEY_CURRENT_WORKSPACE);
        String trashFolderPath = workspace + ConfigurationService.FILE_SEP + TRASH_FOLDER_NAME;
        File trashFolder = new File(trashFolderPath);
        if (!trashFolder.exists()) {
            trashFolder.mkdir();
        }
        File newFile = new File(trashFolder, file.getName());
        file.renameTo(newFile);
    }

    public void moveFile(String currentPath, File folder) throws Exception {
        String newPath = folder.getAbsolutePath();
        File oldFile = new File(currentPath);
        String fileName = oldFile.getName();
        newPath += ConfigurationService.FILE_SEP + fileName;
        File newFile = new File(newPath);
        oldFile.renameTo(newFile);
    }

    public ConfigurationService getConfigService() {
        return configService;
    }

    public void setConfigService(ConfigurationService configService) {
        this.configService = configService;
    }
}
