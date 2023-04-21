package net.sf.dpdesktop;

import java.util.Locale;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Heiner Reinhardt
 */
public class Main {

    public static void main(String args[]) {
        Level level = Level.INFO;
        boolean enableTrayIfSupported = true;
        BasicConfigurator.configure();
        Logger root = Logger.getRootLogger();
        root.setLevel(level);
        new MainController(enableTrayIfSupported);
    }
}
