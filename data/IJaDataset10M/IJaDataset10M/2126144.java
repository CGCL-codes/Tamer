package jmri.web.miniserver;

import java.io.File;
import jmri.jmrit.XmlFile;

/**
 *	@author Modifications by Steve Todd   Copyright (C) 2011
 *	@version $Revision: 1.2 $
 */
public class MiniServerManager {

    private static MiniServerManager root;

    private MiniServerPreferences MiniServerPreferences = null;

    public MiniServerManager() {
        MiniServerPreferences = new MiniServerPreferences(XmlFile.prefsDir() + "miniserver" + File.separator + "MiniServerPreferences.xml");
    }

    private static MiniServerManager instance() {
        if (root == null) root = new MiniServerManager();
        return root;
    }

    public static MiniServerPreferences miniServerPreferencesInstance() {
        return instance().MiniServerPreferences;
    }
}
