package com.jaeksoft.searchlib.analysis.stopwords;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import com.jaeksoft.searchlib.SearchLibException;
import com.jaeksoft.searchlib.config.Config;
import com.jaeksoft.searchlib.util.ReadWriteLock;

public class AbstractDirectoryManager {

    protected final ReadWriteLock rwl = new ReadWriteLock();

    private File directory;

    private Config config;

    public AbstractDirectoryManager(Config config, File directory) {
        this.config = config;
        this.directory = directory;
    }

    private class FileOnly implements FileFilter {

        @Override
        public boolean accept(File file) {
            return file.isFile();
        }
    }

    protected Config getConfig() {
        return config;
    }

    public String[] getList(boolean addEmptyOne) {
        rwl.r.lock();
        try {
            File[] files = directory.listFiles(new FileOnly());
            if (files == null) return null;
            String[] list = addEmptyOne ? new String[files.length + 1] : new String[files.length];
            int i = 0;
            if (addEmptyOne) list[i++] = "";
            for (File file : files) list[i++] = file.getName();
            return list;
        } finally {
            rwl.r.unlock();
        }
    }

    public String[] getList() {
        return getList(false);
    }

    protected File getFile(String name) {
        return new File(directory, name);
    }

    public boolean exists(String name) {
        rwl.r.lock();
        try {
            if (name == null || name.length() == 0) return false;
            return getFile(name).exists();
        } finally {
            rwl.r.unlock();
        }
    }

    public void create(String name) throws IOException {
        rwl.w.lock();
        try {
            if (!directory.exists()) directory.mkdir();
            File createFile = getFile(name);
            if (createFile.exists()) return;
            createFile.createNewFile();
        } finally {
            rwl.w.unlock();
        }
    }

    public void delete(String name) {
        rwl.w.lock();
        try {
            File deleteFile = getFile(name);
            if (!deleteFile.exists()) return;
            deleteFile.delete();
        } finally {
            rwl.w.unlock();
        }
    }

    public String getContent(String name) throws IOException {
        rwl.r.lock();
        try {
            return FileUtils.readFileToString(getFile(name));
        } finally {
            rwl.r.unlock();
        }
    }

    public void saveContent(String name, String content) throws IOException, SearchLibException {
        rwl.w.lock();
        try {
            FileUtils.writeStringToFile(getFile(name), content);
        } finally {
            rwl.w.unlock();
        }
    }
}
