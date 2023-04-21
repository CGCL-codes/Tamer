package net.sf.refactorit.ui.module;

import net.sf.refactorit.common.util.AppRegistry;
import net.sf.refactorit.commonIDE.IDEController;
import net.sf.refactorit.commonIDE.ShortcutAction;
import net.sf.refactorit.refactorings.undo.IUndoManager;
import net.sf.refactorit.refactorings.undo.RitUndoManager;
import net.sf.refactorit.ui.DialogManager;
import net.sf.refactorit.ui.JProgressDialog;
import net.sf.refactorit.ui.SearchingInterruptedException;
import net.sf.refactorit.ui.ShortcutKeyStrokes;
import javax.swing.KeyStroke;
import javax.swing.undo.CannotUndoException;

/**
 * @author Tonis Vaga
 */
public class UndoAction extends AbstractIdeAction implements ShortcutAction {

    public static final String KEY = "refactorit.action.UndoAction";

    public static final String NAME = "Undo";

    public UndoAction() {
    }

    public KeyStroke getKeyStroke() {
        return ShortcutKeyStrokes.getByKey(UndoAction.KEY);
    }

    public boolean isAvailable() {
        IDEController controller = IDEController.getInstance();
        IUndoManager manager = RitUndoManager.getInstance(controller.getActiveProject());
        return manager != null && manager.canUndo();
    }

    public String getName() {
        IUndoManager manager = RitUndoManager.getInstance();
        if (manager == null) {
            return NAME;
        }
        return manager.getUndoPresentationName();
    }

    public String getKey() {
        return KEY;
    }

    public char getMnemonic() {
        return 'U';
    }

    public boolean run(IdeWindowContext context) {
        if (!isAvailable()) {
            return false;
        }
        IDEController controller = IDEController.getInstance();
        IUndoManager manager = RitUndoManager.getInstance(controller.getActiveProject());
        boolean changed = false;
        if (manager == null || !manager.canUndo()) {
            final String msg;
            if (manager == null) {
                msg = "UndoManager == null";
            } else {
                msg = "called when (canUndo == false)";
            }
            AppRegistry.getLogger(this.getClass()).debug(msg);
            return false;
        }
        DialogManager dlgMgr = DialogManager.getInstance();
        int result = dlgMgr.showYesNoHelpQuestion(context, "question.undo", "Do you really want to undo refactoring " + manager.getPresentationNameWIthDetails(true) + " ?", "refact.undo");
        if (result != DialogManager.YES_BUTTON) {
            return false;
        }
        try {
            controller.saveAllFiles();
            try {
                JProgressDialog.run(context, new Runnable() {

                    public void run() {
                        RitUndoManager.getInstance(IDEController.getInstance().getActiveProject()).undo();
                    }
                }, "Undo ...", false);
            } catch (SearchingInterruptedException e) {
            }
            changed = true;
        } catch (CannotUndoException ex) {
            AppRegistry.getExceptionLogger().debug(ex, this.getClass());
        }
        return changed;
    }
}
