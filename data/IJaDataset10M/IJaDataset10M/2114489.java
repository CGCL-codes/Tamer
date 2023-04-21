package org.rubypeople.rdt.internal.launching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.rubypeople.rdt.launching.IRuntimeLoadpathEntry;
import org.rubypeople.rdt.launching.IRuntimeLoadpathProvider;

/**
 * Proxy to a runtime classpath provider extension.
 */
public class RuntimeLoadpathProvider implements IRuntimeLoadpathProvider {

    private IConfigurationElement fConfigurationElement;

    private IRuntimeLoadpathProvider fDelegate;

    /**
	 * Constructs a new resolver on the given configuration element
	 */
    public RuntimeLoadpathProvider(IConfigurationElement element) {
        fConfigurationElement = element;
    }

    /**
	 * Returns the resolver delegate (and creates if required) 
	 */
    protected IRuntimeLoadpathProvider getProvider() throws CoreException {
        if (fDelegate == null) {
            fDelegate = (IRuntimeLoadpathProvider) fConfigurationElement.createExecutableExtension("class");
        }
        return fDelegate;
    }

    public String getIdentifier() {
        return fConfigurationElement.getAttribute("id");
    }

    /**
	 * @see IRuntimeLoadpathProvider#computeUnresolvedLoadpath(ILaunchConfiguration)
	 */
    public IRuntimeLoadpathEntry[] computeUnresolvedLoadpath(ILaunchConfiguration configuration) throws CoreException {
        return getProvider().computeUnresolvedLoadpath(configuration);
    }

    /**
	 * @see IRuntimeLoadpathProvider#resolveLoadpath(IRuntimeLoadpathEntry[], ILaunchConfiguration)
	 */
    public IRuntimeLoadpathEntry[] resolveLoadpath(IRuntimeLoadpathEntry[] entries, ILaunchConfiguration configuration) throws CoreException {
        return getProvider().resolveLoadpath(entries, configuration);
    }
}
