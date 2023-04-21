package org.mobicents.eclipslee.servicecreation.wizards.ra;

import java.util.HashMap;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.ResourceAdaptorRaTypesPanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.ResourceAdaptorTypeFinder;
import org.mobicents.eclipslee.servicecreation.wizards.WizardChangeListener;
import org.mobicents.eclipslee.servicecreation.wizards.generic.FilenamePage;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorRaTypesPage extends WizardPage implements WizardChangeListener {

    private static final String PAGE_DESCRIPTION = "Select the resource adaptor types that this Resource implements.";

    /**
   * @param pageName
   */
    public ResourceAdaptorRaTypesPage(String title) {
        super("wizardPage");
        setTitle(title);
        setDescription(PAGE_DESCRIPTION);
    }

    public void createControl(Composite parent) {
        ResourceAdaptorRaTypesPanel panel = new ResourceAdaptorRaTypesPanel(parent, SWT.NONE, this);
        setControl(panel);
        dialogChanged();
    }

    public void onWizardPageChanged(WizardPage page) {
        if (getControl() == null) {
            return;
        }
        if (page instanceof FilenamePage) {
            if (((FilenamePage) page).getSourceContainer() == null) {
                return;
            }
            String projectName = ((FilenamePage) page).getSourceContainer().getProject().getName();
            if (projectName == null || projectName.equals(project)) {
                return;
            }
            this.project = projectName;
            try {
                getContainer().run(true, true, new IRunnableWithProgress() {

                    public void run(IProgressMonitor monitor) {
                        monitor.beginTask("Retrieving available JAIN SLEE Components ...", 100);
                        initialize();
                        monitor.done();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initialize() {
        if (project == null) {
            return;
        }
        getShell().getDisplay().asyncExec(new Runnable() {

            public void run() {
                ResourceAdaptorRaTypesPanel panel = (ResourceAdaptorRaTypesPanel) getControl();
                panel.clearRaTypes();
                DTDXML xml[] = ResourceAdaptorTypeFinder.getDefault().getComponents(BaseFinder.ALL, project);
                for (int i = 0; i < xml.length; i++) {
                    ResourceAdaptorTypeJarXML rat = (ResourceAdaptorTypeJarXML) xml[i];
                    ResourceAdaptorTypeXML[] raTypes = rat.getResourceAdaptorTypes();
                    for (int j = 0; j < raTypes.length; j++) {
                        panel.addResourceAdaptorType(rat, raTypes[j]);
                    }
                }
                panel.repack();
            }
        });
    }

    public void dialogChanged() {
        IWizard wizard = getWizard();
        if (wizard instanceof ResourceAdaptorWizard) {
            ((ResourceAdaptorWizard) wizard).pageChanged(this);
        }
        updateStatus(null);
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public HashMap[] getSelectedRaTypes() {
        ResourceAdaptorRaTypesPanel panel = (ResourceAdaptorRaTypesPanel) getControl();
        return panel.getSelectedRaTypes();
    }

    private String project;
}
