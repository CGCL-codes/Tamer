package net.sf.gridarta.gui.dialog.prefs;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import org.jetbrains.annotations.NotNull;

/**
 * Helper class for preference panes.
 * @author Andreas Kirschbaum
 */
public class PreferencesHelper {

    /**
     * The {@link Container} to add to.
     */
    @NotNull
    private final Container container;

    /**
     * The {@link GridBagConstraints} for adding elements.
     */
    @NotNull
    private final GridBagConstraints gbc = new GridBagConstraints();

    /**
     * Creates a new instance.
     * @param container the container to add to
     */
    public PreferencesHelper(@NotNull final Container container) {
        this.container = container;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
    }

    /**
     * Adds a component to the container.
     * @param component the component to add
     */
    public void addComponent(@NotNull final Component component) {
        container.add(component, gbc);
        gbc.gridy++;
    }
}
