package org.seamantics.session.api;

import java.util.ArrayList;
import java.util.List;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.seamantics.dao.BaseDao;
import org.seamantics.dao.FileDao;
import org.seamantics.dao.FolderDao;
import org.seamantics.model.ContentFile;
import org.seamantics.model.impl.Folder;
import org.seamantics.session.gui.FolderTree;
import org.seamthebits.measure.MeasureCalls;

@Name("folderService")
@AutoCreate
@MeasureCalls
public class FolderService extends AbstractFolderService<Folder> {

    @Logger
    Log log;

    @In
    FolderDao folderDao;

    @In
    FileDao fileDao;

    @In
    Session jcrSession;

    @In
    StatusMessages statusMessages;

    /**
     * removes a file from our workspace, by moving it to seamantics trash We do not remove the file in jcr, due to the
     * fact that there is no possibility to restore a deleted node in jcr but it is possible, that file to delete is
     * currently not referenced by any other node (HEAD-Version). But we can think of a situation where a user wants to
     * restore a version of a node where this file was referenced, hence we still need a possibility to access this file
     * later even don't need it now anymore.
     * 
     * @param file
     * @throws RepositoryException
     */
    public void removeFile(ContentFile file) throws RepositoryException {
        log.info("Removing file {0} from folder {1}", file.getName(), file.getParent().getPath());
        try {
            new BaseDao<ContentFile>(ContentFile.class).move(file, FolderDao.TRASH_FOLDER_ROOT_PATH + FolderDao.FILES_PATH);
            file.getParent().getFiles().remove(file);
            statusMessages.add(Severity.INFO, "File '#0' removed successfully", file.getSimplePath());
        } catch (Exception e) {
            StatusMessages.instance().addFromResourceBundle(Severity.ERROR, "error.delete.article", e.getLocalizedMessage());
            jcrSession.refresh(false);
            if (e.getCause() != null && e.getCause() instanceof ReferentialIntegrityException) log.info("Could not delete node #0 cause it is still referenced by another node", file.getUuid()); else e.printStackTrace();
        }
    }

    /**
     * recursive removing a folder
     * 
     * @param folderTree
     * @param folder
     * @throws RepositoryException
     */
    public void removeFolder(FolderTree folderTree, Folder folder) throws RepositoryException {
        for (Folder subfolder : new ArrayList<Folder>(folder.getFolders())) {
            removeFolder(folderTree, subfolder);
        }
        for (ContentFile file : new ArrayList<ContentFile>(folder.getFiles())) {
            removeFile(file);
        }
        folderDao.remove(folder.getPath());
        if (folderTree.getSelectedFolder().equals(folder)) {
            folderTree.setSelectedFolder(folder.getParent());
        }
        log.info("Deleting folder {0}", folder.getPath());
    }

    public void rename(Folder folder) {
        Folder beforeFolder = null;
        if (folder.getParent() != null) {
            List<Folder> containingFolder = folder.getParent().getFolders();
            int indexOfSelectedFolder = containingFolder.indexOf(folder);
            if (indexOfSelectedFolder < containingFolder.size() - 1) {
                beforeFolder = containingFolder.get(indexOfSelectedFolder + 1);
            }
        }
        folderDao.update(folder, "none", 0);
        if (beforeFolder != null) {
            folderDao.orderBefore(folder, beforeFolder);
        }
        log.info("Folder renamed: {0}", folder.getName());
    }

    public void add(FolderTree folderTree) {
        Folder folder = new Folder("NewFolder");
        log.info("Adding folder {0}", folder.getName());
        if (folderTree.getSelectedFolder() == null) {
            folder = folderDao.create(folderTree.getJcrRootPath(), folder);
            folderTree.getRoots().add(folder);
        } else {
            folder = folderDao.create(folderTree.getSelectedFolder(), folder);
            folderTree.getSelectedFolder().getFolders().add(folder);
        }
    }
}
