package org.jsens.editors.internal.effects;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;
import org.jsens.businesslogic.Effect;
import org.jsens.editors.internal.IEffectChangeListener;

/**
 * This class is responsible for the execution of extension point calls. It gets
 * messages from the GraphManager and calls then the extenders.
 * 
 * @author romi
 * 
 */
public class ExtensionPointExecuter implements IGraphChangeListener {

    public void effectChanged(final Effect effect) {
        IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.jsens.editors.effects.editorchanges");
        for (IConfigurationElement element : elements) {
            try {
                final Object effectChangeListener = element.createExecutableExtension("class");
                if (effectChangeListener instanceof IEffectChangeListener) {
                    PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

                        public void run() {
                            IEffectChangeListener.class.cast(effectChangeListener).effectChanged(effect);
                        }
                    });
                }
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
    }
}
