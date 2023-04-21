package snooker.scoreboard.button;

import snooker.scoreboard.action.IUndoActionListener;
import android.content.res.Resources;
import android.widget.ImageView;

public class UndoButton extends AbstractControlButton {

    private IUndoActionListener action;

    public UndoButton(IUndoActionListener action, ImageView v, Resources r, int resource1, int resource2, int resource3) {
        super(v, r, resource1, resource2, resource3);
        this.action = action;
    }

    protected void actionUp() {
        action.undo();
    }
}
