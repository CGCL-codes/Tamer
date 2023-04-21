package org.monet.modelling.application;

import java.net.URL;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.ide.IDEInternalWorkbenchImages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.registry.ActionSetRegistry;
import org.eclipse.ui.internal.registry.IActionSetDescriptor;
import org.osgi.framework.Bundle;

/**
 * Methods copied from {@link org.eclipse.ui.internal.ide.IDEWorkbenchAdvisor}
 */
public abstract class WorkbenchAdvisorHack extends WorkbenchAdvisor {

    @Override
    public void preStartup() {
        IDE.registerAdapters();
        ActionSetRegistry reg = WorkbenchPlugin.getDefault().getActionSetRegistry();
        IActionSetDescriptor[] actionSets = reg.getActionSets();
        String[] removeActionSets = new String[] { "org.eclipse.ui.cheatsheets.actionSet", "org.eclipse.ui.edit.text.actionSet.annotationNavigation", "org.eclipse.ui.edit.text.actionSet.convertLineDelimitersTo", "org.eclipse.ui.WorkingSetActionSet", "org.eclipse.update.ui.softwareUpdates" };
        for (int i = 0; i < actionSets.length; i++) {
            boolean found = false;
            for (int j = 0; j < removeActionSets.length; j++) {
                if (removeActionSets[j].equals(actionSets[i].getId())) found = true;
            }
            if (!found) continue;
            IExtension ext = actionSets[i].getConfigurationElement().getDeclaringExtension();
            reg.removeExtension(ext, new Object[] { actionSets[i] });
        }
    }

    @Override
    public void initialize(IWorkbenchConfigurer configurer) {
        configurer.setSaveAndRestore(true);
        declareWorkbenchImages();
    }

    @Override
    public IAdaptable getDefaultPageInput() {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        return workspace.getRoot();
    }

    /**
	 * Declares all IDE-specific workbench images. This includes both "shared"
	 * images (named in {@link IDE.SharedImages}) and internal images (named in
	 * {@link org.eclipse.ui.internal.ide.IDEInternalWorkbenchImages}).
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
        Bundle ideBundle = Platform.getBundle(IDEWorkbenchPlugin.IDE_WORKBENCH);
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
	 *            the path of the image file; this path is relative to the base
	 *            of the IDE plug-in
	 * @param shared
	 *            <code>true</code> if this is a shared image, and
	 *            <code>false</code> if this is not a shared image
	 * @see org.eclipse.ui.internal.ide.IDEWorkbenchAdvisor#declareImage
	 */
    private void declareWorkbenchImage(Bundle ideBundle, String symbolicName, String path, boolean shared) {
        URL url = FileLocator.find(ideBundle, new Path(path), null);
        ImageDescriptor desc = ImageDescriptor.createFromURL(url);
        getWorkbenchConfigurer().declareImage(symbolicName, desc, shared);
    }
}
