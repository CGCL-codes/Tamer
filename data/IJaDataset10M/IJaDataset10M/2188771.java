package uk.ac.ed.csbe.sbsivisual.paralleldimensionview;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public interface IDialogUtilities {

    void openError(String title, String message);

    void openInfo(String title, String message);

    boolean openConfirm(String title, String message);

    boolean openQuestion(String title, String message);

    public final IDialogUtilities DEFAULT = new IDialogUtilities() {

        public void openError(String title, String message) {
            MessageDialog.openError(Display.getCurrent().getActiveShell(), title, message);
        }

        public void openInfo(String title, String message) {
            MessageDialog.openInformation(Display.getCurrent().getActiveShell(), title, message);
        }

        public boolean openConfirm(String title, String message) {
            return MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), title, message);
        }

        public boolean openQuestion(String title, String message) {
            return MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), title, message);
        }
    };
}
