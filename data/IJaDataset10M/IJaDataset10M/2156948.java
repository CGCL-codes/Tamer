package org.torbs.gui.controller;

import java.io.IOException;
import org.torbs.ui.StartServerScreen;
import org.torbs.util.TorbsExceptionNotExists;
import org.torbs.util.TorbsExceptionSecurity;

/**
 */
public class StartServer extends ScrCtrl {

    public StartServer() {
    }

    @Override
    public void onStartScreen() {
        setText("StartServer/RaceName", StartServerScreen.getInstance().getRaceName());
    }

    public void selectAIs() {
        pushScreen("ais selection");
    }

    public void selectRace() {
        pushScreen("race selection");
    }

    public void startServer() throws TorbsExceptionNotExists, TorbsExceptionSecurity {
        try {
            StartServerScreen.getInstance().startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pushScreen("multiplayer initialization");
    }
}
