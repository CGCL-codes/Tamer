package org.jjflyboy.slee.descriptors.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class Slee11FormToolkit extends FormToolkit {

    public Slee11FormToolkit(Display display) {
        super(display);
        String osname = System.getProperty("os.name");
        if (osname.startsWith("Linux")) {
            setBorderStyle(SWT.BORDER);
        }
    }
}
