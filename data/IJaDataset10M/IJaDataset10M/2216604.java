package simple.http.load;

import simple.http.serve.Context;
import java.lang.reflect.Constructor;
import java.net.URLClassLoader;
import java.util.Hashtable;
import java.util.Vector;
import java.net.URL;

/**
 * The <code>Registry</code> object is used to load <code>Service</code>
 * implementations from a URL classpath. The <code>Service</code> is
 * loaded using a <code>URLClassLoader</code> with a set URLs from
 * where to load the code. This will attempt to load the objects from
 * the users classpath and if this fails the URL classpath, as the 
 * class loading framework suggests. If at any point during the load
 * of the class by the <code>ClassLoader</code> there is a problem or
 * if the implementation does not have a suitable constructor then a 
 * <code>LoadingException</code> is thrown.  
 * <p>
 * If no <code>URL[]</code> codebase is specified in the constructor
 * then this will load the implementations from the class loader that
 * was used to load this, which is equivelant to using the loader 
 * <code>Registry.class.getClassLoader()</code>. This will ensure that 
 * if a loader engine is loaded from a dynamic class that it will be
 * able to inherit an extended classpath. 
 * <p>
 * This will attempt to construct all <code>Service</code> instances
 * using a constructor with  a single <code>Context</code> argument, 
 * if that is not implemented, or the constructor is not public then 
 * a <code>LoadingException</code> is thrown.
 * <p>
 * This will register each <code>Service</code> instance created with
 * the name specified in the <code>load</code> method. This is done 
 * so that this can maintain multiple instances of the same class
 * using specific names for each <code>Service</code> object loaded.
 *
 * @see java.net.URLClassLoader
 *
 * @author Niall Gallagher
 */
final class Registry {

    /**
    * This represents the loader with an extended classpath.
    */
    private ClassLoader loader;

    /**
    * This contains the created instances indexed by name.
    */
    private Hashtable loaded;

    /**
    * The <code>Context</code> the resource is created with.
    */
    private Context context;

    /**
    * This is the <code>URL</code> array used as the codebase.
    */
    private URL[] codebase;

    /**
    * This is an ordered list of the class names of services.
    */
    private Vector classes;

    /**
    * This is an ordered list of the names of the services.
    */
    private Vector names;

    /** 
    * Constructor that creates a <code>Registry</code> to load 
    * from the specified URL codebase. This will enable the code
    * for the <code>Service</code> implementation to be loaded
    * from the specified URL classpath. This loads the services
    * with the <code>URLCLassLoader</code>. 
    *
    * @param context this is the context that resources must use
    * @param codebase this is the URL codebase that all the code
    * for the <code>Service</code> implementations is loaded
    */
    public Registry(Context context, URL[] codebase) {
        this.loader = Registry.class.getClassLoader();
        this.loaded = new Hashtable();
        this.names = new Vector();
        this.classes = new Vector();
        this.codebase = codebase;
        this.context = context;
    }

    /**
    * This is used to load the <code>Service</code> object from the 
    * specified classpath. If the <code>Service</code> cannot be 
    * loaded from the specified classpath then this will throw a 
    * <code>LoadingException</code>. Also if the class is loaded 
    * from the specified classpath and does not instantiate because
    * it does not have a suitable constructor then an exception is
    * also thrown. The fully qualified package name must be given.
    * <p>
    * This requires that the <code>Service</code> subclass has a
    * constructor with a single <code>Context</code> argument, if 
    * this does not exist then the service cannot be instantiated.
    * The service instance is stored under the specified name.
    *
    * @param name this is the name of the instance that is created
    * @param className this is the fully qualified class name 
    * of the <code>Service</code>    
    *
    * @exception LoadingException is thrown if the class could 
    * not be loaded
    */
    public Service load(String name, String className) throws LoadingException {
        try {
            loaded.put(name, load(className));
            names.add(name);
            classes.add(className);
            return retrieve(name);
        } catch (Exception cause) {
            throw new LoadingException(cause);
        }
    }

