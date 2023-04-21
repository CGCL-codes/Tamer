package com.ivis.xprocess.ui.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import com.ivis.xprocess.core.MessageUtility;
import com.ivis.xprocess.ui.dialogs.VCSConnectionDetailsDialog;
import com.ivis.xprocess.ui.properties.VCSMessages;

public class DialogUtil {

    private static MessageUtility messageUtility;

    private static int button = -1;

    private static boolean answer = false;

    public static MessageUtility getMessageUtility() {
        if (messageUtility == null) {
            messageUtility = new MessageUtility() {

                public void informUser(String title, String message) {
                    openInfoDialog(title, message);
                }

                public void warnUser(String title, String message) {
                    openErrorDialog(title, message);
                }

                public boolean askUser(String title, String question) {
                    return openQuestionDialog(title, question);
                }
            };
        }
        return messageUtility;
    }

    public static void openErrorDialog(final String title, final String message) {
        final Display display = ViewUtil.getDisplay();
        display.syncExec(new Runnable() {

            public void run() {
                MessageDialog.openError(ViewUtil.getCurrentShell(), title, message);
            }
        });
    }

    public static boolean openQuestionDialog(final String title, final String message) {
        final Display display = ViewUtil.getDisplay();
        display.syncExec(new Runnable() {

            public void run() {
                answer = MessageDialog.openQuestion(ViewUtil.getCurrentShell(), title, message);
            }
        });
        return answer;
    }

    public static int openVCSDialog(final String message) {
        final Display display = ViewUtil.getDisplay();
        display.syncExec(new Runnable() {

            public void run() {
                VCSConnectionDetailsDialog vcsUserEntryDialog = new VCSConnectionDetailsDialog(ViewUtil.getCurrentShell(), message);
                button = vcsUserEntryDialog.open();
            }
        });
        return button;
    }

    public static void openInfoDialog(final String title, final String message) {
        final Display display = ViewUtil.getDisplay();
        display.syncExec(new Runnable() {

            public void run() {
                MessageDialog.openInformation(ViewUtil.getCurrentShell(), title, message);
            }
        });
    }

    public static boolean openConfirmDialog(final String title, final String message) {
        final Display display = ViewUtil.getDisplay();
        display.syncExec(new Runnable() {

            public void run() {
                answer = MessageDialog.openConfirm(ViewUtil.getCurrentShell(), title, message);
            }
        });
        return answer;
    }

    public void openDialog(final String title, final String message) {
        final Display display = ViewUtil.getDisplay();
        display.syncExec(new Runnable() {

            public void run() {
                MessageDialog.openInformation(ViewUtil.getCurrentShell(), title, message);
            }
        });
    }

    public static void openVCSOldClientDialog() {
        final Display display = ViewUtil.getDisplay();
        display.syncExec(new Runnable() {

            public void run() {
                MessageDialog.openError(ViewUtil.getCurrentShell(), VCSMessages.svn_old_client_title, VCSMessages.svn_old_client_message1 + "\n\n" + VCSMessages.svn_old_client_message2);
            }
        });
    }
}
