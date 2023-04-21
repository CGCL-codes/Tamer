package de.beas.explicanto.client.sec.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.sec.control.TimelineCell;
import de.beas.explicanto.client.sec.model.ScreenplayModel;

/**
 * InserFrameAfterAction
 *
 * @author Alexandru.Gyulai
 * @version 1.0
 *
 */
public class InserFrameAfterAction implements IEditorActionDelegate, IActionDelegate2 {

    private static Log log = LogFactory.getLog(InserFrameAfterAction.class);

    private IEditorPart activeEditor;

    private TimelineCell cell;

    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        this.activeEditor = targetEditor;
    }

    public void run(IAction action) {
        log.error(" Unexpected method invocation");
    }

    public void selectionChanged(IAction action, ISelection selection) {
        if (!(selection instanceof IStructuredSelection)) {
            cell = null;
            return;
        }
        IStructuredSelection currentSelection = (IStructuredSelection) selection;
        if (!(currentSelection.getFirstElement() instanceof TimelineCell)) {
            cell = null;
            return;
        }
        cell = (TimelineCell) currentSelection.getFirstElement();
    }

    public void init(IAction action) {
        action.setText(translate("sec.scene.insertFrameAfter"));
    }

    public void dispose() {
    }

    public void runWithEvent(IAction action, Event event) {
        Object modelObj = activeEditor.getEditorInput().getAdapter(ScreenplayModel.class);
        if (modelObj == null) {
            log.error("(ScreenplayModel == null)");
            return;
        }
        ScreenplayModel model = (ScreenplayModel) modelObj;
        if (cell == null) {
            log.debug("runWithEvent cell == null");
            return;
        }
        model.addFrame(cell.getSceneUid(), cell.getExactFrame() + 1, true);
    }

    public String translate(String string) {
        return I18N.translate(string);
    }
}
