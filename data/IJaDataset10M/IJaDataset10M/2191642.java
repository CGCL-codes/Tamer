package ch.sahits.codegen.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.osgi.framework.Bundle;

/**
 * This utility is a super class for all test classes
 * that read configurations from a property file that
 * lies in the same package.
 * @author Andi Hotz, Sahits GmbH
 * @since 1.2.0
 */
public class PropertyFileLoader {

    /**
	 * Load the properties from the specified file.
	 * This method is inteded to be called from within the
	 * constructor of the subclass
	 * @param fileName file name of the property file
	 * @return Instance of the property file read from the system or an
	 * empty property file if an error occured during load.
	 */
    protected Properties loadFile(String fileName) {
        Properties prop = new Properties();
        try {
            Bundle b = TestPlugin.getDefault().getBundle();
            String packageName = getClass().getName();
            packageName = packageName.substring(0, packageName.lastIndexOf("."));
            packageName = packageName.replace('.', File.separatorChar);
            packageName += File.separator;
            packageName += fileName;
            URL url0 = b.getResource(packageName);
            final InputStream input = url0.openStream();
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
