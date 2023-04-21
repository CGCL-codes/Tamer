package org.hypas;

import java.io.File;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Starts up the root module, which receives the responsibility to start the
 * whole schabang.
 * 
 * @author Ravinder Singh
 */
public class Kickstart {

    public static ModuleContext rootModule;

    /**
    * Dumps an error message if there are too few/many parameters, otherwise 
    * uses the single parameter to parse the file, and give it to a ModuleContext.
    * 
    * @param args
    * @throws Exception
    */
    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        boolean isSilent = isSilent(args);
        if (isSilent) {
            Logger.getRootLogger().setLevel(Level.OFF);
        }
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        if (args.length == 1 || (args.length == 2 && isSilent)) {
            SAXBuilder builder = new SAXBuilder(false);
            Document doc = builder.build(new File(args[0]));
            Element root = doc.getRootElement();
            System.setProperty("hypas.configurationfile", args[0]);
            if ("Module".equals(root.getName())) {
                rootModule = new ModuleContext();
                rootModule.prepare(root);
                rootModule.init();
                rootModule.start();
            } else {
                System.err.println("No module configured as root!");
            }
        } else {
            System.err.println("Parameters too many or too few. This only needs a parameter with path to configuration file.");
        }
    }

    /**
    * Shuts all modules down properly.
    */
    public static class ShutdownHook extends Thread {

        public void run() {
            Kickstart.destroy();
        }
    }

    /**
    * Destroys the application.
    */
    public static void destroy() {
        if (rootModule != null) {
            rootModule.destroyRecursive();
        }
    }

    /**
    * @return the rooutmodule.
    */
    public static ModuleContext getRootModule() {
        return rootModule;
    }

    /**
    * Checks whether we are ivoked in silent mode.
    * 
    * @param args
    * @return
    */
    public static boolean isSilent(String[] args) {
        for (String string : args) {
            if (string.equals("-s")) {
                return true;
            }
        }
        return false;
    }
}
