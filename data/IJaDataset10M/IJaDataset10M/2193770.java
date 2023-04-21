package hu.brinkus.etr.ui.editors;

import hu.brinkus.etr.core.model.payments.PayOut;
import hu.brinkus.etr.ui.EtrUIPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * TODO
 * 
 * @author balazs.brinkus (<a href="mailto:balazs@brinkus.hu">balazs@brinkus.hu</a>)
 */
public class PayOutEditorInput implements IEditorInput {

    private PayOut payOut;

    public PayOutEditorInput(PayOut payOut) {
        this.payOut = payOut;
    }

    public boolean exists() {
        return true;
    }

    public ImageDescriptor getImageDescriptor() {
        return EtrUIPlugin.getImageDescriptor("/images/pay_out.gif");
    }

    public String getName() {
        return "Pay-out";
    }

    public IPersistableElement getPersistable() {
        return null;
    }

    public String getToolTipText() {
        return "Pay-out";
    }

    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        if (adapter == PayOut.class) return payOut;
        return null;
    }
}
