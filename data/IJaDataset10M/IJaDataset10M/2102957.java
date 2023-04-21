package org.aarboard.ckeditor.connector.providers.filesystem;

import com.vaadin.data.Property;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.aarboard.ckeditor.connector.VaadinBrowser;
import org.aarboard.ckeditor.connector.providers.IFolder;
import org.aarboard.ckeditor.connector.providers.IObject;
import org.aarboard.ckeditor.connector.providers.ProviderStatus;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author a.schild
 */
public class Folder implements IFolder {

    private static final Log _log = LogFactory.getLog(Folder.class);

    protected File _mySelf;

    protected Folder _parent;

    protected boolean _isFileBrowser = true;

    protected ArrayList<IFolder> _childrenFolders;

    public Folder(File myself, boolean isFileBrowser) {
        _mySelf = myself;
        _parent = null;
        _isFileBrowser = isFileBrowser;
    }

    public Folder(File myself, Folder parent, boolean isFileBrowser) {
        _mySelf = myself;
        _parent = parent;
        _isFileBrowser = isFileBrowser;
    }

    public List<IFolder> getChildrenFolders() {
        if (_childrenFolders == null) {
            DirectoryFilter dFilter = new DirectoryFilter(true);
            File children[] = _mySelf.listFiles(dFilter);
            _childrenFolders = new ArrayList<IFolder>();
            for (File child : children) {
                _childrenFolders.add(new Folder(child, this, _isFileBrowser));
            }
        }
        return _childrenFolders;
    }

    public List<IObject> getChildrenObjects() {
        DirectoryFilter dFilter = new DirectoryFilter(false);
        File children[] = _mySelf.listFiles(dFilter);
        ArrayList<IObject> retVal = new ArrayList<IObject>();
        for (File child : children) {
            retVal.add(new FileObject(child, this, _isFileBrowser));
        }
        return retVal;
    }

    public String getName() {
        return _mySelf.getName();
    }

    public boolean hasChildren() {
        List<IFolder> children = getChildrenFolders();
        return !children.isEmpty();
    }

    public IFolder getParent() {
        return _parent;
    }

    public boolean isRenameAllowed() {
        return _parent != null;
    }

    public boolean isDeleteAllowed() {
        return _parent != null;
    }

    public boolean isCreateFolderAllowed() {
        return true;
    }

    public boolean isUploadAllowed() {
        return true;
    }

    public ProviderStatus handleUpload(File file, String fileName, String mimeType, long length) {
        ProviderStatus retVal = null;
        File newFile = new File(_mySelf.getAbsolutePath() + File.separator + fileName);
        if (newFile.exists()) {
            String baseName = FilenameUtils.removeExtension(fileName);
            String extension = FilenameUtils.getExtension(fileName);
            int i = 1;
            newFile = new File(_mySelf.getAbsolutePath() + File.separator + baseName + "-" + i + "." + extension);
            while (newFile.exists()) {
                i++;
                newFile = new File(_mySelf.getAbsolutePath() + File.separator + baseName + "-" + i + "." + extension);
            }
            FileObject newObject = new FileObject(newFile, this, _isFileBrowser);
            retVal = new ProviderStatus(ProviderStatus.Status.FILE_RENAMED, baseName + "-" + i + "." + extension, newObject);
        } else {
            FileObject newObject = new FileObject(newFile, this, _isFileBrowser);
            retVal = new ProviderStatus(ProviderStatus.Status.OPERATION_OK, null, newObject);
        }
        try {
            FileUtils.moveFile(file, newFile);
        } catch (IOException ex) {
            _log.error("Error in file move", ex);
        }
        return retVal;
    }

    public boolean handleFileRename(String oldName, String newName) {
        File oldFile = new File(_mySelf + File.separator + oldName);
        return oldFile.renameTo(new File(newName));
    }

    public Property getItemProperty(Object id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<?> getItemPropertyIds() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean handleFolderDelete() {
        return _mySelf.delete();
    }

    public boolean handleFolderRename(String newName) {
        return _mySelf.renameTo(new File(_mySelf.getParent() + File.separator + newName));
    }

    public boolean handleFolderCreation(String newFolderName) {
        File newFolder = new File(_mySelf.getAbsolutePath() + File.separator + newFolderName);
        return newFolder.mkdir();
    }

    /**
     * In the file browser we don't allow selecting folders
     * In the link browser we allow selecting folders
     * 
     * @return True when this folder can be selected
     */
    public boolean isSelectAllowed() {
        return !_isFileBrowser;
    }

    public String getURL() {
        String retVal = null;
        String rootPath = VaadinBrowser.getContextString("ckeditor.filesystem.rootfolder", "C:\tmp");
        String rootURL = VaadinBrowser.getContextString("ckeditor.filesystem.rooturl", "http://localhost/");
        String filePath = _mySelf.getAbsolutePath();
        if (filePath.startsWith(rootPath)) {
            String remainingPart = filePath.substring(rootPath.length());
            retVal = rootURL + remainingPart.replace('\\', '/');
        } else {
            retVal = filePath;
        }
        return retVal;
    }
}
