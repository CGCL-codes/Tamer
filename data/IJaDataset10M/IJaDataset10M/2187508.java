package org.mariella.rcp.actions;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionManagerOverrides;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

@Deprecated
public class ButtonContributionItem extends ContributionItem {

    private IAction action;

    private Listener buttonListener;

    private Button button;

    private final IPropertyChangeListener propertyListener = new IPropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent event) {
            actionPropertyChange(event);
        }
    };

    public ButtonContributionItem(IAction action) {
        this.action = action;
    }

    private void actionPropertyChange(final PropertyChangeEvent e) {
        if (isVisible() && button != null) {
            Display display = button.getDisplay();
            if (display.getThread() == Thread.currentThread()) {
                update(e.getProperty());
            } else {
                display.asyncExec(new Runnable() {

                    public void run() {
                        update(e.getProperty());
                    }
                });
            }
        }
    }

    public void setButton(Button b) {
        b.setData(this);
        b.addListener(SWT.Dispose, getButtonListener());
        b.addListener(SWT.Selection, getButtonListener());
        if (action.getHelpListener() != null) {
            b.addHelpListener(action.getHelpListener());
        }
        button = b;
        update(null);
        action.addPropertyChangeListener(propertyListener);
    }

    private Listener getButtonListener() {
        if (buttonListener == null) {
            buttonListener = new Listener() {

                public void handleEvent(Event event) {
                    switch(event.type) {
                        case SWT.Dispose:
                            handleWidgetDispose(event);
                            break;
                        case SWT.Selection:
                            Widget ew = event.widget;
                            if (ew != null) {
                                handleWidgetSelection(event, ((Button) ew).getSelection());
                            }
                            break;
                    }
                }
            };
        }
        return buttonListener;
    }

    private void handleWidgetSelection(Event e, boolean selection) {
        action.runWithEvent(e);
    }

    private void handleWidgetDispose(Event e) {
        if (e.widget == button) {
            action.removePropertyChangeListener(propertyListener);
            button = null;
        }
    }

    @Override
    public void update(String propertyName) {
        if (button != null) {
            boolean enableStateChanged = propertyName == null || propertyName.equals(IAction.ENABLED) || propertyName.equals(IContributionManagerOverrides.P_ENABLED);
            if (enableStateChanged) {
                boolean shouldBeEnabled = action.isEnabled();
                if (button.getEnabled() != shouldBeEnabled) {
                    button.setEnabled(shouldBeEnabled);
                }
            }
            return;
        }
    }
}
