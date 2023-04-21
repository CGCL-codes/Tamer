package ingenias.jade.components;

import java.util.*;
import ingenias.jade.exception.*;

public abstract class UserGUIApp extends Application {

    public UserGUIApp() {
        super();
    }

    public void showGUI() {
    }

    public abstract void presentTicket(int seat, int cost);

    public abstract void apologizeForFailure();
}
