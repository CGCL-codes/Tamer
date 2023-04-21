package net.sf.refactorit.refactorings.undo;

import net.sf.refactorit.common.util.CollectionUtil;
import net.sf.refactorit.commonIDE.IDEController;
import net.sf.refactorit.ui.DialogManager;
import net.sf.refactorit.vfs.Source;
import java.util.Iterator;
import java.util.List;

/**
 *
 *
 * @author Tonis Vaga
 * @author Igor Malinin
 */
class ModifiedStatus extends UndoableStatus {

    List sources;

    /**
   * @param collection of can't create headers
   */
    public ModifiedStatus(List headers) {
        super(WARNING);
        this.sources = headers;
    }

    public void resolve() {
        String title = "Modified Sources Found";
        String msg = null;
        DialogManager dialogManager = DialogManager.getInstance();
        if (sources.size() < 10) {
            msg = "<html>RefactorIT found the following sources with potential manual changes:<br><UL> ";
            for (Iterator i = sources.iterator(); i.hasNext(); ) {
                Source item = (Source) i.next();
                msg += "<LI>" + item.getDisplayPath() + "</LI>";
            }
            msg += "</UL>";
        } else {
            msg = "<html>RefactorIT found " + sources.size() + " sources with potential manual changes.<P>";
        }
        msg += "Any manual changes made in these files will be overwritten!<br>Do you want to continue?</html>";
        int res = dialogManager.showCustomYesNoQuestion(IDEController.getInstance().createProjectContext(), title, msg, DialogManager.YES_BUTTON);
        if (res == DialogManager.YES_BUTTON) {
            setStatus(OK);
        }
    }

    public void merge(ModifiedStatus status) {
        CollectionUtil.addAllNew(sources, status.sources);
    }
}
