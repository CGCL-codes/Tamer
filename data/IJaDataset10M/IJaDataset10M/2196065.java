package cz.vse.keg.patomat.xd.transformation;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.stlab.xd.core.XDCorePlugin;
import org.stlab.xd.manager.ResourceProviderFilter;
import it.cnr.stlab.xd.plugin.widgets.IOntologyResourceSelection;
import it.cnr.stlab.xd.plugin.widgets.IOntologyResourcesSelectionPage;
import it.cnr.stlab.xd.plugin.widgets.Method;
import it.cnr.stlab.xd.plugin.widgets.OntologyResourceSelectionWidget;
import it.cnr.stlab.xd.plugin.widgets.XDControlsProvider;

public class IntroPage extends WizardPage implements IOntologyResourcesSelectionPage {

    protected TransformationModel tmodel;

    protected TransformationWizard twizard;

    protected Text tpURIField = null;

    protected IOntologyResourceSelection ontologyResourceSelection = null;

    protected IntroPage(String pageName, TransformationWizard tw, TransformationModel tm) {
        super(pageName);
        setTitle("Transformation");
        setDescription("Intro page");
        this.tmodel = tm;
        this.twizard = tw;
    }

    @Override
    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(1, false);
        composite.setLayout(layout);
        composite.setLayoutData(new GridData());
        XDControlsProvider.createBoldLabel(composite, "CODP to be imported:");
        Label lbl = XDControlsProvider.createSimpleLabel(composite);
        lbl.setText(this.tmodel.codpIRI);
        lbl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        XDControlsProvider.createBoldLabel(composite, "Select existing project and ontology on which apply adaptive import (i.e. transformation):");
        ontologyResourceSelection = new OntologyResourceSelectionWidget(composite, this, new ResourceProviderFilter() {

            @Override
            public boolean isValid(IResource resource) {
                if (resource instanceof IProject) {
                    String pn = ((IProject) resource).getName();
                    boolean passit = false;
                    for (Object o : XDCorePlugin.getManager().getProjects()) {
                        if (o instanceof IProject) {
                            if (o.equals(resource)) {
                                passit = true;
                                break;
                            }
                        } else if (o instanceof String) {
                            if (((String) o).equals(pn)) {
                                passit = true;
                                break;
                            }
                        }
                    }
                    return passit;
                }
                return true;
            }
        });
        XDControlsProvider.createBoldLabel(composite, "Transformation pattern to br import:");
        tpURIField = XDControlsProvider.createURIField(composite);
        tpURIField.setText("");
        tpURIField.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                tmodel.ontologyIRI = tpURIField.getText();
            }
        });
        setControl(composite);
    }

    @Override
    public Method getMethod() {
        return Method.CREATE_AND_IMPORT;
    }

    @Override
    public Text getNewOntologyUriField() {
        return null;
    }

    @Override
    public Object getOntologyProject() {
        return tmodel.outputProjectString;
    }

    @Override
    public Object getOntologyResource() {
        return tmodel.ontologyIRI;
    }

    @Override
    public void setOntologyProject(Object prj) {
        tmodel.outputProjectString = (String) prj;
    }

    @Override
    public void setOntologyResource(Object resource) {
        tmodel.ontologyImportedIRI = (String) resource;
    }

    @Override
    public boolean validatePage() {
        return false;
    }

    public boolean canFlipToNextPage() {
        if (getErrorMessage() != null) return false; else if (validURI(ontologyResourceSelection.getOntologyResourceIRI().toString())) {
            return true;
        } else return false;
    }

    private static boolean validURI(String s) {
        if ((s.matches("^http.*")) || (s.matches("^file.*"))) return true;
        return false;
    }
}
