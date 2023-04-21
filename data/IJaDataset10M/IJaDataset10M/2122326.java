package org.zclasspath;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ZClassPathItemFile implements ZIClassPathItem {

    private File file;

    public ZClassPathItemFile(File file) {
        this.file = file;
    }

    public ZClassPathItemFile(String file) {
        this(new File(file));
    }

    public ZClassPathItemFile(Class clazz, String path) {
        this(new File(clazz.getPackage().getName().replace('.', File.separatorChar), path));
    }

    @Override
    public String toString() {
        return file.getAbsolutePath();
    }

    /**
   * list
   * 
   * @return ClassPathItem[]
   */
    public ZIClassPathItem[] list() {
        File[] content = file.listFiles();
        if (content == null) {
            return new ZIClassPathItem[0];
        }
        ZIClassPathItem[] ret = new ZIClassPathItem[content.length];
        for (int i = 0; i < content.length; i++) {
            ret[i] = new ZClassPathItemFile(content[i]);
        }
        return ret;
    }

    /**
   * getRelativePath
   * 
   * @param root
   *          ClassPathItem
   * @return String
   */
    public String getRelativePath(ZIClassPathItem root) throws Exception {
        String p = getName();
        String rp = root.getName();
        String name = p.substring(rp.length() + 1);
        return name;
    }

    /**
   * getName
   * 
   * @return String
   */
    public String getName() throws Exception {
        return file.getCanonicalPath();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public boolean isFile() {
        return file.isFile();
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    public long getHash() throws Exception {
        return file.lastModified() + file.length();
    }
}
