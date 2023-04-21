package org.simbrain.workspace.component_actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import org.simbrain.resource.ResourceManager;
import org.simbrain.workspace.gui.GuiComponent;

/**
 * Open component action.
 */
public final class OpenAction extends AbstractAction {

    /** Network panel. */
    private final GuiComponent guiComponent;

    /**
     * Create a new open component action with the specified.
     *
     * @param guiComponent networkPanel, must not be null
     */
    public OpenAction(final GuiComponent guiComponent) {
        super("Open...");
        if (guiComponent == null) {
            throw new IllegalArgumentException("component must not be null");
        }
        putValue(SMALL_ICON, ResourceManager.getImageIcon("Open.png"));
        this.putValue(this.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        this.guiComponent = guiComponent;
    }

    /** @see AbstractAction */
    public void actionPerformed(final ActionEvent event) {
        guiComponent.showOpenFileDialog();
    }
}
