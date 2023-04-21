package com.werken.forehead;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * * ClassLoader configurator and application launcher.
 * <p>
 * This is the main command-line entry-point into the
 * <code>forehead</code> framework. Please see the
 * <code>forehead</code> documentation for usage instructions.
 * </p>
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 */
public class Forehead {

    /** * Singleton. */
    private static final Forehead INSTANCE = new Forehead();

    /** * ClassLoaders indexed by name. */
    private Map classLoaders;

    /** * Root unnamed loader. */
    private ForeheadClassLoader rootLoader;

    /** * The loader to use for entry-point lookup. */
    private ForeheadClassLoader entryLoader;

    /** * The entry-point class. */
    private Class entryClass;

    /** * The entry-point method. */
    private Method entryMethod;

    /**
     * * Construct.
     */
    public Forehead() {
        this.classLoaders = new HashMap();
    }

    /**
     * * Configure from an <code>InputStream</code>.
     * @param in <code>InputStream</code> carrying configuration
     *        information.
     * @throws ForeheadException If there is an error during
     *         configuration.
     * @throws IOException If there is an error reading configuration
     *         information.
     * @throws ClassNotFoundException If unable to locate entry-point
     *         class.
     */
    public void config(InputStream in) throws ForeheadException, IOException, ClassNotFoundException {
        config(new BufferedReader(new InputStreamReader(in)));
    }

    /**
     * * Configure from an <code>Reader</code>.
     * @param in <code>Reader</code> carrying configuration
     *        information.
     * @throws ForeheadException If there is an error during
     *         configuration.
     * @throws IOException If there is an error reading configuration
     *         information.
     * @throws ClassNotFoundException If unable to locate entry-point
     *         class.
     */
    public void config(Reader in) throws ForeheadException, IOException, ClassNotFoundException {
        if (in instanceof BufferedReader) {
            config(in);
        } else {
            config(new BufferedReader(in));
        }
    }

