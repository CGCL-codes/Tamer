package dymnd;

import java.awt.Color;
import java.awt.Toolkit;
import java.util.Hashtable;

public class Settings extends Hashtable implements java.io.Serializable {

    static final long serialVersionUID = 3;

    private Settings() {
    }

    public static Settings defaultSettings() {
        Settings s = new Settings();
        s.put("username", System.getProperty("user.name"));
        s.put("layerCount", new Integer(1));
        s.put("canvasWidth", new Integer(500));
        s.put("canvasHeight", new Integer(500));
        s.put("mainWindowWidth", new Integer(700));
        s.put("mainWindowHeight", new Integer(700));
        s.put("lastDirectory", "/");
        s.put("screenSize", Toolkit.getDefaultToolkit().getScreenSize());
        s.put("networkHost", "localhost");
        s.put("networkPort", new Integer(5000));
        s.put("primaryColor", Color.black);
        s.put("secondaryColor", Color.white);
        s.put("layerDialogWidth", new Integer(100));
        s.put("layerDialogHeight", new Integer(200));
        s.put("layerDialogX", new Integer(650));
        s.put("layerDialogY", new Integer(500));
        s.put("selectedPlugin", new Integer(0));
        s.put("toolDialogX", new Integer("550"));
        s.put("toolDialogY", new Integer("170"));
        s.put("layerIndex", new Integer(0));
        return s;
    }
}
