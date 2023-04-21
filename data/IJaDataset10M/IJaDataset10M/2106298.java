package com.holzer.fusedoc.dialogs.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class AssertionsPage extends AbstractFusedocPage {

    @Override
    protected Control createContents(Composite parent) {
        Composite controls = new Composite(parent, SWT.NONE);
        return controls;
    }
}
