package org.in4ama.editor.projectdesigner.localfs;

import net.xoetrope.xui.XProjectManager;
import org.in4ama.editor.projectdesigner.XuiProjectDesigner;

/** Local file system project designer. */
public class LocalFsProjectDesigner extends XuiProjectDesigner {

    public static final String NAME = "project.designer.localfs";

    private boolean installed = false;

    /** Returns the name of this designer. */
    public String getName() {
        return NAME;
    }

    public String getDescription() {
        return LocalFsProjectPage.NAME;
    }

    /** Indicates whether this plugin has been already installed. */
    public boolean isInstalled() {
        return installed;
    }

    /** Returns the page providing components for the 
	 * project configuration on the local file system. */
    @Override
    protected LocalFsProjectPage createPage() {
        return (LocalFsProjectPage) XProjectManager.getCurrentProject().getPageManager().loadPage("localfsprojectconfig");
    }
}
