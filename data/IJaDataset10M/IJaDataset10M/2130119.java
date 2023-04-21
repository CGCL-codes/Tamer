package penguin;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.penguinuri.siteSeasonDatabase.SiteSeasonDBDocument;
import org.penguinuri.siteSeasonDatabase.SiteSeasonDBDocument.SiteSeasonDB;
import org.penguinuri.siteSeasonDatabase.SiteSeasonDBDocument.SiteSeasonDB.Sites.Site;
import penguin.gui.*;
import penguin.helpers.*;

/**
 * The Main bootstrap for the program.
 * 
 * @author Tim Dunstan
 *
 */
public class Program {

    private static Session s;

    /**
	 * Main entry point of the Penguin Nest Picture Analyser.
	 * @param args Command line arguments. Only accepts "debug" to activate debugging output.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, Exception {
        for (int i = 0; i < args.length; i++) {
            if (args[0].toLowerCase().contains("debug")) {
                Debug.setDebugging(true);
            }
        }
        System.out.println("Debugging is: " + (Debug.isOn() ? "on" : "off"));
        PenguinGUI window = new PenguinGUI();
        window.setVisible(true);
    }

    public static String Name = "Penguin Nest Picture Analyser";

    private static ISettings settings;

    public static ISettings Settings() {
        if (settings == null) {
            settings = AppSettings.get();
        }
        return settings;
    }

    private static String VERSION = Program.class.getPackage().getImplementationVersion();

    public static String getVersion() {
        return VERSION + (Debug.isOn() ? " (debug build)" : "");
    }

    public static Session getSession() {
        if (s == null) {
            s = new Session();
        }
        return s;
    }

    private static SiteSeasonDBDocument localDB = null;

    private static File localDBFile = new File(String.format("%s/localDB.xml", Program.Settings().getLocationFolder()));

    public static SiteSeasonDBDocument getLocalSiteDatabase() {
        File f = new File(String.format("%s/localDB.xml", Program.Settings().getLocationFolder()));
        if (!localDBFile.equals(f)) {
            localDBFile = f;
            localDB = null;
        }
        if (localDB == null) {
            reloadLocalSiteData();
        }
        return localDB;
    }

    public static void saveLocalSiteDatabase() throws IOException {
        XmlOptions ops = new XmlOptions();
        ops.setSavePrettyPrint().setSavePrettyPrintIndent(4);
        localDB.save(localDBFile, ops);
    }

    public static void reloadLocalSiteData() {
        if (localDBFile.exists()) {
            try {
                localDB = SiteSeasonDBDocument.Factory.parse(localDBFile);
            } catch (XmlException e) {
                Debug.print(e);
            } catch (IOException e) {
                Debug.print(e);
            }
        } else {
            File f = new File(Program.Settings().getLocationFolder());
            f.mkdirs();
            localDB = SiteSeasonDBDocument.Factory.newInstance();
            SiteSeasonDB d = localDB.addNewSiteSeasonDB();
            d.addNewSeasons();
            d.addNewSites();
            try {
                localDB.save(localDBFile);
            } catch (IOException e) {
                Debug.print(e);
            }
        }
        LinkedList<String> l = new LinkedList<String>();
        Site sites[] = localDB.getSiteSeasonDB().getSites().getSiteArray();
        for (int i = sites.length; i > 0; i--) {
            Site s = sites[i - 1];
            if (s.getName() == null) {
                localDB.getSiteSeasonDB().getSites().removeSite(i - 1);
            } else if (l.contains(s.getName().toLowerCase())) {
                localDB.getSiteSeasonDB().getSites().removeSite(i - 1);
            } else {
                l.add(s.getName().toLowerCase());
            }
        }
    }

    public static int getSettingsVersion() {
        return 4;
    }
}
