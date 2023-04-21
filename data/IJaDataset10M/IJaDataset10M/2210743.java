package sun.tools.java;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.List;
import java.util.Collections;
import java.io.IOException;

/**
 * This class describes the classes and packages imported
 * from a source file. A Hashtable called bindings is maintained
 * to quickly map symbol names to classes. This table is flushed
 * everytime a new import is added.
 *
 * A class name is resolved as follows:
 *  - if it is a qualified name then return the corresponding class
 *  - if the name corresponds to an individually imported class then return that class
 *  - check if the class is defined in any of the imported packages,
 *    if it is then return it, make sure it is defined in only one package
 *  - assume that the class is defined in the current package
 *
 * WARNING: The contents of this source file are not part of any
 * supported API.  Code that depends on them does so at its own risk:
 * they are subject to change or removal without notice.
 */
public class Imports implements Constants {

    /**
     * The current package, which is implicitly imported,
     * and has precedence over other imported packages.
     */
    Identifier currentPackage = idNull;

    /**
     * A location for the current package declaration.  Used to
     * report errors against the current package.
     */
    long currentPackageWhere = 0;

    /**
     * The imported classes, including memoized imports from packages.
     */
    Hashtable classes = new Hashtable();

    /**
     * The imported package identifiers.  This will not contain duplicate
     * imports for the same package.  It will also not contain the
     * current package.
     */
    Vector packages = new Vector();

    /**
     * The (originally) imported classes.
     * A vector of IdentifierToken.
     */
    Vector singles = new Vector();

    /**
     * Are the import names checked yet?
     */
    protected int checked;

    /**
     * Constructor, always import java.lang.
     */
    public Imports(Environment env) {
        addPackage(idJavaLang);
    }

    /**
     * Check the names of the imports.
     */
    public synchronized void resolve(Environment env) {
        if (checked != 0) {
            return;
        }
        checked = -1;
        Vector resolvedPackages = new Vector();
        for (Enumeration e = packages.elements(); e.hasMoreElements(); ) {
            IdentifierToken t = (IdentifierToken) e.nextElement();
            Identifier nm = t.getName();
            long where = t.getWhere();
            if (env.isExemptPackage(nm)) {
                resolvedPackages.addElement(t);
                continue;
            }
            try {
                Identifier rnm = env.resolvePackageQualifiedName(nm);
                if (importable(rnm, env)) {
                    if (env.getPackage(rnm.getTopName()).exists()) {
                        env.error(where, "class.and.package", rnm.getTopName());
                    }
                    if (!rnm.isInner()) rnm = Identifier.lookupInner(rnm, idNull);
                    nm = rnm;
                } else if (!env.getPackage(nm).exists()) {
                    env.error(where, "package.not.found", nm, "import");
                } else if (rnm.isInner()) {
                    env.error(where, "class.and.package", rnm.getTopName());
                }
                resolvedPackages.addElement(new IdentifierToken(where, nm));
            } catch (IOException ee) {
                env.error(where, "io.exception", "import");
            }
        }
        packages = resolvedPackages;
        for (Enumeration e = singles.elements(); e.hasMoreElements(); ) {
            IdentifierToken t = (IdentifierToken) e.nextElement();
            Identifier nm = t.getName();
            long where = t.getWhere();
            Identifier pkg = nm.getQualifier();
            nm = env.resolvePackageQualifiedName(nm);
            if (!env.classExists(nm.getTopName())) {
                env.error(where, "class.not.found", nm, "import");
            }
            Identifier snm = nm.getFlatName().getName();
            Identifier className = (Identifier) classes.get(snm);
            if (className != null) {
                Identifier f1 = Identifier.lookup(className.getQualifier(), className.getFlatName());
                Identifier f2 = Identifier.lookup(nm.getQualifier(), nm.getFlatName());
                if (!f1.equals(f2)) {
                    env.error(where, "ambig.class", nm, className);
                }
            }
            classes.put(snm, nm);
            try {
                ClassDeclaration decl = env.getClassDeclaration(nm);
                ClassDefinition def = decl.getClassDefinitionNoCheck(env);
                Identifier importedPackage = def.getName().getQualifier();
                for (; def != null; def = def.getOuterClass()) {
                    if (def.isPrivate() || !(def.isPublic() || importedPackage.equals(currentPackage))) {
                        env.error(where, "cant.access.class", def);
                        break;
                    }
                }
            } catch (AmbiguousClass ee) {
                env.error(where, "ambig.class", ee.name1, ee.name2);
            } catch (ClassNotFound ee) {
                env.error(where, "class.not.found", ee.name, "import");
            }
        }
        checked = 1;
    }

