package de.miethxml.toolkit.application;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth </a>
 *
 *
 *
 *
 *
 *
 *
 */
public class Main {

    private static final String[] defaultPath = new String[] { "lib/core", "lib/endorsed", "lib/optional" };

    ApplicationLoader al;

    private String initClass = "de.miethxml.hawron.ApplicationContainer";

    /**
     *
     *
     *
     */
    public Main() {
        super();
        al = new ApplicationLoader();
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.init(args);
    }

    public void init(String[] cliargs) {
        Hashtable params = parseParameters(cliargs);
        if (params.containsKey("jarrepository")) {
            setClasspath(((String) params.get("jarrepository")).split(File.pathSeparator));
        } else {
            setClasspath(defaultPath);
        }
        Thread.currentThread().setContextClassLoader(al);
        try {
            Class c = al.loadClass(initClass);
            try {
                Launcher l = (Launcher) c.newInstance();
                l.init(params);
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void dumpSystemProperties() {
        Properties props = System.getProperties();
        Enumeration e = props.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            System.out.println(key + "=" + props.getProperty(key));
        }
    }

    private void setClasspath(String[] path) {
        for (int i = 0; i < path.length; i++) {
            al.addPathElement(path[i]);
        }
    }

    private Hashtable parseParameters(String[] args) {
        Hashtable params = new Hashtable();
        if (args.length > 1) {
            for (int i = 0; (i < args.length) && ((i + 1) < args.length); i += 2) {
                String key = null;
                if (args[i].startsWith("--")) {
                    params.put(args[i].substring(2, args[i].length()), args[i + 1]);
                } else if (args[i].startsWith("-")) {
                    params.put(args[i].substring(1, args[i].length()), args[i + 1]);
                }
            }
        }
        return params;
    }
}
