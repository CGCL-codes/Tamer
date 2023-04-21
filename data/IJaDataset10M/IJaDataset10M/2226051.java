package bsh.classpath;

import java.net.*;
import java.util.*;
import java.lang.ref.*;
import java.io.IOException;
import java.io.*;
import bsh.classpath.BshClassPath.ClassSource;
import bsh.classpath.BshClassPath.JarClassSource;
import bsh.classpath.BshClassPath.GeneratedClassSource;
import bsh.BshClassManager;
import bsh.ClassPathException;
import bsh.Interpreter;
import bsh.UtilEvalError;

/**
	<pre>
	Manage all classloading in BeanShell.
	Allows classpath extension and class file reloading.

	This class holds the implementation of the BshClassManager so that it
	can be separated from the core package.

	This class currently relies on 1.2 for BshClassLoader and weak references.
	Is there a workaround for weak refs?  If so we could make this work
	with 1.1 by supplying our own classloader code...

	See "http://www.beanshell.org/manual/classloading.html" for details
	on the bsh classloader architecture.

	Bsh has a multi-tiered class loading architecture.  No class loader is
	created unless/until a class is generated, the classpath is modified, 
	or a class is reloaded.

	Note: we may need some synchronization in here

	Note on jdk1.2 dependency:

	We are forced to use weak references here to accomodate all of the 
	fleeting namespace listeners.  (NameSpaces must be informed if the class 
	space changes so that they can un-cache names).  I had the interesting 
	thought that a way around this would be to implement BeanShell's own 
	garbage collector...  Then I came to my senses and said - screw it, 
	class re-loading will require 1.2.

	---------------------

	Classloading precedence:

	in-script evaluated class (scripted class)
	in-script added / modified classpath

	optionally, external classloader
	optionally, thread context classloader

	plain Class.forName()
	source class (.java file in classpath)

	</pre>

*/
public class ClassManagerImpl extends BshClassManager {

    static final String BSH_PACKAGE = "bsh";

    /**
		The classpath of the base loader.  Initially and upon reset() this is
		an empty instance of BshClassPath.  It grows as paths are added or is
		reset when the classpath is explicitly set.  This could also be called
		the "extension" class path, but is not strictly confined to added path
		(could be set arbitrarily by setClassPath())
	*/
    private BshClassPath baseClassPath;

    private boolean superImport;

    /**
		This is the full blown classpath including baseClassPath (extensions),
		user path, and java bootstrap path (rt.jar)

		This is lazily constructed and further (and more importantly) lazily
		intialized in components because mapping the full path could be
		expensive.

		The full class path is a composite of:
			baseClassPath (user extension) : userClassPath : bootClassPath
		in that order.
	*/
    private BshClassPath fullClassPath;

    private Vector listeners = new Vector();

    private ReferenceQueue refQueue = new ReferenceQueue();

    /**
		This handles extension / modification of the base classpath
		The loader to use where no mapping of reloaded classes exists.

		The baseLoader is initially null meaning no class loader is used.
	*/
    private BshClassLoader baseLoader;

    /**
		Map by classname of loaders to use for reloaded classes
	*/
    private Map loaderMap;

    /**
		Used by BshClassManager singleton constructor
	*/
    public ClassManagerImpl() {
        reset();
    }

