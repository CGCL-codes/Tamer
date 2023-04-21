package org.sgodden.echo.ext20.buttons;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import nextapp.echo.app.Window;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import org.sgodden.echo.ext20.AbstractButton;
import org.sgodden.echo.ext20.Button;

/**
 * A toggle group that takes buttons and adds those buttons to its group.
 * 
 * @author rcharlton
 */
@SuppressWarnings("serial")
public class ToggleGroup implements PropertyChangeListener, Serializable {

    private String toggleGroupName;

    private static final String DEFAULT_TGROUP_NAME = "toggle_group_";

    private List<Button> buttons = new ArrayList<Button>();

    /**
     * Constructs a toggle group with a default toggle group name that is unique
     * to the Application Instance.
     */
    public ToggleGroup() {
        setToggleGroupName(DEFAULT_TGROUP_NAME + Window.getActive().generateId());
    }

    /**
     * Constructs a toggle group with a toggle group name provided.
     * 
     * @param toggleGroupName
     */
    public ToggleGroup(String toggleGroupName) {
        setToggleGroupName(toggleGroupName);
    }

    private void setToggleGroupName(String toggleGroupName) {
        this.toggleGroupName = toggleGroupName;
    }

    /**
     * Adds the button to this toggle group by setting the toggle group name on
     * the button.
     */
    public void addButton(Button button) {
        button.setToggleGroup(toggleGroupName);
        button.addPropertyChangeListener(AbstractButton.PROPERTY_PRESSED, this);
        buttons.add(button);
    }

    public void removeButton(Button button) {
        button.setToggleGroup(null);
        button.removePropertyChangeListener(AbstractButton.PROPERTY_PRESSED, this);
        buttons.remove(button);
    }

    /**
     * @return buttons All the buttons that have been added to this toggle
     *         group.
     */
    public List<Button> getButtons() {
        return buttons;
    }

    /**
     * Ensures that only one button in the toggle group may have property
     * pressed as true at any one time.
     */
    public void propertyChange(PropertyChangeEvent arg0) {
        Button source = (Button) arg0.getSource();
        if (Boolean.TRUE.equals(arg0.getNewValue())) {
            for (Button b : buttons) {
                if (b != source) {
                    b.setPressed(false);
                }
            }
        }
    }
}
