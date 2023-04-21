package net.scharlie.lumberjack4logs.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.filechooser.FileSystemView;

/**
 * This class is necessary due to an annoying bug on Windows NT where
 * instantiating a JFileChooser with the default FileSystemView will cause a
 * "drive A: not ready" error every time. I grabbed the Windows FileSystemView
 * impl from the 1.3 SDK and modified it so as to not use
 * java.io.File.listRoots() to get fileSystem roots. java.io.File.listRoots()
 * does a SecurityManager.checkRead() which causes the OS to try to access drive
 * A: even when there is no disk, causing an annoying "abort, retry, ignore"
 * popup message every time we instantiate a JFileChooser!
 * 
 * Instead of calling listRoots() we use a straightforward alternate method of
 * getting file system roots.
 * 
 * @author gentsch / scharlemann
 */
public class WindowsAltFileSystemView extends FileSystemView {

    public WindowsAltFileSystemView() {
    }

    /**
     * Returns true if the given file is a root.
     */
    @Override
    public boolean isRoot(final File f) {
        if (!f.isAbsolute()) {
            return false;
        }
        final String parentPath = f.getParent();
        if (parentPath == null) {
            return true;
        } else {
            final File parent = new File(parentPath);
            return parent.equals(f);
        }
    }

    /**
     * creates a new folder with a default folder name.
     */
    @Override
    public File createNewFolder(final File containingDir) throws IOException {
        if (containingDir == null) {
            throw new IOException("Containing directory is null:");
        }
        File newFolder = null;
        newFolder = createFileObject(containingDir, "New Folder");
        int i = 2;
        while (newFolder.exists() && i < 100) {
            newFolder = createFileObject(containingDir, "New Folder (" + i + ")");
            i++;
        }
        if (newFolder.exists()) {
            throw new IOException("Directory already exists:" + newFolder.getAbsolutePath());
        } else {
            newFolder.mkdirs();
        }
        return newFolder;
    }

    /**
     * Returns whether a file is hidden or not. On Windows there is currently no
     * way to get this information from io.File, therefore always return false.
     */
    @Override
    public boolean isHiddenFile(final File f) {
        return false;
    }

    /**
     * Returns all root partitians on this system. On Windows, this will be the
     * A: through Z: drives.
     */
    @Override
    public File[] getRoots() {
        final List<File> rootsList = new ArrayList<File>();
        final FileSystemRoot floppy = new FileSystemRoot("A" + ":" + "\\");
        rootsList.add(floppy);
        for (char c = 'C'; c <= 'Z'; c++) {
            final char device[] = { c, ':', '\\' };
            final String deviceName = new String(device);
            final File deviceFile = new FileSystemRoot(deviceName);
            if (deviceFile != null && deviceFile.exists()) {
                rootsList.add(deviceFile);
            }
        }
        return rootsList.toArray(new File[0]);
    }

    private static class FileSystemRoot extends File {

        private static final long serialVersionUID = -2873411406778652832L;

        public FileSystemRoot(final File f) {
            super(f, "");
        }

        public FileSystemRoot(final String s) {
            super(s);
        }

        @Override
        public boolean isDirectory() {
            return true;
        }
    }
}
