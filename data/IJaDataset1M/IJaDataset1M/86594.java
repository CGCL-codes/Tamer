package controller;

import eve.sys.Event;
import eve.sys.EventListener;
import eve.ui.event.ControlEvent;

/**
 *
 * @author JPEXS
 */
public class MainMenuEventListener implements EventListener {

    private static MainMenuEventListener instance;

    private MainMenuEventListener() {
    }

    public static MainMenuEventListener getInstance() {
        if (instance == null) {
            instance = new MainMenuEventListener();
        }
        return instance;
    }

    public void onEvent(Event ev) {
        if (ev.type == ControlEvent.PRESSED) {
            ControlEvent cev = (ControlEvent) ev;
            if (cev.action.equals("NEWGAME")) {
                model.Main.selectGamePlayers();
            }
            if (cev.action.equals("EXIT")) {
                model.Main.exit();
            }
        }
    }
}
