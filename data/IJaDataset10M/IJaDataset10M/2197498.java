package org.bitdrive.jlan.impl;

import org.alfresco.jlan.server.SrvSession;
import org.alfresco.jlan.server.core.DeviceContext;
import org.bitdrive.Main;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class JlanReadOnlyDevice implements DiskInterface {

    protected Logger logger = Main.getLogger(LogTypes.LOG_FILESYS);

    private final String separator = "\\";

    protected String filterPath(String path) {
        return filterPath(path, false);
    }

    protected String filterPath(String path, boolean search) {
        logger.log(Level.FINEST, "JlanReadOnlyDevice.filterPath: Input path, " + path);
        if (path.equals("")) {
            path = separator;
            return path;
        }
        if (path.endsWith("\\.")) path = path.replaceFirst("\\\\\\.", separator);
        if (!search) {
            if (path.endsWith("*.*")) path = path.replaceFirst("\\*\\.\\*", "");
            if (path.endsWith("\\*")) {
                path = path.replaceFirst("\\*", "");
            }
        }
        logger.log(Level.FINEST, "JlanReadOnlyDevice.filterPath: Output path, " + path);
        return path;
    }

    private String getFileName(TreeConnection tree, FileOpenParams params) {
        DeviceContext ctx = tree.getContext();
        return FileName.buildPath(ctx.getDeviceName(), params.getPath(), null, java.io.File.separatorChar);
    }

    public void createDirectory(SrvSession sess, TreeConnection tree, FileOpenParams params) throws IOException {
        throw new AccessDeniedException("Folder " + getFileName(tree, params) + " is read-only");
    }

    public NetworkFile createFile(SrvSession sess, TreeConnection tree, FileOpenParams params) throws IOException {
        throw new AccessDeniedException("File/Folder " + getFileName(tree, params) + " is read-only");
    }

    public void deleteDirectory(SrvSession sess, TreeConnection tree, String dir) throws IOException {
        throw new AccessDeniedException("Folder " + dir + " is read-only");
    }

    public void deleteFile(SrvSession sess, TreeConnection tree, String name) throws IOException {
        throw new AccessDeniedException("File " + name + " is read-only");
    }

    public void flushFile(SrvSession sess, TreeConnection tree, NetworkFile file) throws IOException {
    }

    public boolean isReadOnly(SrvSession sess, DeviceContext ctx) throws IOException {
        return true;
    }

    public void renameFile(SrvSession sess, TreeConnection tree, String oldName, String newName) throws IOException {
        throw new AccessDeniedException("File " + oldName + " is read-only");
    }

    public void setFileInformation(SrvSession sess, TreeConnection tree, String name, FileInfo info) throws IOException {
        throw new AccessDeniedException("File " + name + " is read-only");
    }

    public void truncateFile(SrvSession sess, TreeConnection tree, NetworkFile file, long siz) throws IOException {
        throw new AccessDeniedException("File " + file.getFullName() + " is read-only");
    }

    public int writeFile(SrvSession sess, TreeConnection tree, NetworkFile file, byte[] buf, int bufoff, int siz, long fileoff) throws IOException {
        throw new AccessDeniedException("File " + file.getFullName() + " is read-only");
    }
}
