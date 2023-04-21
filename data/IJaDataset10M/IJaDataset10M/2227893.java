package net.sourceforge.javo2.configuration.file;

import generated.GlobalConfig;
import net.sourceforge.javo2.compiler.configuration.Configuration;

/**
 * @author Nicolás Di Benedetto
 * {@link mailto:nikodb@users.sourceforge.net nikodb@users.sourceforge.net}.<br>
 * Created on 26/09/2007.<br>
 * This class is intended to parse and validate the Global-Configuration portion
 * of the configuration file.
 */
public interface IGlobalConfigurationParser {

    /**
	 * Parses the Global Configuration portion of the configuration file.
	 * @param globalConfig The GobalConfig element to parse.
	 * @return a Configuration object properly initialized extracted from the 
	 * GlobalConfig parsed.
	 */
    public Configuration parse(GlobalConfig globalConfig);
}
