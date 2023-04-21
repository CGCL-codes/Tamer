package org.xvr.xvrengine.wizard;

import java.io.ByteArrayInputStream;
import java.net.URI;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.xvr.xvrengine.util.XVRUtils;
import org.xvr.xvrengine.wizard.html.HTMLPropertiesPage;
import org.xvr.xvrengine.wizard.pages.XVRWizardBaseProjectPage;
import org.xvr.xvrengine.wizard.pages.XVRWizardBaseProjectPage.HTMLType;
import xvrengine.XVRPlugin;
import xvrengine.ui.XVRProjectSupport;

/**
 * @author Raffaello
 *
 */
public class EmptyWizard extends Wizard implements INewWizard {

    protected static final String HTML_PAGE_NAME = "Additional Settings";

    protected static final String HTML_PROP_TITLE = "HTML settings page";

    protected static final String HTML_PROP_DESCRIPTION = "Select which settings you prefer for the new html page(s) that will be created.";

    protected static final String HTML_DEFAULT_FILE_NAME = "index.html";

    protected static final String HTML_DEFAULT_DEBUG_FILE_NAME = "index_debug.html";

    protected XVRWizardBaseProjectPage page1;

    protected HTMLPropertiesPage page2;

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.initPrjPage();
        this.initHTMLPage();
        this.addPage(this.page1);
        this.addPage(this.page2);
    }

    protected void initHTMLPage() {
        this.page2 = new HTMLPropertiesPage(HTML_PAGE_NAME);
        this.page2.setTitle(HTML_PROP_TITLE);
        this.page2.setDescription(HTML_PROP_DESCRIPTION);
    }

    protected void initPrjPage() {
        this.page1 = new XVRWizardBaseProjectPage("XVR Wizard", "Create a new empty project", XVRPlugin.getImageDescriptor("icons/xvr48x48.bmp"));
    }

    @Override
    public boolean performFinish() {
        IProject n_prj = this.createPrj();
        if (n_prj == null) return false;
        HTMLType type = this.page1.getHTMLType();
        IFile st, dyn;
        try {
            switch(type) {
                case BOTH:
                    st = n_prj.getFile(HTML_DEFAULT_FILE_NAME);
                    dyn = n_prj.getFile(HTML_DEFAULT_DEBUG_FILE_NAME);
                    st.create(new ByteArrayInputStream(this.page2.getStaticHtmlDocument().getBytes()), true, new NullProgressMonitor());
                    dyn.create(new ByteArrayInputStream(this.page2.getDynamicHtml().getBytes()), true, new NullProgressMonitor());
                    break;
                case STATIC:
                    st = n_prj.getFile(HTML_DEFAULT_FILE_NAME);
                    st.create(new ByteArrayInputStream(this.page2.getStaticHtmlDocument().getBytes()), true, new NullProgressMonitor());
                    break;
                case DYNAMIC:
                    dyn = n_prj.getFile(HTML_DEFAULT_DEBUG_FILE_NAME);
                    dyn.create(new ByteArrayInputStream(this.page2.getDynamicHtml().getBytes()), true, new NullProgressMonitor());
                    XVRUtils.setBuilderProperty(n_prj, XVRProjectSupport.ARG_MAIN_HTML_KEY, HTML_DEFAULT_DEBUG_FILE_NAME);
                    break;
                default:
                    break;
            }
        } catch (CoreException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {
        if (this.page1.equals(page)) {
            String p_name = this.page1.getProjectName();
            String active_s3d = XVRProjectSupport.FOLDER_SRC + "/" + p_name.concat(XVRProjectSupport.S3D_EXTENSION);
            this.page2.setDefaults(p_name, active_s3d);
        }
        return super.getNextPage(page);
    }

    protected IProject createPrj() {
        String name = page1.getProjectName();
        URI location = null;
        if (!page1.isDefaultLocation()) {
            location = page1.getLocationURI();
        }
        return XVRProjectSupport.createEmptyProject(name, location, this.page1.isActiveProject());
    }
}
