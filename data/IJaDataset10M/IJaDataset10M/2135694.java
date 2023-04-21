package com.lubq.lm.bestwiz.order.ui.view;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class OrderEditorPart1 extends EditorPart {

    public static final String ID_VIEW = "com.lubq.lm.bestwiz.order.ui.view.OrderEditorPart1";

    Composite composite1;

    public OrderEditorPart1() {
        super();
    }

    public void createPartControl(Composite parent) {
        composite1 = new Composite(parent, SWT.NULL);
        composite1.setLayout(new GridLayout(4, false));
    }

    public void setFocus() {
    }

    public void dispose() {
        super.dispose();
    }

    public void doSave(IProgressMonitor arg0) {
    }

    public void doSaveAs() {
    }

    public void init(IEditorSite arg0, IEditorInput arg1) throws PartInitException {
    }

    public boolean isDirty() {
        return false;
    }

    public boolean isSaveAsAllowed() {
        return false;
    }
}
