package editor.view.action;

import java.awt.event.ActionEvent;
import editor.view.GraphicFrame;
import editor.view.dialog.ConfigurationDialog;

public final class ConfigurationAction extends AbstractLocaleAction {

    private static final String ACTION_NAME = "action.config";

    private final GraphicFrame oFrame;

    public ConfigurationAction(final GraphicFrame frame) {
        super(ACTION_NAME);
        oFrame = frame;
    }

    /**
     * Opens a configuration dialog
     *
     * @param e ActionEvent
     */
    @Override
    public final void actionPerformed(final ActionEvent e) {
        final ConfigurationDialog dialog = new ConfigurationDialog(oFrame);
        dialog.showDialog();
        if (dialog.isCommitted()) {
            oFrame.getActionFactory().updateActions();
        }
    }
}
