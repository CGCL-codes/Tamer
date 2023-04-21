package com.myJava.file.driver;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import com.myJava.file.metadata.FileMetaData;
import com.myJava.object.ToStringHelper;

public abstract class AbstractLinkableFileSystemDriver extends AbstractFileSystemDriver implements LinkableFileSystemDriver {

    protected FileSystemDriver predecessor;

    public FileSystemDriver getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(FileSystemDriver predecessor) {
        this.predecessor = predecessor;
    }

    public boolean delete(File file) {
        return predecessor.delete(file);
    }

    public boolean setReadOnly(File file) {
        return predecessor.setReadOnly(file);
    }

    public void applyMetaData(FileMetaData p, File f) throws IOException {
        predecessor.applyMetaData(p, f);
    }

    public boolean createNewFile(File file) throws IOException {
        return predecessor.createNewFile(file);
    }

    public boolean supportsLongFileNames() {
        return predecessor.supportsLongFileNames();
    }

    public void deleteOnExit(File f) {
        predecessor.deleteOnExit(f);
    }

    public void flush() throws IOException {
        predecessor.flush();
    }

    public boolean exists(File file) {
        return predecessor.exists(file);
    }

    public void mount() throws IOException {
        predecessor.mount();
    }

    public void unmount() throws IOException {
        predecessor.unmount();
    }

    public FileMetaData getMetaData(File f, boolean onlyBasicAttributes) throws IOException {
        return predecessor.getMetaData(f, onlyBasicAttributes);
    }

    public boolean createNamedPipe(File pipe) throws IOException {
        return predecessor.createNamedPipe(pipe);
    }

    public boolean createSymbolicLink(File symlink, String realPath) throws IOException {
        return predecessor.createSymbolicLink(symlink, realPath);
    }

    public String toString() {
        StringBuffer sb = ToStringHelper.init(this);
        ToStringHelper.append("Predecessor", this.predecessor, sb);
        return ToStringHelper.close(sb);
    }

    public short getAccessEfficiency() {
        return predecessor.getAccessEfficiency();
    }

    public File getAbsoluteFile(File file) {
        return this.predecessor.getAbsoluteFile(file);
    }

    public boolean isDirectory(File file) {
        return this.predecessor.isDirectory(file);
    }

    public boolean isFile(File file) {
        return this.predecessor.isFile(file);
    }

    public boolean mkdir(File file) {
        return this.predecessor.mkdir(file);
    }

    public boolean mkdirs(File file) {
        return this.predecessor.mkdirs(file);
    }

    public boolean renameTo(File source, File dest) {
        return this.predecessor.renameTo(source, dest);
    }

    public boolean setLastModified(File file, long time) {
        return this.predecessor.setLastModified(file, time);
    }

    public String getAbsolutePath(File file) {
        return this.predecessor.getAbsolutePath(file);
    }

    public File getCanonicalFile(File file) throws IOException {
        return this.predecessor.getCanonicalFile(file);
    }

    public String getCanonicalPath(File file) throws IOException {
        return this.predecessor.getCanonicalPath(file);
    }

    public String getName(File file) {
        return this.predecessor.getName(file);
    }

    public String getParent(File file) {
        return this.predecessor.getParent(file);
    }

    public File getParentFile(File file) {
        return this.predecessor.getParentFile(file);
    }

    public String getPath(File file) {
        return this.predecessor.getPath(file);
    }

    public File[] listFiles(File file, FileFilter filter) {
        return predecessor.listFiles(file, filter);
    }

    public File[] listFiles(File file, FilenameFilter filter) {
        return predecessor.listFiles(file, filter);
    }

    public String[] list(File file, FilenameFilter filter) {
        return predecessor.list(file, filter);
    }

    public String[] list(File file) {
        return predecessor.list(file);
    }

    public File[] listFiles(File file) {
        return predecessor.listFiles(file);
    }

    public boolean canRead(File file) {
        return predecessor.canRead(file);
    }

    public boolean canWrite(File file) {
        return predecessor.canWrite(file);
    }

    public boolean isAbsolute(File file) {
        return predecessor.isAbsolute(file);
    }

    public long length(File file) {
        return predecessor.length(file);
    }

    public boolean isHidden(File file) {
        return predecessor.isHidden(file);
    }

    public long lastModified(File file) {
        return predecessor.lastModified(file);
    }

    public FileCacheableInformations getInformations(File file) {
        return predecessor.getInformations(file);
    }

    public short getType(File file) throws IOException {
        return predecessor.getType(file);
    }

    public void clearCachedData(File file) throws IOException {
        predecessor.clearCachedData(file);
    }
}
