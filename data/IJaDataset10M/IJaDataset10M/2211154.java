package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionCreateNoteFromClipboard extends AbstractAction {

    public ActionCreateNoteFromClipboard() {
        this(32, 32);
    }

    public ActionCreateNoteFromClipboard(int width, int height) {
        super(Translations.getString("action.create_note_from_clipboard"), ImageUtils.getResourceImage("information.png", width, height));
        this.putValue(SHORT_DESCRIPTION, Translations.getString("action.create_note_from_clipboard"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        ViewUtils.getMainNoteView().getNoteTableView().pasteNote();
    }
}
