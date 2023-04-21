package net.sourceforge.freejava.snm.abc;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.freejava.sysinfo.SystemInfo;
import net.sourceforge.freejava.util.file.FilePath;

public class ModulesRoot {

    private File root;

    private Map<String, File> modules;

    public ModulesRoot(File root, Map<String, File> modules) {
        this.root = root;
        this.modules = modules;
    }

    public ModulesRoot(File root) {
        this(root, new HashMap<String, File>());
    }

    @Override
    public String toString() {
        return "ModulesRoot: " + root;
    }

    /**
     * Max-matched prefixes & suffixes.
     */
    private static class MaxFixes implements FilenameFilter {

        final String pattern;

        public String maxPrefix;

        public String maxSuffix;

        public MaxFixes(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public boolean accept(File dir, String name) {
            if (name.startsWith(pattern)) {
                if (maxSuffix == null) maxSuffix = name; else if (name.compareTo(maxSuffix) > 0) maxSuffix = name;
            } else if (pattern.startsWith(name)) {
                if (maxPrefix == null) maxPrefix = name; else if (name.length() > maxPrefix.length()) maxPrefix = name;
            }
            return false;
        }
    }

    public File findabc(String name, File root) {
        File file = new File(root, name);
        if (file.exists()) return file;
        MaxFixes max = new MaxFixes(name);
        root.list(max);
        if (max.maxSuffix != null) return new File(root, max.maxSuffix);
        if (max.maxPrefix != null) {
            root = new File(root, max.maxPrefix);
            if (root.isDirectory()) return findabc(name, root);
        }
        return null;
    }

    public File findabc(String name) {
        return findabc(name, root);
    }

    /**
     * @example /lapiota/abc.d/eclipse&#42;/plugins/org.eclipse.core&#42;<br>
     *          &#64;module/abc.d/eclipse&#42;/plugins/org.eclipse.core&#42;<br>
     *          eclipse&#42;/plugins/org.eclipse.core&#42;<br>
     *          $ECLIPSE_HOME/plugins/org.eclipse.core&#42;<br>
     */
    public File findexp(String exp) {
        return findexp(exp, null);
    }

    public File findexp(String exp, File parent) {
        if (exp == null) return parent;
        boolean first = parent == null;
        if (parent == null) {
            if (exp.startsWith("/")) return findexp(exp.substring(1), FilePath.canoniOf("/"));
            if (SystemInfo.isWin32() && exp.length() > 2 && exp.charAt(1) == ':') {
                File driveRoot = FilePath.canoniOf(exp.substring(0, 2));
                return findexp(exp.substring(2), driveRoot);
            }
            parent = root;
        }
        String component = null;
        int slash = exp.indexOf('/');
        if (slash == -1) {
            component = exp;
            exp = null;
        } else {
            component = exp.substring(0, slash);
            exp = exp.substring(slash + 1);
        }
        if (first) {
            File newParent = null;
            if (component.startsWith("@")) {
                String mod = component.substring(1);
                newParent = modules.get(mod);
                if (newParent == null) return null;
            } else if (component.startsWith("$")) {
                String env = System.getenv(component.substring(1));
                if (env == null) return null;
                newParent = FilePath.canoniOf(env);
            }
            if (newParent != null) {
                if (!newParent.isDirectory()) return null;
                return findexp(exp, newParent);
            }
        }
        if (component.endsWith("*")) {
            String prefix = component.substring(0, component.length() - 1);
            File expanded = findabc(prefix, parent);
            if (expanded == null || !expanded.exists()) return null;
            return findexp(exp, expanded);
        }
        parent = new File(parent, component);
        if (!parent.exists()) throw null;
        return findexp(exp, parent);
    }

    public static ModulesRoot DEFAULT;

    static {
        File root = new File("/");
        if (SystemInfo.isWin32()) {
            String programFiles = System.getenv("ProgramFiles");
            if (programFiles == null) programFiles = "C:/Program Files";
            root = FilePath.canoniOf(programFiles);
        }
        DEFAULT = new ModulesRoot(root);
    }
}
