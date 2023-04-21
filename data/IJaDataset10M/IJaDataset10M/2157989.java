package org.progeeks.bcel;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.apache.bcel.*;
import org.apache.bcel.classfile.*;
import org.progeeks.util.log.*;

/**
 *  Given a class reference or a Jar file, this will
 *  print a list of package dependencies.
 *
 *  @version   $Revision: 1.14 $
 *  @author    Paul Speed
 */
public class Dependencies {

    static Log log = Log.getLog(Dependencies.class);

    private static final String[] help = { "Usage: java org.progeeks.bcel.Dependencies <options> classes", "Where:", "classes is the list of fully qualified class names, jar files,", "        .class files, or directories to search for dependencies.", "        Each argument is used as a root when generating the report.", "", "Options:", "  -ignore package", "       Will ignore the specified package when doing", "       dependency searches.", "  -ignoreRegex regex", "       Will ignore packages that match the specified regular expression", "  -ignoreDefault", "       Will ignore a standard set of Java packages including:", "           java.*, javax.accessibility.*, javax.crypto.*, javax.imageio.*,", "           javax.naming.*, javax.net.*, javax.print.*, javax.rmi.*,", "           javax.security.*, javax.sound.*, javax.sql.*, javax.swing.*,", "           javax.transaction.*, javax.xml.*, sun.*, com.sun.*, org.omg.*", "           org.w3c.*, org.xml.*", "  -recurse", "       Set this option if directory processing should be recursive.", "  -classes", "       Show class information in generated report.", "  -noPackages", "       Do not show package information in the generated report.", "  -separatePasses", "       Run each class argument as a separate reporting pass." };

    private boolean showAdds = false;

    private boolean recurse = false;

    private boolean showLoadErrors = false;

    private Set root = new HashSet();

    private Set rootNames = new HashSet();

    private Set dependencies = new HashSet();

    private Set dependencyNames = new TreeSet();

    private Set packages = new TreeSet();

    private Set processed = new HashSet();

    private Set ignorePackages = new HashSet();

    private List todo = new LinkedList();

    public Dependencies() {
    }

    /**
     *  Returns the number of starting classes.
     */
    public int getRootSize() {
        return (root.size());
    }

    public void reset() {
        root.clear();
        rootNames.clear();
        dependencies.clear();
        dependencyNames.clear();
        packages.clear();
        processed.clear();
        todo.clear();
    }

    public void setShowAdds(boolean show) {
        this.showAdds = show;
    }

    public void setRecurse(boolean recurse) {
        this.recurse = recurse;
    }

    public void collectDependencies() {
        todo.addAll(root);
        while (todo.size() > 0) {
            JavaClass jc = (JavaClass) todo.remove(0);
            if (processed.contains(jc)) continue;
            log.info("Processing:" + jc.getClassName());
            processed.add(jc);
            ConstantPool constants = jc.getConstantPool();
            int length = constants.getLength();
            for (int i = 0; i < length; i++) {
                Constant c = constants.getConstant(i);
                if (c instanceof ConstantClass) {
                    String className = constants.constantToString(c);
                    className = className.replaceAll("\\[\\[", "[");
                    if (className.startsWith("[")) {
                        if (!className.startsWith("[L")) continue;
                        className = className.substring(2, className.length() - 1);
                    }
                    JavaClass dc = Repository.lookupClass(className);
                    if (dc == null) {
                        if (showLoadErrors) System.out.println("Error looking up class:" + className);
                        addNamedDependency(className);
                    } else {
                        addDependency(dc);
                    }
                }
            }
        }
    }

    /**
     *  Adds a dependent class by name.  This is useful for when
     *  we were unable to load the class but can still determine its
     *  name and package.
     */
    protected void addNamedDependency(String name) {
        if (name.endsWith(".class")) name = name.substring(0, name.length() - 6);
        int split = name.lastIndexOf('.');
        if (split > 0) {
            String pkg = name.substring(0, split);
            packages.add(pkg);
        }
        dependencyNames.add(name);
    }

    /**
     *  Adds a dependent class if necessary.
     */
    protected void addDependency(JavaClass jc) {
        if (rootNames.contains(jc.getClassName())) return;
        if (isClassIgnored(jc)) {
            processed.add(jc);
            return;
        }
        if (dependencies.add(jc)) {
            packages.add(jc.getPackageName());
            dependencyNames.add(jc.getClassName());
            todo.add(jc);
        }
    }

    /**
     *  Returns true if the specified class should be ignored
     */
    protected boolean isClassIgnored(JavaClass jc) {
        for (Iterator i = ignorePackages.iterator(); i.hasNext(); ) {
            Pattern p = (Pattern) i.next();
            Matcher m = p.matcher(jc.getPackageName());
            if (m.matches()) return (true);
        }
        return (false);
    }

    protected void addRootClass(JavaClass jc) {
        Attribute[] attrs = jc.getAttributes();
        ConstantPool pool = jc.getConstantPool();
        root.add(jc);
        rootNames.add(jc.getClassName());
        for (int i = 0; i < attrs.length; i++) {
            if (attrs[i] instanceof InnerClasses) {
                InnerClasses ic = (InnerClasses) attrs[i];
                InnerClass[] list = ic.getInnerClasses();
                for (int j = 0; j < list.length; j++) {
                    int index = list[j].getInnerClassIndex();
                    Constant c = pool.getConstant(index);
                    String innerName = pool.constantToString(c);
                    if (rootNames.contains(innerName)) continue;
                    JavaClass inner = Repository.lookupClass(innerName);
                    if (inner != null) addRootClass(inner);
                }
            }
        }
    }

