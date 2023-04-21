package org.wvc.config.xml.pojos;

import java.util.List;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * AppConfig root tag.
 * 
 * @author julian
 *
 */
@XStreamAlias("appConfig")
public class AppConfig {

    /**
	 * Screen list.
	 */
    @XStreamImplicit(itemFieldName = "screen")
    private List<Screen> screens;

    /**
	 * Initial screen name.
	 */
    @XStreamAlias("initialScreen")
    private InitialScreen initialScreen;

    /**
	 * Debug mode options.
	 */
    @XStreamAlias("debugMode")
    private DebugMode debugMode;

    /**
	 * Application entry point.
	 */
    @XStreamAlias("entryPoint")
    private EntryPoint entryPoint;

    /**
	 * @param screens
	 * @param initialScreen
	 * @param debugMode
	 * @param entryPoint
	 */
    public AppConfig(List<Screen> screens, InitialScreen initialScreen, DebugMode debugMode, EntryPoint entryPoint) {
        super();
        this.screens = screens;
        this.initialScreen = initialScreen;
        this.debugMode = debugMode;
        this.entryPoint = entryPoint;
    }

    /**
	 * @return the screens
	 */
    public List<Screen> getScreens() {
        return screens;
    }

    /**
	 * @return the initialScreen
	 */
    public InitialScreen getInitialScreen() {
        return initialScreen;
    }

    /**
	 * @return the debugMode
	 */
    public DebugMode getDebugMode() {
        return debugMode;
    }

    /**
	 * @return the entryPoint
	 */
    public EntryPoint getEntryPoint() {
        return entryPoint;
    }
}
