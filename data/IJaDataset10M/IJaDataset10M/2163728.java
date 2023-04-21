package net.sourceforge.javautil.network.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import net.sourceforge.javautil.common.exception.ThrowableManagerRegistry;
import net.sourceforge.javautil.common.io.IVirtualArtifact;
import net.sourceforge.javautil.common.io.VirtualArtifactNotFoundException;
import net.sourceforge.javautil.common.io.IVirtualDirectory;
import net.sourceforge.javautil.common.io.IVirtualPath;
import net.sourceforge.javautil.common.io.impl.SimplePath;
import net.sourceforge.javautil.common.io.remote.IRemoteDirectory;
import net.sourceforge.javautil.common.io.remote.IRemoteFile;
import net.sourceforge.javautil.common.io.remote.IRemoteLocation;
import net.sourceforge.javautil.common.io.remote.impl.RemoteDirectoryAbstract;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;

/**
 * Secure File Transfer Protocol over a {@link SecureShellSession}.
 *
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class SecureFTP implements IRemoteLocation {

    protected final SecureShellSession session;

    protected final ChannelSftp ftp;

    protected SecureFTPDirectory root;

    /**
	 * This will be prepended to all file/directory related commands
	 */
    protected String rootPrefix;

    public SecureFTP(SecureShellSession session, ChannelSftp ftp) {
        this(session, ftp, "/");
    }

    public SecureFTP(SecureShellSession session, ChannelSftp ftp, String rootPrefix) {
        this.session = session;
        this.ftp = ftp;
        try {
            this.ftp.connect();
        } catch (JSchException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
        this.setRootPrefix(rootPrefix);
    }

    /**
	 * @return The {@link #rootPrefix}
	 */
    public String getRootPrefix() {
        return rootPrefix;
    }

    public void setRootPrefix(String rootPrefix) {
        this.rootPrefix = rootPrefix;
        this.root = new SecureFTPDirectory(this, null, new SimplePath(""), getFiles(new SimplePath(".")).firstElement().getAttrs());
    }

    public List<IVirtualPath> getPaths(IVirtualPath directoryPath) throws IOException {
        try {
            List<IVirtualPath> paths = new ArrayList<IVirtualPath>();
            IVirtualDirectory subdir = root.getDirectory(directoryPath);
            Iterator<IVirtualArtifact> artifacts = subdir.getArtifacts();
            while (artifacts.hasNext()) {
                paths.add(artifacts.next().getPath());
            }
            return paths;
        } catch (VirtualArtifactNotFoundException e) {
            return Collections.EMPTY_LIST;
        }
    }

    public IRemoteFile getRemoteFile(IVirtualPath path) throws IOException {
        try {
            return (IRemoteFile) root.getFile(path);
        } catch (VirtualArtifactNotFoundException e) {
            return null;
        }
    }

    public IRemoteDirectory getRootDirectory() throws IOException {
        return root;
    }

    /**
	 * @param path The path for which to get an input stream for reading/downloading
	 * @return The input stream
	 */
    public InputStream getInputStream(IVirtualPath path) {
        try {
            return ftp.get(this.getRealPath(path));
        } catch (SftpException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }

    /**
	 * @param path The path for which to get an output stream for writing/uploading
	 * @return The output stream
	 */
    public OutputStream getOutputStream(IVirtualPath path) {
        try {
            return ftp.put(this.getRealPath(path), ChannelSftp.OVERWRITE);
        } catch (SftpException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }

    /**
	 * @param path The path to get a list of files for
	 * @return
	 */
    public Vector<LsEntry> getFiles(IVirtualPath path) {
        try {
            return this.ftp.ls(this.getRealPath(path));
        } catch (SftpException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }

    /**
	 * @param path The path of a new directory to make
	 */
    public void makeDirectory(IVirtualPath path) {
        try {
            this.ftp.mkdir(this.getRealPath(path));
        } catch (SftpException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }

    /**
	 * @param path The path to a directory to be removed
	 */
    public void removeDirectory(IVirtualPath path) throws SftpException {
        this.ftp.rmdir(this.getRealPath(path));
    }

    /**
	 * @param path The path to a file to be removed
	 */
    public void removeFile(IVirtualPath path) throws SftpException {
        this.ftp.rm(this.getRealPath(path));
    }

    /**
	 * @param path The path to rename
	 * @param newPath The new name for the path
	 */
    public void rename(IVirtualPath path, IVirtualPath newPath) {
        try {
            this.ftp.rename(this.getRealPath(path), this.getRealPath(newPath));
        } catch (SftpException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }

    /**
	 * Close this ftp connection
	 */
    public void close() {
        this.ftp.disconnect();
    }

    /**
	 * @param path The path to retrieve attributes
	 * @return The attributes, or null if the path does not exist
	 */
    protected SftpATTRS getAttributes(IVirtualPath path) {
        try {
            return ftp.stat(this.getRealPath(path));
        } catch (SftpException e) {
            return null;
        }
    }

    /**
	 * @param path The path to create a real path for
	 * @return A real path including the {@link #rootPrefix} prepended
	 */
    protected String getRealPath(IVirtualPath path) {
        return this.rootPrefix + "/" + path.toString("/");
    }
}
