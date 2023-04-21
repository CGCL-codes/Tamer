package org.columba.mail.folderoptions;

import org.columba.api.plugin.IExtensionInterface;
import org.columba.core.xml.XmlElement;
import org.columba.mail.config.FolderItem;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.gui.frame.MailFrameMediator;

/**
 * AbstractMessageFolder options plugin abstract class.
 * <p>
 * Plugins implementing this abstract class can load/save their configuration
 * data. They don't need to take care if this data is applied globally or on a
 * per-folder basis.
 * <p>
 * The most interest methods which you need to implement are:
 * <ul>
 * <li>createDefaultElement(boolean)</li>
 * <li>loadOptionsFromXml(AbstractMessageFolder)</li>
 * <li>saveOptionsToXml(AbstractMessageFolder)</li>
 * </ul>
 * <p>
 * Note, that every {@link MailFrameMediator} keeps its own
 * {@link FolderOptionsController}, which makes sure that all plugins are
 * singletons.
 * 
 * @author fdietz
 */
public abstract class AbstractFolderOptionsPlugin implements IExtensionInterface {

    /**
	 * mail frame mediator
	 */
    private MailFrameMediator mediator;

    /**
	 * name of configuration node
	 */
    private String name;

    /**
	 * Constructor
	 * 
	 * TODO (@author fdietz):  pluginID is never used locally
	 * @param name
	 *            name of plugin
	 * @param pluginId
	 *            id of plugin used by plugin handler
	 * @param mediator
	 *            mail frame mediator
	 */
    public AbstractFolderOptionsPlugin(String name, String pluginId, MailFrameMediator mediator) {
        this.name = name;
        this.mediator = mediator;
    }

    /**
	 * Save configuration of this plugin.
	 * <p>
	 * 
	 * Following a simple example of a toolbar configuration:<br>
	 * 
	 * <pre>
	 * 
	 *  
	 *   &lt;toolbar enabled=&quot;true&quot; show_icon=&quot;true&quot; show_text=&quot;false&quot;&gt;
	 *    &lt;button name=&quot;Cut&quot;/&gt;
	 *    &lt;button name=&quot;Copy&quot;/&gt;
	 *    &lt;button name=&quot;Paste&quot;/&gt;
	 *    &lt;button name=&quot;Delete&quot;/&gt;
	 *   &lt;/toolbar&gt;
	 *   
	 *  
	 * </pre>
	 * 
	 * @param folder
	 *            selected folder
	 */
    public abstract void saveOptionsToXml(IMailbox folder);

    /**
	 * Load options of this plugin.
	 * 
	 * @param folder
	 *            selected folder
	 */
    public abstract void loadOptionsFromXml(IMailbox folder);

    /**
	 * Get frame mediator
	 * 
	 * @return frame mediator
	 */
    public MailFrameMediator getMediator() {
        return mediator;
    }

    /**
	 * Get configuration node.
	 * <p>
	 * Determine if this should be applied globally or on a per-folder basis.
	 * <p>
	 * This way, plugins don't have to know, if they work on global or local
	 * options.
	 * <p>
	 * Example for the sorting plugin configuration node. This is how it can be
	 * found in options.xml and tree.xml:<br>
	 * 
	 * <pre>
	 * 
	 *  
	 *    &lt;sorting column=&quot;Date&quot; order=&quot;true&quot; /&gt;
	 *   
	 *  
	 * </pre>
	 * 
	 * @param folder
	 *            currently selected folder
	 * @return xml node
	 */
    public XmlElement getConfigNode(IMailbox folder) {
        if (folder == null) {
            XmlElement result = FolderItem.getGlobalOptions().getElement(getName());
            if (result == null) {
                return createDefaultElement(true);
            } else {
                return result;
            }
        }
        XmlElement parent = folder.getConfiguration().getFolderOptions();
        XmlElement child = parent.getElement(getName());
        if (child == null) {
            child = createDefaultElement(false);
            parent.addElement(child);
        }
        if (child.getAttribute("overwrite").equals("true")) {
            return child;
        } else {
            parent = FolderItem.getGlobalOptions();
            child = parent.getElement(getName());
            if (child == null) {
                child = createDefaultElement(true);
                parent.addElement(child);
            }
            return child;
        }
    }

    /**
	 * Create default node.
	 * <p>
	 * Overwrite this method to add plugin-specific information to the parent
	 * node.
	 * <p>
	 * 
	 * @param global
	 *            true, if this is a global options. False, otherwise
	 * 
	 * @return xml node
	 */
    public XmlElement createDefaultElement(boolean global) {
        XmlElement parent = new XmlElement(getName());
        if (!global) {
            parent.addAttribute("overwrite", "false");
        }
        return parent;
    }

    /**
	 * Get name of configuration node
	 * 
	 * @return config name
	 */
    public String getName() {
        return name;
    }
}
