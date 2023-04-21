package org.intellij.plugins.junit4;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiJavaFile;
import static org.intellij.plugins.junit4.StringUtil.*;

/**
 * Resolve (and pull apart) fully qualified class names.
 * <p/>
 * Does NOT work with paths on disk. Class names only.
 * <p/>
 * Need to either make a new one for each config change, or have it listen to config changes.
 * <p/>
 * Note: much of the brains for this class was taken from the TestDox project.
 *
 * @author Justin Tomich
 */
public class ClassNameResolver {

    private static final String DEFAULT_SUFFIX = "Test";

    private static final String DEFAULT_PREFIX = "";

    private final String prefix;

    private final String suffix;

    public ClassNameResolver(Configuration config) {
        this(config.getPrefix(), config.getSuffix());
    }

    public ClassNameResolver(String prefix, String suffix) {
        if (isEmpty(prefix) && (isEmpty(suffix))) {
            prefix = DEFAULT_PREFIX;
            suffix = DEFAULT_SUFFIX;
        }
        this.prefix = deNull(prefix);
        this.suffix = deNull(suffix);
    }

    /**
     * Strip package from fully qualified class name, returning class name only.
     *
     * @param classname fully qualified name of class
     * @return name of class only (no package)
     */
    public String nameOnly(String classname) {
        if (!classname.contains(".")) return classname;
        return classname.substring(classname.lastIndexOf('.') + 1);
    }

    /**
     * Get package from fully qualified classname.
     *
     * @param classname fully qualified name of class
     * @return package of class, without class name itself.
     */
    public String packageOnly(String classname) {
        if (!classname.contains(".")) return DEFAULT_PREFIX;
        return classname.substring(0, classname.lastIndexOf('.') + 1);
    }

    /**
     * Toggle classname from production to test, or from test to production.
     *
     * @param file PsiJavaFile to toggle between production and test
     * @return fully qualified 'toggled' class name.
     */
    public String toggleName(PsiJavaFile file) {
        VirtualFile vFile = file.getVirtualFile();
        assert vFile != null;
        String classname = file.getPackageName() + "." + vFile.getNameWithoutExtension();
        return toggleName(classname);
    }

    /**
     * Toggle classname, production for test, test for production.
     *
     * @param classname fully qualified classname
     * @return toggled fully qualified classname
     */
    public String toggleName(String classname) {
        return isTestName(classname) ? getProductionClassName(classname) : getTestClassName(classname);
    }

    protected String getProductionClassName(String classname) {
        notEmpty(classname, "classname");
        if (isProductionName(classname)) return classname;
        String nameOnly = nameOnly(classname);
        nameOnly = trimPrefix(nameOnly);
        nameOnly = trimSuffix(nameOnly);
        return packageOnly(classname) + nameOnly;
    }

    /**
     * Calculate the corresponding test classname for the given classaame. Classname is
     * calculated to conform to the test prefix/suffix set for this resolver. If the
     * classname is already a test classname, it'll be returned as-is.
     *
     * @param classname
     * @return fqn of test classame for the given classname.
     */
    public String getTestClassName(String classname) {
        notEmpty(classname, "classname");
        if (isTestName(classname)) return classname;
        return prefix + packageOnly(classname) + nameOnly(classname) + suffix;
    }

    protected boolean isTestName(String classname) {
        if (isEmpty(classname)) return false;
        String nameOnly = nameOnly(classname);
        if (!isEmpty(prefix) && !nameOnly.startsWith(prefix)) return false;
        if (!isEmpty(suffix) && !nameOnly.endsWith(suffix)) return false;
        return true;
    }

    protected boolean isProductionName(String classname) {
        if (isEmpty(classname)) return false;
        return !isTestName(classname);
    }

    private String trimPrefix(String classname) {
        if (isEmpty(prefix)) return classname;
        if (!classname.startsWith(prefix)) return classname;
        return classname.substring(prefix.length());
    }

    private String trimSuffix(String classname) {
        if (isEmpty(suffix)) return classname;
        if (!classname.endsWith(suffix)) return classname;
        return classname.substring(0, classname.length() - suffix.length());
    }
}
