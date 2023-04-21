package saadadb.generationclass;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import saadadb.database.Database;
import saadadb.util.Messenger;

/**
 * SaadaClassReloader overrides the default class loader. The default class loader
 * System.classloader cannot be used because it does not support to unload/reload modified class.
 * In FUSION mapping mode a loaded class can be modified to fit new product. The modified class
 * must be reloaded for the data-laoder to work.
 * courtesy of http://tutorials.jenkov.com/java-reflection/dynamic-class-loading-reloading.html
 *  This loader must be invoked by static methods: forGeneratedName and reloadGeneratedClas
 *  Method loadClass is not private although it should be, because it overrides this one of ClassLoader
 *  The class to load is supposed to belong to the package generated.DBNAME
 *  Loaded classes are stored in a map. They musn't be called by the default loader especially in admin mode.
 *  If this loader can not access the .class file, it invokes the default loader which can deal with jar files. 
 *  That is the case in servlet mode
 *
 * @author michel
 * * @version $Id: SaadaClassReloader.java 353 2012-04-12 13:58:38Z laurent.mistahl $
 *
 * 04/2012: Do not reload a class already loaded by the loader. 
 *          Made otherwise strange messages like classA could not be casted to classA
 */
public class SaadaClassReloader extends ClassLoader {

    private String class_to_reload;

    private static final String PACKAGE = "generated." + Database.getDbname() + ".";

    private static final String CLASSPATH = Database.getRoot_dir() + Database.getSepar() + "class_mapping" + Database.getSepar() + "generated" + Database.getSepar() + Database.getDbname() + Database.getSepar();

    @SuppressWarnings("rawtypes")
    private static LinkedHashMap<String, Class> loadedClasses = new LinkedHashMap<String, Class>();

    /**
	 * @param class_to_reload
	 */
    public SaadaClassReloader(String class_to_reload) {
        super(SaadaClassReloader.class.getClassLoader());
        ;
        this.class_to_reload = class_to_reload;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Class loadClass(String classname) throws ClassNotFoundException {
        if (!class_to_reload.equals(classname)) {
            return SaadaClassReloader.class.getClassLoader().loadClass(classname);
        }
        try {
            Messenger.printMsg(Messenger.TRACE, "(re)load generated class " + classname);
            String url = "file:" + CLASSPATH + classname + ".class";
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();
            while (data != -1) {
                buffer.write(data);
                data = input.read();
            }
            input.close();
            byte[] classData = buffer.toByteArray();
            Class retour = defineClass(PACKAGE + classname, classData, 0, classData.length);
            loadedClasses.put(classname, retour);
            return retour;
        } catch (Exception e) {
            Class cl = Class.forName(PACKAGE + classname);
            loadedClasses.put(classname, cl);
            return cl;
        }
    }

    /**
	 * Similar to the Class.forName method except that, te class is loaded if needed
	 * @param classname: class to return (package generated.DBNAME)
	 * @return : classname reflexion
	 * @throws ClassNotFoundException
	 */
    @SuppressWarnings("rawtypes")
    public static Class forGeneratedName(String classname) throws ClassNotFoundException {
        try {
            Class retour = loadedClasses.get(classname);
            if (retour == null) {
                if ((retour = Class.forName(PACKAGE + classname)) != null) {
                    if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Class " + classname + " already loaded by the standard loader");
                    return retour;
                }
                return SaadaClassReloader.reloadGeneratedClass(classname);
            } else {
                return retour;
            }
        } catch (Exception e) {
            return SaadaClassReloader.reloadGeneratedClass(classname);
        }
    }

    /**
	 * Builds a Saada class loader dedicated to the class classname (package generated.DBNAME) ans invoke it 
	 * load the class. The class is loaded in any case whereas its components are just asked to the default
	 * class loader
	 * @param classname
	 * @return
	 * @throws ClassNotFoundException
	 */
    @SuppressWarnings("rawtypes")
    public static Class reloadGeneratedClass(String classname) throws ClassNotFoundException {
        SaadaClassReloader scr = new SaadaClassReloader(classname);
        return scr.loadClass(classname);
    }
}
