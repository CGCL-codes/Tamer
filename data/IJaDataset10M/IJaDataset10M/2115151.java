package classes.pack;

import android.util.Log;

public class StartupManager {

    Profiling profiling;

    public StartupManager(UIMain uiMain) {
        profiling = new Profiling(uiMain);
        ParkingZones parkingZones = new ParkingZones();
        Gui.setProfiling(profiling);
        Gui.setParkingZones(parkingZones);
        Gui.setUiMain(uiMain);
        Logging.Initialize();
        Logging.loadLogs();
        Gui.createExtInterfaces();
    }
}
