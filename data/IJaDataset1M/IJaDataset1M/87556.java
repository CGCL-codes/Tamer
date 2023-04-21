package pl.edu.pw.polygen.core.file;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.polygen.beans.FileBean;
import pl.edu.pw.polygen.beans.ProjectBean;
import pl.edu.pw.polygen.beans.UserBean;
import pl.edu.pw.polygen.db.dao.PolyFileDao;
import pl.edu.pw.polygen.db.dao.ProjectDao;
import pl.edu.pw.polygen.exception.FileExistException;

@Service("fileService")
public class FileServiceImpl implements FileService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private PolyFileDao polyFileDao;

    public FileBean createNewFile(String fileName, String fileDesc, Long projectId) throws FileExistException {
        ProjectBean project = projectDao.findProject(projectId);
        FileBean fileBean = new FileBean();
        fileBean.setFileName(fileName);
        fileBean.setDescription(fileDesc);
        fileBean.setCreateDate(new Date());
        fileBean.setModificationDate(new Date());
        fileBean.setProject(project);
        if (polyFileDao.isExisting(fileBean) == false) {
            return polyFileDao.saveFile(fileBean);
        } else {
            throw new FileExistException("File " + fileName + " already exist for project " + project.getName());
        }
    }

    public FileBean saveFile(FileBean fileBean) {
        return polyFileDao.saveFile(fileBean);
    }

    public void removeFile(String fileName, String projectName, Long userId) {
        FileBean fileBean = polyFileDao.findFile(fileName, projectName, userId);
        polyFileDao.removeFile(fileBean);
    }

    public FileBean findFile(String fileName, String projectName, Long userId) {
        FileBean fileBean = polyFileDao.findFile(fileName, projectName, userId);
        return fileBean;
    }
}
