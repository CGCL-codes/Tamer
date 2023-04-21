package org.celerity;

import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.lang.reflect.Method;
import org.w3c.dom.*;

public class CelerityExecutor extends Thread {

    static boolean SingleThreadMasterController = false;

    public static void main(String argv[]) throws Exception {
        int numArguments = argv.length;
        boolean useMasterController = false;
        boolean useDistributedControl = false;
        boolean wrongNumberOfArguments = false;
        String xmlFileName = null;
        String mode = null;
        if (numArguments > 0) {
            mode = argv[0];
        }
        if (mode.equalsIgnoreCase("-mc")) {
            useMasterController = true;
            if (numArguments != 2) {
                wrongNumberOfArguments = true;
            } else {
                xmlFileName = argv[1];
            }
        }
        long controllerSleepTime = -1;
        int bufferSize = -1;
        if (mode.equalsIgnoreCase("-dc")) {
            useDistributedControl = true;
            if (numArguments != 4) {
                wrongNumberOfArguments = true;
            } else {
                controllerSleepTime = Long.parseLong(argv[1]);
                bufferSize = Integer.parseInt(argv[2]);
                xmlFileName = argv[3];
            }
        }
        boolean modeError = (!useMasterController && !useDistributedControl);
        if (modeError || wrongNumberOfArguments) {
            System.out.println("Usage: java FreeFlowExecutor -mc XMLFileName");
            System.out.println("   or: java FreeFlowExecutor -dc sleeptime buffersize XMLFileName");
            System.exit(1);
        }
        SplashScreen.print();
        System.out.print("command line arguments: ");
        for (int i = 0; i < argv.length; i++) {
            System.out.print(" " + argv[i]);
        }
        System.out.println();
        Module[] modules = D2KItineraryParser.parse(xmlFileName);
        int numModules = modules.length;
        Date startTime = new Date();
        if (useMasterController) {
            System.out.println("starting MasterSingleThreadController");
            MasterSingleThreadController.execute(modules);
        } else if (useDistributedControl) {
            System.out.println("starting DistributedMultiThreadedController");
            DistributedMultiThreadedController.execute(modules, controllerSleepTime, bufferSize);
        }
        Date endTime = new Date();
        double duration = (endTime.getTime() - startTime.getTime()) / 1000.0;
        System.out.println("Finished execution!");
        System.out.println("duration = " + duration + " seconds");
        System.gc();
    }
}
