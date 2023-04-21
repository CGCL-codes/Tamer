package net.sf.RecordEditor.edit.display.Action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import net.sf.RecordEditor.edit.display.common.AbstractFileDisplay;
import net.sf.RecordEditor.edit.display.util.ChangeLayout;
import net.sf.RecordEditor.re.openFile.AbstractLayoutSelectCreator;
import net.sf.RecordEditor.utils.screenManager.AbstractActiveScreenAction;
import net.sf.RecordEditor.utils.screenManager.ReFrame;

@SuppressWarnings("serial")
public class ChangeLayoutAction extends AbstractAction implements AbstractActiveScreenAction {

    private AbstractLayoutSelectCreator<?> creator;

    /**
	 * @param creator
	 */
    public ChangeLayoutAction(AbstractLayoutSelectCreator<?> layoutSelectionCreator) {
        super("Change Layout");
        this.creator = layoutSelectionCreator;
        checkActionEnabled();
    }

    /**
	 * @see net.sf.RecordEditor.utils.screenManager.AbstractActiveScreenAction#checkActionEnabled()
	 */
    @Override
    public void checkActionEnabled() {
        ReFrame actionHandler = ReFrame.getActiveFrame();
        super.setEnabled(actionHandler != null && actionHandler instanceof AbstractFileDisplay);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        ReFrame actionHandler = ReFrame.getActiveFrame();
        if (actionHandler instanceof AbstractFileDisplay) {
            new ChangeLayout(creator.create(), ((AbstractFileDisplay) actionHandler).getFileView());
        }
    }
}
