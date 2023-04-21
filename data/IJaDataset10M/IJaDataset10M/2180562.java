package org.nexopenframework.ide.eclipse.jst.datamodel.ear;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.common.project.facet.WtpUtils;
import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.datamodel.FacetDataModelProvider;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.web.ui.internal.Logger;
import org.nexopenframework.ide.eclipse.jst.datamodel.INexOpenFacetInstallDataModelProperties;
import org.nexopenframework.ide.eclipse.ui.NexOpenUIActivator;
import org.nexopenframework.ide.eclipse.ui.util.NexOpenProjectUtils;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Install delegate for NexOpen features. Internally creates
 * the Maven2 structure for dealing with NexOpen EAR projects</p>
 * 
 * @see IDelegate
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class NexOpenFacetInstallDelegate implements IDelegate {

    /**
	 * <p>Creates the structure for NexOpen-Maven2 EAR projects</p>
	 * 
	 * @see org.eclipse.wst.common.project.facet.core.IDelegate#execute(org.eclipse.core.resources.IProject, org.eclipse.wst.common.project.facet.core.IProjectFacetVersion, java.lang.Object, org.eclipse.core.runtime.IProgressMonitor)
	 */
    public void execute(final IProject project, final IProjectFacetVersion fv, final Object cfg, final IProgressMonitor monitor) throws CoreException {
        if (monitor != null) {
            monitor.beginTask("", 1);
        }
        try {
            {
                QualifiedName qn = new QualifiedName(NexOpenUIActivator.PLUGIN_ID, NexOpenProjectUtils.NEXOPEN_VERSION_KEY);
                project.setPersistentProperty(qn, NexOpenUIActivator.getDefault().getNexOpenVersion());
            }
            IDataModel model = (IDataModel) cfg;
            final IWorkspace ws = ResourcesPlugin.getWorkspace();
            final IPath pjpath = project.getFullPath();
            IJavaProject jproject = null;
            if (project.exists()) {
                jproject = JavaCore.create(project);
            }
            WtpUtils.addNaturestoEAR(project);
            if (jproject != null) {
                List collEntries = new ArrayList();
                IClasspathEntry entries[] = jproject.getRawClasspath();
                collEntries.add(entries[0]);
                String businessResources = model.getStringProperty(IJ2EEModuleFacetInstallDataModelProperties.CONFIG_FOLDER);
                final IPath srcbrdir = pjpath.append(businessResources);
                if (!srcbrdir.equals(pjpath)) {
                    ws.getRoot().getFolder(srcbrdir).getLocation().toFile().mkdirs();
                    collEntries.add(JavaCore.newSourceEntry(srcbrdir));
                }
                String testJava = model.getStringProperty(INexOpenFacetInstallDataModelProperties.BUSINESS_TEST_JAVA_FOLDER);
                final IPath srctjdir = pjpath.append(testJava);
                if (!srctjdir.equals(pjpath)) {
                    ws.getRoot().getFolder(srctjdir).getLocation().toFile().mkdirs();
                    collEntries.add(JavaCore.newSourceEntry(srctjdir));
                }
                String testResources = model.getStringProperty(INexOpenFacetInstallDataModelProperties.BUSINESS_TEST_RESOURCES_FOLDER);
                final IPath srctrdir = pjpath.append(testResources);
                if (!srctrdir.equals(pjpath)) {
                    ws.getRoot().getFolder(srctrdir).getLocation().toFile().mkdirs();
                    collEntries.add(JavaCore.newSourceEntry(srctrdir, new Path[] { new Path("**") }, srctrdir));
                }
                String ejbResources = model.getStringProperty(INexOpenFacetInstallDataModelProperties.EJB_MAIN_RESOURCES_FOLDER);
                final IPath srcerdir = pjpath.append(ejbResources);
                if (!srcerdir.equals(pjpath)) {
                    ws.getRoot().getFolder(srcerdir).getLocation().toFile().mkdirs();
                    collEntries.add(JavaCore.newSourceEntry(srcerdir));
                }
                String webJava = model.getStringProperty(INexOpenFacetInstallDataModelProperties.WEB_MAIN_JAVA_FOLDER);
                final IPath srcwjdir = pjpath.append(webJava);
                if (!srcwjdir.equals(pjpath)) {
                    ws.getRoot().getFolder(srcwjdir).getLocation().toFile().mkdirs();
                    collEntries.add(JavaCore.newSourceEntry(srcwjdir));
                }
                String webResources = model.getStringProperty(INexOpenFacetInstallDataModelProperties.WEB_MAIN_RESOURCES_FOLDER);
                final IPath srcwrdir = pjpath.append(webResources);
                if (!srcwrdir.equals(pjpath)) {
                    ws.getRoot().getFolder(srcwrdir).getLocation().toFile().mkdirs();
                    collEntries.add(JavaCore.newSourceEntry(srcwrdir));
                }
                String webApp = model.getStringProperty(INexOpenFacetInstallDataModelProperties.WEB_TEST_JAVA_FOLDER);
                final IPath srcwtdir = pjpath.append(webApp);
                if (!srcwtdir.equals(pjpath)) {
                    ws.getRoot().getFolder(srcwtdir).getLocation().toFile().mkdirs();
                    collEntries.add(JavaCore.newSourceEntry(srcwtdir));
                }
                String webResApp = model.getStringProperty(INexOpenFacetInstallDataModelProperties.WEB_TEST_RESOURCES_FOLDER);
                if (webResApp != null) {
                    final IPath srcwtrdir = pjpath.append(webResApp);
                    if (!srcwtdir.equals(pjpath)) {
                        ws.getRoot().getFolder(srcwtrdir).getLocation().toFile().mkdirs();
                        collEntries.add(JavaCore.newSourceEntry(srcwtrdir, new Path[] { new Path("**") }, srcwtrdir));
                    }
                }
                for (int k = 1; k < entries.length; k++) {
                    collEntries.add(entries[k]);
                }
                try {
                    final IClasspathEntry[] cp = (IClasspathEntry[]) collEntries.toArray(new IClasspathEntry[collEntries.size()]);
                    jproject.setRawClasspath(cp, null);
                } catch (JavaModelException e) {
                    Logger.log(Logger.INFO, "Java Model Exception. In the setRawClasspath method :: " + e);
                }
            }
            final IVirtualComponent nexopen = ComponentCore.createComponent(project);
            nexopen.create(0, null);
            if (nexopen != null) {
                try {
                    nexopen.setMetaProperty("context-root", project.getName());
                } catch (NullPointerException e) {
                    Logger.log(Logger.INFO, "NullPointer adding meta property context-root");
                }
            }
            try {
                final IDataModelOperation operation = ((IDataModelOperation) model.getProperty(FacetDataModelProvider.NOTIFICATION_OPERATION));
                final IStatus status = operation.execute(monitor, null);
                Logger.log(Logger.INFO, "Status :: " + status);
            } catch (ExecutionException e) {
                Logger.log(Logger.ERROR, "ExecutionException", e);
            }
            if (monitor != null) {
                monitor.worked(1);
            }
        } catch (JavaModelException e) {
            Logger.log(Logger.ERROR, "Java Model Exception. Exception :: " + e);
            throw e;
        } finally {
            if (monitor != null) {
                monitor.done();
            }
        }
    }
}