    /**
     * * Configure from an <code>BufferedReader</code>.
     * @param in <code>BufferedReader</code> carrying configuration
     *        information.
     * @throws ForeheadException If there is an error during
     *         configuration.
     * @throws IOException If there is an error reading configuration
     *         information.
     * @throws ClassNotFoundException If unable to locate entry-point
     *         class.
     */
    public void config(BufferedReader in) throws ForeheadException, IOException, ClassNotFoundException {
        this.rootLoader = new ForeheadClassLoader(getClass().getClassLoader(), "$forehead-root$");
        String line = null;
        ForeheadClassLoader currentLoader = this.rootLoader;
        Properties props = new Properties();
        String entryLine = null;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if ("".equals(line)) {
                continue;
            }
            if (line.startsWith("#")) {
                continue;
            }
            if (line.startsWith("+")) {
                String propName = line.substring(1);
                String propValue = System.getProperty(propName);
                if (propValue == null) {
                    throw new NoSuchPropertyException(propName);
                }
                props.setProperty(propName, propValue);
                continue;
            }
            if (line.startsWith("=")) {
                entryLine = line;
                continue;
            }
            ForeheadClassLoader parentLoader = null;
            if (line.startsWith("[") && line.endsWith("]")) {
                String loaderName = line.substring(1, line.length() - 1);
                int dotLoc = loaderName.lastIndexOf(".");
                if (dotLoc > 0) {
                    String parentName = loaderName.substring(0, dotLoc);
                    parentLoader = getClassLoader(parentName);
                    if (parentLoader == null) {
                        throw new NoSuchClassLoaderException(parentName);
                    }
                } else {
                    parentLoader = this.rootLoader;
                }
                currentLoader = createClassLoader(parentLoader, loaderName);
            } else {
                String resolvedLine = resolveProperties(line, props);
                load(resolvedLine, currentLoader);
            }
        }
        if (entryLine == null) {
            throw new NoEntryDescriptorException();
        }
        setupEntry(entryLine);
    }

    /**
     * * Setup the entry-point.
     * @param line The entry-point configuration line.
     * @throws MalformedEntryDescriptorException If the entry-point
     *         descriptor is malformed.
     * @throws NoSuchClassLoaderException If the entry-point
     *         descriptor references an unknown ClassLoader.
     * @throws ClassNotFoundException If unable to locate the
     *         entry-point class.
     */
    protected void setupEntry(String line) throws MalformedEntryDescriptorException, NoSuchClassLoaderException, ClassNotFoundException {
        int leftBrackLoc = line.indexOf("[");
        if (leftBrackLoc < 0) {
            throw new MalformedEntryDescriptorException(line);
        }
        int rightBrackLoc = line.indexOf("]", leftBrackLoc + 1);
        if (rightBrackLoc < 0) {
            throw new MalformedEntryDescriptorException(line);
        }
        String loaderName = line.substring(leftBrackLoc + 1, rightBrackLoc);
        String className = line.substring(rightBrackLoc + 1).trim();
        this.entryLoader = getClassLoader(loaderName);
        if (this.entryLoader == null) {
            throw new NoSuchClassLoaderException(loaderName);
        }
        this.entryClass = Class.forName(className, true, this.entryLoader);
    }

    /**
     * * Load a glob, file, or URL into the specified classloader.
     * @param line The path configuration line.
     * @param loader The loader to populate
     * @throws MalformedURLException If the line does not represent a
     *         valid path element.
     */
    protected void load(String line, ForeheadClassLoader loader) throws MalformedURLException {
        if (line.indexOf("*") >= 0) {
            loadGlob(line, loader);
        } else {
            loadFileOrUrl(line, loader);
        }
    }

    /**
     * * Load a glob into the specified classloader.
     * @param line The path configuration line.
     * @param loader The loader to populate
     * @throws MalformedURLException If the line does not represent a
     *         valid path element.
     */
    protected void loadGlob(String line, ForeheadClassLoader loader) throws MalformedURLException {
        File globFile = new File(line);
        File dir = globFile.getParentFile();
        String localName = globFile.getName();
        int starLoc = localName.indexOf("*");
        final String prefix = localName.substring(0, starLoc);
        final String suffix = localName.substring(starLoc + 1);
        File[] matches = dir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (prefix != null && !name.startsWith(prefix)) {
                    return false;
                }
                if (suffix != null && !name.endsWith(suffix)) {
                    return false;
                }
                return true;
            }
        });
        for (int i = 0; i < matches.length; ++i) {
            loader.addURL(matches[i].toURL());
        }
    }

    /**
     * * Load a file or URL into the specified classloader.
     * @param line The path configuration line.
     * @param loader The loader to populate
     * @throws MalformedURLException If the line does not represent a
     *         valid path element.
     */
    protected void loadFileOrUrl(String line, ForeheadClassLoader loader) throws MalformedURLException {
        URL url = null;
        File file = new File(line);
        if (file.exists()) {
            url = file.toURL();
        } else {
            try {
                url = new URL(line);
            } catch (MalformedURLException mue) {
                System.err.println("File or url '" + line + "' could not be found");
                throw mue;
            }
        }
        loader.addURL(url);
    }

    /**
     * * Create a new ClassLoader given a parent and a name.
     * @param parent The parent of the ClassLoader to create.
     * @param name The name of the ClassLoader to create.
     * @return A newly configured <code>ClassLoader</code>.
     */
    protected ForeheadClassLoader createClassLoader(ForeheadClassLoader parent, String name) {
        ForeheadClassLoader loader = new ForeheadClassLoader(parent, name);
        this.classLoaders.put(name, loader);
        return loader;
    }

    /**
     * * Retrieve a ClassLoader by name.
     * @param name The name of the ClassLoader to retrieve.
     * @return The associated ClassLoader, or <code>null</code> if
     *         none.
     */
    public ForeheadClassLoader getClassLoader(String name) {
        return (ForeheadClassLoader) this.classLoaders.get(name);
    }

    /**
     * * Resolve imported properties.
     * @param input The string input to resolve properties.
     * @param props Properties to resolve against.
     * @return The string with properties resolved.
     */
    public String resolveProperties(String input, Properties props) {
        String output = "";
        int cur = 0;
        int prefixLoc = 0;
        int suffixLoc = 0;
        while (cur < input.length()) {
            prefixLoc = input.indexOf("${", cur);
            if (prefixLoc < 0) {
                break;
            }
            suffixLoc = input.indexOf("}", prefixLoc);
            String propName = input.substring(prefixLoc + 2, suffixLoc);
            output = output + input.substring(cur, prefixLoc);
            output = output + props.getProperty(propName);
            cur = suffixLoc + 1;
        }
        output = output + input.substring(cur);
        return output;
    }

    /**
     * * Launch the wrapped application.
     * @param args Command-line args to pass to wrapped application.
     * @throws NoSuchEntryMethodException If unable to find the entry
     *         method on the class.
     * @throws IllegalAccessException If an error occurs while
     *         attempting to invoke the entry-point method.
     * @throws InvocationTargetException If an error occurs while
     *         attempting to invoke the entry-point method.
     */
    public void run(String[] args) throws NoSuchEntryMethodException, IllegalAccessException, InvocationTargetException {
        Method[] methods = this.entryClass.getMethods();
        for (int i = 0; i < methods.length; ++i) {
            if (!"main".equals(methods[i].getName())) {
                continue;
            }
            int modifiers = methods[i].getModifiers();
            if (!(Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))) {
                continue;
            }
            if (methods[i].getReturnType() != Void.TYPE) {
                continue;
            }
            Class[] paramTypes = methods[i].getParameterTypes();
            if (paramTypes.length != 1) {
                continue;
            }
            if (paramTypes[0] != String[].class) {
                continue;
            }
            this.entryMethod = methods[i];
            break;
        }
        if (this.entryMethod == null) {
            throw new NoSuchEntryMethodException(this.entryClass, "public static void main(String[] args)");
        }
        Thread.currentThread().setContextClassLoader(this.entryLoader);
        this.entryMethod.invoke(this.entryClass, new Object[] { args });
    }

    public static Forehead getInstance() {
        return INSTANCE;
    }

    /**
     * * Main.
     * @param args Command-line arguments to pass to the wrapped
     *        application.
     */
    public static void main(String[] args) {
        String confFileName = System.getProperty("forehead.conf.file");
        File confFile = new File(confFileName);
        Forehead forehead = Forehead.getInstance();
        try {
            forehead.config(new FileInputStream(confFile));
            forehead.run(args);
        } catch (NoSuchEntryMethodException e) {
            System.err.println("No method on class " + e.getEntryClass() + " matching: " + e.getEntryMethodDescriptor());
            e.printStackTrace();
        } catch (ForeheadException e) {
            System.err.println("Error during configuration: " + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading configuration: " + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
