package com.patelsoft.fastopen;

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.BufferHistory;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.MiscUtilities;
import org.gjt.sp.jedit.OperatingSystem;
import org.gjt.sp.jedit.View;

/**
    *    A class that handles files generically.
    *
    *    @created 10 Mar 2003
    *    @author Jiger Patel
    *
  */
public class FastOpenFile {

    private final String name, path;

    private final boolean projectFile;

    private final int hashcode;

    private final String decoratedPath;

    public FastOpenFile(Buffer buffer) {
        name = buffer.getName();
        path = buffer.getPath();
        projectFile = false;
        hashcode = calculateHashCode();
        decoratedPath = decorate(name, path);
    }

    public FastOpenFile(projectviewer.vpt.VPTFile file) {
        name = file.getName();
        path = file.getNodePath();
        projectFile = true;
        hashcode = calculateHashCode();
        decoratedPath = decorate(name, path);
    }

    public FastOpenFile(String fileName, String path, boolean projectFile) {
        this.name = fileName;
        this.path = path;
        this.projectFile = projectFile;
        hashcode = calculateHashCode();
        decoratedPath = decorate(name, path);
    }

    public FastOpenFile(BufferHistory.Entry entry) {
        this.name = MiscUtilities.getFileName(entry.path);
        this.path = entry.path;
        this.projectFile = false;
        hashcode = calculateHashCode();
        decoratedPath = decorate(name, path);
    }

    private int calculateHashCode() {
        if (OperatingSystem.isWindows()) return (this.path.toLowerCase() + this.name).hashCode();
        return (this.path + this.name).hashCode();
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public boolean isProjectFile() {
        return this.projectFile;
    }

    public Buffer open(View view) {
        return jEdit.openFile(view, path);
    }

    public boolean isOpened() {
        return (jEdit.getBuffer(path) != null);
    }

    public boolean equals(Object fofile) {
        if (fofile != null && fofile instanceof FastOpenFile) {
            FastOpenFile f = (FastOpenFile) fofile;
            if (OperatingSystem.isWindows()) return (getPath().equalsIgnoreCase(f.getPath()) && getName().equalsIgnoreCase(f.getName()));
            return (getPath().equals(f.getPath()) && getName().equals(f.getName()));
        }
        return super.equals(fofile);
    }

    public int hashCode() {
        return hashcode;
    }

    public String getDecoratedPath() {
        return decoratedPath;
    }

    private String decorate(String name, String path) {
        return name + " (" + path + ")";
    }
}
