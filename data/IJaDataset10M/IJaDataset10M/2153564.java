package net.sourceforge.jinit;

import java.io.File;
import java.util.Properties;

/**
 * Main entry point into program.
 * @author Trevor Miller
 */
public class Main {

    /** User's working directory */
    private static final String user_dir = System.getProperty("user.dir");

    /**
     * Class has static access and should not be instanced so we make the 
     * constructor private!
     */
    private Main() {
    }

    /**
	 * Main entry point into program.
	 * @param args Command Line arguments
	 */
    public static void main(String[] args) {
        printMessage();
        File propsFile = new File(user_dir, "jinit.properties");
        if (!propsFile.exists()) {
            System.out.println("Fatal Error: could not find " + propsFile);
            System.exit(1);
        }
        long start = System.currentTimeMillis();
        Properties props = loadProperties(propsFile);
        try {
            Project project = Project.parseProject(props);
            project.initialize(props);
        } catch (ConfigurationException e) {
            System.out.println("Fatal Error: Your configuration file is not valid.");
            System.out.println(e.getMessage());
            System.exit(2);
        }
        long end = System.currentTimeMillis();
        long total = end - start;
        System.out.println("JInit COMPLETED SUCCESSFULLY.\nTotal Time: " + total + "ms.");
    }

    /**
     * Load the properties from the given file into a properties object.
     * @param propsFile The properties file.
     * @return A Properties object
     */
    private static Properties loadProperties(File propsFile) {
        Properties props = new Properties();
        try {
            String source = Util.loadFile(propsFile);
            source = TemplateEngine.merge(System.getProperties(), source);
            source = TemplateEngine.merge(Util.getProperties(), source);
            props = Util.loadProperties(source);
            source = TemplateEngine.merge(props, source);
            props = Util.loadProperties(source);
            System.out.println("Loaded properties from: " + propsFile);
            return props;
        } catch (Exception e) {
            System.out.println("Fatal Error: could not load properties, " + e.getMessage());
            System.exit(2);
            return props;
        }
    }

    /**
     * Print out banner.
     */
    private static void printMessage() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("                            JINIT                          ");
        System.out.println("               http://jinit.sourceforge.net                ");
        System.out.println("-----------------------------------------------------------");
        System.out.println("\nCreating project in: " + user_dir);
    }
}
