package util.gui;

import net.Settings;
import util.DataProperties;
import java.util.HashMap;

/**
 * @author Jesper Nordenberg
 * @version $Revision: 1.1 $ $Date: 2002/05/08 23:38:15 $
 */
public class UISettings {

    private static final UISettings instance = new UISettings();

    private DataProperties properties = Settings.getInstance().getProperties("ui");

    private MetalTheme metalTheme = new MetalTheme("User Defined");

    private String lookAndFeel;

    private UISettings() {
        metalTheme.read(properties.getChild("metalTheme"));
    }

    public static final UISettings getInstance() {
        return instance;
    }

    public MetalTheme getMetalTheme() {
        return metalTheme;
    }

    public String getLookAndFeel() {
        return properties.get("lookAndFeel", "metal");
    }

    public void setLookAndFeel(String name) {
        properties.set("lookAndFeel", name);
    }

    public boolean useDefaultMetalTheme() {
        return properties.getBoolean("useDefaultMetalTheme", true);
    }

    public void setUseDefaultMetalTheme(boolean value) {
        properties.setBoolean("useDefaultMetalTheme", value);
    }

    public void save() {
        metalTheme.write(properties.getChild("metalTheme"));
    }
}
