package org.jalgo.main.gui.components;

import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import org.jalgo.main.IModuleInfo;
import org.jalgo.main.JAlgoMain;
import org.jalgo.main.util.Messages;

/**
 * 
 * @author Matthias Schmidt
 */
public class HelpSystem {

    private List<IModuleInfo> knownModuleInfos;

    public HelpSystem() {
        knownModuleInfos = JAlgoMain.getInstance().getKnownModuleInfos();
    }

    /**
	 * Retrieves an JavaHelp specific ActionListener which launches the JavaHelpDialog.
	 * @return an ActionListener 
	 */
    public ActionListener getJavaHelpActionListener() {
        HelpSet mainHS = getHSByURL(Messages.getResourceURL("main", "Main_HelpSet"));
        URL path = null;
        for (IModuleInfo element : knownModuleInfos) {
            path = element.getHelpSetURL();
            if (path == null) System.out.println("HelpSetURL from " + element.getName() + " is null!"); else mainHS.add(getHSByURL(path));
        }
        HelpBroker hb = mainHS.createHelpBroker();
        return new CSH.DisplayHelpFromSource(hb);
    }

    private HelpSet getHSByURL(URL path) {
        try {
            return new HelpSet(null, path);
        } catch (HelpSetException ee) {
            System.err.println("HelpSet " + ee.getMessage());
        }
        return null;
    }
}
