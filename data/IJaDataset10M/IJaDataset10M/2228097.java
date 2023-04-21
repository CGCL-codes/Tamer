package org.columba.api.plugin;

public interface IExtensionHandlerKeys {

    /**
	 * Every action in Columba is handled by this class.
	 * <p>
	 * These actions are used to generate the menu and the toolbar dynamically.
	 * <p>
	 * @see org.columba.core.gui.action.AbstractColumbaAction
	 */
    public static final String ORG_COLUMBA_CORE_ACTION = "org.columba.core.action";

    /**
	 * Provides access to main entrypoint of components like addressbook
	 * and mail.
	 * <p>
	 * @see org.columba.core.component.api.IComponentPlugin
	 * @see org.columba.addressbook.main.AddressbookMain
	 * @see org.columba.mail.main.MailMain
	 */
    public static final String ORG_COLUMBA_CORE_COMPONENT = "org.columba.core.component";

    /**
	 * ConfigExtensionHandler provides an easy way for plugins to have their own
	 * configuration dialog.
	 * <p>
	 * Note that every plugin can have its own configuration file "config.xml"
	 * in its folder anyway.
	 * <p>
	 * @see org.columba.core.gui.plugin.AbstractConfigPlugin
	 */
    public static final String ORG_COLUMBA_CORE_CONFIG = "org.columba.core.config";

    /**
	 * Provides an easy way to integrate external apps in Columba.
	 * <p>
	 * This includes a first-time assistant for the user. And a configuration
	 * file "external_tools.xml" to store the options of the external tools.
	 * <p>
	 * When using external commandline (already used examples are aspell and
	 * GnuPG) tools, you should just use this handler to get the location of the
	 * executable.
	 * <p>
	 * If the executable wasn't configured, yet a wizard will assist the user in
	 * configuring the external tool. If everything is correctly configured, it
	 * will just return the path of the commandline tool as <code>File</code>.
	 * <p>
	 * <verbatim> File file = getLocationOfExternalTool("gpg"); </verbatim>
	 * <p>
	 * @see org.columba.core.gui.externaltools.AbstractExternalToolsPlugin
	 */
    public static final String ORG_COLUMBA_CORE_EXTERNALTOOLS = "org.columba.core.externaltools";

    /**
	 * Frame component which is shown in the JFrame container's content pane.
	 * <p>
	 * @see org.columba.api.gui.frame.IFrameMediator
	 * @see org.columba.core.gui.frame
	 * @see org.columba.mail.gui.frame.ThreePaneMailFrameController 
	 */
    public static final String ORG_COLUMBA_CORE_FRAME = "org.columba.core.frame";

    /**
	 * Html Viewer component.
	 * <p>
	 * @see org.columba.core.gui.htmlviewer.api.IHTMLViewerPlugin
	 * @see org.columba.core.gui.htmlviewer
	 */
    public static final String ORG_COLUMBA_CORE_HTMLVIEWER = "org.columba.core.htmlviewer";

    /**
	 * Tray Icon plugged into the statusbar
	 * <p>
	 * @see org.columba.api.statusbar.IStatusBarExtension
	 */
    public static final String ORG_COLUMBA_CORE_STATUSBAR = "org.columba.core.statusbar";

    /**
	 * Look and Feel plugin handler.
	 * <p>
	 * @see org.columba.core.gui.themes.plugin.AbstractThemePlugin
	 */
    public static final String ORG_COLUMBA_CORE_THEME = "org.columba.core.theme";

    /**
	 * Background services
	 * <p>
	 * @see org.columba.core.scripting.service.api.IColumbaService
	 */
    public static final String ORG_COLUMBA_CORE_SERVICE = "org.columba.core.service";

    /**
	 * Actions can be registered. These actions should be representing "new item".
	 * For example "New Mail", "New Contact", "New Appointment". 
	 * <p>
	 * They will be promptly presented in the "File->New" menu and the toolbar.
	 * <p>
	 * @see org.columba.core.gui.action.AbstractColumbaAction
	 */
    public static final String ORG_COLUMBA_CORE_NEWITEM = "org.columba.core.newitem";

    /**
	 * Search Providers should implement <code>org.columba.core.search.api.ISearchProvider</code>
	 * <p>
	 * @see org.columba.core.search.api.ISearchProvider
	 */
    public static final String ORG_COLUMBA_CORE_SEARCH = "org.columba.core.search";
}
