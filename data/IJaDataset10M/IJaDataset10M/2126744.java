package edu.princeton.wordnet.browser.component;

import java.io.File;
import java.net.MalformedURLException;

/**
 * Help
 * 
 * @author <a href="mailto:bbou@ac-toulouse.fr">Bernard Bou</a>
 */
public class Help {

    /**
	 * Help
	 */
    public static void help(final String thisBrowser, final String thatHelp) {
        String thisHelp = thatHelp;
        if (!thisHelp.startsWith("file:") && !thisHelp.startsWith("http:")) {
            final File thisFolder = new File(thisHelp);
            try {
                thisHelp = thisFolder.toURI().toURL().toString();
            } catch (final MalformedURLException thisException) {
                return;
            }
        }
        if (!thisHelp.endsWith("/")) {
            thisHelp += "/";
        }
        thisHelp += "index.html";
        Help.run(thisBrowser + " " + thisHelp);
    }

    /**
	 * Run command as separate process
	 * 
	 * @param thisCommand
	 *            command line
	 */
    private static void run(final String thisCommand) {
        if (thisCommand != null && !thisCommand.isEmpty()) {
            try {
                Runtime.getRuntime().exec(thisCommand);
            } catch (final Exception e) {
                System.err.println("Cannot run " + thisCommand + " " + e.toString());
            }
        }
    }
}
