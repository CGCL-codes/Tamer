package de.tud.eclipse.plugins.controlflow.control.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import de.tud.eclipse.plugins.controlflow.control.settings.ViewSettings;

/**
 * Toggles Horizontal/Vertical alignment.
 * @author Leo Nobach
 *
 */
public class ToggleHorizVertAction extends Action {

    static final ImageDescriptor image = ImageDescriptor.createFromFile(ToggleHorizVertAction.class, "icons/AlignVertical.png");

    static final String text = "Toggle Vertical/Horizontal alignment";

    private ViewSettings settings;

    public ToggleHorizVertAction(ViewSettings settings) {
        this.settings = settings;
        this.setImageDescriptor(image);
        this.setText(text);
    }

    public void run() {
        settings.setAlignHorizontal(!isHorizontal());
    }

    public boolean isHorizontal() {
        return settings.isAlignHorizontal();
    }
}
