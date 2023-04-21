package org.gvt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.gvt.ChisioMain;
import org.gvt.inspector.AboutDialog;

/**
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class AboutAction extends Action {

    ChisioMain main;

    public AboutAction(ChisioMain main) {
        setText("About Chisio");
        setToolTipText("About Chisio");
        setImageDescriptor(ImageDescriptor.createFromFile(ChisioMain.class, "icon/chisio-icon-small.png"));
        this.main = main;
    }

    public void run() {
        new AboutDialog(main.getShell()).open();
    }
}
