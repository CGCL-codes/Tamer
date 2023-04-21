package yaw.core.swt.wizards.generator.pages;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import yaw.core.controller.GeneratorMgr;
import yaw.core.swt.va.ControlMediator;
import yaw.core.swt.va.DynamicForm;
import yaw.core.swt.va.IControlMediatorListener;
import yaw.core.va.ValueOwnerAdapter;

public class ParameterPage extends WizardPage {

    public static final String PAGENAME = "Parameter";

    private GeneratorMgr mgr;

    private DynamicForm form;

    public ParameterPage(GeneratorMgr generatorMgr) {
        super(PAGENAME);
        setTitle("Parameter");
        this.mgr = generatorMgr;
    }

    public void createControl(Composite parent) {
        form = new DynamicForm(parent);
        form.getControlMediatorMgr().addListener(new IControlMediatorListener() {

            public void onChange(ControlMediator controlMediator) {
                String check = controlMediator.getMgr().check();
                setErrorMessage(check);
                setPageComplete(check == null);
            }
        });
        setControl(form.getControl());
    }

    public void init() {
        mgr.createProcessor();
        form.init(new ValueOwnerAdapter(mgr.getModel()));
    }

    @Override
    public boolean canFlipToNextPage() {
        return isPageComplete();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) mgr.clearTransaction();
        super.setVisible(visible);
    }

    @Override
    public boolean isPageComplete() {
        return this.form != null && this.form.check() == null;
    }

    @Override
    public IWizardPage getNextPage() {
        this.form.get();
        mgr.generate();
        if (mgr.isError()) {
            setErrorMessage(mgr.getError());
            return null;
        } else return super.getNextPage();
    }
}
