package net.entropysoft.dashboard.plugin.variables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.entropysoft.dashboard.plugin.StatusHelper;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.osgi.framework.Bundle;

/**
 * Manages variables
 * 
 * @author cedric
 * 
 */
public class VariableManager implements IVariableFactory {

    private static VariableManager instance = new VariableManager();

    private List<IVariableFactory> factories = new ArrayList<IVariableFactory>();

    private ListenerList monitorVariableListeners = new ListenerList();

    private MonitorVariableListener monitorVariableListener = new MonitorVariableListener();

    private VariableManager() {
        registerVariableFactories();
    }

    public static VariableManager getInstance() {
        return instance;
    }

    /**
	 * register the variable factories declared using the extension point
	 */
    private void registerVariableFactories() {
        IExtensionPoint extPoint = Platform.getExtensionRegistry().getExtensionPoint("net.entropysoft.dashboard.variableFactories");
        if (extPoint == null) {
            StatusHelper.logError("Could not find net.entropysoft.dashboard.variableFactories extension point", null);
            return;
        }
        IExtension[] extensions = extPoint.getExtensions();
        for (IExtension extension : extensions) {
            String contributorName = extension.getContributor().getName();
            final Bundle bundle = Platform.getBundle(contributorName);
            IConfigurationElement[] elements = extension.getConfigurationElements();
            for (IConfigurationElement configElement : elements) {
                if ("factory".equals(configElement.getName())) {
                    try {
                        IVariableFactory variableFactory = (IVariableFactory) configElement.createExecutableExtension("class");
                        register(variableFactory);
                    } catch (CoreException e) {
                        StatusHelper.logError("Error while registering a variable factory from plugin " + configElement.getContributor().getName() + ": " + e.getMessage(), e);
                    }
                }
            }
        }
    }

    /**
	 * register a variable factory
	 * 
	 * @param variableFactory
	 */
    public void register(IVariableFactory variableFactory) {
        factories.add(variableFactory);
        variableFactory.addVariableManagerListener(monitorVariableListener);
    }

    /**
	 * unregister a variable factory
	 * 
	 * @param variableFactory
	 */
    public void unregister(IVariableFactory variableFactory) {
        if (factories.remove(variableFactory)) {
            variableFactory.removeVariableManagerListener(monitorVariableListener);
        }
    }

    public IVariable[] getMonitoredVariables() {
        List<IVariable> monitoredVariables = new ArrayList<IVariable>();
        for (IVariableFactory variableFactory : factories) {
            monitoredVariables.addAll(Arrays.asList(variableFactory.getMonitoredVariables()));
        }
        return monitoredVariables.toArray(new IVariable[monitoredVariables.size()]);
    }

    public IVariable getVariable(VariablePath variablePath) {
        IVariableFactory variableFactory = getVariableFactory(variablePath);
        if (variableFactory == null) {
            return null;
        }
        return variableFactory.getVariable(variablePath);
    }

    public void addVariableListener(IVariable variable, IMonitoredVariableListener listener) {
        IVariableFactory variableFactory = getVariableFactory(variable.getPath());
        if (variableFactory == null) {
            return;
        }
        variableFactory.addVariableListener(variable, listener);
    }

    public void removeVariableListener(IVariable variable, IMonitoredVariableListener listener) {
        IVariableFactory variableFactory = getVariableFactory(variable.getPath());
        if (variableFactory == null) {
            return;
        }
        variableFactory.removeVariableListener(variable, listener);
    }

    public void monitorVariable(IVariable variable, boolean forceAvailable) {
        IVariableFactory variableFactory = getVariableFactory(variable.getPath());
        if (variableFactory == null) {
            return;
        }
        variableFactory.monitorVariable(variable, forceAvailable);
    }

    public void monitorVariable(IVariable variable) {
        monitorVariable(variable, false);
    }

    public boolean isAvailable(IVariable variable) {
        IVariableFactory variableFactory = getVariableFactory(variable.getPath());
        if (variableFactory == null) {
            return false;
        }
        return variableFactory.isAvailable(variable);
    }

    public void unmonitorVariable(IVariable variable) {
        IVariableFactory variableFactory = getVariableFactory(variable.getPath());
        if (variableFactory == null) {
            return;
        }
        variableFactory.unmonitorVariable(variable);
    }

