package it.infodea.tapestrydea.pages.demodea.jcr;

import it.infodea.tapestrydea.components.jcr.FilesNavigator;
import it.infodea.tapestrydea.components.jcr.FoldersNavigator;
import it.infodea.tapestrydea.components.jcr.VersionsNavigator;
import it.infodea.tapestrydea.services.jcr.JcrWorkspaceService;
import it.infodea.tapestrydea.services.jcr.nodes.wrappers.JcrFileNode;
import it.infodea.tapestrydea.services.jcr.nodes.wrappers.JcrFolderNode;
import it.infodea.tapestrydea.support.annotations.PageInfo;
import it.infodea.tapestrydea.support.enumerations.SecurityGroup;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.version.Version;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;

@PageInfo(category = "repository", allowedSecurityGroup = SecurityGroup.ANONYMOUS, mode = PageInfo.MODE_NORMAL)
public class TestJcr {

    @Property
    private UploadedFile file;

    @Property
    private String name;

    @Inject
    private JcrWorkspaceService service;

    @Persist
    private String currentFolderPath;

    @Persist
    private JcrFileNode selectedFile;

    @Persist("flash")
    private Version selectedVersion;

    @Persist("flash")
    private List<JcrFileNode> files;

    @Component
    private Form fileform;

    @Component
    private Form folderform;

    @Component
    private Form versionForm;

    @BeginRender
    void prepare() throws RepositoryException {
        if (currentFolderPath == null || "/".equals(currentFolderPath)) {
            currentFolderPath = "/";
            files = new ArrayList<JcrFileNode>();
        }
    }

    @OnEvent(FoldersNavigator.SELECT_FOLDER_EVENT)
    void selectFolder(JcrFolderNode folder) throws RepositoryException {
        currentFolderPath = folder.getPath();
        files = new ArrayList<JcrFileNode>();
        selectedFile = null;
        selectedVersion = null;
    }

    @OnEvent(FilesNavigator.SELECT_FILE_EVENT)
    void selectFile(JcrFileNode file) throws RepositoryException {
        selectedFile = file;
        selectedVersion = null;
    }

    @OnEvent(FilesNavigator.CHECKOUT_FILE_EVENT)
    void checkoutFile(JcrFileNode file) throws RepositoryException {
        file.checkOut();
    }

    @OnEvent(FilesNavigator.UNDO_CHECKOUT_FILE_EVENT)
    void unodCheckoutFile(JcrFileNode file) throws RepositoryException {
        file.undoCheckOut();
    }

    @OnEvent(FilesNavigator.CHECKIN_FILE_EVENT)
    void checkinFile(JcrFileNode file) throws RepositoryException {
        file.checkIn();
    }

    @OnEvent(FilesNavigator.DELETE_FILE_EVENT)
    void deleteFile(JcrFileNode file) throws RepositoryException {
        String selectedUUID = "";
        if (selectedFile != null) {
            selectedUUID = selectedFile.getUUID();
        }
        String testUuid = file.getUUID();
        file.remove();
        if (testUuid.equals(selectedUUID)) {
            selectedFile = null;
        }
    }

    @OnEvent(VersionsNavigator.SELECT_VERSION_EVENT)
    void selectVersion(JcrFileNode file, Version version) throws RepositoryException {
        selectedVersion = version;
    }

    @OnEvent(VersionsNavigator.RESTORE_VERSION_EVENT)
    void restoreVersion(JcrFileNode file, Version version) throws RepositoryException {
        if (file.isCheckedOut()) {
            file.restoreVersion(version);
        } else {
            versionForm.recordError("The file " + file.getName() + " must be checkedout to allow version restoring.");
        }
    }

    @OnEvent(value = "success", component = "folderform")
    void addFolder() throws RepositoryException {
        JcrFolderNode folder = service.getFolder(currentFolderPath);
        if (folder.contains(name)) {
            folderform.recordError("The folder " + name + " already exists in folder " + currentFolderPath + ". Choose different name.");
        } else {
            folder.createFolder(name);
        }
    }

    @OnEvent(value = "success", component = "fileform")
    void addFile() throws RepositoryException {
        JcrFolderNode folder = service.getFolder(currentFolderPath);
        String fileName = file.getFileName();
        if (folder.contains(fileName)) {
            JcrFileNode fileToBeUpdate = folder.getFile(fileName);
            if (fileToBeUpdate.isCheckedOut()) {
                fileToBeUpdate.updateDataStream(file.getStream());
            } else {
                fileform.recordError("The file " + fileName + " already exists in folder " + currentFolderPath + ". You must checkout corresponding file before upload and override.");
            }
        } else {
            folder.createFile(fileName, null, file.getStream());
        }
    }

    @OnEvent(value = "deleteNode")
    void deleteNode(String path) throws RepositoryException {
        service.removeNode(path);
    }

    public boolean isRoot() {
        return "/".equals(currentFolderPath);
    }

    public String getSelectedFileId() throws ValueFormatException, PathNotFoundException, RepositoryException {
        return selectedFile != null ? selectedFile.getUUID() : null;
    }

    public String getCurrentFolderPath() {
        return currentFolderPath;
    }

    public List<JcrFileNode> getFiles() {
        return files;
    }

    public JcrFileNode getSelectedFile() {
        return selectedFile;
    }

    public JcrFileNode getSelectedVersion() throws RepositoryException {
        return new JcrFileNode(selectedVersion.getNode("jcr:frozenNode"));
    }

    public boolean isFileSelected() {
        return selectedFile != null;
    }

    public boolean isVersionSelected() {
        return selectedVersion != null;
    }

    public String getPreviewVersionTitle() throws ValueFormatException, PathNotFoundException, RepositoryException {
        String title = "";
        if (selectedFile != null && selectedVersion != null) {
            title = "Preview file " + selectedFile.getName() + " version " + selectedVersion.getName();
        }
        return title;
    }
}
