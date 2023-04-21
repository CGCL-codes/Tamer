package net.sf.RecordEditor.edit.display.Action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import net.sf.RecordEditor.edit.display.common.AbstractFileDisplayWithFieldHide;
import net.sf.RecordEditor.utils.common.Common;
import net.sf.RecordEditor.utils.screenManager.AbstractActiveScreenAction;
import net.sf.RecordEditor.utils.screenManager.ReFrame;

@SuppressWarnings("serial")
public class LoadSavedVisibilityAction extends AbstractAction implements AbstractActiveScreenAction {

    /**
	 * @param creator
	 */
    public LoadSavedVisibilityAction() {
        super("Load Saved Hidden Fields");
        checkActionEnabled();
    }

    /**
	 * @see net.sf.RecordEditor.utils.screenManager.AbstractActiveScreenAction#checkActionEnabled()
	 */
    @Override
    public void checkActionEnabled() {
        ReFrame actionHandler = ReFrame.getActiveFrame();
        super.setEnabled(actionHandler != null && actionHandler instanceof AbstractFileDisplayWithFieldHide);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        ReFrame actionHandler = ReFrame.getActiveFrame();
        if (actionHandler instanceof AbstractFileDisplayWithFieldHide) {
            try {
                net.sf.RecordEditor.edit.display.util.SaveRestoreHiddenFields.restoreHiddenFields((AbstractFileDisplayWithFieldHide) actionHandler);
            } catch (NoClassDefFoundError e) {
                Common.logMsg("Unable to loaved saved definition: jibx not present ???", null);
            }
        }
    }
}
