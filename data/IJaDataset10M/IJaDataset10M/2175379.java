package org.hardtokenmgmt.core.token;

import iaik.pkcs.pkcs11.Module;
import iaik.pkcs.pkcs11.TokenException;
import java.io.IOException;
import java.util.logging.Level;
import org.hardtokenmgmt.core.InterfaceFactory;
import org.hardtokenmgmt.core.log.LocalLog;
import org.hardtokenmgmt.core.settings.GlobalSettings;

/**
 * Class connecting the PKCS11 wrapper from IAIK with the
 * pkcs11.dll specified in the globalsettings
 * 
 * @author Philip Vendil 2006-aug-28
 *
 * @version $Id$
 */
public class PKCS11Factory {

    private static Module module = null;

    private static String moduleName;

    /**
    * 
    * @return The returns a wrapper to the PKCS11 module configured in the
    * global settings.
    * 
    * @see iaik.pkcs.pkcs11.Module
    */
    public static synchronized Module getPKCS11Module() {
        if (module == null) {
            try {
                boolean foundModule = false;
                String[] moduleLocations = InterfaceFactory.getGlobalSettings().getProperty(GlobalSettings.PKCS11_NAME).split(",");
                for (String moduleLocation : moduleLocations) {
                    try {
                        module = Module.getInstance(moduleLocation);
                        foundModule = true;
                        moduleName = moduleLocation;
                    } catch (IOException e) {
                    }
                }
                if (foundModule) {
                    module.initialize(null);
                } else {
                    throw new IOException();
                }
            } catch (IOException e) {
                LocalLog.getLogger().log(Level.SEVERE, "Couldn't load PKCS11 module : " + InterfaceFactory.getGlobalSettings().getProperty(GlobalSettings.PKCS11_NAME));
                LocalLog.debug(e);
            } catch (TokenException e) {
                LocalLog.getLogger().log(Level.SEVERE, "Error when initializing PKCS11 module : " + InterfaceFactory.getGlobalSettings().getProperty(GlobalSettings.PKCS11_NAME));
                LocalLog.debug(e);
            }
        }
        return module;
    }

    /**
    * Method finalizing the PKCS11 module 
    * @see java.lang.Object#finalize()
    */
    protected void finalize() throws Throwable {
        module.finalize(null);
        super.finalize();
    }

    public static String getPKCS11Name() {
        if (moduleName == null) {
            getPKCS11Module();
        }
        return moduleName;
    }
}
