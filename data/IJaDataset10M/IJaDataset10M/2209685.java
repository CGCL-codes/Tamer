package org.jalgo.module.unifikation;

import java.net.URL;
import org.jalgo.main.IModuleInfo;
import org.jalgo.main.util.Messages;

public class ModuleInfo implements IModuleInfo {

    /** The singleton instance */
    private static IModuleInfo instance;

    /**
	 * The only constructor is unusable from outside this class. This is part of
	 * the singleton design pattern.
	 */
    private ModuleInfo() {
    }

    /**
	 * Retrieves the singleton instance of <code>IModuleInfo</code>.
	 * 
	 * @return the singleton instance
	 */
    public static IModuleInfo getInstance() {
        if (instance == null) instance = new ModuleInfo();
        return instance;
    }

    public String getName() {
        return "Unifikation";
    }

    public String getVersion() {
        return "1.0";
    }

    public String getAuthor() {
        return "Kai-Uwe Storek, Alexander Grund, Paul Eichst" + Constants.lowercasedAE + "dt, Thomas Weber, Sebastian Simon";
    }

    public String getDescription() {
        return "Dieses Modul simuliert die Unifikation von Termen anhand des Unifikationsalgorithmus von Bistarelli und Montanari.";
    }

    public URL getLogoURL() {
        return Messages.getResourceURL("unifikation", "Icon");
    }

    public String getLicense() {
        return "GPL";
    }

    public URL getHelpSetURL() {
        return Messages.getResourceURL("unifikation", "HelpSet_Name");
    }
}
