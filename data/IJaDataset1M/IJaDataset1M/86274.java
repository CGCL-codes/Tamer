package net.sf.yari.ui.util.action;

import net.sf.yari.internal.Activator;
import net.sf.yari.ui.util.IImageKeys;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Control;

public class SwitchStackAction extends Action {

    private final StackLayout layout;

    private final Control switchControl1;

    private final Control switchControl2;

    public SwitchStackAction(StackLayout sLayout, Control startControl, Control switchControl) {
        super("Switch representation", AS_PUSH_BUTTON);
        this.layout = sLayout;
        this.switchControl2 = switchControl;
        this.switchControl1 = startControl;
        setImageDescriptor(Activator.imageDescriptor(IImageKeys.SWITCH));
    }

    @Override
    public void run() {
        if (layout.topControl == switchControl1) layout.topControl = switchControl2; else layout.topControl = switchControl1;
        switchControl1.getParent().layout();
    }
}
