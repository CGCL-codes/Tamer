package org.fluid.commons.ui.swt;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ActionButton extends Button {

    private IAction action;

    public ActionButton(Composite parent, int style, IAction action) {
        super(parent, style);
        this.action = action;
        setToolTipText(action.getToolTipText());
        setText(action.getText());
        if (action.getImageDescriptor() != null) setImage(action.getImageDescriptor().createImage());
        setToolTipText(action.getToolTipText());
        addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                getAction().run();
            }
        });
        action.addPropertyChangeListener(new IPropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent event) {
                if (event.getProperty().equals(IAction.ENABLED)) setEnabled(ActionButton.this.action.isEnabled());
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();
        getImage().dispose();
    }

    @Override
    public boolean isEnabled() {
        return action.isEnabled();
    }

    public IAction getAction() {
        return action;
    }

    @Override
    protected void checkSubclass() {
    }
}
