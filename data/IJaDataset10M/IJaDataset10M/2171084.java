package mw.client.input;

import mw.client.managers.ConnectionManager;
import mw.client.managers.GameManager;
import mw.client.managers.SettingsManager;
import mw.client.managers.WindowManager;
import mw.mtgforge.Input;

public class Input_PlayFirst extends Input {

    public Input_PlayFirst() {
        WindowManager.getGuideWindow().getImageButtonOk().setText("Yes");
        WindowManager.getGuideWindow().getImageButtonCancel().setText("No");
        WindowManager.getGuideWindow().setTextRemotely("Would you like to play first?");
        WindowManager.getGuideWindow().enableCancelButton(true);
        if (SettingsManager.getManager().isDebug()) {
            selectButtonOK();
        }
    }

    public void selectButtonOK() {
        if (GameManager.getManager().getMyHandSize() == 0) {
            selectButtonCancel();
            return;
        }
        if (ConnectionManager.getRMIConnection() != null) {
            InputAsyncUtil.sendPlayFirst();
            WindowManager.getGuideWindow().setVisible(false);
        }
    }

    public void selectButtonCancel() {
        InputAsyncUtil.sendDrawFirst();
    }
}
