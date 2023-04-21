package org.gwm.samples.gwmdemo.client;

import org.gwm.client.GDialog;
import org.gwm.client.GFrame;
import org.gwm.client.event.GDialogChoiceListener;
import org.gwm.client.impl.DefaultGDialog;
import org.gwm.client.impl.DefaultGFrame;
import org.gwm.client.util.GwmUtilities;
import com.google.gwt.user.client.ui.Hyperlink;

public class ConfirmDialogScenarii extends AbstractScenarii {

    public ConfirmDialogScenarii() {
        super();
    }

    public void runScenarii() {
        DefaultGDialog.showConfirmDialog(null, "Do you like holidays?", "Are you an Iron Man ? :)", GDialog.YES_NO_CANCEL_OPTION_TYPE, new GDialogChoiceListener() {

            public void onChoice(GDialog dialog) {
                if (dialog.getSelectedOption() == (DefaultGDialog.YES_OPTION)) {
                    displayResponse("Where do you like go for vacation?");
                } else if (dialog.getSelectedOption() == (DefaultGDialog.NO_OPTION)) {
                    displayResponse("You are an iron man.");
                } else if (dialog.getSelectedOption() == (DefaultGDialog.CANCEL_OPTION)) {
                    displayResponse("You didn't give answer :(");
                }
            }

            private void displayResponse(String response) {
                GFrame responseWin = new DefaultGFrame("Response");
                responseWin.setMinimizable(false);
                responseWin.setMaximizable(false);
                responseWin.setResizable(false);
                responseWin.setContent(response);
                GwmUtilities.displayAtScreenCenter(responseWin);
            }
        });
    }

    protected Hyperlink createLink() {
        Hyperlink desktopDemoLink = new Hyperlink("Confirm Dialog", "confirm-dialog");
        return desktopDemoLink;
    }
}
