package com.alturatec.dienstreise.mvc.oevm;

import org.eclipse.swt.widgets.Composite;
import org.jrcaf.mvc.IView;
import org.jrcaf.mvc.IViewController;

public class OEVMView implements IView {

    private OEVMComposite oevmComposite;

    public void createViewControl(Composite aParent, IViewController aViewController) {
        oevmComposite = new OEVMComposite(aParent);
    }

    public Composite getViewControl() {
        return oevmComposite;
    }

    public void layout() {
        oevmComposite.layout();
    }

    public void setFocus() {
        oevmComposite.setFocus();
    }

    public Class getViewControlClass() {
        return OEVMComposite.class;
    }
}
