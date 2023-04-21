package com.aptana.ide.core.ui.io.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.filechooser.FileSystemView;
import org.eclipse.swt.graphics.Image;
import com.aptana.ide.core.StringUtils;
import com.aptana.ide.core.io.IVirtualFile;
import com.aptana.ide.core.io.IVirtualFileManager;
import com.aptana.ide.core.io.VirtualFile;
import com.aptana.ide.core.ui.CoreUIUtils;
import com.aptana.ide.core.ui.ImageUtils;

/**
 * @author Robin Debreuil
 */
public class LocalFile extends VirtualFile {

    /**
	 * filesys
	 */
    protected static FileSystemView filesys;

    private File _file;

    private LocalFileManager _manager;

    private long _permissions = 0;

    private Image _image = null;

    static {
        if (CoreUIUtils.runningOnMac == false) {
            filesys = FileSystemView.getFileSystemView();
        }
    }

    /**
	 * LocalFile
	 * 
	 * @param manager
	 * @param file
	 */
    public LocalFile(LocalFileManager manager, File file) {
        this._file = file;
        this._manager = manager;
    }

    /**
	 * getFile
	 * 
	 * @return File
	 */
    public File getFile() {
        return this._file;
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#getName() 
	 */
    public String getName() {
        String fileName = this._file.getName();
        if (!CoreUIUtils.runningOnMac) {
            if (fileName == null || StringUtils.EMPTY.equals(fileName) || !filesys.isFileSystem(this._file)) {
                String tempFileName = filesys.getSystemDisplayName(this._file);
                if (tempFileName != null) {
                    fileName = tempFileName;
                }
            }
        }
        return fileName;
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#getExtension()
	 */
    public String getExtension() {
        String result = "";
        String path = this._file.getName();
        int lastDot = path.lastIndexOf(".");
        if (lastDot > -1 && lastDot < path.length() - 2) {
            result = path.substring(lastDot);
        }
        return result;
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#getAbsolutePath()
	 */
    public String getAbsolutePath() {
        try {
            return this._file.getCanonicalPath();
        } catch (IOException e) {
            return this._file.getAbsolutePath();
        }
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#getPath()
	 */
    public String getPath() {
        return this._file.getPath();
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#isDirectory()
	 */
    public boolean isDirectory() {
        if (CoreUIUtils.runningOnWindows && (this._file.getName().endsWith(".zip") || this._file.getName().endsWith(".rar"))) {
            return false;
        } else {
            return this._file.isDirectory();
        }
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#isFile()
	 */
    public boolean isFile() {
        return this._file.isFile();
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#getFileManager()
	 */
    public IVirtualFileManager getFileManager() {
        return this._manager;
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#hasFiles()
	 */
    public boolean hasFiles() {
        return hasFiles(false);
    }

    /**
	 * hasFiles
	 *
	 * @param includeCloakedFiles
	 * @return boolean
	 */
    public boolean hasFiles(boolean includeCloakedFiles) {
        return isDirectory();
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#getFiles()
	 */
    public IVirtualFile[] getFiles() throws IOException {
        return this._manager.getFiles(this);
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#getFiles(boolean, boolean)
	 */
    public IVirtualFile[] getFiles(boolean recurse, boolean includeCloakedFiles) throws IOException {
        return this._manager.getFiles(this, recurse, includeCloakedFiles);
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#getGroup()
	 */
    public String getGroup() {
        return "";
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#setGroup(java.lang.String)
	 */
    public void setGroup(String group) {
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#getParentFile()
	 */
    public IVirtualFile getParentFile() {
        IVirtualFile result = null;
        File parent = (!CoreUIUtils.runningOnMac) ? filesys.getParentDirectory(this._file) : this._file.getParentFile();
        if (parent != null) {
            result = new LocalFile(this._manager, parent);
        }
        return result;
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#delete()
	 */
    public boolean delete() {
        return this._file.delete();
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#rename(java.lang.String)
	 */
    public boolean rename(String newName) {
        return this._manager.renameFile(this, newName);
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#getCreationMillis()
	 */
    public long getCreationMillis() {
        return 0;
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#getModificationMillis()
	 */
    public long getModificationMillis() {
        return this._file.lastModified();
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#setModificationMillis(long)
	 */
    public void setModificationMillis(long modificationTime) {
        this._file.setLastModified(modificationTime);
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#getOwner()
	 */
    public String getOwner() {
        return null;
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#setOwner(java.lang.String)
	 */
    public void setOwner(String owner) {
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#setPermissions(long)
	 */
    public void setPermissions(long permissions) {
        this._permissions = permissions;
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#getPermissions()
	 */
    public long getPermissions() {
        return this._permissions;
    }

    /**
	 * getSize
	 * @return long
	 */
    public long getSize() {
        return this._file.length();
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#getStream()
	 */
    public InputStream getStream() {
        return this._manager.getStream(this);
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#putStream(java.io.InputStream)
	 */
    public void putStream(InputStream input) throws IOException {
        this._manager.putStream(input, this);
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#getImage()
	 */
    public Image getImage() {
        if (_image == null) {
            return ImageUtils.getIcon(this._file, null);
        } else {
            return _image;
        }
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#setImage(org.eclipse.swt.graphics.Image)
	 */
    public void setImage(Image image) {
        _image = image;
    }

    /**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    public int compareTo(Object o) {
        if (o instanceof LocalFile) {
            return this.getName().compareToIgnoreCase(((LocalFile) o).getName());
        } else {
            return 0;
        }
    }

    /**
	 * package local method to reset the internal file, needed for 'rename'.
	 * 
	 * @param file
	 */
    void setInternalFile(File file) {
        this._file = file;
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#canRead()
	 */
    public boolean canRead() {
        boolean result = false;
        if (this._file != null) {
            result = this._file.canRead();
        }
        return result;
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#canWrite()
	 */
    public boolean canWrite() {
        boolean result = false;
        if (this._file != null) {
            result = this._file.canWrite();
        }
        return result;
    }

    /**
	 * @see com.aptana.ide.core.io.IVirtualFile#exists()
	 */
    public boolean exists() {
        return this._file.exists();
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof LocalFile) {
            String s = ((LocalFile) arg0).getAbsolutePath();
            if (s.equals(this.getAbsolutePath())) {
                result = true;
            }
        }
        return result;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    public int hashCode() {
        return this.getAbsolutePath().hashCode();
    }

    /**
	 * @see IVirtualFile#getRelativePath()
	 */
    public String getRelativePath() {
        String basePath = "";
        if (this.getFileManager().getBasePath() != null) {
            basePath = this.getFileManager().getBaseFile().getAbsolutePath();
        }
        if (basePath.length() <= this.getAbsolutePath().length()) {
            return this.getAbsolutePath().substring(basePath.length());
        } else {
            return this.getAbsolutePath();
        }
    }

    /**
	 * @see IVirtualFile#setCloaked(boolean)
	 */
    public void setCloaked(boolean cloak) {
        if (cloak) {
            this._manager.addCloakedFile(this);
        } else {
            this._manager.removeCloakedFile(this);
        }
    }

    /**
	 * @see IVirtualFile#isCloaked()
	 */
    public boolean isCloaked() {
        return this._manager.isFileCloaked(this);
    }
}
