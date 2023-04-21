package com.luxoft.fitpro.plugin.registry;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import com.luxoft.fitpro.adapters.eclipse.TemplateBuilderExtensionElement;
import com.luxoft.fitpro.core.templates.ITemplateBuilderExtensionElement;
import com.luxoft.fitpro.core.templates.ITemplateBuilderRegistry;
import com.luxoft.fitpro.plugin.config.FitPlugin;

public class TemplateBuilderRegistry implements ITemplateBuilderRegistry {

    public List<ITemplateBuilderExtensionElement> getElements() {
        List<ITemplateBuilderExtensionElement> builderExtensionElements = new ArrayList<ITemplateBuilderExtensionElement>();
        IExtension[] extensions = getExtensions();
        for (IExtension extension : extensions) {
            IConfigurationElement[] configElements = extension.getConfigurationElements();
            for (IConfigurationElement configElement : configElements) {
                String fixtureType = configElement.getAttribute("fixtureType");
                String fixtureBuilderClass = configElement.getAttribute("templateBuilderClass");
                TemplateBuilderExtensionElement builderElement = new TemplateBuilderExtensionElement(fixtureType, fixtureBuilderClass, configElement);
                builderExtensionElements.add(builderElement);
            }
        }
        return builderExtensionElements;
    }

    private IExtension[] getExtensions() {
        IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(FitPlugin.getDefault().getPluginId(), "fixtureTemplateBuilder").getExtensions();
        return extensions;
    }
}
