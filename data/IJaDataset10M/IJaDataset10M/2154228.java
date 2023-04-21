package cluster5.server.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import cluster5.server.datastorage.dao.FileDao;
import cluster5.server.datastorage.domain.Project;
import cluster5.server.datastorage.domain.SharedFile;
import cluster5.server.datastorage.domain.User;
import cluster5.shared.utils.FileUtils;

public class FileManager {

    static {
        ManagerInitializer.autowireManagers();
    }

    public static final String FILES_SAVE_PATH = "./shared-files/";

    private static FileManager instance;

    private static Logger logger = Logger.getLogger(FileManager.class);

    private FileDao fileDao;

    protected UserManager userManager;

    protected ProjectManager projectManager;

    private FileManager() {
        fileDao = new FileDao();
        File f = new File(FILES_SAVE_PATH);
        if (!f.exists()) f.mkdir();
    }

    public static FileManager getInstance() {
        if (instance == null) instance = new FileManager();
        return instance;
    }

    public void saveFile(String token, Long projectId, SharedFile file) {
        User user = userManager.getByToken(token);
        Project project = projectManager.getProject(token, projectId);
        if (user != null && project != null && user.getId() == project.getUserId()) {
            file.setUserId(user.getId());
            file.setProjectId(projectId);
            SharedFile oldFile = fileDao.get(file.getUserId(), file.getProjectId(), file.getType(), file.getFilename(), file.getCanonicalClassName());
            if (oldFile != null) file.setId(oldFile.getId());
            fileDao.save(file);
            Long fileId = file.getId();
            assert fileId != null;
            try {
                String fullPath = FILES_SAVE_PATH + fileId;
                FileUtils.saveToFile(fullPath, file.getData());
            } catch (FileNotFoundException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    /**
	 * @param fileId
	 * @return file by ID; null if there is no such file
	 */
    public SharedFile getFile(Long fileId) {
        SharedFile file = fileDao.getById(fileId);
        if (file != null) {
            String fullPath = FILES_SAVE_PATH + fileId;
            byte[] data = FileUtils.readFromFile(fullPath);
            file.setData(data);
        } else file = null;
        return file;
    }

    /**
	 * Deletes specified file from the database and the filesystem.
	 * 
	 * @param fileId
	 * @return true if operation was successful
	 */
    public boolean deleteFile(Long fileId) {
        boolean operationResult = false;
        if (fileDao.deleteFile(fileId)) {
            String fullPath = FILES_SAVE_PATH + fileId;
            operationResult = FileUtils.deleteFile(fullPath);
        }
        return operationResult;
    }

    /**
	 * @param token
	 * @param projectId
	 * @return list of shared file IDs according to specified parameters
	 */
    public List<Long> listFileIds(String token, Long projectId) {
        List<Long> list = new ArrayList<Long>();
        Project project = projectManager.getProject(token, projectId);
        if (project != null) {
            Long userId = project.getUserId();
            list.addAll(fileDao.listFileIds(userId, projectId));
        }
        return list;
    }

    /**
	 * Lists all files in the database without loading binary data.
	 * 
	 * @return list of {@link SharedFile} or an empty list if there are none
	 */
    public List<SharedFile> listAllFiles() {
        return fileDao.listAllFiles();
    }

    /**
	 * @param fileId ID of a {@link SharedFile}
	 * @return binary data of a file by its ID or null
	 */
    public byte[] getFileData(Long fileId) {
        SharedFile file = fileDao.getById(fileId);
        byte[] data = null;
        if (file != null) {
            data = FileUtils.readFromFile(FILES_SAVE_PATH + File.separatorChar + file.getId());
        }
        return data;
    }
}
