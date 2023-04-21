package org.hip.vif.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.hip.kernel.sys.VSys;
import org.hip.vif.core.VIFSys;
import org.hip.vif.core.service.PreferencesHandler;
import org.osgi.framework.FrameworkUtil;

/**
 * Utility class to help domain object home classes that provide the object definition
 * in an external file.
 *
 * @author Luthiger
 * Created: 28.12.2008
 */
public class ExternalObjectDefUtil {

    /**
	 * Returns the <code>File</code> object containing the object definition.
	 * 
	 * @param inObjectDefFilename String Name of the file containing the object definition.
	 * @return File
	 */
    public static File getObjectDefFile(String inObjectDefFilename) {
        String lFilename = null;
        String lDirectory = VIFSys.getVSysCanonicalPath(PreferencesHandler.KEY_CONF_ROOT);
        if (lDirectory.length() == 0) {
            String lProperty = VSys.getContextPath();
            if (lProperty != null) {
                if (lProperty.length() == 0) {
                    lProperty = FrameworkUtil.getBundle(ExternalObjectDefUtil.class).getBundleContext().getBundle().getLocation();
                }
                lDirectory = VIFSys.getVSysCanonicalPath(PreferencesHandler.KEY_CONF_ROOT, lProperty);
            }
        }
        lFilename = lDirectory + inObjectDefFilename;
        return new File(lFilename);
    }

    /**
	 * Reads the specified file and returns it's content.
	 * 
	 * @param inObjectDefFile File
	 * @return String the content of the specified file.
	 */
    public static String readObjectDef(File inObjectDefFile) {
        StringBuilder lContent = new StringBuilder();
        FileReader lReader = null;
        BufferedReader lBuffer = null;
        try {
            lReader = new FileReader(inObjectDefFile);
            lBuffer = new BufferedReader(lReader);
            String lLine = lBuffer.readLine();
            while (lLine != null) {
                lContent.append(lLine);
                lLine = lBuffer.readLine();
            }
        } catch (FileNotFoundException exc) {
            VIFSys.log(exc);
        } catch (IOException exc) {
            VIFSys.log(exc);
        } finally {
            try {
                if (lBuffer != null) lBuffer.close();
            } catch (IOException exc) {
            }
        }
        return new String(lContent);
    }
}
