package com.jiexplorer.model;

import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import com.jiexplorer.jobs.IProgressMonitor;
import com.jiexplorer.util.UID;

public class RootNode extends AbstractTreeNode implements IFileNode {

    public RootNode() {
        parent = null;
    }

    public void update(final File file) {
        name = file.getName().length() > 0 ? file.getName() : file.toURI().getPath();
        if (osName.startsWith("win") && name.indexOf('/') == 0) {
            name = name.substring(1);
        }
        if (parent != null) {
            parent.fireNodesUpdated(new ITreeNode[] { this });
        }
    }

    public void deleteDeep() {
    }

    public boolean exists() {
        return true;
    }

    public String getAbsolutePath() {
        return "";
    }

    public String getCRC32() {
        return null;
    }

    public String getDateStr() {
        return null;
    }

    public String getDisplayName() {
        return "My Computer";
    }

    public String getEscapedPortablePath() throws MalformedURLException {
        return null;
    }

    public File getFile() {
        return null;
    }

    public String getSizeStr() {
        return "";
    }

    public String getSuffix() {
        return "";
    }

    public String getTypeStr() {
        return null;
    }

    public boolean isDirectory() {
        return true;
    }

    public boolean isFile() {
        return false;
    }

    public long lastModified() {
        return 0;
    }

    public long length() {
        return 0;
    }

    public void rename(final File file) {
    }

    public String toExternalForm() {
        return null;
    }

    public URI toURI() {
        return null;
    }

    public URL toURL() throws MalformedURLException {
        return null;
    }

    public Image getImage() {
        return null;
    }

    @Override
    public Object getNodeObject() {
        return null;
    }

    @Override
    public ITreeNode getParent() {
        return null;
    }

    @Override
    public boolean hasChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public String getName() {
        return "My Computer";
    }

    @Override
    protected void createChildren() {
    }

    public void deleteDeep(final IProgressMonitor monitor) {
    }

    public void setImage(final Image image) {
    }

    public String getPath() {
        return "";
    }

    public String getParentPath() {
        return "";
    }

    public String generateUid() {
        final UID uuid = new UID();
        uid = uuid.getUidStr();
        return uid;
    }

    @Override
    public String toString() {
        return getName();
    }

    public void setDate(final long date) {
    }

    public void setSize(final long size) {
    }
}