    /**
    * This is used to load the <code>Service</code> object from the 
    * specified classpath. If the <code>Service</code> cannot be 
    * loaded from the specified classpath then this will throw a 
    * <code>LoadingException</code>. Also if the class is loaded 
    * from the specified classpath and does not instantiate because
    * it does not have a suitable constructor then an exception is
    * also thrown. The fully qualified package name must be given.   
    * <p>
    * This requires that the <code>Service</code> subclass has a
    * constructor with a single <code>Context</code> argument, if 
    * this does not exist then the service cannot be instantiated.    
    *
    * @param className this is the fully qualified class name 
    * of the <code>Service</code>       
    *
    * @exception Exception is thrown if the class could not be 
    * instantiated
    */
    private Object load(String className) throws Exception {
        Class[] types = new Class[] { Context.class };
        Class type = getClassLoader().loadClass(className);
        Constructor target = type.getDeclaredConstructor(types);
        return target.newInstance(new Object[] { context });
    }

    /**
    * This creates a new <code>ClassLoader</code> that loads from the
    * URL codebase issued. If <code>Service</code> objects are loaded
    * from this instances class loader then they are loaded once. This
    * is not convienient if the <code>Service</code> byte codes change.
    * <p>
    * This returns a new <code>ClassLoader</code> that will load only
    * from the specified codebase, and from the parents classpath. 
    * This ensures that every time an object is loaded it will return
    * the most up to date version of the service implementation.
    *
    * @return a <code>ClassLoader</code> for the specified codebase
    * 
    * @exception Exception if the class loader could not be created
    */
    private ClassLoader getClassLoader() throws Exception {
        return getClassLoader(loader);
    }

    /**
    * This creates a new <code>ClassLoader</code> that loads from the
    * URL codebase issued. This will use the <code>ClassLoader</code> 
    * that was used to load this class as a parent. This ensures that
    * if there is a dynamically loaded object using this, that it 
    * will have no problem loading <code>Service</code> objects.
    *
    * @param parent the parent loader which contains extra classpaths
    *
    * @return a <code>ClassLoader</code> for the specified codebase
    * 
    * @exception Exception if the class loader could not be created
    */
    private ClassLoader getClassLoader(ClassLoader parent) throws Exception {
        return new URLClassLoader(codebase, parent);
    }

    /**
    * This is used to retrieve the fully qualified class names of
    * the resources loaded by this <code>Registry</code>. This
    * contains the class names that match by index the names of
    * the resources retrieved with <code>getNames</code>.
    *
    * @return an array of strings that is parallel to the names
    * retrieved from the <code>getNames</code> method
    */
    public String[] getClassNames() {
        String[] list = new String[names.size()];
        return (String[]) classes.toArray(list);
    }

    /**
    * This is used to retrieve the unique names of the service
    * instances loaded by this <code>Registry</code>. The list
    * of names returns matches by index the class type of the
    * instance as retrieved by <code>getClassNames</code>.
    *
    * @return an array of strings that is parallel to the class
    * names from the <code>getClassNames</code> method    
    */
    public String[] getNames() {
        String[] list = new String[names.size()];
        return (String[]) names.toArray(list);
    }

    /**
    * This retrieves the <code>Service</code> instance loaded with
    * the specified name. This will retrieve the instance if it 
    * has been loaded previously by this <code>Registry</code> if 
    * not then null is returned.
    *
    * @param name this is the name of the <code>Service</code>
    * instance that was loaded previously
    */
    public Service retrieve(String name) {
        return (Service) loaded.get(name);
    }

    /**
    * This determines whether the service instance was loaded
    * by this <code>Registry</code>. Returns true if there is
    * a <code>Service</code> instance by the specified name.
    *     
    * @param name this is the name of a <code>Service</code>
    * instance that was loaded previously
    *
    * @return this returns true if there is a service by the
    * specified name loaded previously
    */
    public boolean contains(String name) {
        return name != null && loaded.containsKey(name);
    }

    /**
    * This removes the <code>Service</code> instance by the given
    * name from the <code>Registry</code>. This ensures that the
    * <code>getClassNames</code> and <code>getNames</code>
    * methods are kept parallel when the service is removed.
    *
    * @param name this is the name of a <code>Service</code>
    * instance that was loaded previously    
    */
    public void remove(String name) {
        int pos = names.indexOf(name);
        if (pos >= 0) {
            classes.removeElementAt(pos);
            names.removeElementAt(pos);
        }
        loaded.remove(name);
    }
}