    /**
		@return the class or null
	*/
    @Override
    public Class classForName(String name) {
        Class c = (Class) absoluteClassCache.get(name);
        if (c != null) return c;
        if (absoluteNonClasses.contains(name)) {
            if (Interpreter.DEBUG) Interpreter.debug("absoluteNonClass list hit: " + name);
            return null;
        }
        if (Interpreter.DEBUG) Interpreter.debug("Trying to load class: " + name);
        final ClassLoader overlayLoader = getLoaderForClass(name);
        if (overlayLoader != null) {
            try {
                c = overlayLoader.loadClass(name);
            } catch (Exception e) {
                if (Interpreter.DEBUG) Interpreter.debug("overlay loader failed for '" + name + "' - " + e);
            }
        }
        if ((c == null) && name.startsWith(BSH_PACKAGE)) {
            final ClassLoader myClassLoader = Interpreter.class.getClassLoader();
            if (myClassLoader != null) {
                try {
                    c = myClassLoader.loadClass(name);
                } catch (ClassNotFoundException e) {
                } catch (NoClassDefFoundError e) {
                }
            } else {
                try {
                    c = Class.forName(name);
                } catch (ClassNotFoundException e) {
                } catch (NoClassDefFoundError e) {
                }
            }
        }
        if ((c == null) && (baseLoader != null)) {
            try {
                c = baseLoader.loadClass(name);
            } catch (ClassNotFoundException e) {
            }
        }
        if ((c == null) && (externalClassLoader != null)) {
            try {
                c = externalClassLoader.loadClass(name);
            } catch (ClassNotFoundException e) {
            }
        }
        if (c == null) {
            try {
                final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                if (contextClassLoader != null) {
                    c = Class.forName(name, true, contextClassLoader);
                }
            } catch (ClassNotFoundException e) {
            } catch (NoClassDefFoundError e) {
            } catch (SecurityException e) {
            }
        }
        if (c == null) try {
            c = Class.forName(name);
        } catch (ClassNotFoundException e) {
        }
        cacheClassInfo(name, c);
        return c;
    }

    /**
		Get a resource URL using the BeanShell classpath
		@param path should be an absolute path
	*/
    @Override
    public URL getResource(String path) {
        URL url = null;
        if (baseLoader != null) url = baseLoader.getResource(path.substring(1));
        if (url == null) url = super.getResource(path);
        return url;
    }

    /**
		Get a resource stream using the BeanShell classpath
		@param path should be an absolute path
	*/
    @Override
    public InputStream getResourceAsStream(String path) {
        InputStream in = null;
        if (baseLoader != null) {
            in = baseLoader.getResourceAsStream(path.substring(1));
        }
        if (in == null) {
            in = super.getResourceAsStream(path);
        }
        return in;
    }

    ClassLoader getLoaderForClass(String name) {
        return (ClassLoader) loaderMap.get(name);
    }

    /**
	*/
    @Override
    public void addClassPath(URL path) throws IOException {
        if (baseLoader == null) setClassPath(new URL[] { path }); else {
            baseLoader.addURL(path);
            baseClassPath.add(path);
            classLoaderChanged();
        }
    }

    /**
		Clear all classloading behavior and class caches and reset to 
		initial state.
	*/
    @Override
    public void reset() {
        baseClassPath = new BshClassPath("baseClassPath");
        baseLoader = null;
        loaderMap = new HashMap();
        classLoaderChanged();
    }

    /**
		Set a new base classpath and create a new base classloader.
		This means all types change. 
	*/
    @Override
    public void setClassPath(URL[] cp) {
        baseClassPath.setPath(cp);
        initBaseLoader();
        loaderMap = new HashMap();
        classLoaderChanged();
    }

    /**
		Overlay the entire path with a new class loader.
		Set the base path to the user path + base path.

		No point in including the boot class path (can't reload thos).
	*/
    @Override
    public void reloadAllClasses() throws ClassPathException {
        BshClassPath bcp = new BshClassPath("temp");
        bcp.addComponent(baseClassPath);
        bcp.addComponent(BshClassPath.getUserClassPath());
        setClassPath(bcp.getPathComponents());
    }

    /**
		init the baseLoader from the baseClassPath
	*/
    private void initBaseLoader() {
        baseLoader = new BshClassLoader(this, baseClassPath);
    }

