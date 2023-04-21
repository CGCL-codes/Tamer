package net.diet_rich.jabak.main;

import java.io.File;
import net.diet_rich.util.ref.FlaggedRef;

public class FileRef extends FlaggedRef<File> {

    public FileRef() {
    }

    public synchronized FileRef stringSet(String newPath) {
        if (newPath == null) set((File) null); else set(new File(newPath));
        return this;
    }

    public synchronized boolean canRead() {
        if (value == null) return false;
        return value.canRead();
    }

    public synchronized boolean isDirectory() {
        if (value == null) return false;
        return value.isDirectory();
    }

    public synchronized boolean isFile() {
        if (value == null) return false;
        return value.isFile();
    }

    public synchronized String getPath() {
        return value.getPath();
    }
}
