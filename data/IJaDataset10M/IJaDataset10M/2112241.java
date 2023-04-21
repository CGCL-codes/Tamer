package net.sf.gridarta.gui.utils.tabbedpanel;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.gridarta.gui.utils.borderpanel.Location;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A list of {@link AbstractButton buttons} where at most one button is active
 * at any time.
 * @author Andreas Kirschbaum
 */
public class ButtonList {

    /**
     * The {@link Container} that contains all buttons.
     */
    @NotNull
    private final Container buttons = new JPanel();

    /**
     * The listeners to notify.
     */
    @NotNull
    private final EventListenerList2<ButtonListListener> listeners = new EventListenerList2<ButtonListListener>(ButtonListListener.class);

    /**
     * The currently selected buttons. Empty if no button is selected. Only the
     * first element is actually in the "selected" state; further entries are
     * re-activated if the active button is deselected.
     */
    @NotNull
    private final List<AbstractButton> selectedButtons = new ArrayList<AbstractButton>();

    /**
     * The {@link ChangeListener} attached to all buttons.
     */
    @NotNull
    private final ChangeListener changeListener = new ChangeListener() {

        @Override
        public void stateChanged(final ChangeEvent e) {
            final AbstractButton button = (AbstractButton) e.getSource();
            if (button.isSelected()) {
                selectButton(button);
            } else if (!selectedButtons.isEmpty() && button == selectedButtons.get(0)) {
                selectedButtons.remove(0);
                fireSelectedButtonChanged(button);
                if (!selectedButtons.isEmpty()) {
                    selectedButtons.get(0).setSelected(true);
                }
            }
        }
    };

    /**
     * Creates a new instance.
     * @param location the location
     */
    public ButtonList(@NotNull final Location location) {
        buttons.setLayout(new BoxLayout(buttons, location.getAxis()));
    }

    /**
     * Adds a {@link ButtonListListener} to be notified.
     * @param listener the button list listener
     */
    public void addButtonListListener(@NotNull final ButtonListListener listener) {
        listeners.add(listener);
    }

    /**
     * Adds a button.
     * @param button the button
     */
    public void addButton(@NotNull final AbstractButton button) {
        if (button.isSelected()) {
            throw new IllegalArgumentException();
        }
        final String title = button.getText();
        int index;
        for (index = 0; index < buttons.getComponentCount(); index++) {
            final Component tmp = buttons.getComponent(index);
            if (tmp instanceof AbstractButton) {
                final AbstractButton tmpButton = (AbstractButton) tmp;
                final Comparable<String> tmpTitle = tmpButton.getText();
                if (tmpTitle.compareTo(title) > 0) {
                    break;
                }
            }
        }
        buttons.add(button, index);
        buttons.validate();
        button.addChangeListener(changeListener);
    }

    /**
     * Removes a button. Does nothing if the button is not part of this button
     * list.
     * @param button the button
     */
    public void removeButton(@NotNull final AbstractButton button) {
        button.removeChangeListener(changeListener);
        buttons.remove(button);
        buttons.validate();
        final int index = selectedButtons.indexOf(button);
        if (index != -1) {
            selectedButtons.remove(index);
            if (index == 0) {
                fireSelectedButtonChanged(button);
                if (!selectedButtons.isEmpty()) {
                    selectedButtons.get(0).setSelected(true);
                }
            }
        }
    }

    /**
     * Selects a button.
     * @param button the button
     */
    public void selectButton(@NotNull final AbstractButton button) {
        if (selectedButtons.isEmpty() || button != selectedButtons.get(0)) {
            final AbstractButton prevSelectedButton = getSelectedButton();
            selectedButtons.remove(button);
            selectedButtons.add(0, button);
            fireSelectedButtonChanged(prevSelectedButton);
            button.setSelected(true);
            if (prevSelectedButton != null) {
                prevSelectedButton.setSelected(false);
            }
        }
    }

    /**
     * The selected button has changed.
     * @param prevSelectedButton the previously selected button
     */
    private void fireSelectedButtonChanged(@Nullable final AbstractButton prevSelectedButton) {
        final AbstractButton selectedButton = getSelectedButton();
        for (final ButtonListListener listener : listeners.getListeners()) {
            listener.selectedButtonChanged(prevSelectedButton, selectedButton);
        }
    }

    /**
     * Returns the currently selected button.
     * @return the selected button or <code>null</code> if no button exists
     */
    @Nullable
    public AbstractButton getSelectedButton() {
        return selectedButtons.isEmpty() ? null : selectedButtons.get(0);
    }

    /**
     * Returns the {@link Container} that contains all buttons.
     * @return the container
     */
    @NotNull
    public Component getButtons() {
        return buttons;
    }

    /**
     * Returns the total number of buttons.
     * @return the total number of buttons
     */
    public int getButtonCount() {
        return buttons.getComponentCount();
    }
}
