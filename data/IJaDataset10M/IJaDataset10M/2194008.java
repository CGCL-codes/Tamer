package test;

import java.util.logging.*;
import java.io.*;
import java.net.*;
import org.xito.boot.*;

/**
 *
 * @author  Deane
 */
public class Service5 {

    private static Logger logger = Logger.getLogger(Service5.class.getName());

    /** Creates a new instance of Service5 */
    public Service5() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) throws Exception {
        new Service1();
        logger.info("***** Started Service 5 ******");
        CacheManager cm = Boot.getCacheManager();
        URL[] urls = new URL[] { new URL("http://xito.sourceforge.net/apps/tools/editor/xito-editor.jar"), new URL("http://xito.sourceforge.net/apps/tools/editor/nb-editor.jar"), new URL("http://xito.sourceforge.net/apps/games/asteroids/asteroids.jar") };
        cm.downloadResources(urls, cm.getDefaultListener(), null, false);
        logger.info("****** Done Downloading ************");
    }
}
