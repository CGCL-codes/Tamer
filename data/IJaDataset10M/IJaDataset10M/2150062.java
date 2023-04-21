package gov.sns.apps.mpx;

import gov.sns.application.Application;
import gov.sns.application.ApplicationAdaptor;
import gov.sns.application.XalDocument;
import gov.sns.xal.model.mpx.ModelProxy;
import gov.sns.xal.smf.application.AcceleratorApplication;
import gov.sns.xal.model.mpx.MPXStopWatch;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JOptionPane;
import org.apache.batik.util.gui.MemoryMonitor;

/**The MPXMain Program. Gives access to the on-line model and uses the 
 * ModelProxy API.
 * @author wdklotz
 * @version $Id: MPXMain.java 2 2006-08-17 12:20:30 +0000 (Thursday, 17 8 2006) t6p $
 */
public class MPXMain extends ApplicationAdaptor {

    public static String PARAM_SRC = ModelProxy.PARAMSRC_DESIGN;

    public static boolean USE_CONSOLE = true;

    public static boolean USE_MEM_MONITOR = false;

    public static boolean USE_STOP_WATCH = false;

    public static NumberFormat fmt;

    static {
        fmt = NumberFormat.getNumberInstance();
        ((DecimalFormat) fmt).applyPattern("0.0000");
    }

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            setOption(args[i]);
        }
        try {
            setOptions(args);
            AcceleratorApplication.launch(new MPXMain());
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            exception.printStackTrace();
            JOptionPane.showMessageDialog(null, exception.getMessage(), exception.getClass().getName(), JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void setOption(String option) {
        if (option.equals("--usesChannelAccess") || option.equals("-ca")) {
            PARAM_SRC = ModelProxy.PARAMSRC_LIVE;
        } else if (option.equals("--usesConsole") || option.equals("-C")) {
            USE_CONSOLE = true;
        } else if (option.equals("--usesMemoryMonitor") || option.equals("-mm")) {
            USE_MEM_MONITOR = true;
        } else if (option.equals("--usesStopWatch") || option.equals("-sw")) {
            USE_STOP_WATCH = true;
        }
    }

    /**
	* Returns the text file suffixes of files this application can open.
	* @return Suffixes of readable files
	*/
    @Override
    public String[] readableDocumentTypes() {
        return new String[] { "mpx", "xml" };
    }

    /**
	* Returns the text file suffixes of files this application can write.
	* @return Suffixes of writable files
	*/
    @Override
    public String[] writableDocumentTypes() {
        return new String[] { "mpx" };
    }

    /**
	 *  Implement this method to return an instance of my custom document.
	* @return An instance of my custom document.
	*/
    @Override
    public XalDocument newEmptyDocument() {
        return new MPXDocument();
    }

    /**
	* Implement this method to return an instance of my custom document 
	* corresponding to the specified URL.
	* @param url The URL of the file to open.
	* @return An instance of my custom document.
	*/
    @Override
    public XalDocument newDocument(java.net.URL url) {
        return new MPXDocument(url);
    }

    /**
	  * Specifies the name of my application.
	  * @return Name of my application.
	  */
    @Override
    public String applicationName() {
        return "MPXMain";
    }

    /**
	* Specifies whether I want to send standard output and error to the console.
	* I don't need to override the superclass adaptor to return true (the default), but
	* it is sometimes convenient to disable the console while debugging.
	* @return false to disable the application's console.
	*/
    @Override
    public boolean usesConsole() {
        String usesConsoleProperty = System.getProperty("usesConsole");
        if (usesConsoleProperty != null) {
            return Boolean.valueOf(usesConsoleProperty).booleanValue();
        } else {
            return USE_CONSOLE;
        }
    }

    /** Capture the application launched event and print it */
    @Override
    public void applicationFinishedLaunching() {
        System.out.println("Application " + applicationName() + "  has finished launching! ");
        if (USE_MEM_MONITOR) {
            java.awt.Window monitor = new MemoryMonitor(3000);
            monitor.setLocationRelativeTo(Application.getActiveWindow());
            monitor.setVisible(true);
        }
        MPXStopWatch.reset(!USE_STOP_WATCH);
    }

    public void documentCreated(XalDocument document) {
        System.out.println("new Document created");
    }
}
