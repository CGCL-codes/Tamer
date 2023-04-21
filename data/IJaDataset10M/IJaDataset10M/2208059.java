package de.tud.eclipse.plugins.controlflow.control.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import de.tud.eclipse.plugins.controlflow.control.settings.ViewSettings;

/**
 * Refreshes the alignment of nodes.
 * @author Leo Nobach, E. Stoffregen
 *
 */
public class RefreshAlignmentAction extends Action {

    static final ImageDescriptor image = ImageDescriptor.createFromFile(ToggleHorizVertAction.class, "icons/RefreshAlignment.png");

    static final String text = "Refresh alignment";

    private ViewSettings settings;

    boolean horizontal = true;

    public RefreshAlignmentAction(ViewSettings settings) {
        this.settings = settings;
        this.setImageDescriptor(image);
        this.setText(text);
    }

    public void run() {
        horizontal = !horizontal;
        settings.settingsChanged(ViewSettings.VIEW_NEEDS_REPOSITIONING);
    }
}
