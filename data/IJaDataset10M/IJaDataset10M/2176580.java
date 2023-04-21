package org.progeeks.util.swing;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 *  Extends the standard Swing action to provide check-box support.
 *  If you override isChecked()/setChecked() then you should not do
 *  any check processing in the action performed.  If you override
 *  actionPerformed() and handle your own toggling, then you should
 *  not override isChecked()/setChecked().  The actionPerformed()
 *  method (depending on the button model) will usually be called
 *  after the check state has changed within the button model.  So
 *  it is possible to let the checkbox action handle 100% of the
 *  toggling and simply override the actionPerformed() method to
 *  update the dependent state.
 *
 *  @version   $Revision: 1.10 $
 *  @author    Paul Speed
 */
public class CheckBoxAction extends AbstractAction implements Cloneable, TogglableAction {

    static final long serialVersionUID = 1;

    public static final String PROP_CHECKED = "checked";

    /**
     *  True if this action is "checked".
     */
    private boolean checked;

    /**
     *  Contains the list of attached ButtonModels.
     */
    private List<ButtonModel> buttonModels = new ArrayList<ButtonModel>();

    /**
     *  The listener that we add to attached button models.
     */
    private ButtonListener listener;

    public CheckBoxAction(String name, boolean checked) {
        super(name);
        this.checked = checked;
    }

    public CheckBoxAction(String name) {
        this(name, false);
    }

    public CheckBoxAction() {
    }

    public Object clone() throws CloneNotSupportedException {
        CheckBoxAction action = (CheckBoxAction) super.clone();
        action.changeSupport = null;
        action.buttonModels = new ArrayList<ButtonModel>();
        action.listener = null;
        PropertyChangeListener[] listeners = action.getPropertyChangeListeners();
        for (int i = 0; i < listeners.length; i++) {
            action.removePropertyChangeListener(listeners[i]);
        }
        return (action);
    }

    /**
     *  Called to set the action's state to "checked".
     */
    public void setChecked(boolean checked) {
        if (this.checked == checked) return;
        this.checked = checked;
        putValue(PROP_CHECKED, Boolean.valueOf(checked));
    }

    /**
     *  Returns the checked state of this action.
     */
    public boolean isChecked() {
        return (checked);
    }

    /**
     *  Default implementation does nothing since the button
     *  model should be toggling its own state.  With most toggling
     *  button models, the isChecked() state will be changed before
     *  this event is fired.  This method can be a convenient place
     *  to detect these changes without having to override and
     *  pass up the setChecked() method.
     */
    public void actionPerformed(ActionEvent event) {
    }

    /**
     *  Attaches this action to the specified button model.
     *  State changes to one are reflected in the other.
     */
    public void addButtonModel(ButtonModel model) {
        buttonModels.add(model);
        model.setSelected(checked);
        if (listener == null) listener = new ButtonListener();
        model.addChangeListener(listener);
    }

    public void removeButtonModel(ButtonModel model) {
        if (listener != null) model.removeChangeListener(listener);
        buttonModels.remove(model);
    }

    /**
     *  Sets the button models' states to reflect our checked state.
     */
    private void updateButtonModels() {
        for (Iterator i = buttonModels.iterator(); i.hasNext(); ) {
            ButtonModel m = (ButtonModel) i.next();
            m.setSelected(checked);
        }
    }

    /**
     *  Overridden to catch our own property changes.
     */
    protected void firePropertyChange(String property, Object oldValue, Object newValue) {
        super.firePropertyChange(property, oldValue, newValue);
        if (PROP_CHECKED.equals(property)) {
            updateButtonModels();
        }
    }

    private class ButtonListener implements ChangeListener {

        public void stateChanged(ChangeEvent event) {
            ButtonModel model = (ButtonModel) event.getSource();
            if (model.isSelected() != isChecked()) setChecked(model.isSelected());
        }
    }
}
