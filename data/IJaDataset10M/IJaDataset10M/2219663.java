package org.nakedobjects.nof.reflect.java.fixture;

import org.nakedobjects.applib.fixtures.FixtureClock;
import org.nakedobjects.applib.fixtures.FixtureServices;

public class JavaFixtureServices implements FixtureServices {

    private final FixtureClock clock;

    public JavaFixtureServices(FixtureClock clock) {
        this.clock = clock;
    }

    public void earlierDate(int years, int months, int days) {
        clock.addDate(-years, -months, -days);
    }

    public void earlierTime(int hours, int minutes) {
        clock.addTime(-hours, -minutes);
    }

    public FixtureClock getFixtureClock() {
        return clock;
    }

    public void laterDate(int years, int months, int days) {
        clock.addDate(years, months, days);
    }

    public void laterTime(int hours, int minutes) {
        clock.addTime(hours, minutes);
    }

    public void resetClock() {
        clock.reset();
    }

    public void setDate(int year, int month, int day) {
        clock.setDate(year, month, day);
    }

    public void setTime(int hour, int minute) {
        clock.setTime(hour, minute);
    }
}
