package org.kalypso.nofdpidss.ui;

import java.net.URL;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.IDEInternalWorkbenchImages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.model.WorkbenchAdapterBuilder;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.NavigatorRoot;
import org.kalypso.nofdpidss.core.base.ProjectUtils;
import org.kalypso.nofdpidss.core.base.WorkspaceSync;
import org.kalypso.nofdpidss.core.common.NofdpIDSSConstants;
import org.kalypso.nofdpidss.core.common.NofdpIdssNatureProjectGlobal;
import org.osgi.framework.Bundle;

@SuppressWarnings("restriction")
public class MyWorkbenchAdvisor extends WorkbenchAdvisor {

    @Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
        return new MyWorkbenchWindowAdvisor(configurer);
    }

    @Override
    public void postStartup() {
        super.postStartup();
        final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        final IProject[] projects = root.getProjects();
        WorkspaceSync.sync(root, IResource.DEPTH_INFINITE);
        for (final IProject project : projects) try {
            if (!project.isOpen()) {
                project.open(null);
                WorkspaceSync.sync(project, IResource.DEPTH_INFINITE);
            }
            final IProjectNature nature = project.getNature(NofdpIdssNatureProjectGlobal.ID);
            if (nature != null) NofdpCorePlugin.getProjectManager().setGlobalProject(project);
        } catch (final CoreException e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
        }
        if (NofdpCorePlugin.getProjectManager().getBaseProject() == null) {
            ProjectUtils.createGlobalProject();
            final IProject globalProject = NofdpCorePlugin.getProjectManager().getBaseProject();
            try {
                globalProject.open(null);
                WorkspaceSync.sync(globalProject, IResource.DEPTH_INFINITE);
            } catch (final CoreException e) {
                NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            }
        }
        final IWorkspace workspace = root.getWorkspace();
        workspace.addResourceChangeListener(new IResourceChangeListener() {

            public void resourceChanged(final IResourceChangeEvent event) {
                final IResource resource = event.getResource();
                if (resource != null) WorkspaceSync.sync(resource, IResource.DEPTH_ONE);
            }
        });
    }

    @Override
    public boolean preShutdown() {
        NofdpCorePlugin.getProjectManager().saveGmlWorkspaces();
        final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page != null) page.closeAllPerspectives(false, true);
        return super.preShutdown();
    }

    /**
   * @see org.kalypso.contribs.eclipse.ui.ide.application.IDEWorkbenchAdvisor#getInitialWindowPerspectiveId()
   */
    @Override
    public String getInitialWindowPerspectiveId() {
        return NofdpIDSSConstants.NOFDP_PERSPECTIVE_NULL;
    }

    /**
   * @see org.eclipse.ui.application.WorkbenchAdvisor#initialize(org.eclipse.ui.application.IWorkbenchConfigurer)
   */
    @Override
    public void initialize(final IWorkbenchConfigurer configurer) {
        configurer.setSaveAndRestore(true);
        declareWorkbenchImages();
    }

    /**
   * Declares all IDE-specific workbench images. This includes both "shared" images (named in {@link IDE.SharedImages})
   * and internal images (named in {@link org.eclipse.ui.internal.ide.IDEInternalWorkbenchImages}).
   * 
   * @see org.eclipse.ui.internal.ide.IDEWorkbenchAdvisor#declareImage
   */
    protected void declareWorkbenchImages() {
        final String ICONS_PATH = "$nl$/icons/full/";
        final String PATH_ELOCALTOOL = ICONS_PATH + "elcl16/";
        final String PATH_DLOCALTOOL = ICONS_PATH + "dlcl16/";
        final String PATH_ETOOL = ICONS_PATH + "etool16/";
        final String PATH_DTOOL = ICONS_PATH + "dtool16/";
        final String PATH_OBJECT = ICONS_PATH + "obj16/";
        final String PATH_WIZBAN = ICONS_PATH + "wizban/";
        final Bundle ideBundle = Platform.getBundle(IDEWorkbenchPlugin.IDE_WORKBENCH);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_BUILD_EXEC, PATH_ETOOL + "build_exec.gif", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_BUILD_EXEC_HOVER, PATH_ETOOL + "build_exec.gif", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_BUILD_EXEC_DISABLED, PATH_DTOOL + "build_exec.gif", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_SEARCH_SRC, PATH_ETOOL + "search_src.gif", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_SEARCH_SRC_HOVER, PATH_ETOOL + "search_src.gif", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_SEARCH_SRC_DISABLED, PATH_DTOOL + "search_src.gif", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_NEXT_NAV, PATH_ETOOL + "next_nav.gif", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_PREVIOUS_NAV, PATH_ETOOL + "prev_nav.gif", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_WIZBAN_NEWPRJ_WIZ, PATH_WIZBAN + "newprj_wiz.png", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_WIZBAN_NEWFOLDER_WIZ, PATH_WIZBAN + "newfolder_wiz.png", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_WIZBAN_NEWFILE_WIZ, PATH_WIZBAN + "newfile_wiz.png", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_WIZBAN_IMPORTDIR_WIZ, PATH_WIZBAN + "importdir_wiz.png", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_WIZBAN_IMPORTZIP_WIZ, PATH_WIZBAN + "importzip_wiz.png", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_WIZBAN_EXPORTDIR_WIZ, PATH_WIZBAN + "exportdir_wiz.png", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_WIZBAN_EXPORTZIP_WIZ, PATH_WIZBAN + "exportzip_wiz.png", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_WIZBAN_RESOURCEWORKINGSET_WIZ, PATH_WIZBAN + "workset_wiz.png", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_DLGBAN_SAVEAS_DLG, PATH_WIZBAN + "saveas_wiz.png", false);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_DLGBAN_QUICKFIX_DLG, PATH_WIZBAN + "quick_fix.png", false);
        declareWorkbenchImage(ideBundle, IDE.SharedImages.IMG_OBJ_PROJECT, PATH_OBJECT + "prj_obj.gif", true);
        declareWorkbenchImage(ideBundle, IDE.SharedImages.IMG_OBJ_PROJECT_CLOSED, PATH_OBJECT + "cprj_obj.gif", true);
        declareWorkbenchImage(ideBundle, IDE.SharedImages.IMG_OPEN_MARKER, PATH_ELOCALTOOL + "gotoobj_tsk.gif", true);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ELCL_QUICK_FIX_ENABLED, PATH_ELOCALTOOL + "smartmode_co.gif", true);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_DLCL_QUICK_FIX_DISABLED, PATH_DLOCALTOOL + "smartmode_co.gif", true);
        declareWorkbenchImage(ideBundle, IDE.SharedImages.IMG_OBJS_TASK_TSK, PATH_OBJECT + "taskmrk_tsk.gif", true);
        declareWorkbenchImage(ideBundle, IDE.SharedImages.IMG_OBJS_BKMRK_TSK, PATH_OBJECT + "bkmrk_tsk.gif", true);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_COMPLETE_TSK, PATH_OBJECT + "complete_tsk.gif", true);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_INCOMPLETE_TSK, PATH_OBJECT + "incomplete_tsk.gif", true);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_WELCOME_ITEM, PATH_OBJECT + "welcome_item.gif", true);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_WELCOME_BANNER, PATH_OBJECT + "welcome_banner.gif", true);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_ERROR_PATH, PATH_OBJECT + "error_tsk.gif", true);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_WARNING_PATH, PATH_OBJECT + "warn_tsk.gif", true);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_INFO_PATH, PATH_OBJECT + "info_tsk.gif", true);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_LCL_FLAT_LAYOUT, PATH_ELOCALTOOL + "flatLayout.gif", true);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_LCL_HIERARCHICAL_LAYOUT, PATH_ELOCALTOOL + "hierarchicalLayout.gif", true);
        declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_PROBLEM_CATEGORY, PATH_ETOOL + "problem_category.gif", true);
    }

    /**
   * Declares an IDE-specific workbench image.
   * 
   * @param symbolicName
   *            the symbolic name of the image
   * @param path
   *            the path of the image file; this path is relative to the base of the IDE plug-in
   * @param shared
   *            <code>true</code> if this is a shared image, and <code>false</code> if this is not a shared image
   * @see org.eclipse.ui.internal.ide.IDEWorkbenchAdvisor#declareImage
   */
    private void declareWorkbenchImage(final Bundle ideBundle, final String symbolicName, final String path, final boolean shared) {
        final URL url = FileLocator.find(ideBundle, new Path(path), null);
        final ImageDescriptor desc = ImageDescriptor.createFromURL(url);
        getWorkbenchConfigurer().declareImage(symbolicName, desc, shared);
    }

    /**
   * @see org.eclipse.ui.application.WorkbenchAdvisor#getDefaultPageInput()
   */
    @Override
    public IAdaptable getDefaultPageInput() {
        return new NavigatorRoot();
    }

    /**
   * @see org.eclipse.ui.application.WorkbenchAdvisor#preStartup()
   */
    @Override
    public void preStartup() {
        WorkbenchAdapterBuilder.registerAdapters();
    }
}
