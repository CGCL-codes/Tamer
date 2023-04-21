package org.sodeja.swing.panel;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.sodeja.swing.action.LocalizedAction;
import org.sodeja.swing.context.ApplicationContext;
import org.sodeja.swing.layout.GridBag;
import org.sodeja.swing.resource.ResourceConstants;

public abstract class FormPanel<T extends ApplicationContext> extends ApplicationPanel<T> {

    private static final long serialVersionUID = -6455716542464549491L;

    protected List<StateListener> stateListeners = new ArrayList<StateListener>();

    protected JPanel pnlContent;

    protected Action okAction;

    protected Action cancelAction;

    protected abstract Enum<?> getResourceKey();

    protected abstract void initContentPanel();

    public void openForm() {
        fireOpened();
    }

    public boolean okCallback() {
        return true;
    }

    public void cancelCallback() {
    }

    public void addStateListner(StateListener listener) {
        stateListeners.add(listener);
    }

    public void removeStateListener(StateListener listener) {
        stateListeners.remove(listener);
    }

    @Override
    public void add(Component comp, Object constraints) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Component add(Component comp) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void initComponents() {
        super.add(createLabelPanel(), GridBag.line(0));
        super.add(createContentPanel(), GridBag.bigPanel(1, 1));
        super.add(new JSeparator(JSeparator.HORIZONTAL), GridBag.line(2));
        super.add(createButtonsPanel(), GridBag.line(3));
        fireInited();
    }

    protected JPanel createLabelPanel() {
        return new LabelPanel<T>(getResourceKey());
    }

    protected JPanel createContentPanel() {
        pnlContent = new JPanel(new GridBagLayout());
        initContentPanel();
        return pnlContent;
    }

    protected ButtonsPanel createButtonsPanel() {
        ButtonsPanel buttons = new ButtonsPanel();
        createOkAction();
        buttons.addAction(okAction);
        createCancelAction();
        buttons.addAction(cancelAction);
        return buttons;
    }

    protected JPanel getContentPanel() {
        return pnlContent;
    }

    protected void createOkAction() {
        okAction = new LocalizedAction<T>(ResourceConstants.BTN_OK) {

            private static final long serialVersionUID = 8332876354284325512L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (okCallback()) {
                    fireClosed();
                }
            }
        };
    }

    protected void createCancelAction() {
        cancelAction = new LocalizedAction<T>(ResourceConstants.BTN_CANCEL) {

            private static final long serialVersionUID = 8463988583353876627L;

            @Override
            public void actionPerformed(ActionEvent e) {
                cancelCallback();
                fireClosed();
            }
        };
        registerKeyboardAction(cancelAction, getResourceProvider().getKeyStroke(ResourceConstants.BTN_CANCEL_KEY), WHEN_IN_FOCUSED_WINDOW);
    }

    protected void fireInited() {
        for (StateListener listener : stateListeners) {
            listener.inited();
        }
    }

    protected void fireOpened() {
        for (StateListener listener : stateListeners) {
            listener.opened();
        }
    }

    protected void fireClosed() {
        for (StateListener listener : stateListeners) {
            listener.closed();
        }
    }

    protected JTextField addLabeledField(Enum<?> lblKey, int row) {
        return FormUtils.addLabeledField(getContext(), pnlContent, lblKey, row);
    }

    protected JPasswordField addLabeledPassword(Enum<?> lblKey, int row) {
        return FormUtils.addLabeledPassword(getContext(), pnlContent, lblKey, row);
    }

    protected JComboBox addLabeledCombo(Enum<?> lblKey, int row) {
        return FormUtils.addLabeledCombo(getContext(), pnlContent, lblKey, row);
    }

    protected JTextArea addLabeledArea(Enum<?> lblKey, int row) {
        return FormUtils.addLabeledArea(getContext(), pnlContent, lblKey, row);
    }

    protected <F extends JComponent> void addLabeledScrollable(Enum<?> lblKey, int row, F component) {
        FormUtils.addLabaledScrollable(getContext(), pnlContent, lblKey, row, component);
    }

    protected void fillEmpty(int row) {
        FormUtils.fillEmpty(pnlContent, row);
    }
}
