package org.dyno.visual.swing.plugin.spi;

import java.util.List;
import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.eclipse.core.runtime.IConfigurationElement;

@SuppressWarnings("unchecked")
public abstract class InvisibleAdapter extends AbstractAdaptable implements IAdapter {

    protected String name;

    protected String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        if (lastName != null) return lastName; else return name;
    }

    public void init(WidgetAdapter rootAdapter, String name, Object object) {
        this.name = name;
        this.lastName = name;
    }

    public abstract List getElements();

    public static InvisibleAdapter createAdapter(WidgetAdapter rootAdapter, String name, Object object) {
        String className = object.getClass().getName();
        IConfigurationElement config = ExtensionRegistry.getInvisibleConfig(className);
        if (config == null) return null;
        try {
            InvisibleAdapter invisible = (InvisibleAdapter) config.createExecutableExtension("class");
            invisible.init(rootAdapter, name, object);
            return invisible;
        } catch (Exception e) {
            VisualSwingPlugin.getLogger().error(e);
            return null;
        }
    }

    public boolean isRenamed() {
        return lastName != null && !lastName.equals(name);
    }
}
