package org.mcisb.beacon.ui.pedro.db;

import java.util.Arrays;
import java.util.ResourceBundle;
import org.mcisb.beacon.ui.pedro.DbExportPlugin;
import pedro.plugins.PedroPlugin;

/**
 * 
 * @author Neil Swainston
 */
public class PersonExportPlugin extends DbExportPlugin implements PedroPlugin {

    /**
	 * 
	 */
    public PersonExportPlugin() {
        super("People", Arrays.asList(new String[] { "Person", "Experimenter", "ProjectLeader" }), ResourceBundle.getBundle("org.mcisb.beacon.ui.pedro.db.messages").getString("PersonExportPlugin.displayName"), "email");
    }
}
