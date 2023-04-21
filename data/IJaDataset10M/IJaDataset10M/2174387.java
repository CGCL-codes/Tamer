package josx.platform.rcx;

import rossum.*;

public class simPublicRangeHandler implements RsRangeSensorEventHandler {

    Sensor hSensor;

    SensorListener rListener;

    int oldValue = 0;

    public simPublicRangeHandler(Sensor aSensor, SensorListener aListener) {
        hSensor = aSensor;
        rListener = aListener;
    }

    public void processTransaction(RsTransaction t) {
        process((RsRangeSensorEvent) t);
    }

    public void process(RsRangeSensorEvent event) {
        int newValue = (int) event.range;
        rListener.stateChanged(hSensor, oldValue, newValue);
        oldValue = newValue;
    }
}
