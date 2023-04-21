package net.sf.javascribe.generator.context.processor;

import java.util.List;
import net.sf.javascribe.appdef.wrapper.configuration.ViewDefinitionConfigurationWrapper;
import net.sf.javascribe.appdef.wrapper.view.ViewDefinitionWrapper;
import net.sf.javascribe.appdef.wrapper.view.ViewGroupWrapper;

/**
 * @author User
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ViewDefinitionProcessorContext extends AbstractViewTierProcessorContext implements ComponentProcessorContext {

    ViewDefinitionWrapper viewDef = null;

    ViewDefinitionConfigurationWrapper config = null;

    public String getProperty(String name) {
        return config.getProperty(name);
    }

    public ViewDefinitionProcessorContext(ViewDefinitionWrapper v) {
        viewDef = v;
    }

    public List getViewGroupNames() {
        return viewDef.getViewGroupNames();
    }

    public ViewGroupWrapper getViewGroup(String viewGroupName) {
        return viewDef.getViewGroup(viewGroupName);
    }

    public ViewDefinitionWrapper getViewDefinition() {
        return viewDef;
    }

    public ViewGroupProcessorContext getViewGroupProcessorContext(String viewGroupName) {
        ViewGroupProcessorContext ret = null;
        ret = new ViewGroupProcessorContext(getViewGroup(viewGroupName));
        ret.setTemplateManager(templates);
        ret.types = types;
        ret.globalObjects = globalObjects;
        ret.config = config.getViewGroupConfig(viewGroupName);
        ret.applicationPlatform = applicationPlatform;
        return ret;
    }
}
