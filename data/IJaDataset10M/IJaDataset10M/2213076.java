package gui.initiatives;

import gui.EarthPanel;
import gui.Presentation;
import simulation.initiatives.EnginePush;
import simulation.threads.EnginePushThread;
import common.SimulationSettings;

public class ClockControlPush extends AbstractClockControl {

    private static final long serialVersionUID = -5822023983023271362L;

    public ClockControlPush(EarthPanel pnlEarth, SimulationSettings settings) {
        super(pnlEarth, settings);
        Presentation presentation = new Presentation(pnlEarth, pnlDateTime);
        if (settings.isSimulationThreaded()) {
            EnginePushThread engineT = new EnginePushThread(presentation, settings);
            Thread t = new Thread(engineT);
            t.setName("SimThread-Pushing");
            t.start();
            engine = engineT;
        } else engine = new EnginePush(presentation, settings);
    }

    public void adjustDisplayRate(int newRate) {
        engine.adjustDisplayRate(newRate);
    }

    public void startClock() {
        engine.setSimTimeStep(settings.getSimTimeStep());
        engine.start();
    }

    public void pauseClock() {
        engine.pause();
    }

    public void resetClock() {
        engine.reset();
    }
}
