package de.beeld;

import java.io.FileNotFoundException;
import java.io.IOException;
import de.beeld.ui.ServerGUI;

/**
 * 
 * Starter class for the server component.
 * 
 * @author Martin R&ouml;bert
 * @version $LastChangedRevision: 85 $
 * @since $HeadURL:
 *        https://beeld.svn.sourceforge.net/svnroot/beeld/src/de/beeld/
 *        Beeld.java $
 * 
 */
public class Beeld {

    /**
	 * Sets up the environment for the server<br/>
	 * First of all there is a check for th OS to use special functions of Mac
	 * OS X
	 * <ul>
	 * <li>use upper MenuBar: apple.laf.useScreenMenuBar</li>
	 * <li>set correct app name: com.apple.mrj.application.apple.menu.about.name
	 * </ul>
	 * Following is the set up of the gui components.
	 * 
	 * @throws IOException
	 */
    public static void setup() throws IOException {
        if (System.getProperty("os.name").equalsIgnoreCase("mac os x")) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.showGrowBox", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Beeld Server");
        }
        ServerGUI.getInstance();
    }

    /**
	 * Calls setup() and starts
	 * 
	 * @param args
	 *            command line arguments
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        setup();
    }
}
