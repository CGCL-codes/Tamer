package net.metasimian.utilities.byteclass.compiler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import sun.tools.java.ClassFile;

/**
 * @author <a href="mailto:metasimian@sourceforge.net">metasimian</a>
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class StringClassFile extends ClassFile {

    private static final boolean DEBUG = "true".equalsIgnoreCase(System.getProperty("javac.debug", "false"));

    private static final String PATH_DELIMITER = "\\";

    private String classPackage = null;

    private String className = null;

    private String src = null;

    public StringClassFile(String classPackage, String className, String src) {
        super(null);
        this.classPackage = classPackage;
        this.className = className;
        this.src = src;
    }

    public InputStream getInputStream() throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(src.getBytes());
        return is;
    }

    public boolean exists() {
        if (DEBUG) System.out.println("exists()");
        return true;
    }

    public String getAbsoluteName() {
        if (DEBUG) System.out.println("getAbsoluteName()");
        return getPath() + PATH_DELIMITER + getName();
    }

    public String getName() {
        if (DEBUG) System.out.println("getName()");
        return className + ".java";
    }

    public String getPath() {
        if (DEBUG) System.out.println("getPath()");
        String result = classPackage.replace('.', '\\');
        return result;
    }

    public boolean isDirectory() {
        if (DEBUG) System.out.println("isDirectory()");
        return false;
    }

    public boolean isZipped() {
        if (DEBUG) System.out.println("isZipped()");
        return false;
    }

    public long lastModified() {
        if (DEBUG) System.out.println("lastModified()");
        return 0;
    }

    public long length() {
        if (DEBUG) System.out.println("length()");
        return src.length();
    }

    public String toString() {
        if (DEBUG) System.out.println("toString()");
        return src;
    }
}
