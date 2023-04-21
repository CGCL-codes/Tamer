package net.sf.refactorit.netbeans.common.vcs;

import net.sf.refactorit.commonIDE.IDEController;
import net.sf.refactorit.ui.DialogManager;
import org.apache.log4j.Logger;

/**
 * @author risto
 */
public class ErrorDialog {

    private static Logger log = Logger.getLogger(ErrorDialog.class);

    static void error(String message) {
        DialogManager.getInstance().showInformation(IDEController.getInstance().createProjectContext(), "info.failed.cvs.command", message);
        log.warn(message);
    }
}
