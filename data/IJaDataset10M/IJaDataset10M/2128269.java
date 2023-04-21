package sk.fiit.mitandao.classloader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import sk.fiit.mitandao.modules.interfaces.AlgorithmModule;
import sk.fiit.mitandao.modules.interfaces.FilterModule;
import sk.fiit.mitandao.modules.interfaces.InputModule;
import sk.fiit.mitandao.modules.interfaces.Module;
import sk.fiit.mitandao.modules.interfaces.OutputModule;

/**
 * This singleton class loads modules (input, output, algorithm and filters) - they have to be placed on the path 
 * stated in the modules_paths.xml. The XML file has to be places somewhere on the CLASSPATH. 
 * 
 * The path in the XML file can be relative or absolute. The absolute path has to start with protocol 
 * prefix file://. The relative path is resolved against the user directory (as returned by the system 
 * property user.dir). 
 * 
 * @author Tomas Jeline, Lucia Jastrzembska
 *
 */
public class MitandaoClassLoader {

    private static final MitandaoClassLoader INSTANCE = new MitandaoClassLoader();

    private List<Class<? extends InputModule>> listInput = new ArrayList<Class<? extends InputModule>>();

    private List<Class<? extends OutputModule>> listOutput = new ArrayList<Class<? extends OutputModule>>();

    private List<Class<? extends FilterModule>> listFilter = new ArrayList<Class<? extends FilterModule>>();

    private List<Class<? extends AlgorithmModule>> listAlgorithm = new ArrayList<Class<? extends AlgorithmModule>>();

    private final Logger log = Logger.getLogger(this.getClass());

    private URLClassLoader classLoader = null;

    /**
	 * Singleton. 
	 */
    private MitandaoClassLoader() {
        log.debug("Creating classloader");
        classLoader = new URLClassLoader(getPathURLs());
        fillLists();
    }

    /**
	 * Returns the single instance of this class. 
	 * @return This class' singleton. 
	 */
    public static MitandaoClassLoader getInstance() {
        return INSTANCE;
    }

    /**
	 * Returns the input modules that are available. Every time this method is called new instances of
	 * modules are created. 
	 * @return List of input modules. 
	 */
    public List<InputModule> getInputModules() {
        log.debug("Returning available input modules");
        return getModules(listInput);
    }

    /**
	 * Returns the output modules that are available. Every time this method is called new instances of
	 * modules are created. 
	 * @return List of output modules. 
	 */
    public List<OutputModule> getOutputModules() {
        log.debug("Returning available output modules");
        return getModules(listOutput);
    }

    /**
	 * Returns the filter modules that are available. Every time this method is called new instances of
	 * filters are created. 
	 * @return List of input modules. 
	 */
    public List<FilterModule> getFilterModules() {
        log.debug("Returning available filter modules");
        return getModules(listFilter);
    }

    /**
	 * Returns the algorithm modules that are available. Every time this method is called new instances of
	 * modules are created. 
	 * @return List of algorithm modules. 
	 */
    public List<AlgorithmModule> getAlgorithmModules() {
        log.debug("Returning available algorithm modules");
        return getModules(listAlgorithm);
    }

    /**
	 * Generic method for filling list of various modules (input, output etc.)
	 * 
	 * @param <T> module class (input, output, algorithm or filter)
	 * @param classList list of found classes of the specified supertype T
	 * @return list of the instances of classes. 
	 */
    private <T extends Module> List<T> getModules(List<Class<? extends T>> classList) {
        List<T> modules = new ArrayList<T>();
        for (Class<? extends T> c : classList) {
            try {
                modules.add(c.newInstance());
            } catch (InstantiationException e) {
                log.error(e.getStackTrace());
            } catch (IllegalAccessException e) {
                log.error(e.getStackTrace());
            }
        }
        return (List<T>) modules;
    }

    /**
	 * Returns the URLs from XML file. 
	 * @return URLs from XML file. 
	 */
    private URL[] getPathURLs() {
        log.debug("Returning modules paths URLs");
        try {
            return XMLClassPathReader.getInstance().getPathURLs();
        } catch (Exception e) {
            log.error("Error while trying to load modules paths");
            return null;
        }
    }

    private void fillLists() {
        URL[] urls = getPathURLs();
        File file = null;
        for (URL url : urls) {
            file = new File(url.getPath());
            locateClasses(file, url.getPath());
        }
    }

    /**
	 * Recursively searches directories until finds the .class files. Than it converts it to the
	 * class name (with package) and creates the Class. Stores input, output, filters and algorithms 
	 * classes in separates lists. 
	 * 
	 * @param file Directory of file. 
	 * @param packagePath The path to the first folder of the package. 
	 */
    private void locateClasses(File file, String packagePath) {
        if (file == null) {
            return;
        }
        if (file.isDirectory()) {
            log.debug("File " + file.getName() + " is a directory, searching content");
            File fileArray[] = file.listFiles();
            if (fileArray == null) {
                return;
            }
            for (int i = 0; i < fileArray.length; i++) {
                locateClasses(fileArray[i], packagePath);
            }
        }
        if (file.getName().endsWith(".class")) {
            log.debug("Found class " + file.getName());
            String name = getClassName(file.getAbsolutePath(), packagePath);
            addClassToList(name);
        }
    }

    /**
	 * Returns the class name from the absolute path to the class and path to the package. 
	 * @param name Absolute path to the class. 
	 * @param packagePath Path to the first directory of the package. 
	 * @return Full class name. 
	 */
    private String getClassName(String name, String packagePath) {
        log.debug("Absolute path to class " + name);
        name = name.replace("\\", "/");
        name = name.substring(0, name.lastIndexOf(".class"));
        int packagePosition = name.lastIndexOf(packagePath);
        name = name.substring(packagePosition + packagePath.length(), name.length());
        log.debug("Package path " + packagePath);
        return name;
    }

    /**
	 * Creates class and adds it to the appropriate list. 
	 * @param name Full class name. 
	 */
    private void addClassToList(String name) {
        log.debug("Class name " + name);
        String fileName = name.replaceAll("/", ".");
        log.debug("Path to class " + fileName);
        try {
            Class<?> c = Class.forName(fileName, true, classLoader);
            for (Class<?> interfaceClass : c.getInterfaces()) {
                if (interfaceClass == InputModule.class) {
                    log.debug("Class " + name + "is an InputModule");
                    listInput.add((Class<? extends InputModule>) c);
                }
                if (interfaceClass == OutputModule.class) {
                    log.debug("Class " + name + "is an OutputModule");
                    listOutput.add((Class<? extends OutputModule>) c);
                }
                if (interfaceClass == FilterModule.class) {
                    log.debug("Class " + name + "is an FilterModule");
                    listFilter.add((Class<? extends FilterModule>) c);
                }
                if (interfaceClass == AlgorithmModule.class) {
                    log.debug("Class " + name + "is an AlgorithmModule");
                    listAlgorithm.add((Class<? extends AlgorithmModule>) c);
                }
            }
        } catch (ClassNotFoundException e) {
            log.error("Error creating class " + name, e);
        }
    }

    /**
	 * Return the special classloader that can find the modules on the path stated in the XML file. 
	 * @return
	 */
    public ClassLoader getURLClassLoader() {
        return classLoader;
    }
}
