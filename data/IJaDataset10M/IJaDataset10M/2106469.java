package org.akrogen.tkui.gui.swtform.widgets.simples;

import org.akrogen.tkui.gui.swt.widgets.simples.SwtGuiButtonImpl;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * GUI Button implemented into SWT.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public class SwtFormGuiButtonImpl extends SwtGuiButtonImpl {

    protected FormToolkit formToolkit;

    public SwtFormGuiButtonImpl(FormToolkit formToolkit) {
        this.formToolkit = formToolkit;
    }

    protected Button createButton(Composite parent, int style) {
        return formToolkit.createButton(parent, "", style);
    }
}
