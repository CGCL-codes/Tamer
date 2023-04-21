package com.anaxima.eslink.ui;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;
import com.anaxima.eslink.EslinkPlugin;
import com.anaxima.eslink.IEslinkPluginConstants;
import com.anaxima.eslink.internal.ui.buildpath.BuildPathsBlock;
import com.anaxima.eslink.model.core.EslinkModelException;
import com.anaxima.eslink.model.core.IEslinkProject;
import com.anaxima.eslink.model.core.IEslinkSession;
import com.anaxima.eslink.model.internal.core.EslinkModel;

/**
 * Property page to configure the catalogs and libraries used
 * by a Eslink project.
 * 
 * @author Thomas Vater
 */
public class BuildPathPropertyPage extends PropertyPage implements IEslinkPluginConstants {

    private static com.anaxima.eslink.tools.log.ILogger log = EslinkPlugin.getLogger();

    /**
     * Element containing the build path UI. 
     */
    private BuildPathsBlock _buildPathsBlock;

    /**
     * Project we are working with.
     */
    private IEslinkProject _slinkProject;

    /**
     * Project resource we are working with.
     */
    private IProject _project;

    /**
     * Old output container.
     */
    private IContainer _oldOut;

    /**
     * Default contructor.
     */
    public BuildPathPropertyPage() {
        super();
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents(Composite argParent) {
        if (log.isDebugEnabled()) log.debug("createContents() invoked");
        noDefaultAndApplyButton();
        _buildPathsBlock = new BuildPathsBlock(_slinkProject, 0);
        Control ret = _buildPathsBlock.createControl(argParent);
        _oldOut = _buildPathsBlock.getOutputContainer();
        return ret;
    }

    /**
     * @see org.eclipse.jface.preference.IPreferencePage#performOk()
     */
    public boolean performOk() {
        IContainer newOut = _buildPathsBlock.getOutputContainer();
        if (!newOut.equals(_oldOut)) {
            BuildPathPropertyPage.checkAndStopSession();
            IEslinkProject project = (IEslinkProject) getElement();
            project.setOutputContainer(newOut);
            try {
                project.storeConfig();
            } catch (EslinkModelException sme) {
                EslinkPlugin.showErrorDialog("eslinkConf.error.storeing.title", "eslinkConf.error.storeing.msg", null, sme);
            }
        }
        return super.performOk();
    }

    /**
     * @see org.eclipse.ui.dialogs.PropertyPage#getElement()
     */
    public IAdaptable getElement() {
        return _slinkProject;
    }

    /**
     * @see org.eclipse.ui.dialogs.PropertyPage#setElement(org.eclipse.core.runtime.IAdaptable)
     */
    public void setElement(IAdaptable argElement) {
        _project = (IProject) argElement.getAdapter(IProject.class);
        _slinkProject = (IEslinkProject) argElement.getAdapter(IEslinkProject.class);
    }

    /**
     * Checks if there is a Eslink session running.
     * If yes the session is killed to allow for new settings.
     */
    public static void checkAndStopSession() {
        IEslinkSession session = EslinkModel.getInstance().getEslinkSession();
        if (session.isActive()) {
            boolean kill = MessageDialog.openQuestion(EslinkPlugin.getPlugin().getShell(), EslinkPlugin.getMessage("eslinkConf.buildpath.restartsession.confirm.title"), EslinkPlugin.getMessage("eslinkConf.buildpath.restartsession.confirm"));
            if (kill) {
                try {
                    session.disposeFacade();
                } catch (CoreException ce) {
                    EslinkPlugin.showErrorDialog("eslinkConf.buildpath.restartsession.error.title", "eslinkConf.buildpath.restartsession.error", null, ce);
                }
            }
        }
    }
}
