package controller;

import eve.sys.Event;
import eve.sys.EventListener;
import eve.ui.event.ControlEvent;
import model.Settings;

/**
 *
 * @author JPEXS
 */
public class NewGameOptionsEventListener implements EventListener {

    private static NewGameOptionsEventListener instance;

    private NewGameOptionsEventListener() {
    }

    public static NewGameOptionsEventListener getInstance() {
        if (instance == null) {
            instance = new NewGameOptionsEventListener();
        }
        return instance;
    }

    public void onEvent(Event ev) {
        if (ev.type == ControlEvent.PRESSED) {
            ControlEvent cev = (ControlEvent) ev;
            if (cev.action.equals("OK")) {
                int gtime = 30;
                try {
                    gtime = Integer.parseInt(view.Main.newGameOptionsForm.gameTimeInput.getText());
                } catch (NumberFormatException ex) {
                    view.Main.newGameOptionsForm.gameTimeInput.setText("" + gtime);
                    return;
                }
                if (gtime < 30) {
                    view.Main.newGameOptionsForm.gameTimeInput.setText("" + 30);
                    return;
                }
                Settings.gameTime = gtime;
                if (view.Main.newGameOptionsForm.explosionModeVHCheckBox.getState()) {
                    Settings.explodeMode = Settings.EM_VERTHORIZ;
                }
                if (view.Main.newGameOptionsForm.explosionModeAllCheckBox.getState()) {
                    Settings.explodeMode = Settings.EM_ALL;
                }
                if (view.Main.newGameOptionsForm.gameTimeLimitedCheckBox.getState()) {
                    Settings.gameTimeMode = Settings.GTM_LIMITED;
                }
                if (view.Main.newGameOptionsForm.gameTimeUnlimitedCheckBox.getState()) {
                    Settings.gameTimeMode = Settings.GTM_UNLIMITED;
                }
                if (view.Main.newGameOptionsForm.explodeCount4CheckBox.getState()) {
                    Settings.setExplodeCount(Settings.EC_FOUR);
                }
                if (view.Main.newGameOptionsForm.explodeCount8CheckBox.getState()) {
                    Settings.setExplodeCount(Settings.EC_EIGHT);
                }
                if (model.Main.gameType == model.Main.GT_NET_GAME) {
                    model.Main.createGame();
                } else {
                    model.Main.newGame();
                }
            }
            if (cev.action.equals("CANCEL")) {
                model.Main.selectGamePlayers();
            }
        }
    }
}
