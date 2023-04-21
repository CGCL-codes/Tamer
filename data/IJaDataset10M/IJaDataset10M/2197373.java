package filemanager.vfs.impl;

import filemanager.vfs.PermissionIfc;
import filemanager.vfs.ReadableFileIfc;
import java.beans.PropertyChangeListener;
import java.net.URI;

/**
 *
 * @author sahaqiel
 */
public class UnionFolder implements ReadableFileIfc {

    @Override
    public void addFileListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean exists() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getBackingObject() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLastModified() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getMimeType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ReadableFileIfc getParent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PermissionIfc getPermissions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getPrefix() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FileTypes getType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URI getURI() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isHidden() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeListeners() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(ReadableFileIfc o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
