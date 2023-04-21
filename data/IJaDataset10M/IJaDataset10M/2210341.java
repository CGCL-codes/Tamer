package org.simbrain.workspace.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import org.simbrain.resource.ResourceManager;
import org.simbrain.util.SFileChooser;
import org.simbrain.workspace.Workspace;
import org.simbrain.workspace.WorkspacePreferences;
import org.simbrain.workspace.WorkspaceSerializer;
import org.simbrain.world.odorworld.OdorWorldComponent;
import org.simbrain.world.odorworld.OdorWorldPreferences;

/**
 * Open an odor world in current workspace. //TODO: Use generic!
 */
public final class OpenOdorWorldAction extends WorkspaceAction {

    private static final long serialVersionUID = 1L;

    /**
     * Create an open odor world action with the specified
     * workspace.
     */
    public OpenOdorWorldAction(Workspace workspace) {
        super("Odor World", workspace);
        putValue(SMALL_ICON, ResourceManager.getImageIcon("SwissIcon.png"));
    }

    /** @see AbstractAction */
    public void actionPerformed(final ActionEvent event) {
        SFileChooser chooser = new SFileChooser(WorkspacePreferences.getCurrentDirectory(OdorWorldComponent.class), "xml file", "xml");
        File theFile = chooser.showOpenDialog();
        if (theFile != null) {
            OdorWorldComponent worldComponent = (OdorWorldComponent) WorkspaceSerializer.open(OdorWorldComponent.class, theFile);
            workspace.addWorkspaceComponent(worldComponent);
        }
    }
}