    /**
		Reloading classes means creating a new classloader and using it
		whenever we are asked for classes in the appropriate space.
		For this we use a DiscreteFilesClassLoader
	*/
    @Override
    public void reloadClasses(String[] classNames) throws ClassPathException {
        if (baseLoader == null) initBaseLoader();
        DiscreteFilesClassLoader.ClassSourceMap map = new DiscreteFilesClassLoader.ClassSourceMap();
        for (int i = 0; i < classNames.length; i++) {
            String name = classNames[i];
            ClassSource classSource = baseClassPath.getClassSource(name);
            if (classSource == null) {
                BshClassPath.getUserClassPath().insureInitialized();
                classSource = BshClassPath.getUserClassPath().getClassSource(name);
            }
            if (classSource == null) throw new ClassPathException("Nothing known about class: " + name);
            if (classSource instanceof JarClassSource) throw new ClassPathException("Cannot reload class: " + name + " from source: " + classSource);
            map.put(name, classSource);
        }
        ClassLoader cl = new DiscreteFilesClassLoader(this, map);
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) loaderMap.put((String) it.next(), cl);
        classLoaderChanged();
    }

    /**
		Reload all classes in the specified package: e.g. "com.sun.tools"

		The special package name "<unpackaged>" can be used to refer 
		to unpackaged classes.
	*/
    @Override
    public void reloadPackage(String pack) throws ClassPathException {
        Collection classes = baseClassPath.getClassesForPackage(pack);
        if (classes == null) classes = BshClassPath.getUserClassPath().getClassesForPackage(pack);
        if (classes == null) throw new ClassPathException("No classes found for package: " + pack);
        reloadClasses((String[]) classes.toArray(new String[0]));
    }

    /**
		Get the full blown classpath.
	*/
    public BshClassPath getClassPath() throws ClassPathException {
        if (fullClassPath != null) return fullClassPath;
        fullClassPath = new BshClassPath("BeanShell Full Class Path");
        fullClassPath.addComponent(BshClassPath.getUserClassPath());
        try {
            fullClassPath.addComponent(BshClassPath.getBootClassPath());
        } catch (ClassPathException e) {
            System.err.println("Warning: can't get boot class path");
        }
        fullClassPath.addComponent(baseClassPath);
        return fullClassPath;
    }

    /**
		Support for "import *;"
		Hide details in here as opposed to NameSpace.
	*/
    @Override
    public void doSuperImport() throws UtilEvalError {
        try {
            getClassPath().insureInitialized();
            getClassNameByUnqName("");
        } catch (ClassPathException e) {
            throw new UtilEvalError("Error importing classpath " + e);
        }
        superImport = true;
    }

    @Override
    protected boolean hasSuperImport() {
        return superImport;
    }

    /**
		Return the name or null if none is found,
		Throw an ClassPathException containing detail if name is ambigous.
	*/
    @Override
    public String getClassNameByUnqName(String name) throws ClassPathException {
        return getClassPath().getClassNameByUnqName(name);
    }

    @Override
    public void addListener(Listener l) {
        listeners.addElement(new WeakReference(l, refQueue));
        Reference deadref;
        while ((deadref = refQueue.poll()) != null) {
            boolean ok = listeners.removeElement(deadref);
            if (ok) {
            } else {
                if (Interpreter.DEBUG) Interpreter.debug("tried to remove non-existent weak ref: " + deadref);
            }
        }
    }

    @Override
    public void removeListener(Listener l) {
        throw new Error("unimplemented");
    }

    public ClassLoader getBaseLoader() {
        return baseLoader;
    }

    @Override
    public Class defineClass(String name, byte[] code) {
        baseClassPath.setClassSource(name, new GeneratedClassSource(code));
        try {
            reloadClasses(new String[] { name });
        } catch (ClassPathException e) {
            throw new bsh.InterpreterError("defineClass: " + e);
        }
        return classForName(name);
    }

    /**
		Clear global class cache and notify namespaces to clear their 
		class caches.

		The listener list is implemented with weak references so that we 
		will not keep every namespace in existence forever.
	*/
    @Override
    protected void classLoaderChanged() {
        clearCaches();
        Vector toRemove = new Vector();
        for (Enumeration e = listeners.elements(); e.hasMoreElements(); ) {
            WeakReference wr = (WeakReference) e.nextElement();
            Listener l = (Listener) wr.get();
            if (l == null) toRemove.add(wr); else l.classLoaderChanged();
        }
        for (Enumeration e = toRemove.elements(); e.hasMoreElements(); ) listeners.removeElement(e.nextElement());
    }

    @Override
    public void dump(PrintWriter i) {
        i.println("Bsh Class Manager Dump: ");
        i.println("----------------------- ");
        i.println("baseLoader = " + baseLoader);
        i.println("loaderMap= " + loaderMap);
        i.println("----------------------- ");
        i.println("baseClassPath = " + baseClassPath);
    }
}
