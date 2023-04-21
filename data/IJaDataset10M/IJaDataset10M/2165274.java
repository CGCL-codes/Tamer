package org.webstrips;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;
import org.coffeeshop.Callback;
import org.coffeeshop.Coffeeshop;
import org.coffeeshop.application.Application;
import org.coffeeshop.arguments.ArgumentsException;
import org.coffeeshop.log.Logger;
import org.coffeeshop.settings.Settings;
import org.coffeeshop.settings.SettingsNotFoundException;
import org.coffeeshop.string.parsers.BooleanStringParser;
import org.coffeeshop.string.parsers.IntSizeStringParser;
import org.coffeeshop.string.parsers.IntegerStringParser;
import org.coffeeshop.string.parsers.ParseException;
import org.webstrips.gui.DebugWindow;
import org.webstrips.gui.Utilities;
import org.webstrips.navigator.Navigator;

/**
 * Entry point to the navigator application. The WebStrips class extends a basic
 * JFrame class and provides all of the top-level interface logic.
 * 
 * @author lukacu
 * @since WebStrips 0.1
 */
public class WebStrips extends Application {

    /**
	 * Name of the application
	 */
    public static final String APPLICATION_NAME = "WebStrips";

    /**
	 * Allocated internal channels constants
	 */
    public static final int COMICS = Logger.APPLICATION_INTERNAL_1;

    public static final int CACHE = Logger.APPLICATION_INTERNAL_2;

    public static final int PRELOADER = Logger.APPLICATION_INTERNAL_6;

    public static final int TRANSFER = Logger.APPLICATION_INTERNAL_5;

    public static final int JSOUT = Logger.APPLICATION_INTERNAL_4;

    public static final int JSINFO = Logger.APPLICATION_INTERNAL_7;

    public static final int JSERROR = Logger.APPLICATION_INTERNAL_8;

    /**
	 * Version of the application
	 */
    public static final String APPLICATION_VERSION = "0.4.3";

    public static Color WEBSTRIPS_PALETTE_BRIGHT = Color.decode("#DAEBFF");

    public static Color WEBSTRIPS_PALETTE_MEDIUM = Color.decode("#A9C9F1");

    public static Color WEBSTRIPS_PALETTE_DARK = Color.decode("#74AFF8");

    private static Splash splash = null;

    public static final String WEBSTRIPS_HOMEPAGE = "http://webstrips.tnode.com";

    public static final String[][] Authors = { { "Luka Čehovin", "http://luka.tnode.com" } };

    public static final String[][] ThanksTo = { { "Marko Toplak", "http://www2.arnes.si/~sodmtopl/" }, { "Ciril Bohak", null } };

    /**
	 * Entry point of the application. Consturcts a new window and opens it.
	 * 
	 * @param args
	 *            input arguments
	 */
    public static void main(String[] args) throws ArgumentsException, SettingsNotFoundException, ParseException {
        new WebStrips(args);
        Logger log = Application.getApplication().getLogger();
        log.setOutputStream(System.out);
        log.setLogFormat(new WebStripsLogFormat());
        log.disableAllChannels();
        Settings settings = getApplication().getSettings();
        if (settings.getBoolean("debug")) log.enableChannels("edw".toCharArray());
        int ideb = settings.getInt("debug.channels");
        if (ideb > 0) log.enableChannels(Integer.toString(ideb).toCharArray());
        if (settings.getBoolean("navigator.status.memory")) System.setProperty("org.webstrips.navigator.monitor_memory", "true");
        Utilities.setSystemProperty("memory", IntSizeStringParser.getParser());
        Utilities.setSystemProperty("cache", IntSizeStringParser.getParser());
        Utilities.setSystemProperty("developer", BooleanStringParser.getParser());
        getApplication().getLogger().report(Logger.DEFAULT, "Launching " + getApplication().getShortDescription());
        getApplication().getLogger().report(Logger.DEFAULT, Coffeeshop.NAME + " " + Coffeeshop.VERSION);
        try {
            splash = new Splash("splash.png", new Rectangle(170, 180, 85, 2), WEBSTRIPS_PALETTE_MEDIUM, Color.WHITE);
        } catch (IOException e) {
            getApplication().getLogger().report(Logger.ERROR, "Unable to display splash screen: " + e.getMessage());
        }
        if (splash != null) splash.showSplash();
        try {
            if (settings.getBoolean("developer")) {
                DebugWindow.getDebugWindow();
            }
            Navigator navigator = new Navigator(new Callback() {

                public void callback(Object source, Object parameter) {
                    if (splash != null) {
                        splash.setProgress(((Float) parameter).floatValue());
                    }
                }
            });
            navigator.setVisible(true);
            if (splash != null) splash.hideSplash();
        } catch (LoadingException e) {
            getApplication().getLogger().report(Logger.ERROR, e.getMessage());
            System.exit(1);
        }
    }

    public WebStrips(String[] arguments) throws ArgumentsException {
        super(APPLICATION_NAME, arguments);
    }

    @Override
    public String getShortDescription() {
        return APPLICATION_NAME + " " + APPLICATION_VERSION;
    }

    @Override
    public String getLongDescription() {
        return APPLICATION_NAME + " " + APPLICATION_VERSION;
    }

    @Override
    protected void defineDefaults(SettingsSetter setter) {
        setter.addDefaultElement("debug", "false", "Enables debug output", null, "debug", null);
        setter.addDefaultElement("debug.channels", "0", "Enables debug output of application internal channels (e.g. --debug-internal 12345678 to enable all internal debug channels)", null, "debug-channels", IntegerStringParser.getParser());
        setter.addDefaultElement("navigator.status.memory", "false", "Display a memory usage status in the status bar", null, "mm", null);
        setter.addDefaultElement("memory", "1M", "Set the size of the local cache that is stored in the memory", null, "memory", IntSizeStringParser.getParser());
        setter.addDefaultElement("cache", "70M", "Set the size of the local cache", null, "cache", IntSizeStringParser.getParser());
        setter.addDefaultElement("developer", "false", "Enables developer options", null, "developer", null);
    }
}