    public IValue getValue(IVariable variable, boolean forceUpdate) {
        IVariableFactory variableFactory = getVariableFactory(variable.getPath());
        if (variableFactory == null) {
            return null;
        }
        return variableFactory.getValue(variable, forceUpdate);
    }

    public IValue getValue(IVariable variable) {
        return getValue(variable, false);
    }

    public IVariableDescription getVariableDescription(IVariable variable, boolean forceUpdate) {
        IVariableFactory variableFactory = getVariableFactory(variable.getPath());
        if (variableFactory == null) {
            return null;
        }
        return variableFactory.getVariableDescription(variable, forceUpdate);
    }

    public IVariableDescription getVariableDescription(IVariable variable) {
        return getVariableDescription(variable, false);
    }

    public void setValue(IVariable variable, Object newValue) throws Exception {
        IVariableFactory variableFactory = getVariableFactory(variable.getPath());
        if (variableFactory == null) {
            return;
        }
        variableFactory.setValue(variable, newValue);
    }

    public IValue getLatestKnownValue(IVariable variable) {
        IVariableFactory variableFactory = getVariableFactory(variable.getPath());
        if (variableFactory == null) {
            return null;
        }
        return variableFactory.getLatestKnownValue(variable);
    }

    public IVariableDescription getLatestKnownVariableDescription(IVariable variable) {
        IVariableFactory variableFactory = getVariableFactory(variable.getPath());
        if (variableFactory == null) {
            return null;
        }
        return variableFactory.getLatestKnownVariableDescription(variable);
    }

    private IVariableFactory getVariableFactory(VariablePath variablePath) {
        for (IVariableFactory variableFactory : factories) {
            if (variableFactory.isHandled(variablePath)) {
                return variableFactory;
            }
        }
        return null;
    }

    /**
	 * check if given variable path is handled by one of the variable factory.
	 * If not, all variable manager methods will fail silently
	 */
    public boolean isHandled(VariablePath variablePath) {
        return getVariableFactory(variablePath) != null;
    }

    public void addVariableManagerListener(IVariableManagerListener listener) {
        monitorVariableListeners.add(listener);
    }

    public void removeVariableManagerListener(IVariableManagerListener listener) {
        monitorVariableListeners.remove(listener);
    }

    private void fireVariableMonitored(final IVariable variable) {
        final Object[] listeners = monitorVariableListeners.getListeners();
        for (int i = 0; i < listeners.length; i++) {
            final IVariableManagerListener listener = (IVariableManagerListener) listeners[i];
            SafeRunner.run(new SafeRunnable() {

                public void run() throws Exception {
                    listener.variableMonitored(variable);
                }

                public void handleException(Throwable e) {
                }
            });
        }
    }

    private void fireVariableUnmonitored(final IVariable variable) {
        final Object[] listeners = monitorVariableListeners.getListeners();
        for (int i = 0; i < listeners.length; i++) {
            final IVariableManagerListener listener = (IVariableManagerListener) listeners[i];
            SafeRunner.run(new SafeRunnable() {

                public void run() throws Exception {
                    listener.variableUnmonitored(variable);
                }

                public void handleException(Throwable e) {
                }
            });
        }
    }

    private void fireVariablesAvailable(final IVariable[] variables) {
        final Object[] listeners = monitorVariableListeners.getListeners();
        for (int i = 0; i < listeners.length; i++) {
            final IVariableManagerListener listener = (IVariableManagerListener) listeners[i];
            SafeRunner.run(new SafeRunnable() {

                public void run() throws Exception {
                    listener.variablesAvailable(variables);
                }

                public void handleException(Throwable e) {
                }
            });
        }
    }

    /**
	 * Listen to newly monitored variables for each registered
	 * {@link IVariableFactory}
	 * 
	 */
    private class MonitorVariableListener implements IVariableManagerListener {

        public void variableMonitored(IVariable variable) {
            fireVariableMonitored(variable);
        }

        public void variableUnmonitored(IVariable variable) {
            fireVariableUnmonitored(variable);
        }

        public void variablesAvailable(IVariable[] variables) {
            fireVariablesAvailable(variables);
        }
    }
}
