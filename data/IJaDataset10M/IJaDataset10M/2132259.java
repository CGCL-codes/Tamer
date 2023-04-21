package org.dengues.core.components;

import java.util.List;
import java.util.ResourceBundle;
import org.dengues.core.module.EMFModule;
import org.dengues.core.process.ICompNode;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 1 2006-09-29 17:06:40Z qiang.zhang $
 * 
 */
public interface IXMLComponent {

    String PROP_NAME = "NAME";

    String PROP_LONG_NAME = "LONG_NAME";

    String CUSTOMIZE = "Customize";

    String CATEGORY = "CATEGORY";

    String CATEGORY_SEPARATOR = ":";

    String COLUMN_SCHEMA = "COLUMN_SCHEMA";

    public String getName();

    public ResourceBundle getResourceBundle();

    public boolean isLoaded();

    public ImageDescriptor getIcon24();

    public void setIcon24(ImageDescriptor icon24);

    public ImageDescriptor getIcon16();

    public void setIcon16(ImageDescriptor icon16);

    public ImageDescriptor getIcon32();

    public void setIcon32(ImageDescriptor icon32);

    public String getDisplayName();

    public String getLongName();

    public List<EJETCodePart> getAvailableCodeParts();

    public String getPathSource();

    public String getVersion();

    public List<NodePropertyParameter> createNodeParameters(ICompNode node);

    public List<EMFConnection> createConnections();

    public boolean isStart();

    public String getCategory();

    public List<EMFModule> getImportModules();

    public String getExternalPlugin();

    public boolean isExternalNode();

    public String getPluginId();

    public void setPluginId(String pluginId);
}
