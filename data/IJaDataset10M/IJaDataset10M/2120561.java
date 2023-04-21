package org.openejb.ui.jedi.openejb11.server;

import javax.swing.ImageIcon;
import org.opentools.deployer.plugins.Category;
import org.opentools.deployer.plugins.ClassDescriptor;
import org.opentools.deployer.plugins.Entry;
import org.opentools.deployer.plugins.FileDescriptor;
import org.opentools.deployer.plugins.MetaData;
import org.opentools.deployer.plugins.Plugin;
import org.opentools.deployer.plugins.PluginUtils;
import org.opentools.deployer.plugins.ToolBarCommand;
import org.opentools.deployer.plugins.j2ee12.ejb11.EJB11Plugin;

/**
 * The main Plugin for OpenEJB server configuration.  This class provides the
 * name to display on tabs holding OpenEJB information, the XML file name to
 * save and load data from, toolbar icons, access to metadata, and the entries
 * and categories to load into the tree.
 *
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version 1.0
 */
public class ServerPlugin extends Plugin {

    public MetaData getMetaDataInstance() {
        return new OpenEjbMetaData();
    }

    public String getName() {
        return "OpenEJB 1.1 Server";
    }

    public String getXMLFileName() {
        return "openejb-config.xml";
    }

    public ClassDescriptor[] getInterestingClasses() {
        return new ClassDescriptor[0];
    }

    public FileDescriptor[] getInterestingFiles() {
        return new FileDescriptor[0];
    }

    public Category[] getCategories(MetaData data) {
        return new Category[] { new CategoryContainer(this, (OpenEjbMetaData) data), new CategoryServer(this, (OpenEjbMetaData) data), new CategoryTransaction(this, (OpenEjbMetaData) data), new CategorySecurity(this, (OpenEjbMetaData) data), new CategoryCM(this, (OpenEjbMetaData) data), new CategoryConnector(this, (OpenEjbMetaData) data) };
    }

    public Plugin[] getDependencies() {
        return new Plugin[] { new EJB11Plugin() };
    }

    public ToolBarCommand[] getToolBarCommands(Category[] cats) {
        return new ToolBarCommand[] { new ToolBarCommand(new ImageIcon(getClass().getClassLoader().getResource("images/container-yellow-text.jpg")), "Create Container", PluginUtils.getCategory("Containers", cats)) };
    }

    public Entry getMainConfigEntry(MetaData data) {
        return null;
    }
}
