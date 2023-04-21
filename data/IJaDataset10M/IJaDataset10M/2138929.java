package javax.rmi.CORBA;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.security.action.GetPropertyAction;
import java.util.Properties;

class GetORBPropertiesFileAction implements PrivilegedAction {

    private boolean debug = false;

    public GetORBPropertiesFileAction() {
    }

    private String getSystemProperty(final String name) {
        String propValue = (String) AccessController.doPrivileged(new PrivilegedAction() {

            public java.lang.Object run() {
                return System.getProperty(name);
            }
        });
        return propValue;
    }

    private void getPropertiesFromFile(Properties props, String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) return;
            FileInputStream in = new FileInputStream(file);
            try {
                props.load(in);
            } finally {
                in.close();
            }
        } catch (Exception exc) {
            if (debug) System.out.println("ORB properties file " + fileName + " not found: " + exc);
        }
    }

    public Object run() {
        Properties defaults = new Properties();
        String javaHome = getSystemProperty("java.home");
        String fileName = javaHome + File.separator + "lib" + File.separator + "orb.properties";
        getPropertiesFromFile(defaults, fileName);
        Properties results = new Properties(defaults);
        String userHome = getSystemProperty("user.home");
        fileName = userHome + File.separator + "orb.properties";
        getPropertiesFromFile(results, fileName);
        return results;
    }
}
