package emulator.hardware.video.test;

import java.util.Observer;
import emulator.hardware.clock.Clock;
import emulator.hardware.clock.ClockHandle;

public class NullClock implements Clock {

    @Override
    public ClockHandle acquireHandle() {
        return new NullClockHandle();
    }

    @Override
    public void releaseHandle(ClockHandle handle) {
    }

    @Override
    public void addClockSpeedObserver(Observer clockSpeedObserver) {
    }
}
