package org.inigma.migrations.cli;

import java.io.File;
import org.inigma.migrations.DriverConfiguration;

public class RegisterDriver {

    private static final String USAGE_MESSAGE = "./register-driver.sh /path/to/jdbc-driver.jar [additional jars]";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(USAGE_MESSAGE);
        }
        DriverConfiguration config = new DriverConfiguration();
        for (String jar : args) {
            File jarFile = new File(jar);
            if (!jarFile.exists()) {
                System.out.println("Unable to locate " + jarFile.getAbsolutePath());
                continue;
            }
            for (String driver : DriverConfiguration.getDriversFromJar(jarFile)) {
                config.put(driver, jarFile.getAbsolutePath());
            }
        }
    }
}
