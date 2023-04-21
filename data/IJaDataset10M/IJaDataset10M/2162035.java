package org.gocha.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author gocha
 */
public class UnixFileSystem implements FileSystem, FileToPath {

    public InputStream getFile(String file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        FileInputStream fin = new FileInputStream(file);
        return fin;
    }

    public OutputStream putFile(String file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        FileOutputStream fout = new FileOutputStream(file);
        return fout;
    }

    public String[] list(String path) throws IOException {
        File[] files = new File(path).listFiles();
        if (files == null) return null;
        String[] res = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            res[i] = files[i].getAbsolutePath();
        }
        return res;
    }

    public long sizeOf(String path) throws IOException {
        return new File(path).length();
    }

    public boolean ignoreCase() {
        return false;
    }

    public boolean exists(String path) throws IOException {
        return new File(path).exists();
    }

    public boolean isFile(String path) throws IOException {
        return new File(path).isFile();
    }

    public boolean isDirectory(String path) throws IOException {
        return new File(path).isDirectory();
    }

    public boolean delete(String path) throws IOException {
        if (isFile(path)) {
            File f = new File(path);
            return f.delete();
        }
        if (isDirectory(path)) {
            String[] subPaths = list(path);
            if (subPaths == null) return false;
            boolean hasErr = false;
            for (String subP : subPaths) {
                String name = nameOf(subP);
                if (name == null) {
                    continue;
                }
                if (name.equals(".") || name.equals("..")) continue;
                boolean res = delete(subP);
                if (!res) {
                    hasErr = true;
                    break;
                }
            }
            if (hasErr) return false;
            File f = new File(path);
            return f.delete();
        }
        return !exists(path);
    }

    public boolean rename(String srcPath, String destPath) throws IOException {
        File sf = new File(srcPath);
        File df = new File(destPath);
        return sf.renameTo(df);
    }

    public boolean mkdir(String path) throws IOException {
        File f = new File(path);
        return f.mkdirs();
    }

    private String nameOf(String path) {
        return DefaultFileNames.instance().nameOf(path);
    }

    public String toPath(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        return file.getAbsolutePath();
    }
}
