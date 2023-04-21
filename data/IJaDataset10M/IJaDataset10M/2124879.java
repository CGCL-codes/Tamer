package deprecated;

import mySBML.KineticLaw;
import mySBML.Model;
import mySBML.SBase;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

public class ParameterEditor extends FormEditor {

    public static SBase modelSlot;

    public static final String ID = "ID_ParameterEditor";

    private SBase model;

    private ParameterPage parameterPage;

    public ParameterEditor() {
        this.model = modelSlot;
        setPartName("Global Parameters");
    }

    public Model getModel() {
        if (model instanceof Model) return (Model) model;
        if (model instanceof KineticLaw) return ((KineticLaw) model).getModel();
        return null;
    }

    @Override
    protected void addPages() {
        try {
            if (model instanceof KineticLaw) parameterPage = new ParameterPage(this, (KineticLaw) model); else parameterPage = new ParameterPage(this, model.getModel());
            addPage(parameterPage);
        } catch (PartInitException e) {
        }
    }

    protected void createPages() {
        super.createPages();
        if (getPageCount() == 1 && getContainer() instanceof CTabFolder) {
            ((CTabFolder) getContainer()).setTabHeight(0);
        }
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public void dispose() {
        if (model instanceof KineticLaw) for (int i = 0; i < ((KineticLaw) model).getParameters().size(); i++) ((KineticLaw) model).getParameters().get(i).deleteObserver(parameterPage); else for (int i = 0; i < model.getModel().getParameters().size(); i++) model.getModel().getParameters().get(i).deleteObserver(parameterPage);
        super.dispose();
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }
}
