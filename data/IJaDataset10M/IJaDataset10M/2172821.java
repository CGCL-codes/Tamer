package org.ufacekit.ui.core;

import org.ufacekit.ui.core.internal.InternalUIApplicationContext;
import org.ufacekit.ui.core.styles.StyleEngine;
import org.ufacekit.ui.core.styles.internal.InternalStyleEngine;
import org.ufacekit.ui.core.util.Value;

/**
 * The application context restores all informations about the applications
 * state and its {@link UIElement}s. The informations provided by the
 * application context can be used e.g. by {@link UIExpression}s to calculate
 * the enabled state.
 * 
 * @since 1.0
 */
public class UIApplicationContext extends InternalUIApplicationContext {

    private StyleEngine styleEngine = new InternalStyleEngine();

    @Override
    public void addContextListener(UIApplicationContextListener listener) {
        super.addContextListener(listener);
    }

    @Override
    public UIElement getElement(String id) {
        return super.getElement(id);
    }

    @Override
    public Value getValue(String name) {
        return super.getValue(name);
    }

    @Override
    public void removeContextListener(UIApplicationContextListener listener) {
        super.removeContextListener(listener);
    }

    public void unregisterValue(String name) {
        super.unregisterValue(name);
    }

    public StyleEngine getStyleEngine() {
        return styleEngine;
    }
}
