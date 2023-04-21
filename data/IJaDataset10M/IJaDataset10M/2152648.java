package org.pubcurator.core.model;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.pubcurator.core.services.IRepositoryInitializer;

/**
 * @author Kai Schlamp (schlamp@gmx.de)
 *
 */
public class RepositoryInitializerDescriptor extends ExtensionDescriptor {

    private String id;

    private String name;

    private IRepositoryInitializer repositoryInitializer;

    public RepositoryInitializerDescriptor(IConfigurationElement element) {
        super(element);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IRepositoryInitializer getRepositoryInitializer() throws CoreException {
        if (repositoryInitializer == null) {
            repositoryInitializer = (IRepositoryInitializer) getElement().createExecutableExtension("class");
        }
        return repositoryInitializer;
    }
}
