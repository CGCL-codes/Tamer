package org.exolab.jms.net.jvm;

import java.util.Enumeration;
import java.util.Properties;

/**
 * Helper for starting a new JVM.
 *
 * @version     $Revision: 1.2 $ $Date: 2008/01/07 13:00:18 $
 * @author      <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 */
public class JVM extends Executor {

    /**
     * Construct a new <code>JVM</code>, specifying the classpath
     * Output will be directed to System.out and System.err
     *
     * @param mainClass the fully qualified name of the main class
     * @param classpath the classpath. If <code>null</code>, uses the
     * value of the <code>"java.class.path"</code> system property
     * @param sysProps system properties to pass to the JVM.
     * May be <code>null</code>
     * @param args the command line arguments. May be <code>null</code>
     */
    public JVM(String mainClass, String classpath, Properties sysProps, String args) {
        super(getCommand(mainClass, classpath, sysProps, args));
    }

    /**
     * Generates the command line for the JVM
     *
     * @param mainClass the fully qualified name of the main class
     * @param classpath the classpath. If <code>null</code>, uses the
     * value of the <code>"java.class.path"</code> system property
     * @param sysProps system properties to pass to the JVM.
     * @param args the command line arguments. May be <code>null</code>
     */
    private static String getCommand(String mainClass, String classpath, Properties sysProps, String args) {
        if (classpath == null) {
            classpath = System.getProperty("java.class.path");
        }
        StringBuffer command = new StringBuffer("java -cp ");
        command.append("\"").append(classpath).append("\"");
        command.append(" ");
        if (sysProps != null) {
            Enumeration names = sysProps.propertyNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                String value = sysProps.getProperty(name);
                command.append("-D");
                command.append(name);
                command.append("=");
                command.append(value);
                command.append(" ");
            }
        }
        command.append(mainClass);
        if (args != null) {
            command.append(" ");
            command.append(args);
        }
        return command.toString();
    }
}
