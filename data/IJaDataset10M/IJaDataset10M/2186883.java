package net.sf.opensoundboard.config;

import jargs.gnu.*;
import jargs.gnu.CmdLineParser.Option;
import jargs.gnu.CmdLineParser.OptionException;
import java.util.List;
import net.sf.opensoundboard.l10n.Text;
import org.apache.log4j.Logger;

/**
 * Handles the command line options
 */
public class CommandLineOptions {

    private final Logger log = Logger.getLogger(this.getClass());

    private Boolean hideIcon;

    private Boolean showIcon;

    private String mappingFile;

    private String configFile;

    private String iconFile;

    private String language;

    private Boolean listLanguages;

    /**
	 * @return wether the option "hideicon" is set. May be null.
	 */
    public Boolean isHideIcon() {
        return hideIcon;
    }

    /**
	 * @return wether the option "showicon" is set. May be null.
	 */
    public Boolean isShowIcon() {
        return showIcon;
    }

    /**
	 * @return the value for the option "mapping". May be null.
	 */
    public String getMappingFile() {
        return mappingFile;
    }

    /**
	 * @return the value for the option "configfile". May be null.
	 */
    public String getConfigFile() {
        return configFile;
    }

    /**
	 * @return the value for the option "iconfile". May be null.
	 */
    public String getIconFile() {
        return iconFile;
    }

    /**
	 * @return the value for the option "language". May be null.
	 */
    public String getLanguage() {
        return language;
    }

    /**
	 * @return wether the option "listlanguages" is set. May be null.
	 */
    public Boolean isListLanguages() {
        return listLanguages;
    }

    /**
	 * Gets the command line options using <a
	 * href="http://jargs.sourceforge.net/">jargs</a>
	 */
    public void parseArgs(List<String> arglist) {
        CmdLineParser parser = new CmdLineParser();
        Option hideiconOption = parser.addBooleanOption('h', "hideicon");
        Option showiconOption = parser.addBooleanOption('s', "showicon");
        Option mappingfileOption = parser.addStringOption('m', "mapping");
        Option configfileOption = parser.addStringOption('c', "config");
        Option iconfileOption = parser.addStringOption('i', "icon");
        Option languageOption = parser.addStringOption('l', "language");
        Option listlanguagesOption = parser.addBooleanOption("listlanguages");
        try {
            parser.parse(toLowerArray(arglist));
            this.hideIcon = (Boolean) parser.getOptionValue(hideiconOption);
            this.showIcon = (Boolean) parser.getOptionValue(showiconOption);
            this.mappingFile = (String) parser.getOptionValue(mappingfileOption);
            this.configFile = (String) parser.getOptionValue(configfileOption);
            this.iconFile = (String) parser.getOptionValue(iconfileOption);
            this.language = (String) parser.getOptionValue(languageOption);
            this.listLanguages = (Boolean) parser.getOptionValue(listlanguagesOption);
        } catch (OptionException e) {
            log.fatal(e.getMessage());
            System.exit(1);
        }
        if (!checkInvalidOptions()) {
            System.exit(1);
        }
    }

    /**
	 * Converts the passed list to an array, where all entries are in lower case
	 * 
	 * @param args the list to be converted
	 * @return an array with all lower case entries
	 */
    private String[] toLowerArray(List<String> list) {
        String[] arr = new String[list.size()];
        int i = 0;
        for (String entry : list) {
            arr[i] = entry.toLowerCase();
            i++;
        }
        return arr;
    }

    /**
	 * Checks if any of the set command line options are invalid, cannot be used
	 * together, etc.
	 * 
	 * @return true if no problems were found
	 */
    private boolean checkInvalidOptions() {
        boolean isOk = true;
        if ((hideIcon != null) && (showIcon != null)) {
            log.fatal("You cannot use --hideicon and --showicon together");
            isOk = false;
        }
        if (language != null) {
            if (!Text.languageExists(language)) {
                log.fatal("The specified language '" + language + "' does not exist");
                isOk = false;
            }
        }
        return isOk;
    }
}
