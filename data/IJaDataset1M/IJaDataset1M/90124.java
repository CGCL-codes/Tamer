package org.torweg.pulse.vfs.filebrowser;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.torweg.pulse.accesscontrol.User;
import org.torweg.pulse.vfs.VirtualFile;
import org.torweg.pulse.vfs.VirtualFileSystem;

/**
 * @author Thomas Weber
 * @version $Revision: 1518 $
 */
public class VFSBrowsingAdapter implements BrowsingAdapter {

    /**
	 * the logger.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(VFSBrowsingAdapter.class);

    /**
	 * the base URI for the {@code VFSBrowsingAdapter}.
	 */
    private URI baseURI;

    /**
	 * sets the base URI for the {@code VFSBrowsingAdapter}.
	 * 
	 * @param uri
	 *            the base URI for the {@code VFSBrowsingAdapter}
	 * @see org.torweg.pulse.vfs.filebrowser.BrowsingAdapter#setBaseURI(java.net.URI)
	 */
    public final void setBaseURI(final URI uri) {
        this.baseURI = uri;
    }

    /**
	 * returns the root directories of the {@code VFSBrowsingAdapter}.
	 * 
	 * @param u
	 *            the {@code User} used for access
	 * @return the root directories of the {@code VFSBrowsingAdapter}
	 * @throws IOException
	 *             on I/O errors
	 */
    public final List<VFSDirectoryDescriptor> getRootDirectories(final User u) throws IOException {
        VirtualFile root = VirtualFileSystem.getInstance().getVirtualFile(this.baseURI, u);
        if (!root.exists()) {
            throw new IOException("The given base URI " + this.baseURI + " does not exist.");
        }
        if (!root.isDirectory()) {
            throw new IOException("The given base URI " + this.baseURI + " is not a directory.");
        }
        DirectoryDescriptor descriptor = new VFSDirectoryDescriptor(root);
        return getChildDirectories(descriptor, u);
    }

    /**
	 * returns the {@code DirectoryDescriptor}s for the given {@code
	 * DirectoryDescriptor}.
	 * 
	 * @param d
	 *            the directory
	 * @param u
	 *            the user for access checks
	 * @return the {@code DirectoryDescriptor}s for the given {@code
	 *         DirectoryDescriptor}
	 */
    public final List<VFSDirectoryDescriptor> getChildDirectories(final DirectoryDescriptor d, final User u) {
        try {
            return ((VFSDirectoryDescriptor) d).getChildren(u);
        } catch (IOException e) {
            LOGGER.warn(e.getLocalizedMessage());
            return new ArrayList<VFSDirectoryDescriptor>();
        }
    }

    /**
	 * returns the {@code FileDescriptor}s for the {@code DirectoryDescriptor}.
	 * 
	 * @param d
	 *            the directory
	 * @param u
	 *            the user for access checks
	 * @return the {@code FileDescriptor}s for the {@code DirectoryDescriptor}.
	 */
    public final List<? extends FileDescriptor> getFiles(final DirectoryDescriptor d, final User u) {
        try {
            return ((VFSDirectoryDescriptor) d).getFiles(u);
        } catch (IOException e) {
            LOGGER.warn(e.getLocalizedMessage());
            return new ArrayList<FileDescriptor>();
        }
    }

    /**
	 * returns a {@code DirectoryDescriptor} for the given {@code URI} .
	 * 
	 * @param dirUri
	 *            the URI
	 * @param u
	 *            the user for access checks
	 * @return a {@code DirectoryDescriptor} for the given {@code URI} , or
	 *         {@code null}, if either the {@code URI} does not denote a
	 *         directory, the {@code URI} does not exist or the {@code URI} is
	 *         not below the base path.
	 */
    public final VFSDirectoryDescriptor getDirectoryDescriptor(final URI dirUri, final User u) {
        VirtualFile vfd;
        try {
            vfd = VirtualFileSystem.getInstance().getVirtualFile(dirUri, u);
        } catch (IOException e) {
            LOGGER.info("Denied DirectoryDescriptor creation for {}: {}", dirUri, e.getLocalizedMessage());
            return null;
        }
        if (!vfd.getURI().equals(this.baseURI) && !jailRootCheck(vfd.getParent())) {
            LOGGER.info("Denied DirectoryDescriptor creation for {}: failed jail root check", dirUri);
            return null;
        }
        return new VFSDirectoryDescriptor(vfd);
    }

    /**
	 * returns a {@code FileDescriptor} for the given {@code URI}.
	 * 
	 * @param fileUri
	 *            the URI
	 * @param u
	 *            the {@code User} used for access
	 * @return a {@code FileDescriptor} for the given {@code URI}, or {@code
	 *         null}, if either the {@code URI} does not denote a file, the
	 *         {@code URI} does not exist or the {@code URI} is not below the
	 *         base path.
	 */
    public final FileDescriptor getFileDescriptor(final URI fileUri, final User u) {
        VirtualFile vfd;
        try {
            vfd = VirtualFileSystem.getInstance().getVirtualFile(fileUri, u);
        } catch (IOException e) {
            LOGGER.info("Denied FileDescriptor creation for {}: {}", fileUri, e.getLocalizedMessage());
            return null;
        }
        if (!vfd.getURI().equals(this.baseURI) && !jailRootCheck(vfd.getParent())) {
            LOGGER.info("Denied DirectoryDescriptor creation for {}: failed jail root check", fileUri);
            return null;
        }
        return new VFSFileDescriptor(vfd);
    }

    /**
	 * checks if the given {@code VirtualFile} is part of the jail root.
	 * 
	 * @param f
	 *            the file to check
	 * @return {@code true}, if the file is part of the jail root; otherwise
	 *         {@code false}
	 */
    private boolean jailRootCheck(final VirtualFile f) {
        if (!f.getURI().equals(this.baseURI)) {
            if (f.getParent() != null) {
                return jailRootCheck(f.getParent());
            }
            return false;
        } else {
            return true;
        }
    }
}
