package org.apptools.plugin;

import java.io.*;
import java.net.URL;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**Plug-in loader that finds plugins based on registry files
 * This loader will parse all META-INF/services/org.apptools.plugin.PlugIn
 * files found and load all plugins registered.
 * */
public class RegistryFilePlugInLoader implements PlugInLoader {

    protected ClassLoader auxLoader;

    protected Log log = LogFactory.getLog(RegistryFilePlugInLoader.class);

    public RegistryFilePlugInLoader(ClassLoader auxLoader) {
        this.auxLoader = auxLoader;
    }

    public List getAvailablePlugIns(Class[] pluginClasses, Collection[] classes) {
        List messages = new ArrayList();
        try {
            Enumeration en = auxLoader.getResources("META-INF/services/org.apptools.plugin.PlugIn");
            log.info("Parsing plug-in registry files");
            List classNames = new ArrayList();
            while (en.hasMoreElements()) {
                URL resourceURL = (URL) en.nextElement();
                log.info("Loading plug-ins from: " + resourceURL);
                InputStream in = resourceURL.openStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = br.readLine()) != null) {
                    String className = line.trim();
                    if (!className.startsWith("#") && className.length() > 0) {
                        classNames.add(className);
                    }
                }
            }
            for (Iterator i = classNames.iterator(); i.hasNext(); ) {
                String className = (String) i.next();
                try {
                    Class c = Class.forName(className, true, auxLoader);
                    for (int index = 0; index < pluginClasses.length; index++) {
                        if (pluginClasses[index] != null) {
                            if (pluginClasses[index].isAssignableFrom(c)) {
                                classes[index].add(c);
                            } else if (pluginClasses[index] == c) {
                                classes[index].add(c);
                            }
                        }
                    }
                } catch (ClassNotFoundException cnfx) {
                    messages.add("(" + className + ") Class not found:" + cnfx.getMessage());
                } catch (Throwable ex) {
                    messages.add("(" + className + ") Error:" + ex.toString());
                }
            }
        } catch (IOException iox) {
            messages.add(iox.toString());
        }
        return messages;
    }
}
