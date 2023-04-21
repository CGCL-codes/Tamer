package org.jalgo.main;

import java.net.URL;

/**
 * The interface <code>IModuleInfo</code> contains methods to get some
 * information about a module, which is shown on a module chooser dialog or in
 * an about frame.<br>
 * The interface <code>IModuleInfo</code> has to be implemented by each module.
 * As convention the implementing classes have to be named 
 * <code>org.jalgo.module.&lt;moduleName&gt;.ModuleInfo</code>. The main program
 * will search for that class when loading the module.
 * For better performance and for the reason, that the interesting information
 * are unique for one module, the implementing classes of
 * <code>IModuleInfo</code> have to be modelled as singleton. The main program
 * will call the the method <code>getInstance()</code>, which has to retrieve
 * the singleton instance of <code>IModuleInfo</code>
 *   
 * @author Alexander Claus, Stephan Creutz
 */
public interface IModuleInfo {

    /**
	 * Retrieves the name of the module, which is shown on a module chooser
	 * dialog, on the tab of the module and as the menu-name of the module menu.
	 * 
	 * @return the name of the module
	 */
    public String getName();

    /**
	 * Retrieves the current build version number of the module as string.
	 * 
	 * @return the version number of the module
	 */
    public String getVersion();

    /**
	 * Retrieves a comma-separated list of the authors of the module.
	 * 
	 * @return tha author(s) of the module
	 */
    public String getAuthor();

    /**
	 * Retrieves a (more or less) short description about the module. It is
	 * shown on the module chooser inter alia. So it should give a short, but
	 * detailled information about the module.
	 * 
	 * @return a string representing a description of the module
	 */
    public String getDescription();

    /**
	 * Retrieves an <code>URL</code> object pointing to the logo image of the
	 * module. The image has to be sized to 16 x 16 pixels. It is shown on the
	 * tab of the module and in the new-Menu. Also this logo image is shown on
	 * the module chooser dialog, but later versions may provide two icons, one
	 * small icon for menu and tab and a big icon for the module chooser dialog. 
	 * 
	 * @return an <code>URL</code> to the logo of the module
	 */
    public URL getLogoURL();

    /**
	 * Retrieves a string describing the license, the module is distributed with.
	 * 
	 * @return the license
	 */
    public String getLicense();

    /**
	 * Retrieves an <code>URL</code> object pointing to the HelpSet file of the module.  
	 * The HelpSet file is the major metafile of the JavaHelp system.
	 * The URL must point to a *.hs-file.
	 * 
	 * @return the HelpSet filename
	 */
    public URL getHelpSetURL();
}
