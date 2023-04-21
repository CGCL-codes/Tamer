package gov.sns.apps.energymaster;

import java.util.TimerTask;

public class EnergyMasterUpdaterNoWindow extends TimerTask {

    EnergyMeasurer measurer;

    public EnergyMasterUpdaterNoWindow(EnergyMeasurer meas) {
        measurer = meas;
    }

    @Override
    public void run() {
        measurer.run();
    }
}
