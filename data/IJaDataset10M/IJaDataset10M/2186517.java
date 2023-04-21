package futbol.gui;

import futbol.FutbolMatch;

public interface FutbolGUI {

    public enum GuiEvent {

        GUI_EVENT_REPAINT, GUI_EVENT_START_MATCH, GUI_EVENT_FINISH_MATCH
    }

    ;

    public void init(FutbolMatch match);

    public void start();

    public void eventHandle(GuiEvent event);
}
