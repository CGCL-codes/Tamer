package ingenias.jade.components;

import java.util.*;
import ingenias.jade.exception.*;
import ingenias.jade.JADEAgent;

public class UserGUIInit {

    private static java.lang.String semaphore = "UserGUI";

    private static Vector<UserGUIAppImp> appsinitialised = new Vector<UserGUIAppImp>();

    public static void initialize(UserGUIAppImp app) {
        final UserGUIAppImp appF = app;
        new Thread() {

            public void run() {
                appF.showGUI();
            }
        }.start();
    }

    public static void shutdown(UserGUIAppImp app) {
    }

    public static void shutdown() {
        synchronized (semaphore) {
            for (int k = 0; k < appsinitialised.size(); k++) {
                shutdown(appsinitialised.elementAt(k));
            }
        }
    }

    public static Vector<UserGUIAppImp> getAppsInitialised() {
        return appsinitialised;
    }

    public static UserGUIApp createInstance() {
        synchronized (semaphore) {
            UserGUIAppImp app = new UserGUIAppImp();
            initialize(app);
            appsinitialised.add(app);
            return app;
        }
    }

    public static UserGUIApp createInstance(JADEAgent owner) {
        synchronized (semaphore) {
            UserGUIAppImp app = new UserGUIAppImp();
            app.registerOwner(owner);
            initialize(app);
            appsinitialised.add(app);
            return app;
        }
    }
}
