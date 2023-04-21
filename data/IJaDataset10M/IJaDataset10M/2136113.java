package net.sf.cotta;

import net.sf.cotta.io.OutputManager;
import net.sf.cotta.io.OutputProcessor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The class that represent the directory.  Even though the constructor is public, the usual
 * way to create TDirectory should be through TFile, TDirectory, and TFileFactory
 *
 * @see TFileFactory#directoryFromJavaFile(java.io.File)
 * @see TFileFactory#dir(String)
 * @see TFile#parent()
 * @see TDirectory#parent()
 * @see TDirectory#dir(String)
 * @see TDirectory#dir(TPath)
 */
public class TDirectory extends TEntry {

    /**
   * Constructor that creates the directory to be mainly used internally.
   *
   * @param fileSystem The file system that backs the file
   * @param path       The path to the file
   */
    public TDirectory(FileSystem fileSystem, TPath path) {
        super(fileSystem, path);
    }

    public boolean exists() {
        return fileSystem.dirExists(path);
    }

    public TDirectory ensureExists() throws TIoException {
        if (!fileSystem.dirExists(path)) {
            fileSystem.createDir(path);
        }
        return this;
    }

    /**
   * Constucts a file given the file name
   *
   * @param relativePath the relative path of the file
   * @return The file that is under the directory with the name
   * @see #file(TPath)
   */
    public TFile file(String relativePath) {
        return file(TPath.parse(relativePath));
    }

    /**
   * Constructs a file given the relative path
   *
   * @param relativePath The relative path to the current directory
   * @return The file that is of the relative to the current directory
   */
    public TFile file(TPath relativePath) {
        return new TFile(fileSystem, path.join(relativePath));
    }

    /**
   * Constructs a subdirectory given the directory name
   *
   * @param relativePath the relative path of the subdirectory
   * @return The directory that is under the current directory with the given name
   */
    public TDirectory dir(String relativePath) {
        return dir(TPath.parse(relativePath));
    }

    /**
   * Constructs a directory given the relative path to the current directory
   *
   * @param relativePath the relative path of the target directory to current directory
   * @return The target directory that is of the given the relative path
   */
    public TDirectory dir(TPath relativePath) {
        return new TDirectory(fileSystem, path.join(relativePath));
    }

    public TDirectory[] listDirs() throws TIoException {
        return listDirs(TDirectoryFilter.ALL);
    }

    public TDirectory[] listDirs(TDirectoryFilter directoryFilter) throws TIoException {
        checkDirectoryExists();
        TPath[] paths = fileSystem.listDirs(this.path);
        List directories = new ArrayList(paths.length);
        for (int i = 0; i < paths.length; i++) {
            TDirectory candidate = new TDirectory(fileSystem, paths[i]);
            if (directoryFilter.accept(candidate)) {
                directories.add(candidate);
            }
        }
        return (TDirectory[]) directories.toArray(new TDirectory[directories.size()]);
    }

    private void checkDirectoryExists() throws TDirectoryNotFoundException {
        if (!fileSystem.dirExists(path)) {
            throw new TDirectoryNotFoundException(path);
        }
    }

    public TFile[] listFiles() throws TIoException {
        return listFiles(TFileFilter.ALL);
    }

    public TFile[] listFiles(TFileFilter fileFilter) throws TIoException {
        checkDirectoryExists();
        TPath[] tPaths = fileSystem.listFiles(path);
        List files = new ArrayList(tPaths.length);
        for (int i = 0; i < tPaths.length; i++) {
            TFile candidate = new TFile(fileSystem, tPaths[i]);
            if (fileFilter.accept(candidate)) {
                files.add(candidate);
            }
        }
        return (TFile[]) files.toArray(new TFile[files.size()]);
    }

    public String toString() {
        return "TDirectory " + path();
    }

    public void delete() throws TIoException {
        fileSystem.deleteDirectory(path);
    }

    public void deleteAll() throws TIoException {
        TDirectory[] subDirectory = listDirs();
        for (int i = 0; i < subDirectory.length; i++) {
            subDirectory[i].deleteAll();
        }
        TFile[] files = listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
        delete();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TDirectory directory = (TDirectory) o;
        return fileSystem.equals(directory.fileSystem) && path.equals(directory.path);
    }

    public void mergeTo(TDirectory target) throws TIoException {
        target.ensureExists();
        copySubDirectories(target);
        copyFiles(target);
    }

    private void copySubDirectories(TDirectory target) throws TIoException {
        TDirectory[] subdirs = listDirs();
        for (int i = 0; i < subdirs.length; i++) {
            TDirectory subdir = subdirs[i];
            subdir.mergeTo(target.dir(subdir.name()));
        }
    }

    private void copyFiles(TDirectory target) throws TIoException {
        TFile[] files = listFiles();
        for (int i = 0; i < files.length; i++) {
            TFile file = files[i];
            file.copyTo(target.file(file.name()));
        }
    }

    public void moveTo(TDirectory target) throws TIoException {
        if (!exists()) {
            throw new TFileNotFoundException(path);
        }
        if (target.exists()) {
            throw new TIoException(target.path, "Destination exists");
        }
        if (fileSystem == target.fileSystem || fileSystem.equals(target.fileSystem)) {
            fileSystem.moveDirectory(path, target.path);
        } else {
            this.mergeTo(target);
            delete();
        }
    }

    /**
   * @return java.io.File presentation of the directory
   * @deprecated use #toJavaFile()
   */
    public File getJavaFile() {
        return toJavaFile();
    }

    public void zipTo(TFile file) throws TIoException {
        file.write(new OutputProcessor() {

            public void process(OutputManager outputManager) throws IOException {
                ZipOutputStream zipStream = new ZipOutputStream(outputManager.outputStream());
                outputManager.registerResource(zipStream);
                addDirEntry(zipStream, "", TDirectory.this);
            }

            private void addDirEntry(ZipOutputStream zipStream, String path, TDirectory directory) throws IOException {
                TFile[] files = directory.listFiles();
                for (int i = 0; i < files.length; i++) {
                    addFileEntry(zipStream, path, files[i]);
                }
                TDirectory[] directories = directory.listDirs();
                for (int i = 0; i < directories.length; i++) {
                    TDirectory subDirectory = directories[i];
                    addDirEntry(zipStream, path + "/" + subDirectory.name(), subDirectory);
                }
                zipStream.putNextEntry(new ZipEntry(path + "/"));
                zipStream.closeEntry();
            }

            private void addFileEntry(ZipOutputStream zipStream, String path, TFile file) throws IOException {
                ZipEntry entry = new ZipEntry(path + file.name());
                zipStream.putNextEntry(entry);
                file.copyTo(zipStream);
                zipStream.closeEntry();
            }
        });
    }

    public void visit(FileVisitor fileVisitor) throws TIoException {
        fileVisitor.visit(this);
    }
}
