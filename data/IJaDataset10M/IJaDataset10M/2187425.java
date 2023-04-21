package org.middleheaven.web.container;

import javax.servlet.ServletContext;
import org.middleheaven.io.repository.ManagedFileRepositoryProvider;
import org.middleheaven.io.repository.machine.MachineFileSystemRepositoryProvider;
import org.middleheaven.process.web.CommonHttpServerContainers;
import org.middleheaven.util.Version;

public class StandardSevletContainer extends WebContainer {

    public StandardSevletContainer(ServletContext context) {
        super(context);
    }

    @Override
    public String getContainerName() {
        return "Standard Servlet Container";
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public WebContainerInfo getWebContainerInfo() {
        return new WebContainerInfo(CommonHttpServerContainers.UNKNOW, Version.unknown());
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public ManagedFileRepositoryProvider getManagedFileRepositoryProvider() {
        return MachineFileSystemRepositoryProvider.getProvider();
    }
}
