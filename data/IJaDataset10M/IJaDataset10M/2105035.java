package net.sf.rcpforms.examples.app.multipageeditor;

import java.util.logging.Logger;
import net.sf.rcpforms.examples.complete.models.AddressModel;
import net.sf.rcpforms.examples.complete.models.NestedAddressModel;
import net.sf.rcpforms.form.IRCPFormEditorInput;
import net.sf.rcpforms.form.RCPFormPageEditorPart;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

public class AddressFormPageEditorPart extends RCPFormPageEditorPart<SandboxAddressForm> {

    private static final Logger LOG = Logger.getLogger(AddressFormPageEditorPart.class.getName());

    public AddressFormPageEditorPart(SandboxAddressForm form) {
        super(form, "AddressFormPageEditorPart", "Lazy initialized AddressPart");
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        this.setDirty(false);
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    protected void initializeDirtyChangeListener(IRCPFormEditorInput oldInput, IRCPFormEditorInput newInput) {
        super.initializeDirtyChangeListener(oldInput, newInput);
        if (oldInput != null) {
            for (Object o : oldInput.getModels()) {
                if (o instanceof AddressModel) {
                    this.removeDirtyChangeListener(o);
                    this.removeDirtyChangeListener(((AddressModel) o).getAddress());
                    this.removeDirtyChangeListener(((NestedAddressModel) ((AddressModel) o).getAddress()).getCountry());
                }
            }
        }
        for (Object o : newInput.getModels()) {
            if (o instanceof AddressModel) {
                this.addDirtyChangeListener(o);
                this.addDirtyChangeListener(((AddressModel) o).getAddress());
                this.addDirtyChangeListener(((NestedAddressModel) ((AddressModel) o).getAddress()).getCountry());
            }
        }
    }

    @Override
    public void createPartControl(Composite parent) {
        LOG.info("createPartControl for object \"" + this.getClass().getName() + "\" called!");
        super.createPartControl(parent);
    }

    @Override
    public boolean selectReveal(Object object) {
        return false;
    }

    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        super.init(site, ((MultipageCompositeEditorInput) input).getAddressEditorInput());
    }
}
