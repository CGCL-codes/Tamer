package com.prolix.editor.main.workspace.reload.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Stack Editor Panel
 * 
 * @author Phillip Beauvoir
 * @version $Id: StackPanel.java,v 1.2 2006/07/10 11:50:31 phillipus Exp $
 */
public class StackPanel extends Composite {

    /**
     * Layout for StackPanel
     */
    private StackLayout _stackLayout;

    /**
     * Constructor
     */
    public StackPanel(Composite parent) {
        super(parent, SWT.NULL);
        _stackLayout = new StackLayout();
        setLayout(_stackLayout);
    }

    /**
     * Set the top control on the Stack and Layout
     * @param control
     */
    public void setControl(Control control) {
        _stackLayout.topControl = control;
        layout();
    }

    public void dispose() {
        super.dispose();
        _stackLayout = null;
    }
}
