package net.sf.xsdutils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import net.sf.xsdutils.wsdl.WsdlMerger;

/**
 * XSD Utils command-line interface.
 * 
 * @author Rustam Abdullaev
 */
public class Run {

    /**
	 * CLI interface
	 * 
	 * @param args
	 *            sourceWsdl, targetWsdl
	 */
    public static void main(String[] args) {
        try {
            int argptr = 0;
            boolean quiet = false;
            if (args.length > 0 && args[0].equals(Messages.getString("WsdlMerger.quietAttr"))) {
                quiet = true;
                argptr++;
            }
            if (args.length != argptr + 2) {
                printUsage();
                return;
            }
            int idx = args[argptr + 0].indexOf(":/");
            URL sourceWsdl = idx > 3 && idx < 8 ? new URL(args[argptr + 0]) : new File(args[argptr + 0]).toURI().toURL();
            File targetWsdl = new File(args[argptr + 1]);
            WsdlMerger wm = new WsdlMerger();
            wm.setProgressLogger(new ConsoleLogger(quiet));
            if (!quiet) {
                System.out.println(Messages.getString("WsdlMerger.merging", sourceWsdl, targetWsdl.getAbsolutePath()));
            }
            int count = wm.mergeWSDL(sourceWsdl, targetWsdl);
            if (!quiet) {
                System.out.println(Messages.getString("WsdlMerger.done", count));
            }
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
	 * prints usage
	 */
    private static void printUsage() {
        System.out.println(Messages.getString("WsdlMerger.usage", Run.class.getName()));
    }
}
