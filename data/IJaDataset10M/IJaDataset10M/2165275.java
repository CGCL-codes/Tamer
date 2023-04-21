package obix;

import java.lang.reflect.*;
import java.util.*;
import obix.asm.*;

/**
 * ContractRegistry serves a central database for mapping
 * contract URIs to Contract definitions.
 *
 * @author    Brian Frank
 * @creation  27 Apr 05
 * @version   $Revision$ $Date$
 */
public class ContractRegistry {

    /**
   * Convenience for <code>toClass(base, contract).newInstance()<code>.
   */
    public static Obj toObj(Class base, Contract contract) {
        try {
            return (Obj) toClass(base, contract).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
    }

    /**
   * Lookup a Class which best supports the specified 
   * contract (set of URIs).  The returned class will
   * be subclassed from base and implement any interfaces
   * registered for URIs in the specified contract list.
   */
    public static Class toClass(Class base, Contract contract) {
        if (contract == null || contract.size() == 0 || contract.containsOnlyObj() || base == Ref.class) return base;
        String key = base.getName() + ": " + contract.toString();
        Class cls = (Class) cache.get(key);
        if (cls == NotFound) return base;
        if (cls != null) return cls;
        try {
            cls = compile(base, contract);
            if (cls == null) {
                cache.put(key, NotFound);
                return base;
            }
            cache.put(key, cls);
            return cls;
        } catch (Exception e) {
            throw new RuntimeException("Cannot compile contract: " + key, e);
        }
    }

    /**
   * Search for a registered interface for each URI in the
   * contract list.  Then dynamically assemble the bytecode 
   * for a class which implements all the interfaces.
   */
    private static Class compile(Class base, Contract contract) throws Exception {
        Uri[] list = contract.list();
        ArrayList acc = new ArrayList();
        for (int i = 0; i < list.length; ++i) {
            Class cls = loader.getClass(list[i]);
            if (cls != null) {
                acc.add(cls);
            }
            String baseClassName = (String) baseContracts.get(list[i].get());
            if (baseClassName != null) {
                if (base != Obj.class && !base.getName().equals(baseClassName)) throw new IllegalArgumentException("Base conflicts with contract: " + base.getName() + " and " + list[i].get());
                base = Class.forName(baseClassName);
            }
        }
        if (acc.size() == 0) return base;
        Class[] interfaces = (Class[]) acc.toArray(new Class[acc.size()]);
        Class cls = ObixAssembler.compile(base, interfaces);
        System.out.println("-- Compile: " + base.getName() + ": " + contract + " -> " + cls.getName());
        return cls;
    }

    /**
   * Convenience for <code>put(new Uri(href), className)</code>.
   */
    public static void put(String href, String className) {
        put(new Uri(href), className);
    }

    /**
   * Register a subclass of Obj for the specified contract uri.
   */
    public static void put(Uri href, String className) {
        if (map.get(href.get()) != null) throw new IllegalStateException("The specified href is already mapped: " + href);
        map.put(href.get(), className);
        cache.clear();
    }

    /**
   * Convenience for <code>put(new Uri(href), loadedClass)</code>.
   */
    public static void put(String href, Class loadedClass) {
        loader.put(href, loadedClass);
    }

    /**
   * Register a subclass of Obj for the specified contract uri.
   */
    public static void put(Uri href, Class loadedClass) {
        loader.put(href, loadedClass);
    }

    public static ClassLoader getContractClassLoader() {
        return loader;
    }

    static ContractClassLoader loader = new ContractClassLoader();

    static HashMap map = new HashMap();

    static HashMap cache = new HashMap();

    static Class NotFound = ContractRegistry.class;

    static HashMap baseContracts = new HashMap();

    static {
        baseContracts.put("obix:obj", "obix.Obj");
        baseContracts.put("obix:bool", "obix.Bool");
        baseContracts.put("obix:int", "obix.Int");
        baseContracts.put("obix:real", "obix.Real");
        baseContracts.put("obix:str", "obix.Str");
        baseContracts.put("obix:enum", "obix.Enum");
        baseContracts.put("obix:abstime", "obix.Abstime");
        baseContracts.put("obix:reltime", "obix.Reltime");
        baseContracts.put("obix:uri", "obix.Uri");
        baseContracts.put("obix:list", "obix.List");
        baseContracts.put("obix:op", "obix.Op");
        baseContracts.put("obix:event", "obix.Event");
        baseContracts.put("obix:ref", "obix.Ref");
        baseContracts.put("obix:err", "obix.Err");
        obix.contracts.ContractInit.init();
    }

    private static class ContractClassLoader extends ClassLoader {

        public ContractClassLoader() {
            super(ContractClassLoader.class.getClassLoader());
        }

        public void put(String href, Class loadedClass) {
            put(new Uri(href), loadedClass);
        }

        public void put(Uri href, Class loadedClass) {
            ContractRegistry.put(href, loadedClass.getName());
            classMap.put(loadedClass.getName(), loadedClass);
        }

        public Class getClass(Uri uri) throws Exception {
            String className = (String) map.get(uri.get());
            return (className == null) ? null : loadClass(className);
        }

        protected Class findClass(String name) throws ClassNotFoundException {
            Class result = (Class) classMap.get(name);
            return (result == null) ? super.findClass(name) : result;
        }

        private HashMap classMap = new HashMap();
    }
}
