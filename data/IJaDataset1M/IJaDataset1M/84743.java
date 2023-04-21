package net.sourceforge.xsdeclipse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import net.sourceforge.xsdeclipse.configurator.FileExistException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Layout;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class XsdeclipsePlugin extends AbstractUIPlugin {

    private static Logger log = null;

    private static XsdeclipsePlugin plugin;

    /**
	 * The constructor.
	 */
    public XsdeclipsePlugin() {
        plugin = this;
    }

    /**
	 * This method is called upon plug-in activation
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        configureLog();
    }

    /**
	 * This method is called when the plug-in is stopped
	 */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    /**
	 * Returns the shared instance.
	 */
    public static XsdeclipsePlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin("net.sourceforge.xsdeclipse", path);
    }

    private void configureLog() {
        if (log == null) log = Logger.getLogger(XsdeclipsePlugin.class);
        Package p = Layout.class.getPackage();
        String loggerTitle = p.getImplementationTitle();
        String loggerVendor = p.getImplementationVendor();
        String loggerVersion = p.getImplementationVersion();
        String loggerConfigFileName = "log4j.xml";
        File loggerConfigFile = null;
        String msg1 = "";
        try {
            try {
                loggerConfigFile = copyToStateLocation(loggerConfigFileName);
                msg1 = "File '" + loggerConfigFileName + "' transferred to state location '" + this.getStateLocation() + "'.";
                System.out.println(msg1);
            } catch (FileExistException e) {
                loggerConfigFile = e.getFile();
            }
            DOMConfigurator loggerConfigurator = new DOMConfigurator();
            loggerConfigurator.doConfigure(loggerConfigFile.getAbsolutePath(), LogManager.getLoggerRepository());
            String msg2 = "logger configured by DOMConfigurator using config file " + loggerConfigFile.getAbsolutePath();
            System.out.println(msg2);
            if (!(msg1 == "")) log.info(msg1);
            log.info(msg2);
        } catch (IOException e) {
            String msg = "Can not transfer default configuration file '" + loggerConfigFileName + "' to state location." + " Logger will be configured by BasicConfigurator.";
            System.out.println(msg);
            BasicConfigurator.configure();
            log.error(msg);
        }
        if (log.isDebugEnabled()) log.debug("logging with " + loggerTitle + "(version " + loggerVersion + ") by " + loggerVendor);
    }

    public File copyToStateLocation(String filename) throws IOException, FileExistException {
        IPath stateLocation = this.getStateLocation();
        IPath inPath = new Path(filename);
        IPath outPath = stateLocation.append(filename);
        File outFile = outPath.toFile();
        if (outFile.exists()) {
            throw new FileExistException(outFile, "File '" + outFile.getAbsolutePath() + "still exists.");
        }
        InputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = this.openStream(inPath);
            outFile.createNewFile();
            outStream = new FileOutputStream(outFile.getAbsolutePath(), true);
            int bufferLength = 1024;
            int receivedLength = bufferLength;
            byte[] buffer = new byte[bufferLength];
            while (receivedLength == bufferLength) {
                receivedLength = inStream.read(buffer, 0, bufferLength);
                outStream.write(buffer, 0, receivedLength);
            }
        } catch (IOException e) {
            throw e;
        }
        return outFile;
    }

    public Logger getLogger() {
        return log;
    }
}
