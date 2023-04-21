package org.zkoss.eclipse.setting.zklib.wst;

import java.io.IOException;
import java.io.InputStreamReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.zkoss.eclipse.setting.Ref;
import org.zkoss.eclipse.setting.zklib.archive.IArchiveEntry;
import org.zkoss.eclipse.setting.zklib.archive.IContentVisitor;
import org.zkoss.eclipse.setting.zklib.archive.IZkWebArchive;
import org.zkoss.eclipse.setting.zklib.archive.ZkPackage;

/**
 * @author Ian Tsai
 *
 */
public class ZkPackageManageFacetInstallDelegate implements IDelegate {

    public void execute(final IProject project, IProjectFacetVersion version, Object conf, final IProgressMonitor pMonitor) throws CoreException {
        ZkPackageManageFacetInstallConfig config = (ZkPackageManageFacetInstallConfig) conf;
        try {
            ZkPackage zPack = config.getZkPackage();
            ProjectZkFrameManager appender = new ProjectZkFrameManager(project, config.getPackageRepository());
            appender.appendZk(zPack, pMonitor, true);
            config.getPackageRepository().closeAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