    /**
     * Lookup a class, given the current set of imports,
     * AmbiguousClass exception is thrown if the name can be
     * resolved in more than one way. A ClassNotFound exception
     * is thrown if the class is not found in the imported classes
     * and packages.
     */
    public synchronized Identifier resolve(Environment env, Identifier nm) throws ClassNotFound {
        if (tracing) env.dtEnter("Imports.resolve: " + nm);
        if (nm.hasAmbigPrefix()) {
            nm = nm.removeAmbigPrefix();
        }
        if (nm.isQualified()) {
            if (tracing) env.dtExit("Imports.resolve: QUALIFIED " + nm);
            return nm;
        }
        if (checked <= 0) {
            checked = 0;
            resolve(env);
        }
        Identifier className = (Identifier) classes.get(nm);
        if (className != null) {
            if (tracing) env.dtExit("Imports.resolve: PREVIOUSLY IMPORTED " + nm);
            return className;
        }
        Identifier id = Identifier.lookup(currentPackage, nm);
        if (importable(id, env)) {
            className = id;
        } else {
            Enumeration e = packages.elements();
            while (e.hasMoreElements()) {
                IdentifierToken t = (IdentifierToken) e.nextElement();
                id = Identifier.lookup(t.getName(), nm);
                if (importable(id, env)) {
                    if (className == null) {
                        className = id;
                    } else {
                        if (tracing) env.dtExit("Imports.resolve: AMBIGUOUS " + nm);
                        throw new AmbiguousClass(className, id);
                    }
                }
            }
        }
        if (className == null) {
            if (tracing) env.dtExit("Imports.resolve: NOT FOUND " + nm);
            throw new ClassNotFound(nm);
        }
        classes.put(nm, className);
        if (tracing) env.dtExit("Imports.resolve: FIRST IMPORT " + nm);
        return className;
    }

    /**
     * Check to see if 'id' names an importable class in `env'.
     * This method was made public and static for utility.
     */
    public static boolean importable(Identifier id, Environment env) {
        if (!id.isInner()) {
            return env.classExists(id);
        } else if (!env.classExists(id.getTopName())) {
            return false;
        } else {
            try {
                ClassDeclaration decl = env.getClassDeclaration(id.getTopName());
                ClassDefinition c = decl.getClassDefinitionNoCheck(env);
                return c.innerClassExists(id.getFlatName().getTail());
            } catch (ClassNotFound ee) {
                return false;
            }
        }
    }

    /**
     * Suppose a resolve() call has failed.
     * This routine can be used silently to give a reasonable
     * default qualification (the current package) to the identifier.
     * This decision is recorded for future reference.
     */
    public synchronized Identifier forceResolve(Environment env, Identifier nm) {
        if (nm.isQualified()) return nm;
        Identifier className = (Identifier) classes.get(nm);
        if (className != null) {
            return className;
        }
        className = Identifier.lookup(currentPackage, nm);
        classes.put(nm, className);
        return className;
    }

    /**
     * Add a class import
     */
    public synchronized void addClass(IdentifierToken t) {
        singles.addElement(t);
    }

    public void addClass(Identifier nm) throws AmbiguousClass {
        addClass(new IdentifierToken(nm));
    }

    /**
     * Add a package import, or perhaps an inner class scope.
     * Ignore any duplicate imports.
     */
    public synchronized void addPackage(IdentifierToken t) {
        final Identifier name = t.getName();
        if (name == currentPackage) {
            return;
        }
        final int size = packages.size();
        for (int i = 0; i < size; i++) {
            if (name == ((IdentifierToken) packages.elementAt(i)).getName()) {
                return;
            }
        }
        packages.addElement(t);
    }

    public void addPackage(Identifier id) {
        addPackage(new IdentifierToken(id));
    }

    /**
     * Specify the current package with an IdentifierToken.
     */
    public synchronized void setCurrentPackage(IdentifierToken t) {
        currentPackage = t.getName();
        currentPackageWhere = t.getWhere();
    }

    /**
     * Specify the current package
     */
    public synchronized void setCurrentPackage(Identifier id) {
        currentPackage = id;
    }

    /**
     * Report the current package
     */
    public Identifier getCurrentPackage() {
        return currentPackage;
    }

    /**
     * Return an unmodifiable list of IdentifierToken representing
     * packages specified as imports.
     */
    public List getImportedPackages() {
        return Collections.unmodifiableList(packages);
    }

    /**
     * Return an unmodifiable list of IdentifierToken representing
     * classes specified as imports.
     */
    public List getImportedClasses() {
        return Collections.unmodifiableList(singles);
    }

    /**
     * Extend an environment with my resolve() method.
     */
    public Environment newEnvironment(Environment env) {
        return new ImportEnvironment(env, this);
    }
}

final class ImportEnvironment extends Environment {

    Imports imports;

    ImportEnvironment(Environment env, Imports imports) {
        super(env, env.getSource());
        this.imports = imports;
    }

    public Identifier resolve(Identifier nm) throws ClassNotFound {
        return imports.resolve(this, nm);
    }

    public Imports getImports() {
        return imports;
    }
}
