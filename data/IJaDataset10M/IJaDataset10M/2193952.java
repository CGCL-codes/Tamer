package com.android.hierarchyviewer.util;

import com.android.hierarchyviewerlib.actions.ImageAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ActionButton implements IPropertyChangeListener, SelectionListener {

    private Button mButton;

    private Action mAction;

    public ActionButton(Composite parent, ImageAction action) {
        this.mAction = (Action) action;
        if (this.mAction.getStyle() == Action.AS_CHECK_BOX) {
            mButton = new Button(parent, SWT.CHECK);
        } else {
            mButton = new Button(parent, SWT.PUSH);
        }
        mButton.setText(action.getText());
        mButton.setImage(action.getImage());
        this.mAction.addPropertyChangeListener(this);
        mButton.addSelectionListener(this);
        mButton.setToolTipText(action.getToolTipText());
        mButton.setEnabled(this.mAction.isEnabled());
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (e.getProperty().toUpperCase().equals("ENABLED")) {
            mButton.setEnabled((Boolean) e.getNewValue());
        } else if (e.getProperty().toUpperCase().equals("CHECKED")) {
            mButton.setSelection(mAction.isChecked());
        }
    }

    public void setLayoutData(Object data) {
        mButton.setLayoutData(data);
    }

    public void widgetDefaultSelected(SelectionEvent e) {
    }

    public void widgetSelected(SelectionEvent e) {
        if (mAction.getStyle() == Action.AS_CHECK_BOX) {
            mAction.setChecked(mButton.getSelection());
        }
        mAction.run();
    }

    public void addSelectionListener(SelectionListener listener) {
        mButton.addSelectionListener(listener);
    }
}
