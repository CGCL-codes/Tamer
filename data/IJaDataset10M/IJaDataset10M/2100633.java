package net.sourceforge.freejava.ant;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import net.sourceforge.freejava.jvm.stack.Caller;
import net.sourceforge.freejava.loader.UCL;
import net.sourceforge.freejava.util.Factory;
import net.sourceforge.freejava.util.exception.CreateException;
import net.sourceforge.freejava.util.exception.IllegalUsageException;
import net.sourceforge.freejava.util.file.Files;
import org.apache.tools.ant.types.Path;

public class ValueConstruct extends WithParameters {

    private Factory<Object> factory;

    private URLClassLoader loader;

    public ValueConstruct() {
        ClassLoader parent = Caller.getCallerClassLoader(0);
        loader = new URLClassLoader(new URL[0], parent);
    }

    void setFactory(Factory<Object> factory) {
        if (this.factory != null) throw new IllegalUsageException("Factory is already specified: " + this.factory);
        this.factory = factory;
    }

    public void setObject(Object obj) {
        setFactory(new Factory.Static<Object>(obj));
    }

    /**
     * @param className
     *            Must be visible to the classloader of this task class. That means, bodz_bas should
     *            not in the bootstrap classpath,
     */
    public void setClassName(int caller, String className) {
        setFactory(new Factory.ByClassName(loader, className));
    }

    public void setXml(String xml) {
        setFactory(new Factory.ByXML(xml, null));
    }

    public void setXmlFile(File xmlFile) {
        setFactory(new Factory.ByXMLFile(xmlFile, null));
    }

    public void addConfiguredClasspath(Path path) {
        String[] paths = path.list();
        URL[] urls = new URL[paths.length];
        for (int i = 0; i < paths.length; i++) {
            File loc = new File(paths[i]);
            URL url = Files.getURL(loc);
            urls[i] = url;
        }
        UCL.addURL(loader, urls);
    }

    public Object create() throws CreateException {
        return create(null, null);
    }

    public Object create(Class<?>[] prependTypes, Object[] prependValues) throws CreateException {
        if (factory == null) throw new IllegalUsageException("Don\'t know how to get bean instance");
        Class<?>[] types = prependTypes(prependTypes).toArray(Empty.Classes);
        Object[] values = prependValues(prependValues).toArray(Empty.Objects);
        return factory._create(types, values);
    }
}
