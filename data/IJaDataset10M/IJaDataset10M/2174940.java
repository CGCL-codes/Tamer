package org.herac.tuxguitar.app.actions.note;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.undo.undoables.measure.UndoableMeasureGeneric;
import org.herac.tuxguitar.song.models.TGBeat;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InsertRestBeatAction extends Action {

    public static final String NAME = "action.beat.general.insert-rest";

    public InsertRestBeatAction() {
        super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
    }

    protected int execute(ActionData actionData) {
        Caret caret = getEditor().getTablature().getCaret();
        TGBeat beat = caret.getSelectedBeat();
        if (beat != null) {
            UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();
            TuxGuitar.instance().getFileHistory().setUnsavedFile();
            if (beat.getVoice(caret.getVoice()).isEmpty()) {
                getSongManager().getMeasureManager().addSilence(beat, caret.getDuration().clone(getSongManager().getFactory()), caret.getVoice());
            } else {
                long start = beat.getStart();
                long length = beat.getVoice(caret.getVoice()).getDuration().getTime();
                getSongManager().getMeasureManager().moveVoices(caret.getMeasure(), start, length, caret.getVoice(), beat.getVoice(caret.getVoice()).getDuration());
            }
            addUndoableEdit(undoable.endUndo());
            updateTablature();
        }
        return 0;
    }

    public void updateTablature() {
        fireUpdate(getEditor().getTablature().getCaret().getMeasure().getNumber());
    }
}