    public void addRootReference(String ref) throws IOException {
        JavaClass jc = Repository.lookupClass(ref);
        if (jc != null) {
            if (showAdds) System.out.println("add ref:" + ref);
            addRootClass(jc);
        } else {
            File f = new File(ref);
            if (f.exists()) addFile(f);
        }
    }

    public void addFile(File f) throws IOException {
        if (f.isDirectory()) {
            if (showAdds) System.out.println("add directory:" + f);
            File[] list = f.listFiles();
            for (int i = 0; i < list.length; i++) {
                if (list[i].isDirectory() && !recurse) continue;
                addFile(list[i]);
            }
            return;
        }
        String name = f.getName().toLowerCase();
        if (name.endsWith(".jar")) addJar(f); else if (name.endsWith(".class")) addClassFile(f); else System.out.println("Ignoring:" + f);
    }

    public void addJar(File f) throws IOException {
        System.out.println("Add jar:" + f);
    }

    public void addClassFile(File f) throws IOException {
        if (showAdds) System.out.println("Add class file:" + f);
        FileInputStream in = new FileInputStream(f);
        try {
            ClassParser cp = new ClassParser(in, f.getName());
            JavaClass jc = cp.parse();
            if (jc != null) addRootClass(jc);
        } finally {
            in.close();
        }
    }

    public void ignorePackage(String pkg) {
        if (log.isDebugEnabled()) log.debug("Ignore package:" + pkg);
        if (pkg.endsWith(".*")) ignorePackage(pkg.substring(0, pkg.length() - 2));
        pkg = pkg.replaceAll("\\.", "\\\\.");
        pkg = pkg.replaceAll("\\*", ".*");
        ignorePackageRegex(pkg);
    }

    public void ignorePackageRegex(String regex) {
        if (log.isDebugEnabled()) log.debug("Ignoring package regex:" + regex);
        Pattern p = Pattern.compile(regex);
        ignorePackages.add(p);
    }

    public static void dumpDependencies(JavaClass jc) {
        ConstantPool constants = jc.getConstantPool();
        int length = constants.getLength();
        for (int i = 0; i < length; i++) {
            Constant c = constants.getConstant(i);
            if (c instanceof ConstantClass) System.out.println("    " + constants.constantToString(c));
        }
    }

    public static void printResults(Dependencies deps, String indent, boolean showPackages, boolean showClasses) {
        if (showPackages && deps.packages.size() > 0) {
            System.out.println(indent + "Package Dependencies:");
            for (Iterator i = deps.packages.iterator(); i.hasNext(); ) {
                System.out.println(indent + "   " + i.next());
            }
        }
        if (showClasses && deps.dependencies.size() > 0) {
            System.out.println(indent + "Class Dependencies:");
            for (Iterator i = deps.dependencyNames.iterator(); i.hasNext(); ) {
                System.out.println(indent + "   " + i.next());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            for (int i = 0; i < help.length; i++) System.out.println(help[i]);
            return;
        }
        Log.initialize();
        Dependencies deps = new Dependencies();
        boolean showPackages = true;
        boolean showClasses = false;
        boolean separatePasses = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                if ("-ignore".equals(args[i]) && i < args.length - 1) {
                    deps.ignorePackage(args[i + 1]);
                    i++;
                }
                if ("-recurse".equals(args[i])) {
                    deps.setRecurse(true);
                } else if ("-ignoreRegex".equals(args[i]) && i < args.length - 1) {
                    deps.ignorePackageRegex(args[i + 1]);
                    i++;
                } else if ("-ignoreDefault".equals(args[i])) {
                    System.out.println("Ignoring default packages.");
                    deps.ignorePackage("java.*");
                    deps.ignorePackage("javax.accessibility.*");
                    deps.ignorePackage("javax.crypto.*");
                    deps.ignorePackage("javax.imageio.*");
                    deps.ignorePackage("javax.naming.*");
                    deps.ignorePackage("javax.net.*");
                    deps.ignorePackage("javax.print.*");
                    deps.ignorePackage("javax.rmi.*");
                    deps.ignorePackage("javax.security.*");
                    deps.ignorePackage("javax.sound.*");
                    deps.ignorePackage("javax.sql.*");
                    deps.ignorePackage("javax.swing.*");
                    deps.ignorePackage("javax.transaction.*");
                    deps.ignorePackage("javax.xml.*");
                    deps.ignorePackage("sun.*");
                    deps.ignorePackage("com.sun.*");
                    deps.ignorePackage("org.omg.*");
                    deps.ignorePackage("org.w3c.*");
                    deps.ignorePackage("org.xml.*");
                } else if ("-classes".equals(args[i])) {
                    showClasses = true;
                } else if ("-noPackages".equals(args[i])) {
                    showPackages = false;
                } else if ("-separatePasses".equals(args[i])) {
                    separatePasses = true;
                } else {
                    System.err.println("Illegal option or option format:" + args[i]);
                }
            } else {
                deps.addRootReference(args[i]);
                if (separatePasses && deps.getRootSize() > 0) {
                    System.out.println("Results for:" + args[i]);
                    deps.collectDependencies();
                    printResults(deps, "    ", showPackages, showClasses);
                    deps.reset();
                    System.out.println();
                }
            }
        }
        if (!separatePasses) {
            deps.collectDependencies();
            printResults(deps, "", showPackages, showClasses);
        }
    }
}
