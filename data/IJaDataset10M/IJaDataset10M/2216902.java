package org.columba.calendar.config;

import java.io.File;
import org.columba.calendar.config.api.ICalendarList;
import org.columba.calendar.config.api.IConfig;
import org.columba.core.config.Config;
import org.columba.core.config.DefaultXmlConfig;
import org.columba.core.io.DiskIO;
import org.columba.core.xml.XmlElement;

public class CalendarConfig implements IConfig {

    public static final String MODULE_NAME = "calendar";

    protected Config config;

    private static CalendarConfig instance = new CalendarConfig(Config.getInstance());

    private File calendarDirectory;

    /**
     * @see java.lang.Object#Object()
     */
    private CalendarConfig(Config config) {
        this.config = config;
        calendarDirectory = new File(config.getConfigDirectory(), MODULE_NAME);
        DiskIO.ensureDirectory(calendarDirectory);
        File caldendarFile = new File(calendarDirectory, "list.xml");
        registerPlugin(caldendarFile.getName(), new DefaultXmlConfig(caldendarFile));
    }

    /**
	 * @return Returns the instance.
	 */
    public static CalendarConfig getInstance() {
        return instance;
    }

    /**
     * Method registerPlugin.
     * 
     * @param id
     * @param plugin
     */
    protected void registerPlugin(String id, DefaultXmlConfig plugin) {
        config.registerPlugin(MODULE_NAME, id, plugin);
    }

    public XmlElement getCalendarConfig() {
        DefaultXmlConfig xml = config.getPlugin(MODULE_NAME, "list.xml");
        return xml.getRoot();
    }

    public File getConfigDirectory() {
        return calendarDirectory;
    }
}
