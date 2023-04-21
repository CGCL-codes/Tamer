package uk.ac.imperial.ma.deliveryEngine.app;

import java.io.File;
import java.io.IOException;
import uk.ac.imperial.ma.deliveryEngine.core.MathQTIEngineCore;

/**
 * This is the application wrapper class for the MathQTI engine. It simply wraps
 * the <code>MathQTIEngineCore</code> so that it can be run as a Java application.
 * 
 * @author <a href="mailto:d.may@imperial.ac.uk">Daniel J. R. May</a>
 * @version 0.1, 30 August 2005
 * @see uk.ac.imperial.ma.deliveryEngine.core.MathQTIEngineCore
 * @see uk.ac.imperial.ma.deliveryEngine.applet.MathQTIEngineApplet
 */
public class MathQTIEngineApp {

    /**
	 * This method launches the MathQTIEngine as an application. If no command line arguments are supplied it
	 * uses the values specified for defaultMathQTIInputFile and defaultXHTMLOutputFile in the resource file.
	 * <code>int 0</code> is returned to the system on completion without errors, otherwise a non-zero value is returned. 
	 * 
	 * @param args the command line argument parameters. This application accepts the following form of 
	 * command line parameters <kbd>input=PATH_TO_INPUT_MATHQTI_FILE output=PATH_TO_OUTPUT_XHTML_FILE</kbd>.
	 */
    public static void main(String[] args) {
        MathQTIEngineCore core = new MathQTIEngineCore();
        String pathToMathQTIFile = MathQTIEngineCore.RESOURCES.getString("defaultMathQTIInputFile");
        String pathToXHTMLFile = MathQTIEngineCore.RESOURCES.getString("defaultXHTMLOutputFile");
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("input=")) {
                    pathToMathQTIFile = args[i].substring(6);
                } else if (args[i].startsWith("output=")) {
                    pathToXHTMLFile = args[i].substring(7);
                } else {
                    System.err.println(MathQTIEngineCore.RESOURCES.getString("commandLineSyntaxErrorMessage"));
                    System.exit(1);
                }
            }
        }
        try {
            File mathQTIFile = new File(pathToMathQTIFile);
            if (!mathQTIFile.canRead()) {
                throw new IOException(MathQTIEngineCore.RESOURCES.getString("canNotReadFile") + mathQTIFile.getCanonicalPath());
            }
            File xhtmlFile = new File(pathToXHTMLFile);
            xhtmlFile.createNewFile();
            if (!xhtmlFile.canWrite()) {
                throw new IOException(MathQTIEngineCore.RESOURCES.getString("canNotWriteFile") + xhtmlFile.getCanonicalPath());
            }
            core.init(mathQTIFile.toURI().toString());
            core.start();
            core.serializeXHTMLDocumentToURI(xhtmlFile.toURI().toString());
            System.exit(0);
        } catch (IOException ioe) {
            System.err.println(MathQTIEngineCore.RESOURCES.getString("inputOrOutputFilePermissionsErrorMessage"));
            ioe.printStackTrace();
        } finally {
            System.exit(2);
        }
    }
}
