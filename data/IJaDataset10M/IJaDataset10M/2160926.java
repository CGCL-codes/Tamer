package solidbase.config;

/**
 * Listens to configuration events.
 * 
 * @author R.M. de Bloois
 * @since Nov 25, 2008
 */
public interface ConfigListener {

    /**
	 * Called when a config file is about to be read.
	 * 
	 * @param path The path of the config file.
	 */
    void readingConfigFile(String path);
}
