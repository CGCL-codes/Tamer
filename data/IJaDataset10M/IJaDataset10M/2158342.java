package org.openejb.ui.jedi.openejb11.jca;

import org.opentools.deployer.plugins.Category;
import org.opentools.deployer.plugins.ClassDescriptor;
import org.opentools.deployer.plugins.FileDescriptor;
import org.opentools.deployer.plugins.MetaData;
import org.opentools.deployer.plugins.Plugin;

/**
 * The main Plugin for OpenEJB RARs.  This class provides the name to
 * display on tabs holding OpenEJB information, the XML file name to save
 * and load data from, toolbar icons, access to metadata, and the entries
 * and categories to load into the tree.
 *
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version $Revision: 1.3 $
 */
public class JcaPlugin extends Plugin {

    public JcaPlugin() {
    }

    public Category[] getCategories(MetaData data) {
        return new Category[] { new DeploymentCategory(this, (JcaMetaData) data) };
    }

    public MetaData getMetaDataInstance() {
        return new JcaMetaData();
    }

    public String getXMLFileName() {
        return "openejb-ra.xml";
    }

    public ClassDescriptor[] getInterestingClasses() {
        return new ClassDescriptor[0];
    }

    public FileDescriptor[] getInterestingFiles() {
        return new FileDescriptor[0];
    }

    public String getName() {
        return "OpenEJB 1.1";
    }
}
