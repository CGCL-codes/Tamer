package uk.ac.gla.cmt.animatics.ui;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

/**
 * CheckBoxMenuItem is an extended JCheckBoxMenuItem which responds to the SelectableAction.SELECTED property
 *
 */
public class CheckBoxMenuItem extends JCheckBoxMenuItem {

    /**
	 * Constructs a new CheckBoxMenuItem for the specified Action
	 *
	 * @param action the Action used to specifiy the new CheckBoxMenuItem
	 */
    public CheckBoxMenuItem(Action action) {
        super(action);
    }

    /**
	 * Sets this CheckBoxMenuItem properties from the specified Action.
	 * This implementation sets the same properties as JCheckBoxMenuItem.configurePropertiesFromAction()
	 * plus the whether this item is currently selected.
	 *
	 * @param action the Action used to set this CheckBoxMenuItem properties
	 */
    protected void configurePropertiesFromAction(Action action) {
        super.configurePropertiesFromAction(action);
        if (action != null) {
            Object obj = action.getValue(SelectableAction.PROPERTY_SELECTED);
            if ((obj != null) && (obj instanceof Boolean)) {
                Boolean b = (Boolean) obj;
                this.setSelected(b.booleanValue());
            }
        }
    }

    protected void fireActionPerformed(ActionEvent e) {
        String state = isSelected() ? SelectableAction.ACTION_COMMAND_SELECT : SelectableAction.ACTION_COMMAND_UNSELECTED;
        super.fireActionPerformed(new ActionEvent(e.getSource(), e.getID(), state, e.getWhen(), e.getModifiers()));
    }

    /**
	 * Constructs a new PropertyChangeListener to update this CheckBoxMenuItem as properties change on the Action instance.
	 *
	 * @see AbstractButton.createActionPropertyChangeListener(Action)
	 * @param action the Action to create a PropertyChangeListener to listen to
	 */
    protected PropertyChangeListener createActionPropertyChangeListener(Action action) {
        return (new AbstractWeakRefActionPropertyChangeListener(this, action) {

            public void propertyChange(PropertyChangeEvent e) {
                String propertyName = e.getPropertyName();
                if (!cleanRef()) {
                    JMenuItem mi = (JMenuItem) getTarget();
                    if (e.getPropertyName().equals(Action.NAME)) {
                        String text = (String) e.getNewValue();
                        mi.setText(text);
                        mi.repaint();
                    } else if (propertyName.equals("enabled")) {
                        Boolean enabledState = (Boolean) e.getNewValue();
                        mi.setEnabled(enabledState.booleanValue());
                        mi.repaint();
                    } else if (e.getPropertyName().equals(Action.SMALL_ICON)) {
                        Icon icon = (Icon) e.getNewValue();
                        mi.setIcon(icon);
                        mi.invalidate();
                        mi.repaint();
                    } else if (e.getPropertyName().equals(Action.MNEMONIC_KEY)) {
                        Integer mn = (Integer) e.getNewValue();
                        mi.setMnemonic(mn.intValue());
                        mi.invalidate();
                        mi.repaint();
                    } else if (e.getPropertyName().equals(SelectableAction.PROPERTY_SELECTED)) {
                        Boolean selectedState = (Boolean) e.getNewValue();
                        mi.setSelected(selectedState.booleanValue());
                        mi.repaint();
                    }
                }
            }
        });
    }
}
