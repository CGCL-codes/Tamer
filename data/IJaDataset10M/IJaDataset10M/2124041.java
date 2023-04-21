package org.vramework.commons.datatypes;

import java.util.StringTokenizer;
import org.vramework.commons.utils.VSert;

/**
 * Handling of class names and their relatives pathes in the file system.
 * 
 * @author tmahring
 */
public class ClassName {

    private String _baseName;

    private String _relativePath;

    private String _packageName;

    /**
   * Constructs a {@link ClassName}, parses the className and sets its baseName
   * and relativePath as described in {@link #parseClassName(String)}.
   * 
   * @param className
   */
    public ClassName(String className) {
        parseClassName(className, this);
    }

    /**
   * Constructs a {@link ClassName} and sets its baseName and relativePath.
   * 
   * @param baseName
   * @param relativePath
   */
    public ClassName(String baseName, String relativePath) {
        setBaseName(baseName);
        setRelativePath(relativePath);
    }

    private ClassName() {
    }

    /**
   * Parses the given className and returns a {@link ClassName} whose baseName
   * ist set to the classes base name and whose relativePath is set to the path
   * corresponding to the "package path" of the class. <br />
   * Example: "org.vramework.SampleClass" will cause the base name "SampleClass"
   * and the relative path "at\\Xgov"
   * 
   * @param className
   * @return The class name.
   */
    public static final ClassName parseClassName(String className) {
        ClassName cn = new ClassName();
        parseClassName(className, cn);
        return cn;
    }

    /**
   * @param className
   * @return The basename of the classname. Example: "org.vramework.SampleClass"
   *         will produce the base name "SampleClass"
   */
    public static final String getBaseName(String className) {
        VSert.argNotEmpty(className);
        int lastIndexOf = className.lastIndexOf(".");
        return className.substring(lastIndexOf + 1);
    }

    /**
   * @param clazz
   * @return The basename of the class. Example: "org.vramework.SampleClass"
   *         will produce the base name "SampleClass"
   */
    public static final String getBaseName(Class<?> clazz) {
        return getBaseName(clazz.getName());
    }

    /**
   * Parses the given className and fills the given {@link ClassName}.
   * 
   * @param className
   * @param cn
   */
    public static final void parseClassName(String className, ClassName cn) {
        VSert.argNotEmpty(className);
        VSert.argNotNull(className);
        String baseName = "";
        int countTokens = 0;
        int currentToken = 0;
        StringBuilder relativePath = new StringBuilder(255);
        StringTokenizer tokenizer = new StringTokenizer(className, ".");
        countTokens = tokenizer.countTokens();
        while (tokenizer.hasMoreElements()) {
            if (currentToken == countTokens - 1) {
                baseName = tokenizer.nextToken();
            } else {
                relativePath.append(tokenizer.nextToken());
                if (currentToken < countTokens - 2) {
                    relativePath.append("/");
                }
            }
            currentToken++;
        }
        String packageName = className.substring(0, className.length() - baseName.length() - 1);
        cn.setPackageName(packageName);
        cn.setBaseName(baseName);
        cn.setRelativePath(relativePath.toString());
    }

    /**
   * Converts the Java "binary class name" to a file path. <br />
   * Example: "org.vramework.SampleClass" will be converted to
   * "org/vramework/SampleClass"
   * 
   * @param className
   * @return The file path corresponding to the given class name
   */
    public static final String convertToPath(String className) {
        return replaceSeperator(className, '/');
    }

    /**
   * Replaces the Java package seperator by the passed one.
   * 
   * @param className
   * @param sep
   * @return The classname with replaced esperator chars.
   */
    public static final String replaceSeperator(String className, char sep) {
        VSert.argNotEmpty(className);
        return className.replace('.', sep);
    }

    /**
   * @param classes
   * @param seperator
   * @return All class names as one String seperated with the passed seperator.
   */
    public static final String concatClassNames(Class<?>[] classes, String seperator) {
        VSert.argNotNull("classes", classes);
        int len = classes.length;
        if (len == 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder(512);
        for (int i = 0; i < len - 1; i++) {
            Class<?> c = classes[i];
            buf.append(c.getName());
            buf.append(seperator);
        }
        buf.append(classes[len - 1].getName());
        return buf.toString();
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(255);
        buf.append(getBaseName());
        buf.append(" - ");
        buf.append(getRelativePath());
        return buf.toString();
    }

    public final String getBaseName() {
        return _baseName;
    }

    public final void setBaseName(String baseName) {
        _baseName = baseName;
    }

    public final String getRelativePath() {
        return _relativePath;
    }

    public final void setRelativePath(String relativePath) {
        _relativePath = relativePath;
    }

    /**
   * @return the packageName
   */
    public final String getPackageName() {
        return _packageName;
    }

    /**
   * @param packageName the packageName to set
   */
    protected final void setPackageName(String packageName) {
        _packageName = packageName;
    }
}
