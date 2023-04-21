package ar.com.coonocer.CodingJoy.model.serialization.project;

import ar.com.coonocer.CodingJoy.model.Project;
import ar.com.coonocer.CodingJoy.model.TreeNode;
import ar.com.coonocer.CodingJoy.utils.FileUtils;

public class ProjectXMLSaveTemporal {

    private static final String TEMP_PROJECTS_FOLDER = "tempFiles";

    public static Project load(String folderName, TreeNode metadata2) throws Exception {
        String folderNameNew = TEMP_PROJECTS_FOLDER + "/" + folderName;
        Project project = ProjectXMLPersistenceUtil.load(folderNameNew, metadata2);
        project.setFolderName(folderName);
        return project;
    }

    public static void save(Project project, String folderName) throws Exception {
        folderName = TEMP_PROJECTS_FOLDER + "/" + folderName;
        ProjectXMLPersistenceUtil.save(project, folderName);
    }

    public static boolean delete(String folderName) throws Exception {
        folderName = TEMP_PROJECTS_FOLDER + "/" + folderName;
        return ProjectXMLPersistenceUtil.delete(folderName);
    }

    public static String getAbsoluteFolderFor(String folderName) {
        return FileUtils.getAbsolutePath(TEMP_PROJECTS_FOLDER + "/" + folderName);
    }

    public static String getAbsoluteProjectFileNameFor(String folderName) {
        return getAbsoluteFolderFor(folderName) + "/" + ProjectXMLPersistenceUtil.PROJECT_FILE_NAME;
    }
}
