package com.l2fprod.common.demo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

/**
 * A File System view which permits to show the DirectoryChooser even
 * in Java Web Start. It circumvents all security exceptions.
 */
public class FakeFileSystemView extends FileSystemView {

    private Map files = new HashMap();

    public FakeFileSystemView() {
        files.put("desktop", new FakeFile("Desktop"));
        files.put("computer", new FakeFile("My Computer"));
        files.put("A", new FakeFile("A"));
        files.put("C", new FakeFile("C"));
        files.put("D", new FakeFile("D"));
        files.put("getFiles(My Computer)", new File[] { (File) files.get("A"), (File) files.get("C"), (File) files.get("D") });
        files.put("network", new FakeFile("My Network Places"));
        files.put("getRoots", new File[] { (File) files.get("desktop") });
        files.put("getFiles(Desktop)", new File[] { (File) files.get("computer"), (File) files.get("network") });
        FakeFile[] folders = new FakeFile[] { new FakeFile("Folder 1"), new FakeFile("Folder 2"), new FakeFile("Folder 3") };
        files.put("getFiles(C)", folders);
        files.put("getFiles(D)", folders);
    }

    @Override
    public File createNewFolder(File containingDir) {
        return null;
    }

    @Override
    public File createFileObject(File dir, String filename) {
        return super.createFileObject(dir, filename);
    }

    @Override
    public File createFileObject(String path) {
        return super.createFileObject(path);
    }

    @Override
    protected File createFileSystemRoot(File f) {
        return super.createFileSystemRoot(f);
    }

    @Override
    public File getChild(File parent, String fileName) {
        return super.getChild(parent, fileName);
    }

    @Override
    public File getDefaultDirectory() {
        return new FakeFile("Default");
    }

    @Override
    public File[] getFiles(File dir, boolean useFileHiding) {
        if (dir.getName().startsWith("Folder")) {
            return new FakeFile[] { new FakeFile(dir.getName() + ".1"), new FakeFile(dir.getName() + ".2"), new FakeFile(dir.getName() + ".3") };
        } else {
            File[] children = (File[]) files.get("getFiles(" + dir.getName() + ")");
            if (children == null) {
                return new File[0];
            } else {
                return children;
            }
        }
    }

    @Override
    public File getHomeDirectory() {
        return new FakeFile("Home");
    }

    @Override
    public File getParentDirectory(File dir) {
        return null;
    }

    @Override
    public File[] getRoots() {
        return (File[]) files.get("getRoots");
    }

    @Override
    public String getSystemDisplayName(File f) {
        return f.getName();
    }

    @Override
    public Icon getSystemIcon(File f) {
        return null;
    }

    @Override
    public String getSystemTypeDescription(File f) {
        return "Description";
    }

    @Override
    public boolean isComputerNode(File dir) {
        return files.get("computer") == dir;
    }

    @Override
    public boolean isDrive(File dir) {
        return "C".equals(dir.getName()) || "D".equals(dir.getName());
    }

    @Override
    public boolean isFileSystem(File f) {
        return false;
    }

    @Override
    public boolean isFileSystemRoot(File dir) {
        return false;
    }

    @Override
    public boolean isFloppyDrive(File dir) {
        return "A".equals(dir.getName());
    }

    @Override
    public boolean isHiddenFile(File f) {
        return false;
    }

    @Override
    public boolean isParent(File folder, File file) {
        return false;
    }

    @Override
    public boolean isRoot(File f) {
        return files.get("desktop") == f;
    }

    @Override
    public Boolean isTraversable(File f) {
        return Boolean.TRUE;
    }

    static class FakeFile extends File {

        public FakeFile(String s) {
            super(s);
        }

        @Override
        public boolean isDirectory() {
            return true;
        }
    }
}
