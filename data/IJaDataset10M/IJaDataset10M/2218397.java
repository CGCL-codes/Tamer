package org.wtc.eclipse.platform.internal.shellhandlers;

import com.windowtester.runtime.IUIContext;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.wtc.eclipse.platform.PlatformActivator;
import org.wtc.eclipse.platform.shellhandlers.AbstractShellHandler;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Parse an extension point that adds a shell handler to the default list of shell
 * handlers that apply to all test plugins loaded.
 */
public class DefaultShellHandlersRegistry {

    private static DefaultShellHandlersRegistry _instance;

    private static final String DEFAULTSHELLHANDLERS_EXTENSION_POINT_ID = "org.wtc.eclipse.platform.shellHandlers";

    private static final String ELEMENT_SHELLHANDER = "shellHandler";

    private static final String ATTR_CLASS = "class";

    private Collection<AbstractShellHandler> _defaultHandlers;

    /**
     * Parse the extension point.
     */
    private DefaultShellHandlersRegistry(IUIContext ui) {
        _defaultHandlers = new ArrayList<AbstractShellHandler>();
        IExtensionRegistry extensionReg = Platform.getExtensionRegistry();
        IConfigurationElement[] shellHandlers = extensionReg.getConfigurationElementsFor(DEFAULTSHELLHANDLERS_EXTENSION_POINT_ID);
        for (IConfigurationElement nextElement : shellHandlers) {
            if (nextElement.getName().equals(ELEMENT_SHELLHANDER)) {
                String bundleID = nextElement.getNamespaceIdentifier();
                Bundle bundle = Platform.getBundle(bundleID);
                String shellHandlerClassString = nextElement.getAttribute(ATTR_CLASS);
                if ((shellHandlerClassString == null) || (shellHandlerClassString.length() == 0)) {
                    String message = MessageFormat.format("ERROR - THE BUNDLE <{0}> " + "> DECLARED A DEFAULT SHELL HANDLER WITHOUT A CLASS ATTRIBUTE", new Object[] { bundleID });
                    PlatformActivator.logError(message);
                    continue;
                }
                Class<?> loadedClass = null;
                try {
                    loadedClass = bundle.loadClass(shellHandlerClassString);
                } catch (ClassNotFoundException ex) {
                    PlatformActivator.logException(ex);
                    continue;
                }
                if (!AbstractShellHandler.class.isAssignableFrom(loadedClass)) {
                    String message = MessageFormat.format("ERROR - THE BUNDLE <{0}> " + "> DECLARED A DEFAULT SHELL HANDLER <{1}> " + "THAT DOES NOT EXTEND {2}; " + "THE HANDLER WILL BE IGNORED", new Object[] { bundleID, shellHandlerClassString, AbstractShellHandler.class.getName() });
                    PlatformActivator.logError(message);
                    continue;
                }
                Constructor<?> ctor = null;
                try {
                    ctor = loadedClass.getConstructor(new Class[] {});
                } catch (SecurityException ex) {
                    PlatformActivator.logException(ex);
                    String message = MessageFormat.format("ERROR - THE BUNDLE <{0}> " + "> DECLARED A DEFAULT SHELL HANDLER <{1}> " + "THAT DOES HAVE A DEFAULT CONSTRUCTOR; " + "THE HANDLER WILL BE IGNORED", new Object[] { bundleID, shellHandlerClassString });
                    PlatformActivator.logError(message);
                    continue;
                } catch (NoSuchMethodException ex) {
                    PlatformActivator.logException(ex);
                    String message = MessageFormat.format("ERROR - THE BUNDLE <{0}> " + "> DECLARED A DEFAULT SHELL HANDLER <{1}> " + "THAT DOES HAVE A DEFAULT CONSTRUCTOR; " + "THE HANDLER WILL BE IGNORED", new Object[] { bundleID, shellHandlerClassString });
                    PlatformActivator.logError(message);
                    continue;
                }
                AbstractShellHandler loadedHandler = null;
                try {
                    loadedHandler = (AbstractShellHandler) ctor.newInstance(new Object[] {});
                } catch (InstantiationException ex) {
                    PlatformActivator.logException(ex);
                    String message = MessageFormat.format("ERROR - THE BUNDLE <{0}> " + "> DECLARED A DEFAULT SHELL HANDLER <{1}> " + "THAT DOES HAVE A DEFAULT CONSTRUCTOR; " + "THE HANDLER WILL BE IGNORED", new Object[] { bundleID, shellHandlerClassString });
                    PlatformActivator.logError(message);
                    continue;
                } catch (IllegalAccessException ex) {
                    PlatformActivator.logException(ex);
                    String message = MessageFormat.format("ERROR - THE BUNDLE <{0}> " + "> DECLARED A DEFAULT SHELL HANDLER <{1}> " + "THAT COULD NOT BE LOADED; " + "THE HANDLER WILL BE IGNORED", new Object[] { bundleID, shellHandlerClassString });
                    PlatformActivator.logError(message);
                    continue;
                } catch (IllegalArgumentException ex) {
                    PlatformActivator.logException(ex);
                    String message = MessageFormat.format("ERROR - THE BUNDLE <{0}> " + "> DECLARED A DEFAULT SHELL HANDLER <{1}> " + "THAT COULD NOT BE LOADED; " + "THE HANDLER WILL BE IGNORED", new Object[] { bundleID, shellHandlerClassString });
                    PlatformActivator.logError(message);
                    continue;
                } catch (InvocationTargetException ex) {
                    PlatformActivator.logException(ex);
                    String message = MessageFormat.format("ERROR - THE BUNDLE <{0}> " + "> DECLARED A DEFAULT SHELL HANDLER <{1}> " + "THAT COULD NOT BE LOADED; " + "THE HANDLER WILL BE IGNORED", new Object[] { bundleID, shellHandlerClassString });
                    PlatformActivator.logError(message);
                    continue;
                }
                StringBuilder buffer = new StringBuilder();
                buffer.append("INFO - SUCCESSFULLY LOADED DEFAULT SHELL HANDLER <");
                buffer.append(shellHandlerClassString);
                buffer.append(">");
                PlatformActivator.logDebug(buffer.toString());
                _defaultHandlers.add(loadedHandler);
            }
        }
    }

    /**
     * Parse the extension point and get the initial conditions.
     */
    public static Collection<AbstractShellHandler> getDefaultShellHandlers(IUIContext ui) {
        return new ArrayList<AbstractShellHandler>(instance(ui)._defaultHandlers);
    }

    /**
     * Get the shared instance.
     */
    private static DefaultShellHandlersRegistry instance(IUIContext ui) {
        if (_instance == null) {
            _instance = new DefaultShellHandlersRegistry(ui);
        }
        return _instance;
    }
}
