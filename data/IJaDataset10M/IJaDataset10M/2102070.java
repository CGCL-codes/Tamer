package matheus.jparallelport.gui;

import java.util.Vector;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class MyComposite extends Composite {

    public MyComposite(Composite parent, int style) {
        super(parent, style);
    }

    public void setEnabledRecursive(boolean enabled) {
        setEnabledRecursive(this, enabled);
        dontTouch = sameEnabledStatus;
        sameEnabledStatus = null;
    }

    private Vector<Control> sameEnabledStatus = new Vector<Control>();

    private Vector<Control> dontTouch = new Vector<Control>();

    private void setEnabledRecursive(Control ctl, boolean enabled) {
        if (ctl.getEnabled() == enabled) sameEnabledStatus.add(ctl); else {
            if (!dontTouch.contains(ctl)) {
                if (ctl instanceof Composite) for (Control innerCtl : ((Composite) ctl).getChildren()) setEnabledRecursive(innerCtl, enabled);
                ctl.setEnabled(enabled);
            }
        }
    }
}
