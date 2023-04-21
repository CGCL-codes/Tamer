package net.sourceforge.javautil.enterprise.server.jboss.ejb3;

import java.io.IOException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import net.sourceforge.javautil.classloader.impl.ClassContext;
import net.sourceforge.javautil.classloader.impl.StandardClassLoaderHeiarchy;
import net.sourceforge.javautil.classloader.resolver.ClassPackageContext;
import net.sourceforge.javautil.classloader.resolver.IClassPackageDependencyReference;
import net.sourceforge.javautil.classloader.resolver.IClassPackageResolver;
import net.sourceforge.javautil.classloader.resolver.ClassPackageResolverContext;
import net.sourceforge.javautil.classloader.resolver.impl.ClassPackageDependencyReferenceImpl;
import net.sourceforge.javautil.classloader.resolver.impl.maven.MavenRepositoryUtil;
import net.sourceforge.javautil.classloader.resolver.impl.maven.ProjectObjectModel;
import net.sourceforge.javautil.classloader.source.CompositeClassSource;
import net.sourceforge.javautil.classloader.source.InternalZipClassSource;
import net.sourceforge.javautil.classloader.source.VirtualDirectoryClassSource;
import net.sourceforge.javautil.classloader.source.ZipClassSource;
import net.sourceforge.javautil.common.ClassNameUtil;
import net.sourceforge.javautil.common.FileUtil;
import net.sourceforge.javautil.common.ReflectionUtil;
import net.sourceforge.javautil.common.ThreadUtil;
import net.sourceforge.javautil.common.URLUtil;
import net.sourceforge.javautil.common.VirtualArtifactUtil;
import net.sourceforge.javautil.common.exception.ThrowableManagerRegistry;
import net.sourceforge.javautil.common.io.VirtualArtifactSystem;
import net.sourceforge.javautil.common.io.IVirtualFile;
import net.sourceforge.javautil.common.io.impl.Directory;
import net.sourceforge.javautil.common.io.impl.DirectoryRoot;
import net.sourceforge.javautil.common.io.impl.SimplePath;
import net.sourceforge.javautil.common.io.impl.SystemDirectory;
import net.sourceforge.javautil.common.reflection.proxy.ReflectiveObjectProxy;
import net.sourceforge.javautil.common.shutdown.Shutdown;
import net.sourceforge.javautil.enterprise.server.ApplicationContainerLifecycleAbstract;
import net.sourceforge.javautil.enterprise.server.ApplicationContainerLifecycle.Phase;
import net.sourceforge.javautil.enterprise.server.ApplicationContainerLifecycle.Phase.Type;

/**
 * This will manage the lifecycle of an {@link Embeddable} container.
 *
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class EmbeddableLifecycle extends ApplicationContainerLifecycleAbstract<Embeddable> {

    public static final String GROUP_ID = "net.sf.javautil.jboss.embedded.ejb3";

    protected String resourcePrefix = ClassNameUtil.toRelativePackagePath(EmbeddableLifecycle.class.getPackage().getName() + ".conf");

    public EmbeddableLifecycle(Embeddable container) {
        super(container);
        this.preStartPhases = new Phase[] { new Phase("CLASSLOADER", Type.PreStart) };
        Shutdown.registerHook(this);
    }

    @Override
    protected void executePreStartPhase(Phase phase) {
        if ("CLASSLOADER".equals(phase.getName())) {
            container.root = new SystemDirectory(FileUtil.createTemporaryDirectory("jboss-ejb3-embedded", "tmp", false));
            CompositeClassSource ccs = new CompositeClassSource();
            ccs.add(new VirtualDirectoryClassSource(container.root));
            container.vroot = VirtualArtifactSystem.get("ejb3-container-jboss", true);
            ccs.add(new VirtualDirectoryClassSource(container.vroot));
            IClassPackageResolver resolver = ClassPackageContext.getPackageResolver();
            String pkgPath = ClassNameUtil.toRelativePackagePath(Embeddable.class.getPackage().getName());
            for (String name : new String[] { "jboss-ejb3-all", "jcainflow", "jms-ra" }) {
                IClassPackageDependencyReference reference = new ClassPackageDependencyReferenceImpl(GROUP_ID, name, "RC9", null);
                ClassPackageResolverContext ctx = resolver.resolve(reference, null);
                if (ctx.getAvailable().size() == 0) {
                    resolver.getImportableRepository().importPackage(VirtualArtifactUtil.getResource(pkgPath + "/lib/" + name + ".jar"), "jar", reference);
                }
                ccs.add(resolver.resolve(reference, null).getAvailable().get(0).createPackage().getMainJarSource());
            }
            SystemDirectory jars = new SystemDirectory(FileUtil.createTemporaryDirectory("jboss-", "-jars", true));
            ccs.add(new VirtualDirectoryClassSource(jars));
            jars.createFile("jms-ra.rar", VirtualArtifactUtil.getResource(EmbeddableLifecycle.class, "jms-ra.rar").getURL());
            jars.createFile("jcainflow.rar", VirtualArtifactUtil.getResource(EmbeddableLifecycle.class, "jcainflow.rar").getURL());
            container.vroot.createFile(EmbeddableLifecycle.class.getResource("conf/jndi.properties"));
            container.vroot.createFile(EmbeddableLifecycle.class.getResource("conf/login-config.xml"));
            container.vroot.createFile(EmbeddableLifecycle.class.getResource("conf/default.persistence.properties"));
            ProjectObjectModel pom = ProjectObjectModel.parse(resolver, container.vroot.createFile(EmbeddableLifecycle.class.getResource("lib/boot-pom.xml")));
            container.context = new ClassContext(new StandardClassLoaderHeiarchy(), MavenRepositoryUtil.createFrom(ClassPackageContext.getPackageResolver(), pom), ccs);
            container.setDeploymentClassLoader(container.context);
        }
    }

    @Override
    protected void startContainer() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(container.context);
            container.embedded = ReflectiveObjectProxy.createProxy(container.context, EJB3EmbeddedStandaloneRC9.class, false);
            container.embedded.createKernel();
            if (MBeanServerFactory.findMBeanServer(null).size() != 0) {
                EJB3EmbeddableMBeanServerLocator mbsl = ReflectiveObjectProxy.createProxy(container.context, EJB3EmbeddableMBeanServerLocator.class, false);
                mbsl.setJBoss((MBeanServer) MBeanServerFactory.findMBeanServer(null).get(0));
            } else {
                container.embedded.loadMBeanServer();
            }
            container.embedded.boot(resourcePrefix);
            container.embedded.deployXmlResource(resourcePrefix + "/jboss-jms-beans.xml");
            container.embedded.deployXmlResource(resourcePrefix + "/security-beans.xml");
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    @Override
    protected void stopContainer() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(container.context);
            try {
                container.embedded.shutdown();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            container.context.cleanup();
            container.root.remove();
            container.root = null;
            container.context = null;
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    public void shutdown() {
        if (this.currentPhase.getType() == Type.Started) {
            this.stop();
        }
    }
}
