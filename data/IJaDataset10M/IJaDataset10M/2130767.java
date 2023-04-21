package org.jlense.uiworks.internal.registry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPluginRegistry;
import org.jlense.uiworks.internal.IWorkbenchConstants;
import org.jlense.uiworks.internal.WorkbenchPlugin;

/**
 * A strategy to read view extensions from the registry.
 */
public class ActionSetRegistryReader extends RegistryReader {

    private static final String TAG_SET = "actionSet";

    private ActionSetRegistry registry;

    /**
 * RegistryViewReader constructor comment.
 */
    public ActionSetRegistryReader() {
        super();
    }

    /**
 * readElement method comment.
 */
    protected boolean readElement(IConfigurationElement element) {
        if (element.getName().equals(TAG_SET)) {
            try {
                ActionSetDescriptor desc = new ActionSetDescriptor(element);
                registry.addActionSet(desc);
            } catch (CoreException e) {
                WorkbenchPlugin.log("Unable to create action set descriptor.", e.getStatus());
            }
            return true;
        } else {
            return false;
        }
    }

    /**
 * Read the view extensions within a registry.
 */
    public void readRegistry(IPluginRegistry in, ActionSetRegistry out) {
        registry = out;
        readRegistry(in, IWorkbenchConstants.PLUGIN_ID, IWorkbenchConstants.PL_ACTION_SETS);
        out.mapActionSetsToCategories();
    }
}
