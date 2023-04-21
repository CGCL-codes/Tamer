package editor.view.action;

import java.awt.event.ActionEvent;
import editor.EditorSettings;
import editor.view.GraphicFrame;

/**
 * Action implements clearing the list of the last used documents
 */
public final class ClearLastUsedListAction extends AbstractLocaleAction {

    private static final String ACTION_NAME = "action.clear_last_used_list";

    private final GraphicFrame oFrame;

    /**
     * Constructor
     *
     * @param frame the controller frame
     */
    public ClearLastUsedListAction(final GraphicFrame frame) {
        super(ACTION_NAME);
        oFrame = frame;
    }

    /**
     * Closes the active datasheet
     *
     * @param e ActionEvent
     */
    @Override
    public final void actionPerformed(final ActionEvent e) {
        EditorSettings.getInstance().clearLastUsedDocumentsList();
        EditorSettings.getInstance().save();
        oFrame.updateView();
    }
}
