package net.sourceforge.squirrel_sql.fw.gui.action.wikiTable;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.squirrel_sql.fw.xml.XMLBeanReader;
import net.sourceforge.squirrel_sql.fw.xml.XMLBeanWriter;

/**
 * A storage for configurations of WIKI tables.
 *  
 * This class is a simple wrapper Bean to serialize a list of {@link IWikiTableConfiguration} via {@link XMLBeanWriter} and read it via {@link XMLBeanReader}.
 * This is needed, because {@link XMLBeanReader} cannot handle List Objects. It needs Arrays.
 * @author Stefan Willinger
 */
public class WikiTableConfigurationStorage {

    /**
	 * Configurations to store.
	 */
    private IWikiTableConfiguration[] configurations = null;

    /**
	 * Default constructor
	 */
    public WikiTableConfigurationStorage() {
        super();
    }

    /**
	 * Constructor to initialize the configurations
	 * @param configs configuration to use.
	 */
    public WikiTableConfigurationStorage(List<IWikiTableConfiguration> configs) {
        super();
        if (configs != null) {
            configurations = configs.toArray(new IWikiTableConfiguration[] {});
        }
    }

    /**
	 * Convenience method to get configurations as list.
	 * @return the configurations as list.
	 */
    public List<IWikiTableConfiguration> configurationsAsList() {
        return Arrays.asList(configurations);
    }

    /**
	 * @return the configurations
	 */
    public IWikiTableConfiguration[] getConfigurations() {
        return configurations;
    }

    /**
	 * @param configurations the configurations to set
	 */
    public void setConfigurations(IWikiTableConfiguration[] configurations) {
        this.configurations = configurations;
    }
}
