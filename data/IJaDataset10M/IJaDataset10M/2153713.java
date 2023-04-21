package net.sourceforge.javautil.developer.enterprise;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.ejb.EJBContainer;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import net.sourceforge.javautil.classloader.impl.ClassContext;
import net.sourceforge.javautil.classloader.source.VirtualDirectoryClassSource;
import net.sourceforge.javautil.common.ShutdownUtil;
import net.sourceforge.javautil.common.ShutdownUtil.ShutdownHook;
import net.sourceforge.javautil.common.exception.ThrowableManagerRegistry;
import net.sourceforge.javautil.common.io.VirtualDirectory;
import net.sourceforge.javautil.common.io.impl.Directory;
import net.sourceforge.javautil.common.io.impl.DirectoryFile;
import net.sourceforge.javautil.common.io.impl.SimplePath;
import net.sourceforge.javautil.developer.enterprise.ServiceDescriptor;
import net.sourceforge.javautil.developer.enterprise.ServiceContainer;
import org.jboss.deployers.client.spi.Deployment;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.vfs.plugins.client.AbstractVFSDeployment;
import org.jboss.deployers.vfs.spi.client.VFSDeployment;
import org.jboss.ejb3.embedded.JBossEJBContainer;
import org.jboss.ejb3.embedded.JBossEJBContainerProvider;
import org.jboss.virtual.VirtualFile;

/**
 * This is the base implementation for interacting with Embedded JBoss EJB3 services.
 *
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class JBossEmbeddedEJB3 implements ServiceContainer {

    private static final JBossEJBContainerProvider provider = new JBossEJBContainerProvider();

    protected JBossEJBContainer container;

    protected Map<String, AbstractVFSDeployment> deployed = new LinkedHashMap<String, AbstractVFSDeployment>();

    public JBossEmbeddedEJB3() {
        this.container = (JBossEJBContainer) provider.createEJBContainer(new LinkedHashMap());
    }

    public String getName() {
        return "Embedded JBoss Services Container";
    }

    public void deploy(ServiceDescriptor descriptor) {
        try {
            JBossVirtualFileContext ctx = new JBossVirtualFileContext(descriptor.getName(), descriptor.getServiceArtifact());
            VirtualFile root = new VirtualFile(ctx.getRoot());
            AbstractVFSDeployment dep = new AbstractVFSDeployment(root);
            this.container.deploy(dep);
            this.deployed.put(descriptor.getName(), dep);
        } catch (IOException e) {
            throw ThrowableManagerRegistry.caught(e);
        } catch (DeploymentException e) {
            throw ThrowableManagerRegistry.caught(e);
        } catch (URISyntaxException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }

    public void undeploy(ServiceDescriptor descriptor) {
        try {
            this.container.getContext().destroySubcontext(descriptor.getName());
            this.deployed.remove(descriptor.getName());
        } catch (NamingException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }

    public boolean isDeployed(ServiceDescriptor descriptor) {
        return this.deployed.containsKey(descriptor.getName());
    }

    public Map<String, AbstractVFSDeployment> getDeployed() {
        return new LinkedHashMap<String, AbstractVFSDeployment>(this.deployed);
    }
}
