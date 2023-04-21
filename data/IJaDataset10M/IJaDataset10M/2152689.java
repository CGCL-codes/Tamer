package osdep.configuration;

import java.util.List;

/**
 * Defines an entity that is able to choose the correct configuration for this platform.
 * It can be an automatic chooser or one that requires the help of the user
 * @author SHZ Mar 7, 2008
 */
public interface IConfigurationChooser {

    /**
	 * set up the chooser with the possible choices
	 * @param lstEntry the list of configurations to choose
	 */
    public void setup(List<ConfigurationEntry> lstEntry);

    /**
	 * Returns the chosen configuration
	 * @return the configuration that has been chosen
	 * @throws ConfigurationNotFound 
	 */
    public ConfigurationEntry getConfiguration() throws ConfigurationNotFound;
}
